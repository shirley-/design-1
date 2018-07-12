package com.zym.Design1.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zym.Design1.bean.vo.EncashVO;
import com.zym.Design1.dao.EncashMapper;
import com.zym.Design1.entity.Encash;
import com.zym.Design1.entity.EncashExample;
import com.zym.Design1.entity.User;
import com.zym.Design1.mydao.AdminEncashMapper;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.ProjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 提现
 * Created on 2018/3/27.
 */
@Service
@Slf4j
//@CacheConfig(cacheNames = "EncashService")
public class EncashService {
    @Autowired
    private EncashMapper encashMapper;

    @Autowired
    private AdminEncashMapper adminEncashMapper;

    @Autowired
    private UserService userService;

//    @Cacheable(keyGenerator = "keyGenerator")
    public Encash getEncashById(Integer id) {
        return encashMapper.selectByPrimaryKey(id);
    }

    /**
     * for admin
     * @param pageIndex
     * @param pageSize
     * @param sort
     * @param sortOrder
     * @return
     */
//    @Cacheable(keyGenerator = "keyGenerator")
    public List<Encash> getEncashList(Integer pageIndex, Integer pageSize, String sort, String sortOrder,
                                      String state, Integer userId) {
        EncashExample encashExample = new EncashExample();
        EncashExample.Criteria criteria = encashExample.createCriteria();//
        if(state != null) {
            criteria.andStateEqualTo(state);//申请/同意/拒绝
        }
        if(userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if(sort != null && sortOrder != null) {
            sort = ProjectUtil.camelToUnderline(sort);
            encashExample.setOrderByClause(sort + " " +sortOrder);
        }
        PageHelper.startPage(pageIndex, pageSize);
        return encashMapper.selectByExample(encashExample);
    }

    public List<EncashVO> getApprovedEncashList(Integer pageIndex, Integer pageSize, String searchDate,
                                                String state, Integer userId) {

        PageHelper.startPage(pageIndex, pageSize);
        return adminEncashMapper.getApprovedListForOp(searchDate);
    }

    /**
     * 提现申请
     */
    //@CachePut
    @Transactional
    public void encash(User user, Integer amount) {
        log.info("before encash - user:{}, amount: {}", JSON.toJSONString(user), amount);

        if(amount<10 || amount%10 != 0 ) {
            throw new RuntimeException("提现金额必须是10的整数倍");
        }
        /*if(user.getCashCoin().multiply(BigDecimal.ONE.subtract(ConstantUtil.ENCASH_FEE))
                .compareTo(new BigDecimal(amount)) < 0) {
            throw new RuntimeException("会员的提现金额大于财富积分90%，无法提现");
        }*/

        Encash encash = new Encash();
        encash.setCreatedDate(new Date());
        encash.setUserId(user.getId());
        encash.setUserUid(user.getUid());
        encash.setState(ConstantUtil.STATE_INVALID);
        encash.setAmount(new BigDecimal(amount));
        if(user.getRole().equals(ConstantUtil.ROLE_ADMIN)) {//admin 没有手续fee,不转购物积分
            encash.setFee(BigDecimal.ZERO);
            encash.setShopPoints(BigDecimal.ZERO);
            encash.setEarnings(new BigDecimal(amount));
        }else {
            encash.setFee(new BigDecimal(amount).multiply(ConstantUtil.ENCASH_FEE));
            BigDecimal left = new BigDecimal(amount).subtract(encash.getFee());
            encash.setEarnings(left.multiply(ConstantUtil.ENCASH_EARNING_RATE));
            encash.setShopPoints(left.subtract(encash.getEarnings()));//购物积分
        }

        //财富积分->提现冻结财富积分
        user.setEncashFrozenCashCoin(user.getEncashFrozenCashCoin().add(new BigDecimal(amount)));
        user.setCashCoin(user.getCashCoin().subtract(new BigDecimal(amount)));

        userService.updateUser(user);

        encashMapper.insertSelective(encash);
        log.info("after encash - user:{}, encash: {}", JSON.toJSONString(user), JSON.toJSONString(encash));
    }


    //提现同意
    @Transactional
    public void approveEncash(User user, Integer id, String reason) {
        Encash encash = getEncashById(id);
        encash.setState(ConstantUtil.STATE_APPROVE);
        encash.setApproveDate(new Date());
        encash.setReason(reason);
        encash.setRemark( "-" + user.getUid());


        User encashUser = userService.getUserById(encash.getUserId());
        log.info("before approveEncash - user:{}, encash: {}", JSON.toJSONString(encashUser), JSON.toJSONString(encash));
        encashUser.setEarnings(encashUser.getEarnings().add(encash.getEarnings()));//收益增加

        if(encashUser.getEncashFrozenCashCoin().compareTo(encash.getAmount()) <0 ) {
            log.error("提现数量错误，user.getEncashFrozenCashCoin:{}, encash.getAmount:{}", encashUser.getEncashFrozenCashCoin(), encash.getAmount());
            throw new RuntimeException("提现信息错误");
        }

        encashUser.setEncashFrozenCashCoin(encashUser.getEncashFrozenCashCoin().subtract(encash.getAmount()));//冻结财富积分减少
        encashUser.setShopPoints(encashUser.getShopPoints().add(encash.getShopPoints()));//购物积分增加
        userService.updateUser(encashUser);

        encash.setAfterEarnings(encashUser.getEarnings());//
        encash.setAfterCashCoin(encashUser.getCashCoin());//
        updateEncash(encash);
        log.info("after approveEncash - user:{}, encash: {}", JSON.toJSONString(encashUser), JSON.toJSONString(encash));
    }

    //拒绝提现，返还财富积分
    @Transactional
    public void rejectEncash(User user, Integer id, String reason) {
        Encash encash = getEncashById(id);
        encash.setState(ConstantUtil.STATE_REJECT);
        encash.setApproveDate(new Date());
        encash.setReason(reason);
        encash.setRemark(" -" + user.getUid());

        updateEncash(encash);

        User encashUser = userService.getUserById(encash.getUserId());
        log.info("before rejectEncash - user:{}, encash: {}", JSON.toJSONString(encashUser), JSON.toJSONString(encash));
        if(encashUser.getEncashFrozenCashCoin().compareTo(encash.getAmount()) <0 ) {
            throw new RuntimeException("提现信息错误");
        }
        encashUser.setEncashFrozenCashCoin(encashUser.getEncashFrozenCashCoin().subtract(encash.getAmount()));
        encashUser.setCashCoin(encashUser.getCashCoin().add(encash.getAmount()));//退回cashCoin

        userService.updateUser(encashUser);
        log.info("after rejectEncash - user:{}, encash: {}", JSON.toJSONString(encashUser), JSON.toJSONString(encash));
    }

    @Transactional
    public void updateEncash(Encash encash) {
        int num = encashMapper.updateByPrimaryKeySelective(encash);
        if(num!=1) {
            log.error("updateEncash错误，num:{}, encash:{}", num , JSON.toJSONString(encash));
            throw new RuntimeException(" 更新提现信息错误");
        }
    }

}

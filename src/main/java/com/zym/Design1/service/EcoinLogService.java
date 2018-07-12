package com.zym.Design1.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.dao.BuyEcoinLogMapper;
import com.zym.Design1.entity.BuyEcoinLog;
import com.zym.Design1.entity.BuyEcoinLogExample;
import com.zym.Design1.entity.User;
import com.zym.Design1.mydao.EcoinChargeMapper;
import com.zym.Design1.util.ProjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created  on 2018/4/6.
 */
@Service
@Slf4j
public class EcoinLogService {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private BuyEcoinLogMapper buyEcoinLogMapper;

    @Autowired
    private EcoinChargeMapper ecoinChargeMapper;

    /**
     * 管理员
     * @param buyEcoinLog
     */
    @Transactional
    public void addEcoinLog(BuyEcoinLog buyEcoinLog) {
        buyEcoinLog.setDate(new Date());
        buyEcoinLog.setRemark(buyEcoinLog.getRemark() + "-" + hostHolder.getUser().getUid() + "充值");
        buyEcoinLogMapper.insertSelective(buyEcoinLog);
        log.info("after insert buyEcoinLog: {}", JSON.toJSONString(buyEcoinLog));
    }

    @Transactional
    public void deleteEcoinLog(Integer id) {
        buyEcoinLogMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void updateEcoinLog(BuyEcoinLog buyEcoinLog) {
        buyEcoinLog.setRemark(buyEcoinLog.getRemark() + "-" + hostHolder.getUser().getUid() + "修改");
        int num = buyEcoinLogMapper.updateByPrimaryKeySelective(buyEcoinLog);
        if(num!=1) {
            log.error("updateEcoinLog错误，num:{}, buyEcoinLog:{}", num , JSON.toJSONString(buyEcoinLog));
            throw new RuntimeException(" 更新错误");
        }
    }

    public List<BuyEcoinLog> queryEcoinLogs(Integer pageIndex, Integer pageSize, String sort, String sortOrder,
                                            String searchKey, Integer searchUserId) {
        BuyEcoinLogExample example = new BuyEcoinLogExample();
        if(sort != null) {
            sort = ProjectUtil.camelToUnderline(sort);
            example.setOrderByClause(sort + " " +sortOrder);
        }
        if(searchUserId != null) {
            try{
                example.createCriteria().andUserIdEqualTo(Integer.valueOf(searchUserId));
            }catch (Exception e) {
                log.error(e.getMessage() + "searchUserId:{}", searchUserId);
            }
        }
        if(searchKey != null) {
            example.or().andUserUidLike( searchKey + "%");
            example.or().andRemarkLike( searchKey + "%");
        }
        PageHelper.startPage(pageIndex, pageSize);
        return buyEcoinLogMapper.selectByExample(example);
    }


    @Transactional
    public BigDecimal getTotalEcoinChargeAmount(String userId) {
        BigDecimal totalChargedAmount = ecoinChargeMapper.getTotalChargedAmount(userId);
        log.info("totalEcoinChargeAmount of user: {}, amount: {}", userId, String.valueOf(totalChargedAmount) );
        return  totalChargedAmount;
    }

}

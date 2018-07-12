package com.zym.Design1.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.bean.UserLevelRule;
import com.zym.Design1.dao.*;
import com.zym.Design1.entity.*;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.ProjectUtil;
import io.netty.util.Constant;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created on 2018/3/6.
 */
@Slf4j
@Service
//@CacheConfig(cacheNames = "UserService")
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserBonusService userBonusService;

    @Autowired
    private DailyReleaseLogMapper dailyReleaseLogMapper;

    @Autowired
    private EcoinLogService ecoinLogService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private PhaseService phaseService;

    //会员选择天数
    @Transactional
    public void addEcoinOpt(User user) {
        if(phaseService.getEarlyPhaseState().equals(ConstantUtil.STATE_INVALID)) {//后期 1
            log.error("该功能已关闭，不能选择");
            throw new RuntimeException("该功能已关闭，不能选择");
        }
        if(user.getEcoin().compareTo(BigDecimal.ZERO) <=0 ) {
            log.error("会员没有积分，无法选择积分释放套餐, user:{}", JSON.toJSONString(user));
            throw new RuntimeException("会员没有积分，无法选择积分释放套餐");
        }
        BigDecimal vc = currencyService.getCurrency();
        log.info("vc:{}", vc);
        //总释放货币增加
        BigDecimal incrementAmount = user.getEcoin().divide(vc, BigDecimal.ROUND_HALF_DOWN);
        currencyService.incrementCurrencyCount(incrementAmount);
        log.info("总释放货币增加 incrementAmount:{}", incrementAmount);
        //ecoin->release_frozen_coin
        BigDecimal coin = user.getReleaseFrozenCoin().add(user.getEcoin().divide(vc, BigDecimal.ROUND_HALF_DOWN));

        //电子币->货币，算一次交易，记录交易
        Trade trade = new Trade();
        trade.setBuyerUid(user.getUid());
        trade.setBuyerId(user.getId());
        trade.setType(ConstantUtil.TYPE_BUY_ORDER);
        trade.setTradeDate(new Date());
        trade.setState(ConstantUtil.TYPE_EARLY_PHASE);//前期
        trade.setVcPrice(vc);
        trade.setAmount(coin);
        trade.setEcoin(user.getEcoin());
        tradeService.insertTrade(trade);
        log.info("电子币->货币，算一次交易，记录交易, trade:{}", JSON.toJSONString(trade));
        user.setTotalCoin(user.getTotalCoin().add(coin));
        user.setReleaseFrozenCoin(coin);//电子币按市值换算为货币
        BigDecimal originEcoin = user.getEcoin();
        user.setEcoin(BigDecimal.ZERO);//ecoin->0
        user.setEcoinSetFlag(ConstantUtil.STATE_VALID);
        UserBonus userBonus = userBonusService.addReleaseRule(user, originEcoin, trade, ConstantUtil.TYPE_EARLY_PHASE);//前期

        updateUser(user);//更新用户信息
    }
    //修改套餐
    @Transactional
    public void updateEcoinOpt(User user, Integer option) {
        if(user.getReleaseDays()>0) {
            log.error("已开始释放，不能修改, user:{}", JSON.toJSONString(user));
            throw new RuntimeException("已开始释放，不能修改");
        }
        user.setRuleId(option);
        updateUser(user);
        log.info("after user:{}", JSON.toJSONString(user));
        userBonusService.updateEcoinOpt(user);
    }
    /**
     * 管理员注册会员
     * @param user
     */
    @Transactional
    public void addUser(User user) {
        initUser(user);
        user.setSalt(ProjectUtil.createSalt());
        user.setPassword(ProjectUtil.MD5(ConstantUtil.MEMBER_INITIAL_PASSWORD + user.getSalt()));
        user.setTradeSalt(ProjectUtil.createSalt());
        user.setTradePassword(ProjectUtil.MD5(ConstantUtil.MEMBER_INITIAL_TRADE_PASSWORD + user.getTradeSalt()));
        user.setRole(ConstantUtil.ROLE_MEMBER);
        user.setCreatedDate(new Date());
        user.setLastModifiedDate(user.getCreatedDate());

       /* BigDecimal originEcoin = BigDecimal.ZERO;
        if(user.getRuleId()!=null && user.getRuleId()>0) {
            if(user.getEcoin().compareTo(BigDecimal.ZERO) <=0 ) {
                throw new RuntimeException("会员没有电子币，无法选择电子币释放套餐");
            }
            //增加货币总释放量
            currencyService.incrementCurrencyCount(user.getEcoin().divide(currencyService.getCurrency()));
            //ecoin->release_frozen_coin
            BigDecimal coin = user.getReleaseFrozenCoin().add(user.getEcoin().divide(currencyService.getCurrency()));
            user.setTotalCoin(user.getTotalCoin().add(coin));
            user.setReleaseFrozenCoin(coin);//电子币按市值换算为货币
            originEcoin = user.getEcoin();
            user.setEcoin(BigDecimal.ZERO);//ecoin->0
            user.setEcoinSetFlag(ConstantUtil.STATE_VALID);
        }*/
        insertUser(user);//插入用户信息

        /*if(user.getRuleId()!=null && user.getRuleId()>0) {
            UserBonus userBonus = addReleaseRule(user, originEcoin);//add release rule, after insert user, 否则setUserId没值
        }*/

    }

    @Transactional
    public void registerUser(User user) {
        if( !StringUtil.isNullOrEmpty(user.getIntroducerUid())) {//有推荐人
            User introducer = getUserByUid(user.getIntroducerUid());
            if(introducer != null && introducer.getState().equals(ConstantUtil.STATE_VALID)) {
                user.setIntroducerId(introducer.getId());
                user.setLevel(introducer.getLevel() + 1);//introducer.level + 1
            }else {
                user.setIntroducerUid(null);
            }
            log.info("user.getIntroducerUid():{}", user.getIntroducerUid());
            orgService.addMemberRecommend(introducer, user);//检查限制，增加人脉关系
            addUser(user);
        }else {//无推荐人
            user.setLevel(1);//初始化1
            user.setIntroducerUid(null);
            addUser(user);
        }
        /*
        BuyEcoinLog buyEcoinLog = null;

        if((user.getEcoin()!=null) && (user.getEcoin().compareTo(BigDecimal.ZERO) >0)) {
            //写入ecoin充值记录
            buyEcoinLog = new BuyEcoinLog();
            buyEcoinLog.setAmount(user.getEcoin());//amount有值，userId无值
            buyEcoinLog.setRemark("注册时记录");
        }

        if( !StringUtil.isNullOrEmpty(user.getIntroducerUid())) {//有推荐人
            User introducer = getUserByUid(user.getIntroducerUid());
            if(introducer != null && introducer.getState().equals(ConstantUtil.STATE_VALID)) {
                user.setIntroducerId(introducer.getId());
                user.setLevel(introducer.getLevel() + 1);//introducer.level + 1
            }else {
                user.setIntroducerUid(null);
            }
            addUser(user);
            orgService.addMemberRecommend(introducer, user);//检查限制，增加人脉关系
        }else {//无推荐人
            user.setLevel(1);//初始化1
            user.setIntroducerUid(null);
            addUser(user);
        }
        if(buyEcoinLog != null) {//此时，user.getEcoin可能已转为release_frozen_coin, 可能无值，所以在最前面赋值
            buyEcoinLog.setUserId(user.getId());
            buyEcoinLog.setUserUid(user.getUid());
            ecoinLogService.addEcoinLog(buyEcoinLog);//after insert user, 否则setUserId没值,所以分两段写
        }
        */
    }

    //注册时初始值
    @Transactional
    private void initUser(User user) {
        user.setState(ConstantUtil.STATE_VALID);
        user.setTradableCoin(BigDecimal.ZERO);
        if(user.getEcoin()==null) {
            user.setEcoin(BigDecimal.ZERO);
        }
        user.setEarnings(BigDecimal.ZERO);
        user.setSoldCoin(BigDecimal.ZERO);
        user.setOnSaleCoin(BigDecimal.ZERO);
        user.setOnBuyEcoin(BigDecimal.ZERO);
        user.setTotalCoin(BigDecimal.ZERO);
        user.setEncashFrozenCashCoin(BigDecimal.ZERO);
        user.setReleaseDays(0);
        user.setReleaseFrozenCoin(BigDecimal.ZERO);
        user.setCashCoin(BigDecimal.ZERO);
        user.setReleaseCoin(BigDecimal.ZERO);
        user.setEcoinSetFlag(ConstantUtil.STATE_INVALID);
        user.setShopPoints(BigDecimal.ZERO);
    }

    //@CacheEvict(value = "user", key = "#id")
    @Transactional
    public void deleteUser(Integer id) {//不是真正的删除user
        User userById = getUserById(id);
        userById.setState(ConstantUtil.STATE_INVALID);
        updateUser(userById);
        //删除org
        orgService.deleteOrg(id);
        log.info("delete user:{}", JSON.toJSONString(userById));
    }

    /**
     * 电子币充值:insert购买ecoin记录，更新用户ecoin数量
     */
    @Transactional
    public void chargeEcoin(BuyEcoinLog buyEcoinLog) {

        User user = getUserById(buyEcoinLog.getUserId());
        if(user.getRole().equals(ConstantUtil.ROLE_OP)) {
            log.error("没有权限，无法充值, user:{}", JSON.toJSONString(user));
            throw new RuntimeException("没有权限，无法充值!");
        }
        user.setEcoin(user.getEcoin().add(buyEcoinLog.getAmount()));
        buyEcoinLog.setAfterEcoin(user.getEcoin());//after ecoin
        ecoinLogService.addEcoinLog(buyEcoinLog);
        updateUser(user);
    }



//    @CachePut(value = "user", key = "#user.id")
    @Transactional
    public  void updateUser(User user) {
        synchronized (user.getUid().intern()) {
            log.info("before updateUser, user:{}", JSONObject.toJSON(user));
            user.setLastModifiedDate(new Date());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(user.getId());
            int num = userMapper.updateByExampleSelective(user, example);
            if(num!=1) {
                log.error("updateUser错误, num:{}, user:{}", num , JSON.toJSONString(user));
                throw new RuntimeException("修改用户错误");
            }
            log.info("after updateUser, user:{}", JSONObject.toJSON(user));
        }
    }

    @Transactional
    public synchronized void insertUser(User user) {
        user.setLastModifiedDate(user.getCreatedDate());
        user.setVersion(1);
        userMapper.insertSelective(user);
        log.info(JSONObject.toJSONString(user));
    }

    /**
     *
     * @param uid
     * @return
     */
    public User getUserByUid(String uid) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUidEqualTo(uid).andStateEqualTo(ConstantUtil.STATE_VALID);
        List<User> users = selectByExample(userExample);
        if(users == null || users.size() != 1) {
            log.info("会员 uid:{} 无效", uid);
            return null;
//            throw new RuntimeException("会员" +uid + "无效");
        }else {
            return users.get(0);
        }
    }

//    @Cacheable(value = "user", key = "#id")
    public User getUserById(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if(user.getState().equals(ConstantUtil.STATE_INVALID)) {
            log.info("会员 id:{} 无效", id);
            throw new RuntimeException("会员" + user.getUid() + "无效");
        }
        return user;
    }

    /**
     * all
     * @return
     */
    public List<User> getAllMembers() {
        UserExample example = new UserExample();
        example.createCriteria().andStateEqualTo(ConstantUtil.STATE_VALID).andRoleEqualTo(ConstantUtil.ROLE_MEMBER);
        return userMapper.selectByExample(example);
    }

    public List<User> selectByExample(UserExample example) {
        return userMapper.selectByExample(example);
    }

    /**
     *
     * @return
     */
    public List<User> getPageUsers(Integer pageIndex, Integer pageSize, String sort, String sortOrder, String searchKey) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andStateEqualTo(ConstantUtil.STATE_VALID);
        if(sort != null) {
            sort = ProjectUtil.camelToUnderline(sort);
            example.setOrderByClause(sort + " " +sortOrder);
        }
        if(searchKey != null) {
            criteria.andUidLike( searchKey + "%");
        }

        PageHelper.startPage(pageIndex, pageSize);
        return userMapper.selectByExample(example);
    }



    @Transactional
    public void quartzTask(User user) {
        log.info("user:{}", JSON.toJSONString(user));
        BigDecimal currency = currencyService.getCurrency();//当前货币单价
        log.info("当前货币单价 currency:{}", currency);
        if (user.getRole().equals(ConstantUtil.ROLE_MEMBER)) {//该会员
            Map<Integer, User> map = new HashMap<>();//id, userLevel
            orgService.getDescendantsMap(user.getId(), map);//map中有该会员的所有下线（含会员自身）
            BigDecimal bonus = BigDecimal.ZERO;//earlyPhase bonus
//            BigDecimal tradeBonus = BigDecimal.ZERO;//trade bonue
            BigDecimal unfrozenCoinNum = BigDecimal.ZERO;//earlyPhase frozen

            for (Map.Entry<Integer, User> entry : map.entrySet()) {//遍历每个下线
                String type = ConstantUtil.TYPE_SELF;//该会员自身
                if (!user.getId().equals(entry.getKey())) {
                    type = ConstantUtil.TYPE_RECOMMEND;//下线类型
                }
                //记录下线和自身的每一笔   前期bonus，trade_bonus
                Map<String, DailyReleaseLog> releasedMap = recordUserCredit(user, type, entry.getKey(), entry.getValue());
                //recordUserCredit已换算过，此处不换算了
                if(releasedMap.containsKey(ConstantUtil.ECOIN_BONUS) && releasedMap.get(ConstantUtil.ECOIN_BONUS).getEcoinBonus().compareTo(BigDecimal.ZERO) > 0) {
                    //X每天ecoin_bonus 换算？？ 此时，bonus是电子币，要按市值换算
                    bonus = bonus.add(releasedMap.get(ConstantUtil.ECOIN_BONUS).getEcoinBonus());//.divide(currency, BigDecimal.ROUND_HALF_DOWN));
                }
                if(releasedMap.containsKey(ConstantUtil.TRADE_ECOIN_BONUS) && releasedMap.get(ConstantUtil.TRADE_ECOIN_BONUS).getEcoinBonus().compareTo(BigDecimal.ZERO) > 0) {
//                    X后期，bonus是货币X, 也是电子币
                    bonus = bonus.add(releasedMap.get(ConstantUtil.TRADE_ECOIN_BONUS).getEcoinBonus());
                }
                if(releasedMap.containsKey(ConstantUtil.UNFROZEN_COIN) && releasedMap.get(ConstantUtil.UNFROZEN_COIN).getUnfrozenCoin().compareTo(BigDecimal.ZERO) >0 ) {
                    unfrozenCoinNum = unfrozenCoinNum.add(releasedMap.get(ConstantUtil.UNFROZEN_COIN).getUnfrozenCoin());
                }
                if(releasedMap.containsKey(ConstantUtil.TRADE_UNFROZEN_COIN) && releasedMap.get(ConstantUtil.TRADE_UNFROZEN_COIN).getUnfrozenCoin().compareTo(BigDecimal.ZERO) >0 ) {
                    unfrozenCoinNum = unfrozenCoinNum.add(releasedMap.get(ConstantUtil.TRADE_UNFROZEN_COIN).getUnfrozenCoin());
                }
            }
            user.setReleaseCoin(user.getReleaseCoin().add(bonus).add(unfrozenCoinNum));//更新user released_credit已释放积分
            user.setReleaseDays(user.getReleaseDays() + 1);
            user.setReleaseFrozenCoin(user.getReleaseFrozenCoin().subtract(unfrozenCoinNum));
            user.setTradableCoin(user.getTradableCoin().add(bonus).add(unfrozenCoinNum));
            user.setTotalCoin(user.getTotalCoin().add(bonus));
            Double count = currencyService.incrementCurrencyCount(bonus);//这里总释放货币只加bonus，电子币转换的货币在前期选套餐时累加了，
            if(unfrozenCoinNum.compareTo(BigDecimal.ZERO) > 0 || bonus.compareTo(BigDecimal.ZERO) >0){
//                    ||(user.getRuleId()==5 && user.getReleaseDays()< 2)) {//根据实际配置修改，一天且没有奖励，需要增加release_day  //锁不住人，去掉一天锁仓的选项
                updateUser(user);
            }
            log.info("user: {} , bonus: {}, unfrozenCoinNum:{}, total_vc_count: {}, user:{}",
                    user.getId(),  bonus, unfrozenCoinNum, count, JSON.toJSON(user));
        }
    }

    /**
     * 记录 user会员得到entity会员为他释放的积分
     * entity为user的下线type=RECOMMEND  （user自身也为其下线type=SELF）
     * @param user
     * @param type
     * @param entityId
     * @param child 
     * @return
     */
    @Transactional
    public Map<String, DailyReleaseLog> recordUserCredit( User user, String type, Integer entityId, User child) {
        Map<String, DailyReleaseLog> logMap = new HashMap<>();
        Map<String, BigDecimal> dailyRelease = getDailyCredit(entityId, user.getId()==entityId);
        //earlyPhase_bonus, trade_bonus, unfrozenCredit, unfrozenEcoin
        for(Map.Entry<String, BigDecimal> entry: dailyRelease.entrySet()) {
            DailyReleaseLog dailyCreditLog = new DailyReleaseLog();//每天积分释放记录
            dailyCreditLog.setDate(new Date());
            dailyCreditLog.setUserId(user.getId());
            dailyCreditLog.setType(type);//自身0还是下线1
            if(entry.getKey().equals(ConstantUtil.UNFROZEN_COIN)) {//early_phase_bonus
                if(entry.getValue().compareTo(BigDecimal.ZERO)>0) {
                    if(ConstantUtil.TYPE_RECOMMEND.equals(type)) {//推荐所得积分
                        continue;
                    }else {//个人
                        dailyCreditLog.setUnfrozenCoin(dailyRelease.get(ConstantUtil.UNFROZEN_COIN));
                        dailyCreditLog.setEntityUserLevel(user.getRuleId());
                        dailyCreditLog.setEntityId(user.getId());
                    }
                    dailyReleaseLogMapper.insertSelective(dailyCreditLog);
                    logMap.put(ConstantUtil.UNFROZEN_COIN, dailyCreditLog);
                }
            }else if(entry.getKey().equals(ConstantUtil.ECOIN_BONUS)) {//当天释放
                if(entry.getValue().compareTo(BigDecimal.ZERO)>0) {
                    if(ConstantUtil.TYPE_RECOMMEND.equals(type)) {//推荐所得积分
                        dailyCreditLog.setEntityId(entityId);
                        dailyCreditLog.setEntityUserLevel(child.getRuleId());//rule_id关联
                        //每天ecoin_bonus 换算？？ 此时，bonus是电子币，要按市值换算
                        dailyCreditLog.setEcoinBonus(dailyRelease.get(ConstantUtil.ECOIN_BONUS).multiply(ConstantUtil.RECOMMEND_RATE)//from child
                                .divide(currencyService.getCurrency(), BigDecimal.ROUND_HALF_DOWN));

                    }else {//个人
                        //每天ecoin_bonus 换算？？ 此时，bonus是电子币，要按市值换算
                        dailyCreditLog.setEcoinBonus(dailyRelease.get(ConstantUtil.ECOIN_BONUS)
                                .divide(currencyService.getCurrency(), BigDecimal.ROUND_HALF_DOWN));
                        dailyCreditLog.setEntityUserLevel(user.getRuleId());
                        dailyCreditLog.setEntityId(user.getId());
                    }
                    dailyReleaseLogMapper.insertSelective(dailyCreditLog);
                    logMap.put(ConstantUtil.ECOIN_BONUS, dailyCreditLog);
                }
            }else if(entry.getKey().equals(ConstantUtil.TRADE_UNFROZEN_COIN)) {//early_phase_bonus
                if(entry.getValue().compareTo(BigDecimal.ZERO)>0) {
                    if(ConstantUtil.TYPE_RECOMMEND.equals(type)) {//推荐所得积分
                        continue;
                    }else {//个人
                        dailyCreditLog.setUnfrozenCoin(dailyRelease.get(ConstantUtil.TRADE_UNFROZEN_COIN));
                        dailyCreditLog.setEntityUserLevel(0);//本来是trade.ruleId，但是可能是多个trade的总和，不细分了，所以0
                        dailyCreditLog.setEntityId(user.getId());
                    }
                    dailyReleaseLogMapper.insertSelective(dailyCreditLog);
                    logMap.put(ConstantUtil.TRADE_UNFROZEN_COIN, dailyCreditLog);
                }
            }else if(entry.getKey().equals(ConstantUtil.TRADE_ECOIN_BONUS)) {//当天释放
                if(entry.getValue().compareTo(BigDecimal.ZERO)>0) {
                    if(ConstantUtil.TYPE_RECOMMEND.equals(type)) {//推荐所得积分
                        dailyCreditLog.setEntityId(entityId);
                        dailyCreditLog.setEntityUserLevel(0);//本来是trade.ruleId，但是可能是多个trade的总和，不细分了，所以0
                        //每天ecoin_bonus 换算？？ 此时，bonus是电子币，要按市值换算
                        dailyCreditLog.setEcoinBonus(dailyRelease.get(ConstantUtil.TRADE_ECOIN_BONUS).multiply(ConstantUtil.RECOMMEND_RATE)//from child
                                .divide(currencyService.getCurrency(), BigDecimal.ROUND_HALF_DOWN));

                    }else {//个人
                        //每天ecoin_bonus 换算？？ 此时，bonus是电子币，要按市值换算
                        dailyCreditLog.setEcoinBonus(dailyRelease.get(ConstantUtil.TRADE_ECOIN_BONUS)
                                .divide(currencyService.getCurrency(), BigDecimal.ROUND_HALF_DOWN));
                        dailyCreditLog.setEntityUserLevel(0);
                        dailyCreditLog.setEntityId(user.getId());
                    }
                    log.info("dailyCreditLog:{}", dailyCreditLog.toString());
                    dailyReleaseLogMapper.insertSelective(dailyCreditLog);
                    logMap.put(ConstantUtil.TRADE_ECOIN_BONUS, dailyCreditLog);
                }
            }
        }
        return logMap;
    }

    /**
     * 某会员的当天bonus,
     * @param userId
     * @return
     */
    @Transactional
    public Map<String, BigDecimal> getDailyCredit(Integer userId, Boolean isSelf) {
        Map<String, BigDecimal> dailyCreditBonusMap = new HashMap<>();
        //early phase
        dailyCreditBonusMap.put(ConstantUtil.ECOIN_BONUS, BigDecimal.ZERO);//ecoin_bonus
        dailyCreditBonusMap.put(ConstantUtil.UNFROZEN_COIN, BigDecimal.ZERO);//unfrozenEcoin
        //trade
        dailyCreditBonusMap.put(ConstantUtil.TRADE_ECOIN_BONUS, BigDecimal.ZERO);//trade_ecoin_bonus
        dailyCreditBonusMap.put(ConstantUtil.TRADE_UNFROZEN_COIN, BigDecimal.ZERO);//trade_unfrozen_coin
        //感觉全部的都需要查出来，等调试 交易的时候看看
        List<UserBonus> allUserBonuses = userBonusService.getUserBonus(userId);//该会员所有的earlyPhase + trade
        for(UserBonus userBonus : allUserBonuses) {
            Date now = new Date();
            Calendar calendarNow = Calendar.getInstance();
            calendarNow.setTime(now);
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(userBonus.getEndDate());
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(userBonus.getStartDate());

            calendarEnd.add(Calendar.DATE, 1);//结束天+1
            if(isSelf) {//只有自己时，才考虑映射 unfrozen
                if((calendarNow.get(Calendar.DAY_OF_YEAR) == calendarEnd.get(Calendar.DAY_OF_YEAR) )&&
                        (calendarNow.get(Calendar.YEAR) == calendarEnd.get(Calendar.YEAR))) {//如果结束天+1 = now，则冻结的释放积分可以映射
                    if(userBonus.getType().equals(ConstantUtil.TYPE_EARLY_PHASE)) {
                    //early phase
                        dailyCreditBonusMap.put(ConstantUtil.UNFROZEN_COIN, dailyCreditBonusMap.get(ConstantUtil.UNFROZEN_COIN).add(userBonus.getFrozenCoin()));//锁仓结束可以映射的总Coin
                    }else if(userBonus.getType().equals(ConstantUtil.TYPE_LATE_PHASE)) {
                    //trade phase
                        dailyCreditBonusMap.put(ConstantUtil.TRADE_UNFROZEN_COIN, dailyCreditBonusMap.get(ConstantUtil.TRADE_UNFROZEN_COIN).add(userBonus.getFrozenCoin()));//锁仓结束可以映射的总coin
                    }
                }
            }
            //下线，只考虑bonus
            //不到锁仓结束日，每天释放
            if( userBonus.getStartDate().before(now) && userBonus.getEndDate().after(now)) {//  (start  [now]  end)
//                dailyCreditBonusMap.put("creditUnfrozenFlag", 0);
                if(userBonus.getType().equals(ConstantUtil.TYPE_EARLY_PHASE)) {//前期，要换算
                    dailyCreditBonusMap.put(ConstantUtil.ECOIN_BONUS, dailyCreditBonusMap.get(ConstantUtil.ECOIN_BONUS).add(userBonus.getDailyBonus()));
                }else if(userBonus.getType().equals(ConstantUtil.TYPE_LATE_PHASE)) {//后期，要换算
                    dailyCreditBonusMap.put(ConstantUtil.TRADE_ECOIN_BONUS, dailyCreditBonusMap.get(ConstantUtil.TRADE_ECOIN_BONUS).add(userBonus.getDailyBonus()));
                }
            }
        }
        return dailyCreditBonusMap;
    }

    public boolean checkPassword(User user, String password) {
        if(Strings.isNotBlank( user.getSalt())) {//salt added
            String pwd = ProjectUtil.MD5(password + user.getSalt());
            if(user.getPassword().equals(pwd)) {
                return true;
            }else {
                return false;
            }
        } else {//just registered, password:00000000
            if(user.getPassword().equals(password)) {
                return true;
            }else {
                return false;
            }
        }
    }

    //pwd + salt
    @Transactional
    public void changePassword(User user, String newPwd) {
        user.setSalt(ProjectUtil.createSalt());
        user.setPassword(ProjectUtil.MD5(newPwd + user.getSalt()));
        int num = userMapper.updateByPrimaryKeySelective(user);
        if(num!=1) {
            log.error("changePwd错误，num:{}, user:{}", num , JSON.toJSONString(user));
            throw new RuntimeException("修改密码错误");
        }
    }

    @Transactional
    public void changeTradePassword(User user, String newPwd) {
        user.setTradeSalt(ProjectUtil.createSalt());
        user.setTradePassword(ProjectUtil.MD5(newPwd+ user.getTradeSalt()));
        updateUser(user);
    }

    public boolean checkTradePassword(User user, String password) {
        if(Strings.isNotBlank( user.getTradeSalt())) {//salt added
            String pwd = ProjectUtil.MD5(password + user.getTradeSalt());
            if(user.getTradePassword().equals(pwd)) {
                return true;
            }else {
                return false;
            }
        } else {//default tradePassword:11111111
            if(user.getTradePassword().equals(password)) {
                return true;
            }else {
                return false;
            }
        }
    }

    public void resetLoginPwd(Integer id) {

        User user = getUserById(id);
        user.setSalt(ProjectUtil.createSalt());
        user.setPassword(ProjectUtil.MD5(ConstantUtil.MEMBER_INITIAL_PASSWORD+ user.getSalt()));
        updateUser(user);
    }

    public void resetTradePwd(Integer id) {
        User user = getUserById(id);
        user.setTradeSalt(ProjectUtil.createSalt());
        user.setTradePassword(ProjectUtil.MD5(ConstantUtil.MEMBER_INITIAL_TRADE_PASSWORD+ user.getTradeSalt()));
        updateUser(user);
    }

    public void userBuyCart(Integer userId, BigDecimal totalPrice) {
        User userById = getUserById(userId);
        if(userById.getShopPoints().compareTo(totalPrice)<0) {
            log.error("会员购物积分不足，无法购买. totalPrice:{}, user:{}", totalPrice, JSON.toJSONString(userById));
            throw new RuntimeException("会员购物积分不足，无法购买");
        }
        userById.setShopPoints(userById.getShopPoints().subtract(totalPrice));
        updateUser(userById);
    }

}

package com.zym.Design1.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zym.Design1.dao.TradeMapper;
import com.zym.Design1.entity.*;
import com.zym.Design1.mydao.AdminTradeMapper;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.ProjectUtil;
import com.zym.Design1.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/3/21.
 */
@Service
@Slf4j
public class TradeService {

    @Autowired
    private TradeMapper tradeMapper;

    @Autowired
    private AdminTradeMapper adminTradeMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserBonusService userBonusService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 实际购买
     * @param buyer 买家
     * @param id 挂单id
     */
//    public void buyVc (User buyer, Long id){
//
//        checkTradeTime();
//
//        //update交易记录
//        Trade trade = getTrade(id);//挂单
//        if(!trade.getState().equals(ConstantUtil.STATE_VALID)) {
//            throw new RuntimeException("该挂单已失效");
//        }
////        trade.setBuyerId(buyer.getId());//sellerId, sellerUid 挂单时已赋值
////        trade.setBuyerUid(buyer.getUid());
////        trade.setTradeDate(new Date());
//        trade.setState(ConstantUtil.STATE_SOLD);//卖出状态
//
//        //用户信息 buyer:total_coin, tradable_coin, ecoin.
//        //        seller:total_coin, on_sale_coin, ecoin.      //tradable_coin(挂单时已转移到on_sale_coin,此处不改变),
//
//        //买家：ecoin减少，vc (total_coin, tradable_coin) 增加; 付出电子币，买到虚拟货币。
//        buyer.setTradableCoin(buyer.getTradableCoin().add(trade.getAmount()));
//        buyer.setTotalCoin(buyer.getTotalCoin().add(trade.getAmount()));
//        //判断 如果买家的电子币不足以支付此单
//        if(buyer.getEcoin().compareTo(trade.getEcoin()) < 0 ) {
//            throw new RuntimeException("买家电子币余额不足");
//        }
//        buyer.setEcoin(buyer.getEcoin().subtract(trade.getEcoin()));
//        userService.updateUser(buyer);
//        //卖家：cash_coin增加，vc(total_coin, on_sale_coin)减少；卖出虚拟货币，得到现金币；sold_coin已卖出增加
//        User seller = userService.getUserById(trade.getSellerId());
//        if(seller.getTotalCoin().compareTo(trade.getAmount()) < 0 ||
//                                seller.getOnSaleCoin().compareTo(trade.getAmount())<0) {
//            throw new RuntimeException("卖家KMC不足");
//        }
//        seller.setTotalCoin(seller.getTotalCoin().subtract(trade.getAmount()));
//        seller.setOnSaleCoin(seller.getOnSaleCoin().subtract(trade.getAmount()));
//        seller.setSoldCoin(seller.getSoldCoin().add(trade.getAmount()));
//        seller.setCashCoin(seller.getCashCoin().add(trade.getEcoin()));//buyer.ecoin -> seller.cashCoin
//        userService.updateUser(seller);
//
//        updateTrade(trade);
////        tradeMapper.updateByExample()->id, state  返回值，更新了几条？？
//
//        //给上级奖励
//        Map<Integer, User> map = new HashMap();
//        orgService.getAncestorsMap(buyer.getId(), map);
//        for(Map.Entry<Integer, User> entry : map.entrySet()) {
//            User parent = entry.getValue();
//            BigDecimal reward = trade.getAmount().multiply(ConstantUtil.RECOMMEND_RATE);
//            parent.setTotalCoin(parent.getTotalCoin().add(reward));
//            parent.setTradableCoin(parent.getTradableCoin().add(reward));
//            //更新上级
//            userService.updateUser(parent);
//            //记录上级奖励
//            tradeRewardService.addTradeReward(parent.getId(), buyer.getId(), buyer.getUid(), reward, trade.getId());
//        }
//    }
    /**
     * 实际售卖
     * @param seller 卖家
     * @param id 求购id
     */
//    public void sellVc (User seller, Long id){
//
//        checkTradeTime();
//
//        //update交易记录
//        Trade trade = getTrade(id);//求购单
//        if(!trade.getState().equals(ConstantUtil.STATE_VALID)) {
//            throw new RuntimeException("该求购已失效");
//        }
////        trade.setSellerUid(seller.getUid());//buyer, buyerUid 求购时已赋值
////        trade.setSellerId(seller.getId());
////        trade.setTradeDate(new Date());
//        trade.setState(ConstantUtil.STATE_SOLD);//卖出状态
//
//        //卖家seller: cashCoin, totalCoin, tradableCoin      (此处不涉及on_sale_coin)
//        //cash_coin增加，vc(total_coin,tradableCoin)减少；卖出虚拟货币，得到现金币；sold_coin已卖出增加
//        if(seller.getTradableCoin().multiply(ConstantUtil.MAX_SELL_RATE).compareTo(trade.getAmount())<0) {
//            throw new RuntimeException("卖家可用KMC不足，最多只能交易90%");
//        }
//        if(seller.getTotalCoin().multiply(ConstantUtil.MAX_SELL_RATE).compareTo(trade.getAmount())<0) {
//            throw new RuntimeException("卖家KMC不足，最多只能交易90%");
//        }
//        seller.setTotalCoin(seller.getTotalCoin().subtract(trade.getAmount()));
//        seller.setTradableCoin(seller.getTradableCoin().subtract(trade.getAmount()));
//        seller.setCashCoin(seller.getCashCoin().add(trade.getEcoin()));
//        seller.setSoldCoin(seller.getSoldCoin().add(trade.getAmount()));
//        userService.updateUser(seller);
//
//        //买家buyer: on_buy_ecoin, tradableCoin, totalCoin
//        //求购中的电子币减去本次交易的电子币， buyer.ecoin已在求购请求中转移到on_buy_ecoin
//        //on_buy_ecoin，vc (total_coin, tradable_coin) 增加; 付出电子币，买到虚拟货币
//        User buyer = userService.getUserById(trade.getBuyerId());
//        if(buyer.getOnBuyEcoin().compareTo(trade.getEcoin())<0) {
//            throw new RuntimeException("买家求购信息错误");
//        }
//        buyer.setOnBuyEcoin(buyer.getOnBuyEcoin().subtract(trade.getEcoin()));//求购中的电子币减去本次交易的电子币， buyer.ecoin已在求购请求中转移到on_buy_ecoin
//        buyer.setTradableCoin(buyer.getTradableCoin().add(trade.getAmount()));
//        buyer.setTotalCoin(buyer.getTotalCoin().add(trade.getAmount()));
//        userService.updateUser(buyer);
//
//        updateTrade(trade);
//
//        //给上级奖励
//        Map<Integer, User> map = new HashMap();
//        orgService.getAncestorsMap(buyer.getId(), map);
//        for(Map.Entry<Integer, User> entry : map.entrySet()) {
//            User parent = entry.getValue();
//            BigDecimal reward = trade.getAmount().multiply(ConstantUtil.RECOMMEND_RATE);
//            parent.setTotalCoin(parent.getTotalCoin().add(reward));
//            parent.setTradableCoin(parent.getTradableCoin().add(reward));
//            //更新上级
//            userService.updateUser(parent);
//            //记录上级奖励
//            tradeRewardService.addTradeReward(parent.getId(), buyer.getId(), buyer.getUid(), reward, trade.getId());
//        }
//    }

    /**
     * 挂卖 419
     * @param seller 卖家
     * @param vc 虚拟货币单价
     * @param amount 购买虚拟货币数量，
     *               单价*数量= 卖出可获得的财富积分
     */
    @Transactional
    public void wantToSell(User seller, BigDecimal vc, BigDecimal amount) {

        if(!seller.getRole().equals(ConstantUtil.ROLE_ADMIN)) {
            ProjectUtil.checkTradeTime();
        }
        checkCanSellToday(seller.getId());
        log.info("before sell, seller:{}, wantToSell- vc:{}, amount:{}", JSON.toJSONString(seller), vc, amount);
        //没有限制了
        /*if(seller.getTradableCoin().multiply(ConstantUtil.MAX_SELL_RATE).compareTo(amount)<0) {
            throw new RuntimeException("卖家可用KMC不足，最多只能交易90%");
        }*/

        //更新用户tradable_coin -> on_sale_coin , 可交易减少，转移到售卖中，售卖中增加
        seller.setOnSaleCoin(seller.getOnSaleCoin().add(amount));
//        seller.setTotalCoin(seller.getTotalCoin().subtract(amount));
        seller.setTradableCoin(seller.getTradableCoin().subtract(amount));
        userService.updateUser(seller);

        Date now = new Date();
        Trade maxBuyOrder = getMaxBuyOrder();//价最高的买单
        if(maxBuyOrder==null || maxBuyOrder.getVcPrice().compareTo(vc) < 0 ) { //出价高没法卖 或者 还没人买
            //挂卖单
            Trade trade = new Trade();
            trade.setOrderDate(now);//挂单时间
            trade.setAmount(amount);
            trade.setVcPrice(vc);
            trade.setEcoin(vc.multiply(amount));
            trade.setState(ConstantUtil.STATE_VALID);
            trade.setSellerId(seller.getId());
            trade.setSellerUid(seller.getUid());
            trade.setType(ConstantUtil.TYPE_SELL_ORDER);//sell 0
            trade.setSrcId(0L);
            insertTrade(trade);
            log.info("出价高没法卖 或者 还没人买. 挂卖单. trade:{}", JSON.toJSONString(trade));

        } else if(maxBuyOrder.getVcPrice().compareTo(vc) >=0) {//可以卖，有买单, 买单单价>该卖单
            log.info("可以卖，有买单, 买单单价>该卖单. ");
            amount = sellWhenExistBuy(now, seller, amount, maxBuyOrder);

            if(amount.compareTo(BigDecimal.ZERO)>0) {//amount>0,取下一个买单
                log.info("amount>0,取下一个买单, amount:{}", amount);
                //continue 下一次循环
                List<Trade> buyOrderList = getBuyOrderList(null, vc);
                int index=0;
                while(index < buyOrderList.size() && amount.compareTo(BigDecimal.ZERO) >0  ) {
                    Trade orderMax = buyOrderList.get(index);
                    log.info("取下一个买单, orderMax:{}", JSON.toJSONString(orderMax));
                    amount = sellWhenExistBuy(now, seller, amount, orderMax);
                    index++;
                }
                if(amount.compareTo(BigDecimal.ZERO)>0) {//卖单剩
                    //挂卖单
                    Trade trade = new Trade();
                    trade.setOrderDate(now);//挂单时间
                    trade.setAmount(amount);
                    trade.setVcPrice(vc);
                    trade.setEcoin(vc.multiply(amount));
                    trade.setState(ConstantUtil.STATE_VALID);
                    trade.setSellerId(seller.getId());
                    trade.setSellerUid(seller.getUid());
                    trade.setType(ConstantUtil.TYPE_SELL_ORDER);//sell 0
                    trade.setSrcId(0L);
                    insertTrade(trade);
                    log.info("卖单剩.挂卖单. trade:{}", JSON.toJSONString(trade));
                }
            }
        }
        setAlreadySellToday(seller.getId());//设置今天已挂卖过
    }

    @Transactional
    private BigDecimal sellWhenExistBuy(Date now, User seller, BigDecimal amount, Trade maxBuyOrder) {
//        for(Trade order : buyOrderList) {
            BigDecimal buyAmount = maxBuyOrder.getAmount();
            if(buyAmount.compareTo(amount) == 0 ) {//正好可以卖出
                log.info("正好可以卖出, before sell, seller:{} amount:{}, maxBuyOrder:{}", JSON.toJSONString(seller), amount, JSON.toJSONString(maxBuyOrder));
                maxBuyOrder.setSellerId(seller.getId());
                maxBuyOrder.setSellerUid(seller.getUid());
                maxBuyOrder.setTradeDate(now);
                maxBuyOrder.setState(ConstantUtil.STATE_SOLD);//交易完成
                updateTrade(maxBuyOrder);
                //更新卖家信息
                updateSeller(seller, maxBuyOrder);
                //更新买家信息+bonus+买家每天释放
                updateBuyerAndBonus(maxBuyOrder, null);
//                    break;//跳出循环
                amount = amount.subtract(maxBuyOrder.getAmount());//amount=0

                //
                seller = userService.getUserById(seller.getId());
                log.info("正好可以卖出, after sell, seller:{}, amount=0:{} ", JSON.toJSONString(seller), amount);

            }else if(amount.compareTo(buyAmount) < 0) {//买单大，卖单小，
                log.info("买单大，卖单小,拆买单. before sell, maxBuyOrder:{}", JSON.toJSONString(maxBuyOrder));
                //拆买单
                BigDecimal originAmount = maxBuyOrder.getAmount();
                if(maxBuyOrder.getSrcId().equals(0L)) {
                    maxBuyOrder.setSrcId(maxBuyOrder.getId());
                }
                maxBuyOrder.setState(ConstantUtil.STATE_SOLD);
                maxBuyOrder.setSellerUid(seller.getUid());
                maxBuyOrder.setSellerId(seller.getId());
                maxBuyOrder.setAmount(amount);//完成的交易数量，按卖单(小）
                maxBuyOrder.setEcoin(maxBuyOrder.getVcPrice().multiply(maxBuyOrder.getAmount()));
                maxBuyOrder.setTradeDate(now);
                updateTrade(maxBuyOrder);//原买单交易完成
                log.info("买单大，卖单小,拆买单. after sell, maxBuyOrder:{}", JSON.toJSONString(maxBuyOrder));
                //更新卖家信息
                updateSeller(seller, maxBuyOrder);
                //更新买家信息+bonus+买家每天释放
                updateBuyerAndBonus(maxBuyOrder, null);

                //
                seller = userService.getUserById(seller.getId());

                //剩余买单，重新建单
                Trade orderNew = new Trade();
                orderNew.setSrcId(maxBuyOrder.getSrcId());
                orderNew.setOrderDate(maxBuyOrder.getOrderDate());
                orderNew.setBuyerId(maxBuyOrder.getBuyerId());
                orderNew.setBuyerUid(maxBuyOrder.getBuyerUid());
                orderNew.setVcPrice(maxBuyOrder.getVcPrice());
                orderNew.setAmount(originAmount.subtract(amount));//买单剩余的数量
                orderNew.setEcoin(orderNew.getAmount().multiply(orderNew.getVcPrice()));
                orderNew.setType(maxBuyOrder.getType());
                orderNew.setRuleId(maxBuyOrder.getRuleId());
                insertTrade(orderNew);

//                    break;//跳出循环
                amount = amount.subtract(originAmount);//amount<0
                log.info("剩余买单amount:{}，重新建单, orderNew:{}", amount, JSON.toJSONString(orderNew));
            } else { //amount > buyAmount
                //卖单数量超过此买单, 需要循环
                //拆卖单
                //完成该买单
                log.info("卖单数量超过此买单.拆卖单.  before sell, maxBuyOrder:{}", JSON.toJSONString(maxBuyOrder));
                maxBuyOrder.setState(ConstantUtil.STATE_SOLD);
                maxBuyOrder.setSellerUid(seller.getUid());
                maxBuyOrder.setSellerId(seller.getId());//amount 不修改，按原买单
                maxBuyOrder.setTradeDate(now);
                updateTrade(maxBuyOrder);//原买单交易完成

                //更新卖家信息
                updateSeller(seller, maxBuyOrder);
                //更新买家信息+bonus+买家每天释放
                updateBuyerAndBonus(maxBuyOrder, null);

                amount = amount.subtract(maxBuyOrder.getAmount());//amount>0
                log.info("卖单数量超过此买单.拆卖单. 剩余amount:{}, after sell, maxBuyOrder:{}", amount, JSON.toJSONString(maxBuyOrder));
                //
                seller = userService.getUserById(seller.getId());

            }
            return amount;
//            }//如果5次循环结束，卖单还有剩余 怎么办？？？
    }

    //更新卖家信息
    // 卖家：cash_coin增加，vc(total_coin, on_sale_coin)减少；卖出虚拟货币，得到现金币；sold_coin已卖出增加
    @Transactional
    public void updateSeller(User seller, Trade trade ) {
//        if(seller==null) {
            seller = userService.getUserById(trade.getSellerId());
        log.info("before updateSeller:{}", JSON.toJSONString(seller));
//        }
        if(seller.getOnSaleCoin().compareTo(trade.getAmount())<0) {
            throw new RuntimeException("卖家信息错误");
        }
        seller.setTotalCoin(seller.getTotalCoin().subtract(trade.getAmount()));//???
        seller.setOnSaleCoin(seller.getOnSaleCoin().subtract(trade.getAmount()));
        seller.setSoldCoin(seller.getSoldCoin().add(trade.getAmount()));
        seller.setCashCoin(seller.getCashCoin().add(trade.getEcoin()));//buyer.ecoin -> seller.cashCoin
        userService.updateUser(seller);
        log.info("after updateSeller:{}", JSON.toJSONString(seller));
    }

    @Transactional
    public void updateBuyerAndBonus(Trade order, User buyer) {
        //买家buyer: on_buy_ecoin, totalCoin, releaseFrozenCoin     (tradableCoin,不修改，锁仓了)
        //求购中的电子币减去本次交易的电子币， buyer.ecoin已在求购请求中转移到on_buy_ecoin
        //on_buy_ecoin，vc (total_coin, tradable_coin) 增加; 付出电子币，买到虚拟货币
//        if(buyer==null) {//卖
            buyer = userService.getUserById(order.getBuyerId());
        log.info("before update buyer:{}", JSON.toJSONString(buyer));
//        }
        if (buyer.getOnBuyEcoin().compareTo(order.getEcoin()) < 0) {
            log.error("买家求购信息错误, buyer.getOnBuyEcoin()<(order.getEcoin()). {} < {}", buyer.getOnBuyEcoin(), order.getEcoin());
            throw new RuntimeException("买家求购信息错误");
        }
        buyer.setOnBuyEcoin(buyer.getOnBuyEcoin().subtract(order.getEcoin()));//求购中的电子币减去本次交易的电子币， buyer.ecoin已在求购请求中转移到on_buy_ecoin
        buyer.setTotalCoin(buyer.getTotalCoin().add(order.getAmount()));
        if(buyer.getRole().equals(ConstantUtil.ROLE_MEMBER)) {//会员锁仓，admin不锁仓
            buyer.setReleaseFrozenCoin(buyer.getReleaseFrozenCoin().add(order.getAmount()));//锁仓, 不增加tradableCoin
        }
        else if(buyer.getRole().equals(ConstantUtil.ROLE_ADMIN)) {//admin不锁仓，直接释放可用
            buyer.setTradableCoin(buyer.getTradableCoin().add(order.getAmount()));
        }else {
            log.error("没有权限买单, buyer:{}", JSON.toJSONString(buyer));
            throw new RuntimeException("没有权限买单");
        }
        userService.updateUser(buyer);//更新买家信息
        log.info("after update buyer:{}", JSON.toJSONString(buyer));
        //trade bonus
        //交易完成当时，buyer没得到tradable_coin, 但是buyer的上级得到了10%的coin ????
        //不需要了，只是每天释放的奖励的10%
        /*Map<Integer, User> map = new HashMap();
        orgService.getAncestorsMap(order.getBuyerId(), map);//买家上级
        for(Map.Entry<Integer, User> entry : map.entrySet()) {
            User parent = entry.getValue();
            BigDecimal reward = order.getAmount().multiply(ConstantUtil.RECOMMEND_RATE);
            parent.setTotalCoin(parent.getTotalCoin().add(reward));
            parent.setTradableCoin(parent.getTradableCoin().add(reward));//？？
            //更新上级
            userService.updateUser(parent);
            //记录上级奖励
            tradeRewardService.addTradeReward(parent.getId(), order.getBuyerId(), order.getBuyerUid(), reward, order.getId());
        }*/
        //或者trade bonus 部分不进行记录了，删除trade bonus部分，放在user锁仓结束后，释放全部锁仓货币时，给推荐人10%的奖励
        ///？？？？

        //daily bonus
        if(buyer.getRole().equals(ConstantUtil.ROLE_ADMIN)) {//admin特权，不锁仓，不奖励
            return;
        }
        userBonusService.addReleaseRule(buyer, order.getEcoin(), order, ConstantUtil.TYPE_LATE_PHASE);
    }

    /**
     * 撤销挂卖请求
     * @param id
     */
    @Transactional
    public void cancelSellOrder(User user, Long id) {
        if(!user.getRole().equals(ConstantUtil.ROLE_ADMIN)) {
            ProjectUtil.checkTradeTime();
        }
        Trade tradeOrder = getTrade(id);
        log.info("before cancelSellOrder, tradeOrder:{}", JSON.toJSONString(tradeOrder));
        if(!user.getId().equals(tradeOrder.getSellerId())) {
            log.error("Id={}会员撤销挂卖，会员不是卖家, {}", JSON.toJSONString(user), JSON.toJSONString(tradeOrder));
            throw new RuntimeException( "挂卖信息错误");
        }
        if(!tradeOrder.getState().equals(ConstantUtil.STATE_VALID)) {
             throw new RuntimeException( "挂单已无效");
        }
        tradeOrder.setState(ConstantUtil.STATE_INVALID);//将挂单状态置为无效
        tradeOrder.setTradeDate(new Date());//撤销时间
        //更新用户on_sale_coin, tradable_coin
        if(user.getOnSaleCoin().compareTo(tradeOrder.getAmount())<0) {
            log.info("Id={}会员撤销求购，挂单信息错误on_sale_coin < tradeTorder.getAmount(), {}", JSON.toJSONString(user), JSON.toJSONString(tradeOrder));
            throw new RuntimeException("挂单信息错误");
        }
        //on_sale, tradable_coin, total_coin
        user.setOnSaleCoin(user.getOnSaleCoin().subtract(tradeOrder.getAmount()));
        user.setTradableCoin(user.getTradableCoin().add(tradeOrder.getAmount()));
//        user.setTotalCoin(user.getTotalCoin().add(tradeOrder.getAmount()));//??
        userService.updateUser(user);

        updateTradeOfState(tradeOrder, ConstantUtil.STATE_VALID);
        log.info("after cancelSellOrder, tradeOrder:{}", JSON.toJSONString(tradeOrder));
        //是否撤销的是今天的挂卖
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        Calendar calendarOrder = Calendar.getInstance();
        calendarOrder.setTime(tradeOrder.getOrderDate());
        if(calendar.get(Calendar.YEAR)==calendarOrder.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == calendarOrder.get(Calendar.DAY_OF_YEAR)) {
            cancelSellToday(user.getId());//redis,一天只能挂卖2次设置
        }
    }

    /**
     * 提交挂买
     * @param buyer 买家
     * @param vc 虚拟货币单价
     * @param amount 购买虚拟货币数量，
     *               单价*数量= 卖出可获得的电子币数量ecoin
     *    买家：ecoin转移到 on_buy_ecoin,
     */
    @Transactional
    public  void  wantToBuy(User buyer, BigDecimal vc, BigDecimal amount, Integer ruleId) {
        if(!buyer.getRole().equals(ConstantUtil.ROLE_ADMIN)) {
            ProjectUtil.checkTradeTime();
        }
        if(!buyer.getRole().equals(ConstantUtil.ROLE_ADMIN)) {
            if(ruleId==null) {
                log.error("未选择锁仓套餐");
                throw new RuntimeException("未选择锁仓套餐!");
            }
        }
        BigDecimal ecoin = vc.multiply(amount);
        log.info("before 转移 buyer:{}", JSON.toJSONString(buyer));
        if(buyer.getEcoin().compareTo(ecoin)<0) {
            log.info("id={}买家积分不足buyer.getEcoin()<(ecoin), buyer:{}, vc:{}, amount:{}, ecoin:{} ", buyer.getId(), JSON.toJSONString(buyer), vc, amount, ecoin);
            throw new RuntimeException("买家积分不足");
        }
        buyer.setEcoin(buyer.getEcoin().subtract(ecoin));
        //买家：ecoin转移到 on_buy_ecoin
        buyer.setOnBuyEcoin(buyer.getOnBuyEcoin().add(ecoin));
        userService.updateUser(buyer);
        log.info("after 买家的ecoin转移到 on_buy_ecoin. buyer:{}", JSON.toJSONString(buyer));
        Date now = new Date();
        Trade minSellOrder = getMinSellOrder();//价最高的买单
        log.info("价最高的买单, minSellOrder:{}", JSON.toJSONString(minSellOrder));
        if(minSellOrder==null ||minSellOrder.getVcPrice().compareTo(vc) > 0 ) { //出价低没法买 或者 还没人卖
            //挂买单
            Trade order = new Trade();
            order.setOrderDate(now);//求购时间
            order.setBuyerId(buyer.getId());
            order.setBuyerUid(buyer.getUid());
            order.setVcPrice(vc);
            order.setAmount(amount);//货币数量
            order.setEcoin(vc.multiply(amount));
            order.setType(ConstantUtil.TYPE_BUY_ORDER);
            order.setState(ConstantUtil.STATE_VALID);
            order.setRuleId(ruleId);
            order.setSrcId(0L);

            log.info("出价低没法买 或者 还没人卖. 挂买单. order:{}", JSON.toJSONString(order));
            insertTrade(order);

        } else if(minSellOrder.getVcPrice().compareTo(vc) <=0) {//可以买，有卖单
            log.info("可以买，有卖单. before minSellOrder:{}", JSON.toJSONString(minSellOrder));
            amount = buyWhenExistSell(now, buyer, amount, minSellOrder, ruleId);

            if(amount.compareTo(BigDecimal.ZERO)>0) {//amount>0,取下一个卖单
                //continue 下一次循环
                log.info("amount>0,取下一个卖单. amount:{}", amount);
                List<Trade> sellOrderList = getSellOrderList(null, vc);
//                if (sellOrderList.size() > 0) {
                    int index = 0;
                    while (index < sellOrderList.size() && amount.compareTo(BigDecimal.ZERO) > 0) {
                        Trade orderMin = sellOrderList.get(index);
                        log.info("orderMin:{}", JSON.toJSONString(orderMin));
                        amount = buyWhenExistSell(now, buyer, amount, orderMin, ruleId);
                        index++;
                    }
//                }
                //买单剩
                if(amount.compareTo(BigDecimal.ZERO) >0){
                    //挂买单
                    Trade order = new Trade();
                    order.setOrderDate(now);//求购时间
                    order.setBuyerId(buyer.getId());
                    order.setBuyerUid(buyer.getUid());
                    order.setVcPrice(vc);
                    order.setAmount(amount);//货币数量
                    order.setEcoin(vc.multiply(amount));
                    order.setType(ConstantUtil.TYPE_BUY_ORDER);
                    order.setState(ConstantUtil.STATE_VALID);
                    order.setRuleId(ruleId);
                    order.setSrcId(0L);

                    insertTrade(order);
                    log.info("买单剩,挂买单. order:{}", JSON.toJSONString(order));
                }

            }
        }
    }

    @Transactional
    private BigDecimal buyWhenExistSell(Date now, User buyer, BigDecimal amount, Trade minSellOrder, Integer ruleId) {
        BigDecimal sellAmount = minSellOrder.getAmount();
        if(sellAmount.compareTo(amount) == 0 ) {//正好可以买
            minSellOrder.setBuyerId(buyer.getId());
            minSellOrder.setBuyerUid(buyer.getUid());
            minSellOrder.setTradeDate(now);
            minSellOrder.setRuleId(ruleId);//rule
            minSellOrder.setState(ConstantUtil.STATE_SOLD);//交易完成

            updateTrade(minSellOrder);
            //更新卖家信息
            updateSeller(null, minSellOrder);
            //更新买家信息+bonus+买家每天释放
            updateBuyerAndBonus(minSellOrder, buyer);
//                    break;//跳出循环
            amount = amount.subtract(minSellOrder.getAmount());//amount=0
            log.info("正好可以买,交易完成. minSellOrder:{}, amount:{}", JSON.toJSONString(minSellOrder), amount);
        }else if(amount.compareTo(sellAmount) < 0) {//买单小，卖单大
            BigDecimal originAmount = minSellOrder.getAmount();
            //拆卖单
            if(minSellOrder.getSrcId().equals(0L)) {
                minSellOrder.setSrcId(minSellOrder.getId());
            }
            minSellOrder.setState(ConstantUtil.STATE_SOLD);
            minSellOrder.setBuyerUid(buyer.getUid());
            minSellOrder.setBuyerId(buyer.getId());
            minSellOrder.setAmount(amount);//完成的交易数量，按买单(小）
            minSellOrder.setEcoin(minSellOrder.getAmount().multiply(minSellOrder.getVcPrice()));
            minSellOrder.setTradeDate(now);
            minSellOrder.setRuleId(ruleId);
            updateTrade(minSellOrder);//原卖单交易完成
            log.info("买单小，卖单大,拆卖单,原卖单交易完成. minSellOrder:{}", JSON.toJSONString(minSellOrder));
            //更新卖家信息
            updateSeller(null, minSellOrder);
            //更新买家信息+bonus+买家每天释放
            updateBuyerAndBonus(minSellOrder, buyer);

            //剩余卖单，重新建单
            Trade orderNew = new Trade();
            orderNew.setSrcId(minSellOrder.getSrcId());
            orderNew.setOrderDate(minSellOrder.getOrderDate());
            orderNew.setSellerId(minSellOrder.getSellerId());
            orderNew.setSellerUid(minSellOrder.getSellerUid());
            orderNew.setVcPrice(minSellOrder.getVcPrice());
            orderNew.setAmount(originAmount.subtract(amount));//卖单剩余的数量
            orderNew.setEcoin(orderNew.getAmount().multiply(orderNew.getVcPrice()));
            orderNew.setType(minSellOrder.getType());
            insertTrade(orderNew);

//                    break;//跳出循环
            amount = amount.subtract(originAmount);//amount<0
            log.info("剩余卖单，重新建单. orderNew:{}, amount:{}", JSON.toJSONString(orderNew), amount);
        } else { //amount > sellAmount
            //买单数量超过此卖单, 需要循环
            //拆买单
            //完成该此单
            minSellOrder.setState(ConstantUtil.STATE_SOLD);
            minSellOrder.setBuyerUid(buyer.getUid());
            minSellOrder.setBuyerId(buyer.getId());//amount 不修改，按原卖单
            minSellOrder.setTradeDate(now);
            minSellOrder.setRuleId(ruleId);//rule
            updateTrade(minSellOrder);//原买单交易完成
            //更新卖家信息
            updateSeller(null, minSellOrder);
            //更新买家信息+bonus+买家每天释放
            updateBuyerAndBonus(minSellOrder, buyer);

            amount = amount.subtract(minSellOrder.getAmount());//amount>0
            log.info("买单数量超过此卖单,拆买单,完成该此单. minSellOrder:{}, 剩余 amount:{}", JSON.toJSONString(minSellOrder), amount);
        }
        return amount;
    }
    /**
     * 撤销求购请求
     * @param id
     */
    @Transactional
    public void cancelBuyOrder(User user, Long id) {
        if(!user.getRole().equals(ConstantUtil.ROLE_ADMIN)) {
            ProjectUtil.checkTradeTime();
        }
        Trade order = getTrade(id);
        log.info("before order:{}", JSON.toJSONString(order));
        if(!user.getId().equals(order.getBuyerId())) {
            log.error("Id={}会员撤销挂买，会员不是买家user.getId()!=order.getBuyerId(), {}", JSON.toJSONString(user), JSON.toJSONString(order));
            throw new RuntimeException( "挂买信息错误");
        }
        if(!order.getState().equals(ConstantUtil.STATE_VALID)) {
            throw new RuntimeException( "挂买已无效");
        }
        if(user.getOnBuyEcoin().compareTo(order.getEcoin())<0) {
            log.info("Id={}会员撤销求购，求购信息错误on_buy_ecoin<order.ecoin, {}", JSON.toJSONString(user), JSON.toJSONString(order));
            throw new RuntimeException("挂买信息错误");
        }
        order.setState(ConstantUtil.STATE_INVALID);
        order.setLastModifiedDate(new Date());
        updateTradeOfState(order, ConstantUtil.STATE_VALID);
        log.info("after order:{}", JSON.toJSONString(order));
        //更新用户 on_buy_ecoin, ecoin  //on_buy_ecoin 返还给 ecoin
        user.setEcoin(user.getEcoin().add(order.getEcoin()));
        user.setOnBuyEcoin(user.getOnBuyEcoin().subtract(order.getEcoin()));
        userService.updateUser(user);
        log.info("更新用户on_buy_ecoin 返还给 ecoin, user:{}", JSON.toJSONString(user));
    }




    /**
     * 挂单列表
     */
    public List<Trade> getOrderList(Integer pageIndex, Integer pageSize, String sort, String sortOrder, String searchKey, String type) {
        TradeExample example = new TradeExample();
        example.createCriteria().andTypeEqualTo(type).andStateEqualTo(ConstantUtil.STATE_VALID);//挂单状态
        if(sort != null) {
            sort = ProjectUtil.camelToUnderline(sort);
            example.setOrderByClause(sort + " " + sortOrder);
        }
        PageHelper.startPage(pageIndex, pageSize);
        return tradeMapper.selectByExample(example);
    }

    public List<Trade> getBuyOrderList( Integer pageSize, BigDecimal price) {//价格高->低
        TradeExample example = new TradeExample();
        TradeExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(ConstantUtil.TYPE_BUY_ORDER).andStateEqualTo(ConstantUtil.STATE_VALID);//挂买状态
        if(price!=null) {
            criteria.andVcPriceGreaterThanOrEqualTo(price);
        }
        example.setOrderByClause(" vc_price desc, order_date asc");
        if(pageSize!=null) {
            PageHelper.startPage(1, pageSize);
        }
        return tradeMapper.selectByExample(example);
    }

    //admin
    public List<Trade> getBuyOrderListByPage(Integer pageIndex, Integer pageSize, String userUid) {
        //价格高->低
        TradeExample example = new TradeExample();
        TradeExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(ConstantUtil.TYPE_BUY_ORDER).andStateEqualTo(ConstantUtil.STATE_VALID);//挂买状态
        if(userUid!=null) {
            criteria.andBuyerUidLike(userUid + "%");
        }
        example.setOrderByClause(" vc_price desc, order_date asc");
        PageHelper.startPage(pageIndex, pageSize);
        return tradeMapper.selectByExample(example);
    }

    public Trade getMaxBuyOrder() {//价格最高的买单
        List<Trade> trades = getBuyOrderList(1, null);
        if(trades!=null && trades.size()==1) {
            return trades.get(0);
        }else{
            return null;
        }
    }

    public List<Trade> getSellOrderList(Integer pageSize, BigDecimal price) {//价格低->高
        TradeExample example = new TradeExample();
        TradeExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(ConstantUtil.TYPE_SELL_ORDER).andStateEqualTo(ConstantUtil.STATE_VALID);//挂卖状态
        if(price!=null) {
            criteria.andVcPriceLessThanOrEqualTo(price);
        }
        example.setOrderByClause(" vc_price asc, order_date asc");
        if(pageSize!=null) {
            PageHelper.startPage(1, pageSize);
        }
        return tradeMapper.selectByExample(example);
    }

    //admin
    public List<Trade> getSellOrderListByPage(Integer pageIndex, Integer pageSize, String userUid) {
        //价格低->高
        TradeExample example = new TradeExample();
        TradeExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(ConstantUtil.TYPE_SELL_ORDER).andStateEqualTo(ConstantUtil.STATE_VALID);//挂卖状态
        if(userUid!=null) {
            criteria.andSellerUidLike(userUid + "%");
        }
        example.setOrderByClause(" vc_price asc, order_date asc");
        PageHelper.startPage(pageIndex, pageSize);
        return tradeMapper.selectByExample(example);
    }

    public Trade getMinSellOrder() {//价格最低的卖单
        List<Trade> trades = getSellOrderList(1, null);
        if(trades!=null && trades.size()==1) {
            return trades.get(0);
        }else{
            return null;
        }
    }

    /**
     * 卖出列表
     */
    public List<Trade> getSoldList(Integer pageIndex, Integer pageSize, Integer userId) {
        TradeExample example = new TradeExample();
        if(userId==null) {
            TradeExample.Criteria criteria = example.createCriteria();
            criteria.andStateEqualTo(ConstantUtil.STATE_SOLD);
        } else { //(searchKey!=null)
            example.createCriteria().andBuyerIdEqualTo(userId).andStateEqualTo(ConstantUtil.STATE_SOLD);
            TradeExample.Criteria criteria2 = example.createCriteria().andSellerIdEqualTo(userId).andStateEqualTo(ConstantUtil.STATE_SOLD);
            example.or(criteria2);
        }
        example.setOrderByClause(" trade_date desc, order_date desc, vc_price desc ");
        PageHelper.startPage(pageIndex, pageSize);
        return tradeMapper.selectByExample(example);
    }

    //admin
    public List<Trade> getSoldListByPage(Integer pageIndex, Integer pageSize, String  buyerUid, String sellerUid, String searchDate) {

        PageHelper.startPage(pageIndex, pageSize);
        return adminTradeMapper.selectSoldTradeByDate(buyerUid+"%", sellerUid+"%", searchDate, ConstantUtil.STATE_SOLD);
    }

    public List<Trade> getListForMyTrade(Integer pageIndex, Integer pageSize, String order, String type, Integer userId) {
        TradeExample example = new TradeExample();
        if(userId==null) {
            TradeExample.Criteria criteria = example.createCriteria();
            criteria.andStateEqualTo(ConstantUtil.STATE_SOLD);
        } else { //(searchKey!=null)
            example.createCriteria().andBuyerIdEqualTo(userId).andStateEqualTo(type);
            TradeExample.Criteria criteria2 = example.createCriteria().andSellerIdEqualTo(userId).andStateEqualTo(type);
            example.or(criteria2);
        }
        example.setOrderByClause(order);
        PageHelper.startPage(pageIndex, pageSize);
        return tradeMapper.selectByExample(example);
    }

    public Trade getTrade(Long id) {
        return tradeMapper.selectByPrimaryKey(id);
    }

//    public Torder getOrder(Long id) {
//        return orderMapper.selectByPrimaryKey(id);
//    }

    @Transactional
    public void insertTrade(Trade trade) {
        if(trade.getState()==null) {
            trade.setState(ConstantUtil.STATE_VALID);
        }
        trade.setVersion(1);
        trade.setLastModifiedDate(trade.getOrderDate());
        tradeMapper.insertSelective(trade);
        log.info("insertTrade, trade:{}", trade);
    }

    public Trade getTradeOfEarlyPhase(Integer userId) {
        TradeExample tradeExample = new TradeExample();
        tradeExample.createCriteria().andStateEqualTo(ConstantUtil.TYPE_EARLY_PHASE).andBuyerIdEqualTo(userId)
                .andTypeEqualTo(ConstantUtil.TYPE_BUY_ORDER);
        List<Trade> trades = tradeMapper.selectByExample(tradeExample);
        if(trades!=null && trades.size()==1) {
            log.info("getTradeOfEarlyPhase, trade:{}", JSON.toJSONString(trades.get(0)));
            return trades.get(0);
        }else {
            log.error("锁仓交易信息错误, trades:{}", trades==null? "null": JSON.toJSONString(trades));
            throw new RuntimeException("锁仓交易信息错误");
        }
    }

    @Transactional
    public void updateTrade(Trade trade) {
        log.info("before update trade:{}", JSON.toJSONString(trade));
        trade.setLastModifiedDate(new Date());
//        tradeMapper.updateByPrimaryKeySelective(trade);
        TradeExample example = new TradeExample();
        example.createCriteria().andIdEqualTo(trade.getId());
        int num = tradeMapper.updateByExampleSelective(trade, example);
        if(num!=1) {
            log.error("updateTrade错误，num:{}, trade:{}", num , JSON.toJSONString(trade));
            throw new RuntimeException(" 更新交易错误");
        }
        log.info("after update trade:{}", JSON.toJSONString(trade));
    }

    @Transactional
    public void updateTradeOfState(Trade trade, String state) {
        log.info("before update trade:{}", JSON.toJSONString(trade));
        TradeExample orderExample = new TradeExample();
        orderExample.createCriteria().andStateEqualTo(state).andIdEqualTo(trade.getId());
        int num = tradeMapper.updateByExampleSelective(trade, orderExample);
        if(num!=1) {
            log.error("updateTradeOfState错误， num:{}, trade:{}", num , JSON.toJSONString(trade));
            throw new RuntimeException(" 更新交易错误");
        }
        log.info("after update trade:{}", JSON.toJSONString(trade));
    }



    //一天只能挂卖2次，撤销后可以再次挂卖
    //检查今天是否可以卖出，不可以就抛异常
    @Transactional
    public void checkCanSellToday(Integer userId)  {
        if(userService.getUserById(userId).getRole().equals(ConstantUtil.ROLE_ADMIN)) {//admin特权
            return;
        }
        String s = redisTemplate.opsForValue().get(RedisKeyUtil.getSellTodayKey(userId));
        log.info("checkCanSellToday, userId:{}, s:{}", userId, s);
        //SELL_TPDAY:1:2018-05-01 =>0,1,2
        if(s!=null && Integer.valueOf(s)>1) {
            //同一天
            log.error("每天只能挂卖2次，今天不能再次挂卖. getSellTodayKey(userId):{}, userId:{}", s, userId);
            throw new RuntimeException("每天只能挂卖2次，今天不能再次挂卖!");
        }


        /*if(s != null ) { //之前是这样的  SELL_TODAY:1 => 2018-05-01
            Calendar instance = Calendar.getInstance();
            instance.setTime(now);
            Calendar instanceRecord = Calendar.getInstance();
            Date record = null;
            try {
                record = sdf.parse(s);
            } catch (ParseException e) {
                log.error("checkCanSellToday parse error, {}", e.getCause());
            }
            instanceRecord.setTime(record);
            if (instance.get(Calendar.YEAR) == instanceRecord.get(Calendar.YEAR) &&
                    instance.get(Calendar.DAY_OF_YEAR) == instanceRecord.get(Calendar.DAY_OF_YEAR)) {
                //同一天
                throw new RuntimeException("每天只能挂卖2次，今天不能再次挂卖!");
            }
        }*///else //没值s=null，可能是撤销挂卖后删除了 {
        //只检查，不设值 //String today = sdf.format(now);
//            redisTemplate.opsForValue().set(RedisKeyUtil.getSellTodayKey(userId), today);//设值  }
    }

    //今天挂卖了，需要更新
    @Transactional
    public void setAlreadySellToday(Integer userId) {
        if(userService.getUserById(userId).getRole().equals(ConstantUtil.ROLE_ADMIN)) {//admin特权
            return;
        }
        String s = redisTemplate.opsForValue().get(RedisKeyUtil.getSellTodayKey(userId));
        if(s==null) {//没值 设1
            log.info("userId:{}, setAlreadySellToday", userId);
            redisTemplate.opsForValue().set(RedisKeyUtil.getSellTodayKey(userId), String.valueOf(1),15, TimeUnit.HOURS);//设值
        }else if(Integer.valueOf(s)<2){//s有值<2
            //s=0, then s=1; s=1 then s=2
            int one = Integer.valueOf(s);
            log.info("userId:{}, setAlreadySellToday， one:{}", userId, one);
            one++;
            redisTemplate.opsForValue().set(RedisKeyUtil.getSellTodayKey(userId), String.valueOf(one),15, TimeUnit.HOURS);//设值
        }else {//s=2
            //同一天
            log.error("每天只能挂卖2次，今天不能再次挂卖. getSellTodayKey(userId):{}, userId:{}", s, userId);
            throw new RuntimeException("每天只能挂卖2次，今天不能再次挂卖!");
        }
    }

    @Transactional
    public void cancelSellToday(Integer userId) {
        if(userService.getUserById(userId).getRole().equals(ConstantUtil.ROLE_ADMIN)) {//admin特权
            return;
        }
        String s = redisTemplate.opsForValue().get(RedisKeyUtil.getSellTodayKey(userId));
        if(s!= null ) {
            log.info("userId:{} cancelSellToday getSellTodayKey(userId):{}", userId, s);
            Integer integer = Integer.valueOf(s);
            integer--;
            redisTemplate.opsForValue().set(RedisKeyUtil.getSellTodayKey(userId), String.valueOf(integer), 15, TimeUnit.HOURS);//设值-1
        }else {
            log.error("挂卖次数错误. getSellTodayKey(userId):{}, userId:{}", s, userId);
            throw new RuntimeException("挂卖次数错误！");
        }

    }


}

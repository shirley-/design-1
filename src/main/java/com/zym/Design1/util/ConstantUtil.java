package com.zym.Design1.util;

import java.math.BigDecimal;

/**
 * Created by YM on 2018/3/12.
 */
public class ConstantUtil {

//    public static final String RELATION_PARENT = "1";
//    public static final String RELATION_child = "2";

    public static final String ROLE_MEMBER = "0";
    public static final String ROLE_ADMIN = "2";
    public static final String ROLE_OP = "1";
    public static final String MEMBER_INITIAL_PASSWORD = "00000000";
    public static final String MEMBER_INITIAL_TRADE_PASSWORD = "11111111";


    public static final String TYPE_RECOMMEND = "1";//推荐奖励
    public static final String TYPE_SELF = "0";//用户等级奖励

    public static final BigDecimal RECOMMEND_RATE = new BigDecimal(0.1);//下级返给上级0.1倍率积分

    public static final BigDecimal ENCASH_EARNING_RATE = new BigDecimal(0.9);//财富积分提现90%到收益，10%到购物积分


    public static final String TYPE_ECOIN = "0";//ecoin
    public static final String TYPE_VC = "1";//vc

    public static final String TYPE_SELL_ORDER = "0";//挂卖
    public static final String TYPE_BUY_ORDER = "1";//求购


    public static final String STATE_VALID = "0";//VALID 有效：在挂单/  购物已购买
    public static final String STATE_INVALID = "1";//INVALID 无效：撤销挂单
    public static final String STATE_SOLD = "2";//SOLD  挂单已卖出
    public static final String STATE_APPROVE = "3";//审批同意
    public static final String STATE_REJECT = "4";//审批拒绝

    public static final String STATE_SHOP_SENT = "7";//购物已发货
    public static final String STATE_SHOP_LACK = "8";//缺货

    public static final String TYPE_EARLY_PHASE ="5"; //前期，用于每天释放
    public static final String TYPE_LATE_PHASE ="6"; //后期

    public static final BigDecimal ENCASH_FEE = BigDecimal.valueOf(0.08);//提现手续费
//    public static final BigDecimal TRADE_FEE = BigDecimal.valueOf(0.08);//交易手续费

    //quarts
    //early_phase
    public static final String ECOIN_BONUS = "ecoin_bonus";
    public static final String UNFROZEN_COIN = "unfrozen_coin";
    //trade
    public static final String TRADE_ECOIN_BONUS = "trade_ecoin_bonus";
    public static final String TRADE_UNFROZEN_COIN = "trade_unfrozen_coin";


    public static final Integer COOKIE_TIMEOUT = 3600* 1;// hours


    public static final BigDecimal OP_ECOIN_CHARGE_MAX = BigDecimal.valueOf(7000);//低级管理员最大充值7000积分



}

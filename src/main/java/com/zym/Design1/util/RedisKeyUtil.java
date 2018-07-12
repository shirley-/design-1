package com.zym.Design1.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YM on 2018/3/8.
 */
public class RedisKeyUtil {

    private static String SPLIT = ":";
    private static final String USER_SESSION = "USER_SESSION";
    private static final String USER_LOGIN = "USER_LOGIN";
    private static final String CURRENCY = "CURRENCY";//货币当前价格
    private static final String CURRENCY_COUNT = "CURRENCY_COUNT";//总货币发行量
    private static final String EARLY_PHASE = "EARLY_PHASE";//前期

//    private static final String MIN_SELL_PRICE = "MIN_SELL_PRICE";   //买时看价格最低的卖单
//    private static final String MAX_BUY_PRICE = "MAX_BUY_PRICE";     //卖时看价格最高的买单

    private static final String SELL_TODAY = "SELL_TODAY";//一天只能卖一次

    private static final String CART = "CART";//购物车

    private static final String HOT_LIST = "HOT_LIST";//是否展示在购物首页


    public static String getUserSessionKey(String sessionTicket) {
        return USER_SESSION + SPLIT + sessionTicket;
    }

    public static String getUserLoginKey(String userId) {
        return USER_LOGIN + SPLIT + userId;
    }

    public static String getCurrencyKey() {
        return CURRENCY;
    }

    public static String getCurrencyCountKey() {
        return CURRENCY_COUNT;
    }

    public static String getPhaseKey() {
        return EARLY_PHASE;
    }

//    public static String getMaxBuyPriceKey() {
//        return MAX_BUY_PRICE;
//    }
//
//    public static String getMinSellPriceKey() {
//        return MIN_SELL_PRICE;
//    }

    public static String getSellTodayKey(Integer userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String todayStr = sdf.format(now);
        return SELL_TODAY + SPLIT + String.valueOf(userId) + SPLIT + todayStr;
    }

    public static String getCartKey(Integer userId) {
        return CART + SPLIT + String.valueOf(userId);
    }


    public static String getHotListKey(String show) {
        return HOT_LIST + SPLIT + show;
    }



}

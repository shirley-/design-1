package com.zym.Design1.mydao;

import java.math.BigDecimal;

/**
 * Created by YM on 2018/5/1.
 */
public interface EcoinChargeMapper {

    BigDecimal getTotalChargedAmount(String UserUid);
}

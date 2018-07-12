package com.zym.Design1.mydao;

import com.zym.Design1.bean.vo.FinVO;
import com.zym.Design1.entity.Trade;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created on 2018/4/4.
 */

public interface AdminTradeMapper {


    List<Trade> selectSoldTradeByDate(@Param("buyerUid") String buyerUid,@Param("sellerUid")  String sellerUid,
                                      @Param("searchDate") String searchDate, @Param("state") String state);


}

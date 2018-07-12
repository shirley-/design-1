package com.zym.Design1.mydao;

import com.zym.Design1.bean.vo.GoodsDetailVO;
import com.zym.Design1.bean.vo.GoodsStatistic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created on 2018/5/13.
 */
public interface ShopMapper {

    List<GoodsDetailVO> getGoodsDetailById(Integer id);

    List<GoodsStatistic> getStatisticData(@Param("days1") String days1,@Param("days2") String days2, @Param("goodsName") String goodsName, @Param("goodsType") Integer goodsType);

}

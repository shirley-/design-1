package com.zym.Design1.dao;

import com.zym.Design1.entity.Trade;
import com.zym.Design1.entity.TradeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TradeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trade
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    long countByExample(TradeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trade
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int deleteByExample(TradeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trade
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trade
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int insert(Trade record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trade
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int insertSelective(Trade record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trade
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    List<Trade> selectByExample(TradeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trade
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    Trade selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trade
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByExampleSelective(@Param("record") Trade record, @Param("example") TradeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trade
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByExample(@Param("record") Trade record, @Param("example") TradeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trade
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByPrimaryKeySelective(Trade record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trade
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByPrimaryKey(Trade record);
}
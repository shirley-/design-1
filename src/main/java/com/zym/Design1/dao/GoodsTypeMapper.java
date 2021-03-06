package com.zym.Design1.dao;

import com.zym.Design1.entity.GoodsType;
import com.zym.Design1.entity.GoodsTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_type
     *
     * @mbg.generated Wed May 16 14:20:21 CST 2018
     */
    long countByExample(GoodsTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_type
     *
     * @mbg.generated Wed May 16 14:20:21 CST 2018
     */
    int deleteByExample(GoodsTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_type
     *
     * @mbg.generated Wed May 16 14:20:21 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_type
     *
     * @mbg.generated Wed May 16 14:20:21 CST 2018
     */
    int insert(GoodsType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_type
     *
     * @mbg.generated Wed May 16 14:20:21 CST 2018
     */
    int insertSelective(GoodsType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_type
     *
     * @mbg.generated Wed May 16 14:20:21 CST 2018
     */
    List<GoodsType> selectByExample(GoodsTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_type
     *
     * @mbg.generated Wed May 16 14:20:21 CST 2018
     */
    GoodsType selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_type
     *
     * @mbg.generated Wed May 16 14:20:21 CST 2018
     */
    int updateByExampleSelective(@Param("record") GoodsType record, @Param("example") GoodsTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_type
     *
     * @mbg.generated Wed May 16 14:20:21 CST 2018
     */
    int updateByExample(@Param("record") GoodsType record, @Param("example") GoodsTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_type
     *
     * @mbg.generated Wed May 16 14:20:21 CST 2018
     */
    int updateByPrimaryKeySelective(GoodsType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_type
     *
     * @mbg.generated Wed May 16 14:20:21 CST 2018
     */
    int updateByPrimaryKey(GoodsType record);
}
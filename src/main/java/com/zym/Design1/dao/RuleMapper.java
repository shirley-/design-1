package com.zym.Design1.dao;

import com.zym.Design1.entity.Rule;
import com.zym.Design1.entity.RuleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RuleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    long countByExample(RuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int deleteByExample(RuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int insert(Rule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int insertSelective(Rule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    List<Rule> selectByExample(RuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    Rule selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByExampleSelective(@Param("record") Rule record, @Param("example") RuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByExample(@Param("record") Rule record, @Param("example") RuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByPrimaryKeySelective(Rule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByPrimaryKey(Rule record);
}
package com.zym.Design1.dao;

import com.zym.Design1.entity.Org;
import com.zym.Design1.entity.OrgExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrgMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    long countByExample(OrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int deleteByExample(OrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int insert(Org record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int insertSelective(Org record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    List<Org> selectByExample(OrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    Org selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByExampleSelective(@Param("record") Org record, @Param("example") OrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByExample(@Param("record") Org record, @Param("example") OrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByPrimaryKeySelective(Org record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByPrimaryKey(Org record);
}
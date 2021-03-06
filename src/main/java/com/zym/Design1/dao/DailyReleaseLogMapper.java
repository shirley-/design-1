package com.zym.Design1.dao;

import com.zym.Design1.entity.DailyReleaseLog;
import com.zym.Design1.entity.DailyReleaseLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DailyReleaseLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table daily_release_log
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    long countByExample(DailyReleaseLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table daily_release_log
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int deleteByExample(DailyReleaseLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table daily_release_log
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table daily_release_log
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int insert(DailyReleaseLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table daily_release_log
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int insertSelective(DailyReleaseLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table daily_release_log
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    List<DailyReleaseLog> selectByExample(DailyReleaseLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table daily_release_log
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    DailyReleaseLog selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table daily_release_log
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByExampleSelective(@Param("record") DailyReleaseLog record, @Param("example") DailyReleaseLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table daily_release_log
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByExample(@Param("record") DailyReleaseLog record, @Param("example") DailyReleaseLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table daily_release_log
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByPrimaryKeySelective(DailyReleaseLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table daily_release_log
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    int updateByPrimaryKey(DailyReleaseLog record);
}
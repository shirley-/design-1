package com.zym.Design1.entity;

public class Org {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org.id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org.user_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org.child_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Integer childId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org.id
     *
     * @return the value of org.id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org.id
     *
     * @param id the value for org.id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org.user_id
     *
     * @return the value of org.user_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org.user_id
     *
     * @param userId the value for org.user_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org.child_id
     *
     * @return the value of org.child_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Integer getChildId() {
        return childId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org.child_id
     *
     * @param childId the value for org.child_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setChildId(Integer childId) {
        this.childId = childId;
    }
}
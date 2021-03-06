package com.zym.Design1.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Encash {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.user_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.user_uid
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private String userUid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.amount
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private BigDecimal amount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.fee
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private BigDecimal fee;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.earnings
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private BigDecimal earnings;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.state
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private String state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.type
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private String type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.created_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Date createdDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.approve_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Date approveDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.remark
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private String remark;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.after_cash_coin
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private BigDecimal afterCashCoin;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.after_earnings
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private BigDecimal afterEarnings;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.shop_points
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private BigDecimal shopPoints;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column encash.reason
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private String reason;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.id
     *
     * @return the value of encash.id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.id
     *
     * @param id the value for encash.id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.user_id
     *
     * @return the value of encash.user_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.user_id
     *
     * @param userId the value for encash.user_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.user_uid
     *
     * @return the value of encash.user_uid
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public String getUserUid() {
        return userUid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.user_uid
     *
     * @param userUid the value for encash.user_uid
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setUserUid(String userUid) {
        this.userUid = userUid == null ? null : userUid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.amount
     *
     * @return the value of encash.amount
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.amount
     *
     * @param amount the value for encash.amount
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.fee
     *
     * @return the value of encash.fee
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.fee
     *
     * @param fee the value for encash.fee
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.earnings
     *
     * @return the value of encash.earnings
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public BigDecimal getEarnings() {
        return earnings;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.earnings
     *
     * @param earnings the value for encash.earnings
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setEarnings(BigDecimal earnings) {
        this.earnings = earnings;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.state
     *
     * @return the value of encash.state
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public String getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.state
     *
     * @param state the value for encash.state
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.type
     *
     * @return the value of encash.type
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.type
     *
     * @param type the value for encash.type
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.created_date
     *
     * @return the value of encash.created_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.created_date
     *
     * @param createdDate the value for encash.created_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.approve_date
     *
     * @return the value of encash.approve_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Date getApproveDate() {
        return approveDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.approve_date
     *
     * @param approveDate the value for encash.approve_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.remark
     *
     * @return the value of encash.remark
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.remark
     *
     * @param remark the value for encash.remark
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.after_cash_coin
     *
     * @return the value of encash.after_cash_coin
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public BigDecimal getAfterCashCoin() {
        return afterCashCoin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.after_cash_coin
     *
     * @param afterCashCoin the value for encash.after_cash_coin
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setAfterCashCoin(BigDecimal afterCashCoin) {
        this.afterCashCoin = afterCashCoin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.after_earnings
     *
     * @return the value of encash.after_earnings
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public BigDecimal getAfterEarnings() {
        return afterEarnings;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.after_earnings
     *
     * @param afterEarnings the value for encash.after_earnings
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setAfterEarnings(BigDecimal afterEarnings) {
        this.afterEarnings = afterEarnings;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.shop_points
     *
     * @return the value of encash.shop_points
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public BigDecimal getShopPoints() {
        return shopPoints;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.shop_points
     *
     * @param shopPoints the value for encash.shop_points
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setShopPoints(BigDecimal shopPoints) {
        this.shopPoints = shopPoints;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column encash.reason
     *
     * @return the value of encash.reason
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public String getReason() {
        return reason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column encash.reason
     *
     * @param reason the value for encash.reason
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }
}
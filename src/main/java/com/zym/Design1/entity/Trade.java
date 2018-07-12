package com.zym.Design1.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Trade {
    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", tradeDate=" + tradeDate +
                ", buyerId=" + buyerId +
                ", buyerUid='" + buyerUid + '\'' +
                ", sellerId=" + sellerId +
                ", sellerUid='" + sellerUid + '\'' +
                ", vcPrice=" + vcPrice +
                ", amount=" + amount +
                ", ecoin=" + ecoin +
                ", state='" + state + '\'' +
                ", type='" + type + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                ", srcId=" + srcId +
                ", ruleId=" + ruleId +
                '}';
    }

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.order_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Date orderDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.trade_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Date tradeDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.buyer_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Integer buyerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.buyer_uid
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private String buyerUid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.seller_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Integer sellerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.seller_uid
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private String sellerUid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.vc_price
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private BigDecimal vcPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.amount
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private BigDecimal amount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.ecoin
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private BigDecimal ecoin;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.state
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private String state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.type
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private String type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.last_modified_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Date lastModifiedDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.after_buyer
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private String afterBuyer;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.after_seller
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private String afterSeller;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.version
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Integer version;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.src_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Long srcId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.rule_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private Integer ruleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade.remark
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    private String remark;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.id
     *
     * @return the value of trade.id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.id
     *
     * @param id the value for trade.id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.order_date
     *
     * @return the value of trade.order_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.order_date
     *
     * @param orderDate the value for trade.order_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.trade_date
     *
     * @return the value of trade.trade_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Date getTradeDate() {
        return tradeDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.trade_date
     *
     * @param tradeDate the value for trade.trade_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.buyer_id
     *
     * @return the value of trade.buyer_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Integer getBuyerId() {
        return buyerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.buyer_id
     *
     * @param buyerId the value for trade.buyer_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.buyer_uid
     *
     * @return the value of trade.buyer_uid
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public String getBuyerUid() {
        return buyerUid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.buyer_uid
     *
     * @param buyerUid the value for trade.buyer_uid
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setBuyerUid(String buyerUid) {
        this.buyerUid = buyerUid == null ? null : buyerUid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.seller_id
     *
     * @return the value of trade.seller_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Integer getSellerId() {
        return sellerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.seller_id
     *
     * @param sellerId the value for trade.seller_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.seller_uid
     *
     * @return the value of trade.seller_uid
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public String getSellerUid() {
        return sellerUid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.seller_uid
     *
     * @param sellerUid the value for trade.seller_uid
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setSellerUid(String sellerUid) {
        this.sellerUid = sellerUid == null ? null : sellerUid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.vc_price
     *
     * @return the value of trade.vc_price
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public BigDecimal getVcPrice() {
        return vcPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.vc_price
     *
     * @param vcPrice the value for trade.vc_price
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setVcPrice(BigDecimal vcPrice) {
        this.vcPrice = vcPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.amount
     *
     * @return the value of trade.amount
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.amount
     *
     * @param amount the value for trade.amount
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.ecoin
     *
     * @return the value of trade.ecoin
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public BigDecimal getEcoin() {
        return ecoin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.ecoin
     *
     * @param ecoin the value for trade.ecoin
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setEcoin(BigDecimal ecoin) {
        this.ecoin = ecoin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.state
     *
     * @return the value of trade.state
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public String getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.state
     *
     * @param state the value for trade.state
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.type
     *
     * @return the value of trade.type
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.type
     *
     * @param type the value for trade.type
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.last_modified_date
     *
     * @return the value of trade.last_modified_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.last_modified_date
     *
     * @param lastModifiedDate the value for trade.last_modified_date
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.after_buyer
     *
     * @return the value of trade.after_buyer
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public String getAfterBuyer() {
        return afterBuyer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.after_buyer
     *
     * @param afterBuyer the value for trade.after_buyer
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setAfterBuyer(String afterBuyer) {
        this.afterBuyer = afterBuyer == null ? null : afterBuyer.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.after_seller
     *
     * @return the value of trade.after_seller
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public String getAfterSeller() {
        return afterSeller;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.after_seller
     *
     * @param afterSeller the value for trade.after_seller
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setAfterSeller(String afterSeller) {
        this.afterSeller = afterSeller == null ? null : afterSeller.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.version
     *
     * @return the value of trade.version
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.version
     *
     * @param version the value for trade.version
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.src_id
     *
     * @return the value of trade.src_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Long getSrcId() {
        return srcId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.src_id
     *
     * @param srcId the value for trade.src_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setSrcId(Long srcId) {
        this.srcId = srcId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.rule_id
     *
     * @return the value of trade.rule_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public Integer getRuleId() {
        return ruleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.rule_id
     *
     * @param ruleId the value for trade.rule_id
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade.remark
     *
     * @return the value of trade.remark
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade.remark
     *
     * @param remark the value for trade.remark
     *
     * @mbg.generated Tue May 01 23:51:54 CST 2018
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}
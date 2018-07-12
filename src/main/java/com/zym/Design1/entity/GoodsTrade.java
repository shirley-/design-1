package com.zym.Design1.entity;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsTrade {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.id
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.date
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private Date date;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.goods_id
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private Integer goodsId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.goods_name
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private String goodsName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.goods_price
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private BigDecimal goodsPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.num
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private Integer num;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.buyer_id
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private Integer buyerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.buyer_uid
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private String buyerUid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.state
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private String state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.name
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.phone
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private String phone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.address
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private String address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.remark
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private String remark;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_trade.cart
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    private String cart;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.id
     *
     * @return the value of goods_trade.id
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.id
     *
     * @param id the value for goods_trade.id
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.date
     *
     * @return the value of goods_trade.date
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public Date getDate() {
        return date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.date
     *
     * @param date the value for goods_trade.date
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.goods_id
     *
     * @return the value of goods_trade.goods_id
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public Integer getGoodsId() {
        return goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.goods_id
     *
     * @param goodsId the value for goods_trade.goods_id
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.goods_name
     *
     * @return the value of goods_trade.goods_name
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.goods_name
     *
     * @param goodsName the value for goods_trade.goods_name
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.goods_price
     *
     * @return the value of goods_trade.goods_price
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.goods_price
     *
     * @param goodsPrice the value for goods_trade.goods_price
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.num
     *
     * @return the value of goods_trade.num
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public Integer getNum() {
        return num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.num
     *
     * @param num the value for goods_trade.num
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setNum(Integer num) {
        this.num = num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.buyer_id
     *
     * @return the value of goods_trade.buyer_id
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public Integer getBuyerId() {
        return buyerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.buyer_id
     *
     * @param buyerId the value for goods_trade.buyer_id
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.buyer_uid
     *
     * @return the value of goods_trade.buyer_uid
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public String getBuyerUid() {
        return buyerUid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.buyer_uid
     *
     * @param buyerUid the value for goods_trade.buyer_uid
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setBuyerUid(String buyerUid) {
        this.buyerUid = buyerUid == null ? null : buyerUid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.state
     *
     * @return the value of goods_trade.state
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public String getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.state
     *
     * @param state the value for goods_trade.state
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.name
     *
     * @return the value of goods_trade.name
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.name
     *
     * @param name the value for goods_trade.name
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.phone
     *
     * @return the value of goods_trade.phone
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.phone
     *
     * @param phone the value for goods_trade.phone
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.address
     *
     * @return the value of goods_trade.address
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.address
     *
     * @param address the value for goods_trade.address
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.remark
     *
     * @return the value of goods_trade.remark
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.remark
     *
     * @param remark the value for goods_trade.remark
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_trade.cart
     *
     * @return the value of goods_trade.cart
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public String getCart() {
        return cart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_trade.cart
     *
     * @param cart the value for goods_trade.cart
     *
     * @mbg.generated Wed May 16 16:55:04 CST 2018
     */
    public void setCart(String cart) {
        this.cart = cart == null ? null : cart.trim();
    }
}
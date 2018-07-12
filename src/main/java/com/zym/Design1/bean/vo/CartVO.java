package com.zym.Design1.bean.vo;

import com.zym.Design1.entity.Goods;

/**
 * Created by YM on 2018/5/16.
 */
public class CartVO {
    private Goods goods;
    private Integer num;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}

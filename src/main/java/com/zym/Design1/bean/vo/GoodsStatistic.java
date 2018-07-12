package com.zym.Design1.bean.vo;

import com.zym.Design1.entity.Goods;
import com.zym.Design1.entity.GoodsTrade;
import com.zym.Design1.entity.GoodsType;

import java.io.Serializable;


public class GoodsStatistic extends GoodsTrade implements Serializable {

    private static final long serialVersionUID = 1L;
    private Goods goods;
    private GoodsType type;
    private Integer num;

    public GoodsType getType() {
        return type;
    }

    public void setType(GoodsType type) {
        this.type = type;
    }

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

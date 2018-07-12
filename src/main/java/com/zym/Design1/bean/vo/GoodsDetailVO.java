package com.zym.Design1.bean.vo;

import com.zym.Design1.entity.Goods;
import com.zym.Design1.entity.GoodsType;

import java.io.Serializable;

/**
 * Created on 2018/5/13.
 */
public class GoodsDetailVO extends Goods implements Serializable {
    private static final long serialVersionUID = 1L;
    private GoodsType type;

    public GoodsType getType() {
        return type;
    }

    public void setType(GoodsType type) {
        this.type = type;
    }
}

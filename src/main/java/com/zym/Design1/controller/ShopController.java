package com.zym.Design1.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.bean.vo.GoodsDetailVO;
import com.zym.Design1.entity.Goods;
import com.zym.Design1.entity.GoodsType;
import com.zym.Design1.service.ShopService;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/5/9.
 */
@Controller
@Slf4j
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping("/shop")
    public String shop(Model model) {
        List<GoodsType> goodsTypes = shopService.getGoodsTypes();
        model.addAttribute("goodsTypes", goodsTypes);
        List<Goods> hotList2 = shopService.getHotList("2");
        model.addAttribute("hotList", hotList2.subList(0, 12>hotList2.size()?hotList2.size():12));
        List<Goods> hotList = shopService.getHotList("1");
        List<List<Goods>> hotSet = new ArrayList<>();
        for(int x = 0; x<hotList.size(); x=x+4) {
            hotSet.add(hotList.subList(x, x+4>hotList.size()?hotList.size():x+4));
        }
        model.addAttribute("hotSet", hotSet);

        return "indexShop";
    }

    @RequestMapping("/category")
    public String category(Model model,
                           @RequestParam(value = "type", required = false, defaultValue = "1") Integer typeId,
                           @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                           @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize) {
        //商品分类
        List<GoodsType> goodsTypes = shopService.getGoodsTypes();
        model.addAttribute("goodsTypes", goodsTypes);

        List<Goods> goods = shopService.getGoodsOfType(typeId, pageIndex, pageSize);
        model.addAttribute("goods", goods);
        long total = ((Page<Goods>) goods).getTotal();
        model.addAttribute("total", total);
        model.addAttribute("type", typeId);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", pageSize);

        return "category";
    }

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public String detail(@RequestParam("id") Integer id, Model model) {
        GoodsDetailVO item = shopService.getGoodsDetail(id);
        model.addAttribute("item", item);
        return "single";
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public String checkout( Model model) {
        return "checkout";
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String cart( Model model) {
        if(hostHolder.getUser()!=null) {
            if(hostHolder.getUser().getRole().equals(ConstantUtil.ROLE_ADMIN)) {
                return "/admin/cart";
            }
            else if(hostHolder.getUser().getRole().equals(ConstantUtil.ROLE_MEMBER)) {
                return "/member/cart";
            } else {
                throw new RuntimeException("没有权限");
            }
        }else {
            return "redirect:/login?next=/cart";
        }
    }

    @RequestMapping(value = "/addToCart", method = RequestMethod.POST)
    public String addToCart(@RequestParam("id") Integer id, @RequestParam("num") Integer num) {
        if(hostHolder.getUser()!=null) {
            if(hostHolder.getUser().getRole().equals(ConstantUtil.ROLE_ADMIN)) {
                return "/admin/addToCart?id=" + id + "&num=" + num;
            }
            else if(hostHolder.getUser().getRole().equals(ConstantUtil.ROLE_MEMBER)) {
                return "/member/addToCart?id=" + id + "&num=" + num;
            } else {
                throw new RuntimeException("没有权限");
            }
        }else {
            throw new RuntimeException("未登录");
        }
    }


}

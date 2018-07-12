package com.zym.Design1.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.entity.Encash;
import com.zym.Design1.entity.User;
import com.zym.Design1.service.EncashService;
import com.zym.Design1.service.UserService;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.ProjectUtil;
import com.zym.Design1.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created on 2018/3/27.
 */
@Controller
@RequestMapping({"/member", "/admin"})
public class EncashController {
    @Autowired
    private EncashService encashService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

  /*  @RequestMapping(value = "/encash", method = RequestMethod.GET)
    public String encashPage(Model model,
                             @RequestParam(value = "m", required = false, defaultValue = "myFin") String mMenu,
                             @RequestParam(value = "s", required = false, defaultValue = "encash") String sMenu,
                             @RequestParam(value = "state", required = false, defaultValue = "1") String state) {
        model.addAttribute("cashCoin", hostHolder.getUser().getCashCoin());
        model.addAttribute("state", state);
        model.addAttribute("m", mMenu);
        model.addAttribute("s", sMenu);
        return "encash";
    }*/

    @ResponseBody
    @RequestMapping(value = "/encash", method = RequestMethod.POST)
    public String encash(@RequestParam("amount") Integer amount,
                         @RequestParam("tradePassword") String tradePwd) {
        if(!hostHolder.getUser().getRole().equals(ConstantUtil.ROLE_ADMIN)) {
            //tradeTime
            ProjectUtil.checkTradeTime();
        }
        User user = hostHolder.getUser();
        if(user == null) {
            return ResultUtil.getJSONString(ResultUtil.NOT_LOGIN);
        }
        boolean checkTradePassword = userService.checkTradePassword(user, tradePwd);
        if(!checkTradePassword) {
            throw new RuntimeException("交易密码错误");
        }
        encashService.encash(user, amount);

        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "/encashRecord", method = RequestMethod.POST)
    public String encashList(@RequestParam(value = "pageIndex", required = false, defaultValue ="1" ) Integer pageIndex,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "sort", required = false, defaultValue = "createdDate") String sort,
                             @RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
                             @RequestParam(value = "state", required = false, defaultValue = "1") String state,
                             Model model) {
        List<Encash> encashList = encashService.getEncashList(pageIndex, pageSize, sort, sortOrder,
                null, hostHolder.getUser().getId());
        model.addAttribute("encashList", encashList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", ((Page) encashList).getTotal());
        jsonObject.put("page", pageIndex);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(encashList);
        jsonObject.put("rows", jsonArray );
        return jsonObject.toString();
    }



}

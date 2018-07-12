package com.zym.Design1.controller;

import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.entity.User;
import com.zym.Design1.service.CurrencyService;
import com.zym.Design1.service.PhaseService;
import com.zym.Design1.service.UserService;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.math.BigDecimal;

/**
 * Created on 2018/3/18.
 */
@Slf4j
@RequestMapping("/admin")
@Controller
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

//    @RequestMapping(value = "/setCurrency", method = RequestMethod.POST)
//    public void setCurrency(@RequestParam("c") String currency) {
//        currencyService.setCurrency(new BigDecimal(currency));
//    }
//
//    @RequestMapping("/getCurrency")
//    public String getCurrency() {
//        return String.valueOf(currencyService.getCurrency());
//    }

    @RequestMapping("/trend")
    public String trend(Model model,
                        @RequestParam(value = "m", required = false, defaultValue = "trend") String mMenu) {
        model.addAttribute("vc", currencyService.getCurrency());
        model.addAttribute("totalVc", currencyService.getCurrencyCount());
        model.addAttribute("m", mMenu);
        model.addAttribute("phaseState", phaseService.getEarlyPhaseState());
        return "currencyTrend";
    }


    @RequestMapping("/recordVcPrice")
    public String recordVcPrice(@RequestParam("vc") BigDecimal price) {
        currencyService.recordCurrencyPrice(price);
        return "redirect:trend";
    }

    @RequestMapping(value = "/closeEarlyPhase", method = RequestMethod.POST)
    public String closeEarlyPhase() {
        phaseService.closeEarlyPhase();
        return "redirect:trend";
    }


    @ResponseBody
    @RequestMapping(value = "/increaseAdminVc", method = RequestMethod.POST)
    public String increaseAdminVc(@RequestParam("amount") BigDecimal amount) {
        if(!hostHolder.getUser().getRole().equals(ConstantUtil.ROLE_ADMIN)) {
            throw new RuntimeException("没有权限");
        }
        User user = hostHolder.getUser();
        user.setTradableCoin(user.getTradableCoin().add(amount));
        user.setTotalCoin(user.getTotalCoin().add(amount));
        userService.updateUser(user);
        currencyService.incrementCurrencyCount(amount);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }



}

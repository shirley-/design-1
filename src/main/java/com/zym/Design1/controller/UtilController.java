package com.zym.Design1.controller;

import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.entity.User;
import com.zym.Design1.service.LoginService;
import com.zym.Design1.service.UserService;
import com.zym.Design1.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created on 2018/3/6.
 */
@Controller
public class UtilController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private HostHolder hostHolder;

    @ResponseBody
    @RequestMapping("/isValidateCodeCorrect")
    public String compareValidateCode(HttpSession session,
                                      @RequestParam("inputVCode") String inputVCode) {
        String vCode = session.getAttribute("vCode").toString();
        if(vCode.toLowerCase().equals(inputVCode.toLowerCase())) {
            return ResultUtil.getValidString(true);
        }else {
            return ResultUtil.getValidString(false);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/isUidExist", method = RequestMethod.POST)
    public String isUidExist( @RequestParam("inputUid") String inputUid) {
        User user = userService.getUserByUid(inputUid);
        if(user == null) {
            return ResultUtil.getValidString(false);
        }else {
            return ResultUtil.getValidString(true);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/isUidNotExist", method = RequestMethod.POST)
    public String isUidNotExist( @RequestParam("inputUid") String inputUid) {
        User user = userService.getUserByUid(inputUid);
        if(user == null) {
            return ResultUtil.getValidString(true);
        }else {
            return ResultUtil.getValidString(false);
        }
    }

    @ResponseBody
    @RequestMapping(value = "isPwdCorrect", method = RequestMethod.POST)
    public String isPwdCorrect(@RequestParam("originPwd") String pwd) {
        if(hostHolder.getUser() == null) {
            return ResultUtil.getValidString(false);
        }
        User user = hostHolder.getUser();
        boolean b = userService.checkPassword(user, pwd);
        if( b) {
            return ResultUtil.getValidString(true);
        } else {
            return ResultUtil.getValidString(false);
        }
    }

    @ResponseBody
    @RequestMapping(value = "isTradePwdCorrect", method = RequestMethod.POST)
    public String isTradePwdCorrect(@RequestParam("originPwd") String pwd) {
        if(hostHolder.getUser() == null) {
            return ResultUtil.getValidString(false);
        }
        User user = hostHolder.getUser();
        boolean b = userService.checkTradePassword(user, pwd);
        if( b) {
            return ResultUtil.getValidString(true);
        } else {
            return ResultUtil.getValidString(false);
        }
    }






}

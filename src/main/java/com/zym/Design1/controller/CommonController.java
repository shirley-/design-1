package com.zym.Design1.controller;

import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.entity.User;
import com.zym.Design1.service.UserService;
import com.zym.Design1.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created on 2018/5/8.
 */
@RequestMapping({"/admin", "/member", "/op"})
@Controller
@Slf4j
public class CommonController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;



}

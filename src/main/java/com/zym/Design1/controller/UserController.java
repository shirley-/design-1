package com.zym.Design1.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.entity.Notice;
import com.zym.Design1.entity.User;
import com.zym.Design1.service.NoticeService;
import com.zym.Design1.service.OrgService;
import com.zym.Design1.service.UserService;
import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by YM on 2018/4/24.
 */

@Slf4j
@Controller
@RequestMapping({"/admin", "/op", "/member"})
public class UserController {

    @Autowired
    private OrgService orgService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    /**
     * 会员组织管理
     */
    @RequestMapping(value = "/memberOrgTreeview",method = RequestMethod.GET)
    @ResponseBody
    public String memberOrgTreeview(@RequestParam("id") Integer id) {
        return  ResultUtil.getJSONString(ResultUtil.SUCCESS, orgService.getOrgOfIdForTreeviewJson(id));
    }

    @RequestMapping(value = "/memberOrgTreeChart",method = RequestMethod.GET)
    @ResponseBody
    public String memberOrgTreeChart(@RequestParam("id") Integer id) {
        return ResultUtil.getJSONString(ResultUtil.SUCCESS, orgService.getOrgOfIdForTreeChartJson(id));
    }

    /**个人基本信息，修改密码
     */
    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public String changePwdPage(@RequestParam(value = "m", required = false, defaultValue = "my") String mMenu,
                                @RequestParam(value = "s", required = false, defaultValue = "myInfo") String sMenu,
                                Model model) {
        if(sMenu.equals("changeTradePwd") && hostHolder.getUser().getRole().equals(ConstantUtil.ROLE_OP) ) {
            throw new RuntimeException("普通管理员不能修改交易密码");
        }
        model.addAttribute("m", mMenu);
        model.addAttribute("s", sMenu);
        return sMenu;
    }


    //notice公告
    @RequestMapping(value = "/readNotice", method = RequestMethod.GET)
    public String readNoticePage(@RequestParam(value = "m", required = false, defaultValue = "notice") String mMenu,
                                 @RequestParam("id") Integer id,
                                 Model model) {
        Notice notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        model.addAttribute("m", mMenu);
        return "readNotice";
    }

    @ResponseBody
    @RequestMapping(value = "/noticeList", method = RequestMethod.POST)
    public String noticeList(@RequestParam(value = "m", required = false, defaultValue = "notice") String mMenu,
                             @RequestParam(value = "pageIndex", required = false, defaultValue ="1" ) Integer pageIndex,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        List<Notice> noticeList = noticeService.getNoticeList(pageIndex, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", ((Page) noticeList).getTotal());
        jsonObject.put("page", pageIndex);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(noticeList);
        jsonObject.put("rows", jsonArray );
        return jsonObject.toString();
    }

    @RequestMapping(value = "/noticePage", method = RequestMethod.GET)
    public String noticeListPage(@RequestParam(value = "m", required = false, defaultValue = "notice") String mMenu,
                                 Model model) {
        model.addAttribute("m", mMenu);
        return "noticePage";
    }

    @ResponseBody
    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    public String changePwdAction(@RequestParam("originPwd") String originPwd,
                                  @RequestParam("newPwd") String newPwd,
                                  @RequestParam("newPwd2") String newPwd2) {

        if(hostHolder.getUser() == null) {
            return ResultUtil.getJSONString(999);
        }
        User user = hostHolder.getUser();
        boolean b = userService.checkPassword(user, originPwd);
        if( !b) {
            return ResultUtil.getJSONString(ResultUtil.SUCCESS, "密码错误");
        } else {
            if( !newPwd.equals(newPwd2)) {
                return ResultUtil.getJSONString(ResultUtil.ERROR, "新密码确认错误");
            }
            userService.changePassword(user, newPwd);
            return ResultUtil.getJSONString(ResultUtil.SUCCESS);
        }
    }

    //会员修改个人信息
    @ResponseBody
    @RequestMapping(value = "/member/{id}", method = RequestMethod.PUT)
    public String updateMember(@RequestBody User user) {
        if(!hostHolder.getUser().getId().equals(user.getId())) {
            log.error("user : {} 修改他人信息, {}, {}", hostHolder.getUser().getUid(),user.getId(), user.getUid());
            throw new RuntimeException("修改信息错误");
        }
        User userOld = userService.getUserById(user.getId());
        userOld.setName(user.getName());
        userOld.setPhone(user.getPhone());
        userOld.setEmail(user.getEmail());
        userOld.setAddress(user.getAddress());
        userOld.setAccountHolder(user.getAccountHolder());
        userOld.setBankAccount(user.getBankAccount());
        userOld.setIdNumber(user.getIdNumber());
        userOld.setDepositBank(user.getDepositBank());
        userService.updateUser(userOld);
        return ResultUtil.getJSONString(ResultUtil.SUCCESS);
    }

}

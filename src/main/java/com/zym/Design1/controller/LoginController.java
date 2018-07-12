package com.zym.Design1.controller;

import com.zym.Design1.service.LoginService;
import com.zym.Design1.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


@Slf4j
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("index", "index");
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet(@RequestParam(value="next", required = false) String next,
                           Model model ) {
        log.info("next: {}", next);
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginPost(HttpSession session,
                            @RequestParam("validateCode") String inputVCode,
                            @RequestParam(value = "name") String uid,
                            @RequestParam("password") String password,
                            @RequestParam(value="next", required = false) String next,
                            Model model,
                            HttpServletResponse response) {
        if(session.getAttribute("vCode")==null) {
            model.addAttribute("msg", "错误");
            return "login";
        }
        String vCode = session.getAttribute("vCode").toString();
        if(!vCode.toLowerCase().equals(inputVCode.toLowerCase())) {
            model.addAttribute("msg", "验证码错误");
            if(next != null) {
                model.addAttribute("next", next);
            }
            return "login" ;
        }
        Map<String, Object> loginMap = loginService.login(uid, password);
        log.info(loginMap.toString());
        if(loginMap.containsKey("ticket") ) {//login success
            Cookie cookie = new Cookie("sakura_ticket", loginMap.get("ticket").toString());
            cookie.setPath("/");
//            if (rememberMe) {
                cookie.setMaxAge(ConstantUtil.COOKIE_TIMEOUT);
//            }
            response.addCookie(cookie);
            if(next != null) {
                return "redirect:" + next;
            }
            if(loginMap.containsKey("role")) {
                if(loginMap.get("role").equals("2")) {//admin
                    return "redirect:/admin/myHome";
                }
                if(loginMap.get("role").equals("1")) {//op
                    return "redirect:/op/memberManage";
                }
                if(loginMap.get("role").equals("0")) {//member
                    return "redirect:/member/myHome";
                }

            }
            return "redirect:/member/myHome";
        }else {
            model.addAttribute("msg", "用户名或密码错误");
            if(next != null) {
                model.addAttribute("next", next);
//                return "/login?next=" +next ;
            }
            return "login" ;
        }
    }


    @RequestMapping({ "/reglogin"})
    public String reglogin(@RequestParam(value = "next", required = false) String next, Model model) {
        if(next != null) {
            model.addAttribute("next", next);
        }
        return "login";
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.POST, RequestMethod.GET})
    public String logout(@CookieValue(value = "sakura_ticket", required = false) String ticket) {
        loginService.logout(ticket);
        return "redirect:/login";
    }

    @RequestMapping("/main")
    public String main(Model model) {
        return "main";
    }


}

package com.zym.Design1.interceptor;

import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 一般会员
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(hostHolder.getUser() == null) {
            response.sendRedirect("/login");// URLEncoder.encode()
            return false; //just redirect and finish
        }
        if(hostHolder.getUser() != null ) {
            if(hostHolder.getUser().getRole().compareTo(ConstantUtil.ROLE_MEMBER) !=0) {//op 1, admin 2， 0 : member
                response.sendRedirect("/404");
                return false;
            }
        }
        return true; //continue chain
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

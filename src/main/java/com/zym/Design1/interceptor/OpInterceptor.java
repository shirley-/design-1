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
 * 管理员 低级
 */
@Component
public class OpInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(hostHolder.getUser() == null) {
            response.sendRedirect("/login?next=" + request.getRequestURI());// URLEncoder.encode()
            return false; //just redirect and finish
        }
        if(hostHolder.getUser() != null ) {
            if(hostHolder.getUser().getRole().compareTo(ConstantUtil.ROLE_OP) !=0 ) {//op 1, admin 2
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

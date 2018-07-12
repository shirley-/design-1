package com.zym.Design1.interceptor;

import com.zym.Design1.dao.UserMapper;
import com.zym.Design1.bean.HostHolder;
import com.zym.Design1.entity.User;
import com.zym.Design1.service.UserService;
import com.zym.Design1.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if(cookies !=null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("sakura_ticket")) {//已登录
                    String ticket = cookie.getValue();
                    String sessionKey = RedisKeyUtil.getUserSessionKey(ticket);
                    String userId = redisTemplate.opsForValue().get(sessionKey);
                    if(userId == null ) {
                        //expired
                        return  true;
                    }else {
                        //still valid ticket
                        User user = userService.getUserById(Integer.valueOf(userId));
                        if (user != null) {
                            hostHolder.setUser(user);
                        } else {
                            return true;
                        }
                    }
                    break;
                }
            }
        }
        return true;//continue chain
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}

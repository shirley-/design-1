package com.zym.Design1.aspect;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Created by nowcoder on 2016/7/10.
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
//    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Before(
                "execution( * com.zym.Design1.service.*.*(..)) " + " || " +
                "execution( * com.zym.Design1.dao.*.*(..)) " + " || " +
                "execution( * com.zym.Design1.mydao.*.*(..)) " + " || " +
                "execution( * com.zym.Design1.util.*.*(..)) " + " || " +
                "execution( * com.zym.Design1.exception.*.*(..)) "
            )
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            if (arg != null) {
                sb.append( arg.toString() + ",");
            }
        }
//        log.info("before method:" + sb.toString());
        log.info("before method: {}, args: {}", joinPoint.getSignature(), sb.toString()  );
    }

//    @After("execution(* com.nowcoder.wenda.controller.IndexController1.*(..))")
//    public void afterMethod() {
//        log.info("after method" + new Date());
//    }



    @Before(    "execution( * com.zym.Design1.service.UserService.updateUser(..)) "   )
    public void beforeUpdateUser(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            if (arg != null) {
                sb.append( JSONObject.toJSONString(arg) + ",");
            }
        }
        log.info("before updateUser method: {}, args: {}", joinPoint.getSignature(), sb.toString()  );
    }

    @After(    "execution( * com.zym.Design1.service.UserService.updateUser(..)) "   )
    public void afterUpdateUser(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            if (arg != null) {
                sb.append( JSONObject.toJSONString(arg) + ",");
            }
        }
        log.info("after updateUser method: {}, args: {}", joinPoint.getSignature(), sb.toString()  );
    }


    /**
     * 定义一个切入点.
     * 解释下：
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 任意包名
     * ~ 第三个 * 代表任意方法.
     * ~ 第四个 * 定义在web包或者子包
     * ~ 第五个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */

    @Pointcut("execution( * com.zym.Design1.controller.*.*(..))")
    public void controllerLog() {
    }

    @Before("controllerLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        log.info("WebLogAspect.doBefore()");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        //获取所有参数方法一：
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            log.info("{} : {}", paraName, request.getParameter(paraName));
        }
    }
    
    
}

package com.zym.Design1.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by YM on 2018/4/24.
 */
@Component
@Slf4j
public class ViewExceptionHandler implements HandlerExceptionResolver {
    @Nullable
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @Nullable Object o, Exception e) {
        log.error("ex:{}", e.getClass());
        Integer statusCode = (Integer) httpServletRequest.getAttribute("javax.servlet.error.status_code");
        statusCode = statusCode == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : statusCode;
        if(statusCode==404) {
            log.error("找不到资源：{}", httpServletRequest.getAttribute("javax.servlet.forward.request_uri"));
        }else {//500
            log.info("================【开始打印异常信息】================");
            log.error("具体错误信息:【" + e.getMessage() + "】");
            int count = 0; //只打印15行的错误堆栈
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                log.error(stackTraceElement.toString());
                if (count++ > 13) break;
            }
            log.info("================【异常信息打印完毕】================");

        }
        return new ModelAndView("/500");
    }

}

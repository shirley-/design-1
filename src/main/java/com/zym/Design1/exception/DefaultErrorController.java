package com.zym.Design1.exception;

import com.zym.Design1.util.ConstantUtil;
import com.zym.Design1.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created  on 2018/4/14.
 */
@ControllerAdvice
@Slf4j
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class DefaultErrorController extends AbstractErrorController {


    public DefaultErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Value("${server.error.path:${error.path:/error}}")
    public static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        log.error("请求出错path");
        return ERROR_PATH;
    }

    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(Exception e, HttpServletRequest request,
                                  HttpServletResponse response) {

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        statusCode = statusCode == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : statusCode;//500
        response.setStatus(getStatus(request).value());
        Map<String, Object> model = new HashMap<>();
        switch (statusCode) {
            case 404:
                break;
            case 500:
                break;
            default:
                statusCode = 500;
                break;
        }
        if(statusCode==404) {
            log.error("找不到资源：{}", request.getAttribute("javax.servlet.forward.request_uri"));
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
        log.info("return HTML");
        return new ModelAndView("" + statusCode, model);
    }

    @RequestMapping
    @ResponseBody
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> error(Exception e, HttpServletRequest request, Exception exception) throws Exception {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        statusCode = statusCode == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : statusCode;
        if(statusCode==404) {
            log.error("找不到资源：{}", request.getAttribute("javax.servlet.forward.request_uri"));
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
        Map<String, Object> body = new HashMap<>();
        HttpStatus status = getStatus(request);
        body.put("code", ResultUtil.ERROR);
        body.put("statusCode", statusCode);
        body.put("message", HttpStatus.valueOf(statusCode));
//        body.put("data", null);
//        body.put("exception", exception.getMessage());
        if(exception.getMessage().length()<35) {
            body.put("msg", exception.getMessage());
        }else {
            body.put("msg","内部错误");
        }
        log.info("return json");
        return new ResponseEntity<>(body, status);
    }

}

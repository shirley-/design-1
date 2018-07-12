/*
package com.zym.Design1.exception;

import com.zym.Design1.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

*/
/**
 * Created  on 2018/3/25.
    进入controller之前的错误
 *//*


@RestController
@Slf4j
public class FinalExceptionHandler implements ErrorController {

    @RequestMapping(value = "/error")
    public String error(HttpServletResponse resp, HttpServletRequest req) {
        log.error("请求出错");
        // 错误处理逻辑
        return ResultUtil.getJSONString(ResultUtil.ERROR, "请求出错");
    }

    @Override
    public String getErrorPath() {
        log.error("请求出错path");
        return "/error";
    }
}
*/

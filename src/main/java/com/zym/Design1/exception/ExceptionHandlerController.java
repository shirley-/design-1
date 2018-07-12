package com.zym.Design1.exception;

import com.zym.Design1.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * Created  on 2018/3/25.
 */

//@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    //@ExceptionHandler
//    @ResponseBody
    public String allExceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.info("================【开始打印异常信息】================");
        log.error("具体错误信息:【" + e.getMessage() + "】");
        int count = 0; //只打印15行的错误堆栈
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            log.error(stackTraceElement.toString());
            if (count++ > 13) break;
        }
        log.info("================【异常信息打印完毕】================");
//        if (e instanceof IllegalArgumentException || e instanceof IllegalStateException)
//            return getFailResult(e.getMessage());
//        if (e instanceof PersistenceException || e instanceof BadSqlGrammarException)
//            return getFailResult(CommunityCode.SQL_ERROR);
//        if (e instanceof NullPointerException) return getFailResult(CommunityCode.NULL_POINTER_ERROR);
//        return getFailResult(CommunityCode.OTHER_ERROR);
        response.setStatus(404);
        return "error";
    }
}


/*
package com.zym.Design1.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

*/
/**
 * Created by YM on 2018/3/25.
 *//*

@ControllerAdvice
@Slf4j
public class RequestMappingExceptionHandler extends ResponseEntityExceptionHandler {


    */
/**
     * 处理@RequestParam错误, 即参数不足
     * @return
     *//*

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("参数不足");
        return new ResponseEntity<>( status);
    }

    */
/**
     * 处理500错误
     * @return
     *//*

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("got internal 500 error : {}", ex);

        // 请求方式不支持
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            return new ResponseEntity<>(status);
        }

        return new ResponseEntity<>( status);
    }


    */
/**
     * 处理参数类型转换失败
     * @param request
     * @return
     *//*

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("type mismatch");

        return new ResponseEntity<>( status);
    }
}
*/

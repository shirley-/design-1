package com.zym.Design1.exception;

/**
 * Created on 2018/3/25.
 */
public class CustomException  extends RuntimeException {

    public CustomException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;
    private String msg;

    // getter & setter

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
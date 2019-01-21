package com.rabbit.common.util;

import java.io.Serializable;

/**
 * @ClassName R
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/19 19:13
 **/
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    private String msg = "success";

    private int code = 0;
    private T data;

    public R() {
    }

    public R(Rstatus rstatus) {
        this.code = rstatus.getCode();
        this.msg = rstatus.getMsg();
    }

    public R(T data) {
        this.data = data;
    }

    public R(Throwable e) {
        this.msg = e.getMessage();
        this.code = 1;
    }

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public R(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean success() {
        return 0 == this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

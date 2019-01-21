package com.rabbit.common.util;

/**
 * @ClassName Rstatus
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/19 19:47
 **/
public enum Rstatus {

    SUCCESS(0, "success"),
    NO_LOGIN(1, "no login"),
    FAIL(1, "fail"),
    NO_PERMISSION(2, "no permission");

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private int code;
    private String msg;

    private Rstatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

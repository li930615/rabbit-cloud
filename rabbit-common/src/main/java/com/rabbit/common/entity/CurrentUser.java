package com.rabbit.common.entity;

import io.swagger.annotations.ApiParam;

import java.io.Serializable;

/**
 * @ClassName CurrentUser
 * @Description 当前用户对象
 * @Author LZQ
 * @Date 2019/1/19 18:50
 **/
public class CurrentUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiParam(hidden=true)
    private String id;

    @ApiParam(hidden=true)
    private String loginName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}

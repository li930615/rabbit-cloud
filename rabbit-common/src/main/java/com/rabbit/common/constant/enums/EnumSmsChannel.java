package com.rabbit.common.constant.enums;

/**
 * 〈一句话功能简述〉
 *
 * @author lzq
 * @date 2019/1/19 19:29
 **/
public enum EnumSmsChannel {

    ALIYUN("ALIYUN_SMS", "阿里大鱼");

    private String name;
    private String description;

    private EnumSmsChannel(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.rabbit.common.constant.enums;

import java.util.*;

/**
 * @ClassName BaseEnumHandler
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/19 19:25
 **/
public class BaseEnumHandler<E extends Enum<E>> {

    private Class<E> type;

    public BaseEnumHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type is null");
        }
        this.type = type;
    }

    /*转换成Map*/
    public Map<String, String> convertMap() {
        BaseEnum[] objs = (BaseEnum[]) this.type.getEnumConstants();
        HashMap<String, String> map = new HashMap<>();
        for (BaseEnum baseEnum : objs) {
            map.put(baseEnum.getKey(), baseEnum.getValue());
        }
        return map;
    }

    public String getValue(String key) {
        BaseEnum[] objs;
        for (BaseEnum em : objs = (BaseEnum[]) this.type.getEnumConstants()) {
            if (!em.getKey().equals(key)) continue;
            return em.getValue();
        }
        return null;
    }

    public String getKey(String value) {
        BaseEnum[] objs;
        for (BaseEnum em : objs = (BaseEnum[]) this.type.getEnumConstants()) {
            if (!em.getValue().equals(value)) continue;
            return em.getKey();
        }
        return null;
    }
}

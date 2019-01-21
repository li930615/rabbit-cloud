package com.rabbit.common.bean.xss;

import com.rabbit.common.util.exception.CheckedException;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName SqlFilter
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/19 19:12
 **/
public class SqlFilter {

    public static String sqlInject(String str)
    {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        str = str.toLowerCase();

        String[] keywords = { "master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop" };

        for (String keyword : keywords) {
            if (str.indexOf(keyword) != -1) {
                throw new CheckedException("包含非法字符");
            }
        }

        return str;
    }
}

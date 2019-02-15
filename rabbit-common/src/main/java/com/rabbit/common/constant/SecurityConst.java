package com.rabbit.common.constant;

import io.jsonwebtoken.*;

import javax.xml.bind.DatatypeConverter;

/**
 * @ClassName SecurityConst
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/19 17:55
 **/
public class SecurityConst {

    public static final String SIGNING_KEY = "rabbit_auth";
    public static final String AUTH_TYPE = "auth_type";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String CURRENT_USER = "current_user";

    /*claims 姑且叫做声明，可以理解为和用户相关的一条一条信息的描述，可以是用户的身份信息(Name,Email,ID)
      也可以是用户的角色，甚至是一些自定义的Claims*/
    public static Claims parseJWT(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SIGNING_KEY)).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception ex) {
            return null;
        }
    }
}

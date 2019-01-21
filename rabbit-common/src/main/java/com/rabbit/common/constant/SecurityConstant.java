package com.rabbit.common.constant;

/**
 * @ClassName SecurityConstant
 * @Description 应该是关于图片验证码的
 * @Author LZQ
 * @Date 2019/1/19 19:34
 **/
public abstract interface SecurityConstant {

    String BASE_ROLE = "ROLE_USER";
    String AUTHORIZATION_CODE = "authorization_code";
    String PASSWORD = "password";
    String REFRESH_TOKEN = "refresh_token";
    String OAUTH_TOKEN_URL = "/oauth/token";
    String MOBILE_TOKEN_URL = "/mobile/token";
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";
    String MOBILE_VALIDATE_CODE_URL_PREFIX = "/smsCode";
    String DEFAULT_IMAGE_WIDTH = "150";
    String DEFAULT_IMAGE_HEIGHT = "32";
    String DEFAULT_IMAGE_LENGTH = "4";
    int DEFAULT_IMAGE_EXPIRE = 60;
    String DEFAULT_COLOR_FONT = "black";
    String DEFAULT_IMAGE_BORDER = "no";
    String DEFAULT_CHAR_SPACE = "5";
    String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY";
    String DEFAULT_IMAGE_FONT_SIZE = "30";
    String TOKEN_USER_DETAIL = "token-user-detail";
    String DEFAULT_SOCIAL_PROCESS_URL = "/social";
    String DEFAULT_SOCIAL_SIGNUP_URL = "/social/signup";
}

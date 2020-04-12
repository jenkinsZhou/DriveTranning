package com.tourcoo.training.config;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年12月27日15:09
 * @Email: 971613168@qq.com
 */
public class RequestConfig {

//    public static String BASE_URL_NO_LINE = "http://192.168.1.76:8099";
    public static String BASE_URL_NO_LINE = "https://api.ggjtaq.com";
    public static String BASE_URL = BASE_URL_NO_LINE+"/";
    /**
     * 接口URL + ""
     */
    public static final String BASE_URL_API = BASE_URL+"api/" ;
    public static final int CODE_REQUEST_SUCCESS = 200;
    public static final int CODE_REQUEST_TOKEN_INVALID = 401;
    public static final int CODE_REQUEST_SUCCESS_NOT_REGISTER = -100;
    public static final String MSG_TOKEN_INVALID = "登录失效";
    public static final String KEY_TOKEN = "accesstoken";
    public static final String TOKEN = "Bearer ";
    public static final String EXCEPTION_NO_NETWORK = "ConnectException";
    public static final String MSG_SEND_SUCCESS = "发送成功";
    public static final String STRING_REQUEST_TOKEN_INVALID = "401";
    public static final int PER_PAGE_SIZE = 10;









    public static final String BANNER_URL = BASE_URL_API + "custom-service/referral";


}

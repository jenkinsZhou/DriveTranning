package com.tourcoo.training.config;

import android.Manifest;

import com.tourcoo.training.BuildConfig;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月27日15:37
 * @Email: 971613168@qq.com
 */
public class AppConfig {


    public static  boolean DEBUG_MODE = false;
    /**
     * 标题栏主标题文字大小(sp)
     */
    public static final int TITLE_MAIN_TITLE_SIZE = 19;


    //权限参数
    public static  String[] PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    public static final String TEXT_NO_FINISH_TIP = "此功能正在开发中，预计5月15日上线使用";

    public static final String TEXT_REQUEST_ERROR = "服务器异常 请稍后再试";
}

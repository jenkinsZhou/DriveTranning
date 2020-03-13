package com.tourcoo.training.config;

import android.Manifest;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月27日15:37
 * @Email: 971613168@qq.com
 */
public class AppConfig {


    public static final boolean DEBUG_MODE = true;
    /**
     * 标题栏主标题文字大小(sp)
     */
    public static final int TITLE_MAIN_TITLE_SIZE = 19;


    //权限参数
    public static  String[] PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
}

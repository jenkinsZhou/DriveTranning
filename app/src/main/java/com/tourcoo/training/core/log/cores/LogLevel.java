package com.tourcoo.training.core.log.cores;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author :JenkinsZhou
 * @description :日志等级常量
 * @company :翼迈科技股份有限公司
 * @date 2020年03月07日22:01
 * @Email: 971613168@qq.com
 */
public class LogLevel {
    public static final int TYPE_VERBOSE = 1;
    public static final int TYPE_DEBUG = 2;
    public static final int TYPE_INFO = 3;
    public static final int TYPE_WARM = 4;
    public static final int TYPE_ERROR = 5;
    public static final int TYPE_WTF = 6;

    @IntDef({TYPE_VERBOSE, TYPE_DEBUG, TYPE_INFO, TYPE_WARM, TYPE_ERROR, TYPE_WTF})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LogLevelType {
    }
}
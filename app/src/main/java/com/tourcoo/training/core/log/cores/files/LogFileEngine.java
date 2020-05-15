package com.tourcoo.training.core.log.cores.files;

import java.io.File;

/**
 * @author :JenkinsZhou
 * @description :日志写入接口
 * @company :途酷科技
 * @date 2020年03月07日22:38
 * @Email: 971613168@qq.com
 */
public interface LogFileEngine {

    void writeToFile(File logFile, String logContent, LogFileParam params);
    void flushAsync();
    void release();
}

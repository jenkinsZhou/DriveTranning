package com.tourcoo.training.core.log.cores;

import com.tourcoo.training.core.log.cores.files.LogFileEngine;
import com.tourcoo.training.core.log.cores.files.LogFileFilter;
import java.io.File;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年03月07日23:06
 * @Email: 971613168@qq.com
 */
public interface Log2FileConfig {

    Log2FileConfig configLog2FileEnable(boolean enable);

    Log2FileConfig configLog2FilePath(String logPath);

    Log2FileConfig configLog2FileNameFormat(String formatName);

    Log2FileConfig configLog2FileLevel(@LogLevel.LogLevelType int level);

    Log2FileConfig configLogFileEngine(LogFileEngine engine);

    Log2FileConfig configLogFileFilter(LogFileFilter fileFilter);

    File getLogFile();

    void flushAsync();

    void release();
}

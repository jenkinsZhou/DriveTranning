package com.tourcoo.training.core.log.cores.files;

import com.tourcoo.training.core.log.cores.LogLevel;

/**
 * @author :JenkinsZhou
 * @description :日志过滤器
 * @company :翼迈科技股份有限公司
 * @date 2020年03月07日22:38
 * @Email: 971613168@qq.com
 */
public interface LogFileFilter {

    boolean accept(@LogLevel.LogLevelType int level, String tag, String logContent);

}

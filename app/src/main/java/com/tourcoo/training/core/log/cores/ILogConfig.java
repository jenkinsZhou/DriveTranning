package com.tourcoo.training.core.log.cores;

/**
 * @author :JenkinsZhou
 * @description :日志相关控制
 * @company :途酷科技
 * @date 2020年03月07日22:03
 * @Email: 971613168@qq.com
 */
public interface ILogConfig {

    ILogConfig configAllowLog(boolean allowLog);

    ILogConfig configTagPrefix(String prefix);

    ILogConfig configFormatTag(String formatTag);

    ILogConfig configShowBorders(boolean showBorder);

    ILogConfig configLevel(@LogLevel.LogLevelType int logLevel);

    ILogConfig addParserClass(Class<? extends IParser>... classes);

    ILogConfig configMethodOffset(int offset);
}

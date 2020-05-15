package com.tourcoo.training.core.log.cores.files;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月07日22:39
 * @Email: 971613168@qq.com
 */
public class LogFileParam {

    private long time;
    private int logLevel;
    private String threadName;
    private String tagName;

    public LogFileParam(long time, int logLevel, String threadName, String tagName) {
        this.time = time;
        this.logLevel = logLevel;
        this.threadName = threadName;
        this.tagName = tagName;
    }

    public long getTime() {
        return time;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getTagName() {
        return tagName;
    }
}

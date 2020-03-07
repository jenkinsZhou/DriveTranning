package com.tourcoo.training.core.log.impl;

import android.text.TextUtils;

import com.tourcoo.training.core.log.cores.ILogConfig;
import com.tourcoo.training.core.log.cores.IParser;
import com.tourcoo.training.core.log.cores.LogLevel;
import com.tourcoo.training.core.log.cores.parsers.ParserManager;
import com.tourcoo.training.core.log.cores.pattern.LogPattern;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年03月07日23:12
 * @Email: 971613168@qq.com
 */
public class LogConfigImpl implements ILogConfig {

    private boolean enable = true;
    private String tagPrefix;
    private boolean showBorder = true;
    @LogLevel.LogLevelType
    private int logLevel = LogLevel.TYPE_VERBOSE;
    private String formatTag;
    private int methodOffset = 0;

    private static LogConfigImpl singleton;

    private LogConfigImpl() {
    }

    public static LogConfigImpl getInstance() {
        if (singleton == null) {
            synchronized (LogConfigImpl.class) {
                if (singleton == null) {
                    singleton = new LogConfigImpl();
                }
            }
        }
        return singleton;
    }

    @Override
    public ILogConfig configAllowLog(boolean allowLog) {
        this.enable = allowLog;
        return this;
    }

    @Override
    public ILogConfig configTagPrefix(String prefix) {
        this.tagPrefix = prefix;
        return this;
    }

    @Override
    public ILogConfig configFormatTag(String formatTag) {
        this.formatTag = formatTag;
        return this;
    }

    public  String getFormatTag(StackTraceElement caller) {
        if (TextUtils.isEmpty(formatTag)) {
            return null;
        }
        return LogPattern.compile(formatTag).apply(caller);
    }

    @Override
    public ILogConfig configShowBorders(boolean showBorder) {
        this.showBorder = showBorder;
        return this;
    }

    @Override
    public ILogConfig configMethodOffset(int offset) {
        this.methodOffset = offset;
        return this;
    }

    public int getMethodOffset() {
        return methodOffset;
    }

    @Override
    public ILogConfig configLevel(@LogLevel.LogLevelType int logLevel) {
        this.logLevel = logLevel;
        return this;
    }


    @SafeVarargs
    @Override
    public final ILogConfig addParserClass(Class<? extends IParser>... classes) {
        ParserManager.getInstance().addParserClass(classes);
        return this;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getTagPrefix() {
        if (TextUtils.isEmpty(tagPrefix)) {
            return "TourCooLogUtil";
        }

        return tagPrefix;
    }

    public boolean isShowBorder() {
        return showBorder;
    }

    public int getLogLevel() {
        return logLevel;
    }
}

package com.tourcoo.training.core.log;

import com.tourcoo.training.core.log.cores.ILogConfig;
import com.tourcoo.training.core.log.cores.IPrinter;
import com.tourcoo.training.core.log.cores.Log2FileConfig;
import com.tourcoo.training.core.log.impl.Log2FileConfigImpl;
import com.tourcoo.training.core.log.impl.LogConfigImpl;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月07日23:25
 * @Email: 971613168@qq.com
 */
public class TourCooLogUtil {
    private static Logger printer = new Logger();
    private static LogConfigImpl logConfig = LogConfigImpl.getInstance();
    private static Log2FileConfigImpl log2FileConfig = Log2FileConfigImpl.getInstance();

    /**
     * 选项配置
     *
     * @return LogConfig
     */
    public static ILogConfig getLogConfig() {
        return logConfig;
    }

    /**
     * 日志写入文件相关配置
     *
     * @return LogConfig
     */
    public static Log2FileConfig getLog2FileConfig() {
        return log2FileConfig;
    }

    public static IPrinter tag(String tag) {
        return printer.setTag(tag);
    }

    /**
     * verbose输出
     *
     * @param msg
     * @param args
     */
    public static void v(String msg, Object... args) {
        printer.v(msg, args);
    }

    public static void v(Object object) {
        printer.v(object);
    }


    /**
     * debug输出
     *
     * @param tag
     * @param content
     */
    public static void d(String tag, Object content) {
        printer.setTag(tag).d(content);
    }

    public static void d(Object object) {
        printer.d(object);
    }

    /**
     * info输出
     *
     * @param tag
     * @param content
     */
    public static void i(String tag, Object content) {
        printer.setTag(tag).i(content);
    }


    public static void i(Object object) {
        printer.i(object);
    }

    /**
     * warn输出
     *
     * @param tag
     * @param content
     */
    public static void w(String tag, Object content) {
        printer.setTag(tag).w(content);
    }

    public static void w(Object object) {
        printer.w(object);
    }

    /**
     * error输出
     *
     * @param tag
     * @param content
     */
    public static void e(String tag, Object content) {
        printer.setTag(tag).e(content);
    }

    public static void e(Object object) {
        printer.e(object);
    }

    /**
     * assert输出
     *
     * @param msg
     * @param args
     */
    public static void wtf(String msg, Object... args) {
        printer.wtf(msg, args);
    }

    public static void wtf(Object object) {
        printer.wtf(object);
    }

    /**
     * 打印json
     *
     * @param json
     */
    public static void json(String json) {
        printer.json(json);
    }

    /**
     * 输出xml
     *
     * @param xml
     */
    public static void xml(String xml) {
        printer.xml(xml);
    }
}

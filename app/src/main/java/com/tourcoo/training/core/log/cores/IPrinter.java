package com.tourcoo.training.core.log.cores;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年03月07日22:12
 * @Email: 971613168@qq.com
 */
public interface IPrinter {

    void d(String message, Object... args);
    void d(Object object);

    void e(String message, Object... args);
    void e(Object object);

    void w(String message, Object... args);
    void w(Object object);

    void i(String message, Object... args);
    void i(Object object);

    void v(String message, Object... args);
    void v(Object object);

    void wtf(String message, Object... args);
    void wtf(Object object);

    void json(String json);
    void xml(String xml);
}

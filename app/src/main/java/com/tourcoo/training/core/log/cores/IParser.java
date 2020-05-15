package com.tourcoo.training.core.log.cores;

import androidx.annotation.NonNull;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月07日22:08
 * @Email: 971613168@qq.com
 */
public interface IParser<T> {
    // 换行符
    String LINE_SEPARATOR = System.getProperty("line.separator");

    @NonNull
    Class<T> parseClassType();

    String parseString(@NonNull T t);
}

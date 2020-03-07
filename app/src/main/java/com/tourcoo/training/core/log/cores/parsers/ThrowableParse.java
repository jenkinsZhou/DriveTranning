package com.tourcoo.training.core.log.cores.parsers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.tourcoo.training.core.log.cores.IParser;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年03月07日22:57
 * @Email: 971613168@qq.com
 */
class ThrowableParse implements IParser<Throwable> {
    @NonNull
    @Override
    public Class<Throwable> parseClassType() {
        return Throwable.class;
    }

    @Override
    public String parseString(@NonNull Throwable throwable) {
        return Log.getStackTraceString(throwable);
    }
}

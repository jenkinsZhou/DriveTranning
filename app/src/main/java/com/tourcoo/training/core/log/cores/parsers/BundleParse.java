package com.tourcoo.training.core.log.cores.parsers;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.tourcoo.training.core.log.cores.IParser;
import com.tourcoo.training.core.log.cores.utils.ObjectUtil;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月07日22:43
 * @Email: 971613168@qq.com
 */
class BundleParse implements IParser<Bundle> {

    @NonNull
    @Override
    public Class<Bundle> parseClassType() {
        return Bundle.class;
    }

    @Override
    public String parseString(@NonNull Bundle bundle) {
        StringBuilder builder = new StringBuilder(bundle.getClass().getName());
        builder.append(" [");
        builder.append(LINE_SEPARATOR);
        for (String key : bundle.keySet()) {
            builder.append(String.format("'%s' => %s " + LINE_SEPARATOR,
                    key, ObjectUtil.objectToString(bundle.get(key))));
        }
        builder.append("]");
        return builder.toString();
    }
}

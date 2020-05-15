package com.tourcoo.training.core.log.cores.parsers;



import androidx.annotation.NonNull;

import com.tourcoo.training.core.log.cores.IParser;
import com.tourcoo.training.core.log.cores.utils.ObjectUtil;

import java.lang.ref.Reference;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月07日22:57
 * @Email: 971613168@qq.com
 */
class ReferenceParse implements IParser<Reference> {
    @NonNull
    @Override
    public Class<Reference> parseClassType() {
        return Reference.class;
    }

    @Override
    public String parseString(@NonNull Reference reference) {
        Object actual = reference.get();
        if (actual == null) {
            return "get reference = null";
        }
        String result = reference.getClass().getSimpleName() + "<"
                + actual.getClass().getSimpleName() + "> {" + "→" + ObjectUtil.objectToString(actual);
        return result + "}";
    }
}

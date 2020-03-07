package com.tourcoo.training.core.log.cores.parsers;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.tourcoo.training.core.log.cores.IParser;
import com.tourcoo.training.core.log.cores.utils.ObjectUtil;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年03月07日22:43
 * @Email: 971613168@qq.com
 */
class CollectionParse implements IParser<Collection> {
    @NonNull
    @Override
    public Class<Collection> parseClassType() {
        return Collection.class;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String parseString(@NonNull Collection collection) {
        String simpleName = collection.getClass().getName();
        StringBuilder msg = new StringBuilder(String.format("%s size = %d [" + LINE_SEPARATOR, simpleName,
                collection.size()));
        if (!collection.isEmpty()) {
            Iterator iterator = collection.iterator();
            int flag = 0;
            while (iterator.hasNext()) {
                String itemString = "[%d]:%s%s";
                Object item = iterator.next();
                msg.append(String.format(itemString, flag, ObjectUtil.objectToString(item),
                        flag++ < collection.size() - 1 ? "," + LINE_SEPARATOR : LINE_SEPARATOR));
            }
        }
        return msg + "]";
    }
}

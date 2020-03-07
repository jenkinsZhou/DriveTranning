package com.tourcoo.training.core.log.cores.parsers;



import androidx.annotation.NonNull;

import com.tourcoo.training.core.log.cores.IParser;
import com.tourcoo.training.core.log.cores.utils.ObjectUtil;

import java.util.Map;
import java.util.Set;


class MapParse implements IParser<Map> {
    @NonNull
    @Override
    public Class<Map> parseClassType() {
        return Map.class;
    }

    @Override
    public String parseString(@NonNull Map map) {
        StringBuilder msg = new StringBuilder(map.getClass().getName() + " [" + LINE_SEPARATOR);
        Set keys = map.keySet();
        for (Object key : keys) {
            String itemString = "%s -> %s" + LINE_SEPARATOR;
            Object value = map.get(key);
            if (value != null) {
                if (value instanceof String) {
                    value = "\"" + value + "\"";
                } else if (value instanceof Character) {
                    value = "\'" + value + "\'";
                }
            }
            msg.append(String.format(itemString, ObjectUtil.objectToString(key),
                    ObjectUtil.objectToString(value)));
        }
        return msg + "]";
    }
}

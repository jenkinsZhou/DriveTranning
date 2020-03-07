package com.tourcoo.training.core.log.cores.parsers;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.tourcoo.training.core.log.cores.IParser;
import com.tourcoo.training.core.log.cores.utils.ObjectUtil;

import java.lang.reflect.Field;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年03月07日22:43
 * @Email: 971613168@qq.com
 */
public class ActivityParse implements IParser<Activity> {
    @NonNull
    @Override
    public Class<Activity> parseClassType() {
        return Activity.class;
    }

    @Override
    public String parseString(@NonNull Activity activity) {
        Field[] fields = activity.getClass().getDeclaredFields();
        StringBuilder builder = new StringBuilder(activity.getClass().getName());
        builder.append(" {");
        builder.append(LINE_SEPARATOR);
        for (Field field : fields) {
            field.setAccessible(true);
            if ("org.aspectj.lang.JoinPoint$StaticPart".equals(field.getType().getName())) {
                continue;
            }
            if (field.getName().equals("$change") || field.getName().equalsIgnoreCase("this$0")) {
                continue;
            }
            try {
                Object fieldValue = field.get(activity);
                builder.append(field.getName()).append("=>")
                        .append(ObjectUtil.objectToString(fieldValue)).append(LINE_SEPARATOR);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        builder.append("}");
        return builder.toString();
    }
}

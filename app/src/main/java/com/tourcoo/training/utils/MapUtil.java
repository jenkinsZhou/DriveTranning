package com.tourcoo.training.utils;

import java.util.Map;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月08日14:51
 * @Email: 971613168@qq.com
 */
@SuppressWarnings("unchecked")
public class MapUtil {

    /**
     * 合并多个map
     * @param maps
     * @param <K>
     * @param <V>
     * @return
     * @throws Exception
     */

    public static <K, V> Map mergeMaps(Map<K, V>... maps) {
        Class clazz = maps[0].getClass(); // 获取传入map的类型
        Map<K, V> map = null;
        try {
            map = (Map) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0, len = maps.length; i < len; i++) {
            map.putAll(maps[i]);
        }
        return map;
    }

}

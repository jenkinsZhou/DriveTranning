package com.tourcoo.training.core.interfaces;

/**
 * @author :JenkinsZhou
 * @description :侧滑是否可用
 * @company :途酷科技
 * @date 2020年02月13日22:21
 * @Email: 971613168@qq.com
 */
public interface ISideControl {
    /**
     * 是否允许侧滑
     */
    default boolean isSlideEnable() {
        return true;
    }
}

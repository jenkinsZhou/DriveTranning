package com.tourcoo.training.core.interfaces;

import android.app.Activity;
import android.view.View;

import com.tourcoo.training.core.widget.navigation.NavigationViewHelper;


/**
 * @Author: JenkinsZhou on 2019/7/19 14:35
 * @E-Mail: 971613168@qq.com
 * @Function: Activity 全局虚拟导航栏控制
 * @Description: 1、2019-7-19 14:35:52 从{@link ActivityFragmentControl}抽离用于Activity做定制化
 */
public interface INavigationBar {

    /**
     * Activity 全局虚拟导航栏控制
     *
     * @param activity   目标Activity
     * @param helper     NavigationViewHelper
     * @param bottomView 底部View
     * @return true 表示调用 helper 的init方法进行设置
     */
    boolean setNavigationBar(Activity activity, NavigationViewHelper helper, View bottomView);
}

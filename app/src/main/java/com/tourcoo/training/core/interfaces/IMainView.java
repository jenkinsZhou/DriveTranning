package com.tourcoo.training.core.interfaces;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.tourcoo.training.R;
import com.tourcoo.training.core.base.entity.FrameTabEntity;
import com.tourcoo.training.core.widget.view.tab.CommonTabLayout;
import com.tourcoo.training.core.widget.view.tab.listener.OnTabSelectListener;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :包含CommonTabLayout的主页面Activity/Fragment
 * @company :途酷科技
 * @date 2019年12月26日15:23
 * @Email: 971613168@qq.com
 */
public interface IMainView extends OnTabSelectListener {

    /**
     * 控制主界面Fragment是否可滑动切换
     *
     * @return true 可滑动切换(需要配合ViewPager)
     */
    default boolean isSwipeEnable() {
        return false;
    }

    /**
     * 承载主界面Fragment的ViewGroup id 一般为FrameLayout
     *
     * @return viewId
     */
    default int getContainerViewId() {
        return R.id.frameLayoutContainerMain;
    }

    /**
     * 用于添加Tab属性(文字-图标)
     *
     * @return 主页tab数组
     */
    @Nullable
    List<FrameTabEntity> getTabList();

    /**
     * 获取onCreate 携带参数
     *
     * @return
     */
    Bundle getSavedInstanceState();

    /**
     * 返回 CommonTabLayout  对象用于自定义设置
     *
     * @param tabLayout CommonTabLayout 对象用于单独属性调节
     */
    void setTabLayout(CommonTabLayout tabLayout);

    /**
     * 设置ViewPager属性
     *
     * @param mViewPager ViewPager属性控制
     */
    default void setViewPager(ViewPager mViewPager) {

    }

    /**
     * tab首次选中
     *
     * @param position
     */
    @Override
    default void onTabSelect(int position) {

    }

    /**
     * tab选中状态再点击
     *
     * @param position
     */
    @Override
    default void onTabReselect(int position) {

    }

}

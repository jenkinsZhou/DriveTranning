package com.tourcoo.training.core.base.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.tourcoo.training.R;
import com.tourcoo.training.core.base.delegate.MainTabDelegate;
import com.tourcoo.training.core.interfaces.IMainView;
import com.tourcoo.training.core.widget.view.tab.listener.OnTabSelectListener;


/**
 * @Author: JenkinsZhou on 2018/7/23 11:27
 * @E-Mail: JenkinsZhou@126.com
 * Function: 快速创建主页布局
 * Description:
 */
public abstract class BaseMainFragment extends BaseFragment implements IMainView, OnTabSelectListener {

    protected MainTabDelegate mFastMainTabDelegate;

    @Override
    public void setViewPager(ViewPager mViewPager) {
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public int getContentLayout() {
        return isSwipeEnable() ? R.layout.frame_activity_main_view_pager : R.layout.frame_activity_main;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mFastMainTabDelegate != null) {
            mFastMainTabDelegate.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mFastMainTabDelegate = new MainTabDelegate(mContentView, this, this);
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }
}

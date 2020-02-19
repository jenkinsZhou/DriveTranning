package com.tourcoo.training.core.base.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.tourcoo.training.R;
import com.tourcoo.training.core.base.delegate.MainTabDelegate;
import com.tourcoo.training.core.interfaces.IMainView;


/**
 * @Author: JenkinsZhou on 2018/7/23 10:00
 * @E-Mail: JenkinsZhou@126.com
 * Function: 快速创建主页Activity布局
 * Description:
 */
public abstract class BaseMainActivity extends BaseActivity implements IMainView {

    protected MainTabDelegate mFastMainTabDelegate;

    @Override
    public int getContentLayout() {
        return isSwipeEnable() ? R.layout.frame_activity_main_view_pager : R.layout.frame_activity_main;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
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
    public Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }

    @Override
    public void onBackPressed() {
        quitApp();
    }

    @Override
    protected void onDestroy() {
        if (mFastMainTabDelegate != null) {
            mFastMainTabDelegate.onDestroy();
        }
        super.onDestroy();
    }
}

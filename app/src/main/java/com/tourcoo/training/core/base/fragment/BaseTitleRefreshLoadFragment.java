package com.tourcoo.training.core.base.fragment;

import android.os.Bundle;

import com.tourcoo.training.core.base.delegate.TitleDelegate;
import com.tourcoo.training.core.interfaces.ITitleView;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;


/**
 * @Author: JenkinsZhou on 2018/7/23 10:34
 * @E-Mail: JenkinsZhou@126.com
 * Function: 设置有TitleBar及下拉刷新Fragment
 * Description:
 */
public abstract class BaseTitleRefreshLoadFragment<T> extends BaseRefreshLoadFragment<T> implements ITitleView {

    protected TitleBarView mTitleBar;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        mTitleBar = new TitleDelegate(mContentView, this, getClass()).mTitleBar;
        super.beforeInitView(savedInstanceState);
    }
}

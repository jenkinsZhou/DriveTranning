package com.tourcoo.training.core.base.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tourcoo.training.core.base.delegate.RefreshLoadDelegate;
import com.tourcoo.training.core.base.delegate.TitleDelegate;
import com.tourcoo.training.core.interfaces.IHttpRequestControl;
import com.tourcoo.training.core.interfaces.IRefreshLoadView;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @Author: JenkinsZhou on 2018/7/20 16:54
 * @E-Mail: JenkinsZhou@126.com
 * Function:下拉刷新及上拉加载更多
 * Description:
 * 1、2018-7-9 09:50:59 修正Adapter 错误造成无法展示列表数据BUG
 * 2、2018-7-20 16:54:47 设置StatusLayoutManager 目标View
 * 3、2019-4-19 09:41:28 修改IFastTitleView 逻辑
 */
public abstract class BaseRefreshLoadActivity<T>
        extends BaseTitleActivity implements IRefreshLoadView<T> {
    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected StatusLayoutManager mStatusManager;
    private BaseQuickAdapter mQuickAdapter;
    protected int mDefaultPage = 0;
    protected int mDefaultPageSize = 10;

    protected RefreshLoadDelegate<T> mFastRefreshLoadDelegate;
    private Class<?> mClass;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mClass = getClass();
        new TitleDelegate(mContentView, this, getClass());
        mFastRefreshLoadDelegate = new RefreshLoadDelegate<>(mContentView, this, getClass());
        mRecyclerView = mFastRefreshLoadDelegate.mRecyclerView;
        mRefreshLayout = mFastRefreshLoadDelegate.mRefreshLayout;
        mStatusManager = mFastRefreshLoadDelegate.mStatusManager;
        mQuickAdapter = mFastRefreshLoadDelegate.mAdapter;
    }

    @Override
    public IHttpRequestControl getIHttpRequestControl() {
        return new IHttpRequestControl() {
            @Override
            public SmartRefreshLayout getRefreshLayout() {
                return mRefreshLayout;
            }

            @Override
            public BaseQuickAdapter getRecyclerAdapter() {
                return mQuickAdapter;
            }

            @Override
            public StatusLayoutManager getStatusLayoutManager() {
                return mStatusManager;
            }

            @Override
            public int getCurrentPage() {
                return mDefaultPage;
            }

            @Override
            public int getPageSize() {
                return mDefaultPageSize;
            }

            @Override
            public Class<?> getRequestClass() {
                return mClass;
            }
        };
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mDefaultPage = 0;
        mFastRefreshLoadDelegate.setLoadMore(isLoadMoreEnable());
        loadData(mDefaultPage);
    }

    @Override
    public void onLoadMoreRequested() {
        loadData(++mDefaultPage);
    }

    @Override
    public void loadData() {
        loadData(mDefaultPage);
    }

    @Override
    protected void onDestroy() {
        if (mFastRefreshLoadDelegate != null) {
            mFastRefreshLoadDelegate.onDestroy();
        }
        super.onDestroy();
    }
}

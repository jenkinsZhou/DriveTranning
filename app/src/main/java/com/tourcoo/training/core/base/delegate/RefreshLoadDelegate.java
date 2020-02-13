package com.tourcoo.training.core.base.delegate;

import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tourcoo.training.R;
import com.tourcoo.training.core.UiManager;
import com.tourcoo.training.core.interfaces.IRefreshLoadView;
import com.tourcoo.training.core.util.FindViewUtil;
import com.tourcoo.training.core.widget.view.FrameLoadMoreView;

import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @Author: JenkinsZhou on 2018/7/13 17:52
 * @E-Mail: JenkinsZhou@126.com
 * Function: 快速实现下拉刷新及上拉加载更多代理类
 * Description:
 * 1、使用StatusLayoutManager重构多状态布局功能
 * 2、2018-7-20 17:00:16 新增StatusLayoutManager 设置目标View优先级
 * 3、2018-7-20 17:44:30 新增StatusLayoutManager 点击事件处理
 */
public class RefreshLoadDelegate<T> {
    public static final String TAG = "RefreshLoadDelegate";
    public SmartRefreshLayout mRefreshLayout;
    public RecyclerView mRecyclerView;
    public BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    public StatusLayoutManager mStatusManager;
    private IRefreshLoadView<T> mIRefreshLoadView;
    private RefreshDelegate mRefreshDelegate;
    private Context mContext;
    private UiManager mManager;
    public View mRootView;
    private Class<?> mTargetClass;

    public RefreshLoadDelegate(View rootView, IRefreshLoadView<T> iRefreshLoadView, Class<?> cls) {
        this.mRootView = rootView;
        this.mIRefreshLoadView = iRefreshLoadView;
        this.mTargetClass = cls;
        this.mContext = rootView.getContext().getApplicationContext();
        this.mManager = UiManager.getInstance();
        if (mIRefreshLoadView == null) {
            return;
        }
        mRefreshDelegate = new RefreshDelegate(rootView, iRefreshLoadView);
        mRefreshLayout = mRefreshDelegate.mRefreshLayout;
        getRecyclerView(rootView);
        initRecyclerView();
        setStatusManager();
    }

    /**
     * 初始化RecyclerView配置
     */
    protected void initRecyclerView() {
        if (mRecyclerView == null) {
            return;
        }
        if (UiManager.getInstance().getFastRecyclerViewControl() != null) {
            UiManager.getInstance().getFastRecyclerViewControl().setRecyclerView(mRecyclerView, mTargetClass);
        }
        mAdapter = mIRefreshLoadView.getAdapter();
        mRecyclerView.setLayoutManager(mIRefreshLoadView.getLayoutManager() == null ? new LinearLayoutManager(mContext) : mIRefreshLoadView.getLayoutManager());
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter != null) {
            setLoadMore(mIRefreshLoadView.isLoadMoreEnable());
            //先判断是否Activity/Fragment设置过;再判断是否有全局设置;最后设置默认
            mAdapter.setLoadMoreView(mIRefreshLoadView.getLoadMoreView() != null
                    ? mIRefreshLoadView.getLoadMoreView() :
                    mManager.getLoadMoreFoot() != null ?
                            mManager.getLoadMoreFoot().createDefaultLoadMoreView(mAdapter) :
                            new FrameLoadMoreView(mContext).getBuilder().build());
            if (mIRefreshLoadView.isItemClickEnable()) {
                mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        mIRefreshLoadView.onItemClicked(adapter, view, position);
                    }

                });
            }
        }
    }

    public void setLoadMore(boolean enable) {
        if (mAdapter != null) {
            mAdapter.setOnLoadMoreListener(enable ? mIRefreshLoadView : null, mRecyclerView);
        }
    }

    private void setStatusManager() {
        //优先使用当前配置
        View contentView = mIRefreshLoadView.getMultiStatusContentView();
        if (contentView == null) {
            contentView = mRefreshLayout;
        }
        if (contentView == null) {
            contentView = mRecyclerView;
        }
        if (contentView == null) {
            contentView = mRootView;
        }
        if (contentView == null) {
            return;
        }
        StatusLayoutManager.Builder builder = new StatusLayoutManager.Builder(contentView)
                .setDefaultLayoutsBackgroundColor(android.R.color.transparent)
                .setDefaultEmptyText(R.string.frame_multi_empty)
                .setDefaultEmptyClickViewTextColor(ContextCompat.getColor(mContext, R.color.colorTitleText))
                .setDefaultLoadingText(R.string.frame_multi_loading)
                .setDefaultErrorText(R.string.frame_multi_error)
                .setDefaultErrorClickViewTextColor(ContextCompat.getColor(mContext, R.color.colorTitleText))
                .setOnStatusChildClickListener(new OnStatusChildClickListener() {
                    @Override
                    public void onEmptyChildClick(View view) {
                        if (mIRefreshLoadView.getEmptyClickListener() != null) {
                            mIRefreshLoadView.getEmptyClickListener().onClick(view);
                            return;
                        }
                        mStatusManager.showLoadingLayout();
                        mIRefreshLoadView.onRefresh(mRefreshLayout);
                    }

                    @Override
                    public void onErrorChildClick(View view) {
                        if (mIRefreshLoadView.getErrorClickListener() != null) {
                            mIRefreshLoadView.getErrorClickListener().onClick(view);
                            return;
                        }
                        mStatusManager.showLoadingLayout();
                        mIRefreshLoadView.onRefresh(mRefreshLayout);
                    }

                    @Override
                    public void onCustomerChildClick(View view) {
                        if (mIRefreshLoadView.getCustomerClickListener() != null) {
                            mIRefreshLoadView.getCustomerClickListener().onClick(view);
                            return;
                        }
                        mStatusManager.showLoadingLayout();
                        mIRefreshLoadView.onRefresh(mRefreshLayout);
                    }
                });
        if (mManager != null && mManager.getMultiStatusView() != null) {
            mManager.getMultiStatusView().setMultiStatusView(builder, mIRefreshLoadView);
        }
        mIRefreshLoadView.setMultiStatusView(builder);
        mStatusManager = builder.build();
        mStatusManager.showLoadingLayout();
    }

    /**
     * 获取布局RecyclerView
     *
     * @param rootView
     */
    private void getRecyclerView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.rvCommon);
        if (mRecyclerView == null) {
            mRecyclerView = FindViewUtil.getTargetView(rootView, RecyclerView.class);
        }
    }

    /**
     * 与Activity 及Fragment onDestroy 及时解绑释放避免内存泄露
     */
    public void onDestroy() {
        if (mRefreshDelegate != null) {
            mRefreshDelegate.onDestroy();
            mRefreshDelegate = null;
        }
        mRefreshLayout = null;
        mRecyclerView = null;
        mAdapter = null;
        mStatusManager = null;
        mIRefreshLoadView = null;
        mContext = null;
        mManager = null;
        mRootView = null;
        mTargetClass = null;
        LogUtils.tag(TAG).i("onDestroy");
    }
}

package com.tourcoo.training.core.base.delegate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tourcoo.training.R;
import com.tourcoo.training.core.UiManager;
import com.tourcoo.training.core.interfaces.IRefreshView;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.util.FindViewUtil;

/**
 * @Author: JenkinsZhou on 2019/3/22 14:18
 * @E-Mail: 971613168@qq.com
 * @Function: 快速实现下拉刷新布局代理
 * @Description:
 */
public class RefreshDelegate {
     public static final String TAG = "RefreshDelegate";
    public SmartRefreshLayout mRefreshLayout;
    public View mRootView;
    private UiManager mManager;
    private IRefreshView mIRefreshView;
    private Context mContext;

    public RefreshDelegate(View rootView, IRefreshView iRefreshView) {
        this.mRootView = rootView;
        this.mIRefreshView = iRefreshView;
        this.mContext = rootView.getContext().getApplicationContext();
        this.mManager = UiManager.getInstance();
        if (mIRefreshView == null) {
            return;
        }
        if (mRootView == null) {
            mRootView = mIRefreshView.getContentView();
        }
        if (mRootView == null) {
            return;
        }
        getRefreshLayout(rootView);
        initRefreshHeader();
        if (mRefreshLayout != null) {
            mIRefreshView.setRefreshLayout(mRefreshLayout);
        }
    }

    /**
     * 初始化刷新头配置
     */
    protected void initRefreshHeader() {
        if (mRefreshLayout == null) {
            return;
        }
        if (mRefreshLayout.getRefreshHeader() != null) {
            return;
        }
        mRefreshLayout.setRefreshHeader(mIRefreshView.getRefreshHeader() != null
                ? mIRefreshView.getRefreshHeader() :
                mManager.getDefaultRefreshHeader() != null ?
                        mManager.getDefaultRefreshHeader().createRefreshHeader(mContext, mRefreshLayout) :
                        new ClassicsHeader(mContext).setSpinnerStyle(SpinnerStyle.Translate));
        mRefreshLayout.setOnRefreshListener(mIRefreshView);
        mRefreshLayout.setEnableRefresh(mIRefreshView.isRefreshEnable());
    }

    /**
     * 获取布局里的刷新Layout
     *
     * @param rootView
     * @return
     */
    private void getRefreshLayout(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.smartRefreshLayoutCommon);
        if (mRefreshLayout == null) {
            mRefreshLayout = FindViewUtil.getTargetView(rootView, SmartRefreshLayout.class);
        }
        //原布局无SmartRefreshLayout 将rootView 从父布局移除并添加进SmartRefreshLayout 将SmartRefreshLayout作为新的
        if (mRefreshLayout == null && mIRefreshView.isRefreshEnable()) {
            ViewGroup parentLayout;
            ViewGroup.LayoutParams params = mRootView.getLayoutParams();

            if (mRootView.getParent() != null) {
                parentLayout = (ViewGroup) mRootView.getParent();
            } else {
                parentLayout = mRootView.getRootView().findViewById(android.R.id.content);
            }
            //如果此时parentLayout为null 可能mRootView为Fragment 根布局
            if (parentLayout == null) {
                return;
            }
            int index = parentLayout.indexOfChild(mRootView);
            //先移除rootView
            parentLayout.removeView(mRootView);
            //新建SmartRefreshLayout
            mRefreshLayout = new SmartRefreshLayout(mRootView.getContext());
            //将rootView添加进SmartRefreshLayout
            mRefreshLayout.addView(mRootView);
            //将SmartRefreshLayout添加进parentLayout
            parentLayout.addView(mRefreshLayout, index, params);
        }
    }


    /**
     * 与Activity 及Fragment onDestroy 及时解绑释放避免内存泄露
     */
    public void onDestroy() {
        mRefreshLayout = null;
        mContext = null;
        mManager = null;
        mRootView = null;
       TourCooLogUtil.i( "onDestroy");
    }
}

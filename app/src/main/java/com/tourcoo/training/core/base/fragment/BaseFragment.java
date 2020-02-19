package com.tourcoo.training.core.base.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentManager;


import com.apkfuns.logutils.LogUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tourcoo.training.core.UiManager;
import com.tourcoo.training.core.constant.FrameConstant;
import com.tourcoo.training.core.interfaces.IBasicView;
import com.tourcoo.training.core.interfaces.IRefreshLoadView;
import com.tourcoo.training.core.manager.RxJavaManager;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.core.util.CommonUtil;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;


/**
 * @Author: JenkinsZhou on 2018/7/13 17:49
 * @E-Mail: JenkinsZhou@126.com
 * Function: 所有Fragment的基类实现懒加载
 * Description:
 * 1、新增控制是否为FragmentActivity的唯一Fragment 方法以优化懒加载方式
 * 2、增加解决StatusLayoutManager与SmartRefreshLayout冲突解决方案
 * 3、2018-7-6 17:12:16 删除IBasisFragment 控制是否单Fragment 通过另一种方式实现
 * 4、2019-1-29 18:33:10 修改对用户可以见回调{@link #setUserVisibleHint(boolean)}{@link #onHiddenChanged(boolean)} (boolean)}
 * 5、2019-12-19 11:54:20 增加EventBus Subscribe注解方法判断
 */
public abstract class BaseFragment extends RxFragment implements IBasicView {

    protected Handler baseHandler = new Handler();
    protected Activity mContext;
    protected View mContentView;
    protected boolean mIsFirstShow;
    protected boolean mIsViewLoaded;
    protected String TAG = getClass().getSimpleName();
    protected boolean mIsVisibleChanged = false;
    private boolean mIsInViewPager;
    protected Bundle mSavedInstanceState;

    /**
     * 检查Fragment或FragmentActivity承载的Fragment是否只有一个
     *
     * @return
     */
    protected boolean isSingleFragment() {
        int size = 0;
        FragmentManager manager = getFragmentManager();
        if (manager != null) {
            manager.getFragments();
            size = manager.getFragments().size();
        }
        LogUtils.tag(TAG).i(TAG + ";FragmentManager承载Fragment数量:" + size);
        return size <= 1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
        mIsFirstShow = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mSavedInstanceState = savedInstanceState;
        beforeSetContentView();
        mContentView = inflater.inflate(getContentLayout(), container, false);
        //解决StatusLayoutManager与SmartRefreshLayout冲突
        if (this instanceof IRefreshLoadView) {
            if (CommonUtil.isClassExist(FrameConstant.SMART_REFRESH_LAYOUT_CLASS)) {
                if (mContentView.getClass() == SmartRefreshLayout.class) {
                    FrameLayout frameLayout = new FrameLayout(mContext);
                    if (mContentView.getLayoutParams() != null) {
                        frameLayout.setLayoutParams(mContentView.getLayoutParams());
                    }
                    frameLayout.addView(mContentView);
                    mContentView = frameLayout;
                }
            }
        }
        mIsViewLoaded = true;
        if (isEventBusEnable()) {
            if (CommonUtil.isClassExist(FrameConstant.EVENT_BUS_CLASS)) {
                if (CommonUtil.haveEventBusAnnotation(this)) {
                    org.greenrobot.eventbus.EventBus.getDefault().register(this);
                }
            }
            if (CommonUtil.isClassExist(FrameConstant.ANDROID_EVENT_BUS_CLASS)) {
                EventBus.getDefault().register(this);
            }
        }
        beforeInitView(savedInstanceState);
        setStatusBarDarkMode(mContext, isStatusBarDarkMode());
        initView(savedInstanceState);

        if (isSingleFragment() && !mIsVisibleChanged) {
            if (getUserVisibleHint() || isVisible() || !isHidden()) {
                onVisibleChanged(true);
            }
        }
        LogUtils.tag(TAG).d( ";mIsVisibleChanged:" + mIsVisibleChanged + ";getUserVisibleHint:" + getUserVisibleHint()
                + ";isHidden:" + isHidden() + ";isVisible:" + isVisible());
        return mContentView;
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        if (UiManager.getInstance().getActivityFragmentControl() != null) {
            UiManager.getInstance().getActivityFragmentControl().setContentViewBackground(mContentView, this.getClass());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContentView = null;
        mContext = null;
        mSavedInstanceState = null;
        TAG = null;
    }

    @Override
    public void onDestroy() {
        if (isEventBusEnable()) {
            if (CommonUtil.isClassExist(FrameConstant.EVENT_BUS_CLASS)) {
                if (CommonUtil.haveEventBusAnnotation(this)) {
                    org.greenrobot.eventbus.EventBus.getDefault().unregister(this);
                }
            }
            if (CommonUtil.isClassExist(FrameConstant.ANDROID_EVENT_BUS_CLASS)) {
                EventBus.getDefault().unregister(this);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.tag(TAG).i( "onResume-isAdded:" + isAdded() + ";getUserVisibleHint:" + getUserVisibleHint()
                + ";isHidden:" + isHidden() + ";isVisible:" + isVisible() + ";isResume:" + isResumed() + ";isVisibleToUser:" + isVisibleToUser(this) + ";host:");
        if (isAdded() && isVisibleToUser(this)) {
            onVisibleChanged(true);
        }
    }

    /**
     * @param fragment
     * @return
     */
    private boolean isVisibleToUser(BaseFragment fragment) {
        if (fragment == null) {
            return false;
        }
        if (fragment.getParentFragment() != null) {
            return isVisibleToUser((BaseFragment) fragment.getParentFragment()) && (fragment.isInViewPager() ? fragment.getUserVisibleHint() : fragment.isVisible());
        }
        return fragment.isInViewPager() ? fragment.getUserVisibleHint() : fragment.isVisible();
    }

    /**
     * 不在viewpager中Fragment懒加载
     */
    @Override
    public void onHiddenChanged(final boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!mIsViewLoaded) {
            RxJavaManager.getInstance().setTimer(10)
                    .compose(bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new BaseObserver<Long>() {
                        @Override
                        public void onDoNext(Long entity) {
                            onHiddenChanged(hidden);
                        }
                    });
        } else {
            onVisibleChanged(!hidden);
        }

    }

    /**
     * 在viewpager中的Fragment懒加载
     */
    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsInViewPager = true;
        if (!mIsViewLoaded) {
            RxJavaManager.getInstance().setTimer(10)
                    .compose(bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new BaseObserver<Long>() {
                        @Override
                        public void onDoNext(Long entity) {
                            setUserVisibleHint(isVisibleToUser);
                        }
                    });
        } else {
            onVisibleChanged(isVisibleToUser);
        }
    }

    /**
     * 是否在ViewPager
     *
     * @return
     */
    public boolean isInViewPager() {
        return mIsInViewPager;
    }

    /**
     * 用户可见变化回调
     *
     * @param isVisibleToUser
     */
    protected void onVisibleChanged(final boolean isVisibleToUser) {
        LogUtils.tag(TAG).i( "onVisibleChanged-isVisibleToUser:" + isVisibleToUser);
        mIsVisibleChanged = true;
        if (isVisibleToUser) {
            //避免因视图未加载子类刷新UI抛出异常
            if (!mIsViewLoaded) {
                RxJavaManager.getInstance().setTimer(10)
                        .compose(bindUntilEvent(FragmentEvent.DESTROY))
                        .subscribe(new BaseObserver<Long>() {
                            @Override
                            public void onDoNext(Long entity) {
                                onVisibleChanged(true);
                            }
                        });
            } else {
                fastLazyLoad();
            }
        }
    }

    private void fastLazyLoad() {
        if (mIsFirstShow && mIsViewLoaded) {
            mIsFirstShow = false;
            loadData();
        }
    }


    protected void setStatusBarColor(Activity activity, int color) {
        if (color <= 0) {
            return;
        }
        baseHandler.postDelayed(() -> ImmersionBar.with(activity)
                .statusBarColor(color)
                .init(), 50);
    }


    protected void setStatusBarDarkMode(Activity activity, boolean isDarkFont) {
        baseHandler.postDelayed(() -> ImmersionBar.with(activity)
                .statusBarDarkFont(isDarkFont, 0.2f)
                .init(), 50);
    }

}

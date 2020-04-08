package com.tourcoo.training.core.impl;

import android.app.Activity;
import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


import com.tourcoo.training.core.UiManager;
import com.tourcoo.training.core.base.activity.BaseMainActivity;
import com.tourcoo.training.core.base.activity.BaseRefreshLoadActivity;
import com.tourcoo.training.core.base.delegate.RefreshDelegate;
import com.tourcoo.training.core.base.delegate.TitleDelegate;
import com.tourcoo.training.core.base.fragment.BaseRefreshLoadFragment;
import com.tourcoo.training.core.constant.FrameConstant;
import com.tourcoo.training.core.interfaces.ActivityFragmentControl;
import com.tourcoo.training.core.interfaces.IRefreshLoadView;
import com.tourcoo.training.core.interfaces.IRefreshView;
import com.tourcoo.training.core.interfaces.ITitleView;
import com.tourcoo.training.core.interfaces.INavigationBar;
import com.tourcoo.training.core.interfaces.IStatusBar;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.manager.DelegateManager;
import com.tourcoo.training.core.manager.RxJavaManager;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.FindViewUtil;
import com.tourcoo.training.core.util.StackUtil;
import com.tourcoo.training.core.widget.navigation.KeyboardHelper;
import com.tourcoo.training.core.widget.navigation.NavigationViewHelper;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.core.widget.view.status.StatusViewHelper;
import com.tourcoo.training.core.widget.view.tab.CommonTabLayout;
import com.tourcoo.training.ui.account.register.RecognizeLicenseActivity;
import com.tourcoo.training.widget.idcardcamera.camera.LicenseCameraActivity;

import org.jetbrains.annotations.NotNull;


/**
 * @Author: JenkinsZhou on 2018/7/30 13:48
 * @E-Mail: 971613168@qq.com
 * Function: Activity/Fragment生命周期
 * Description:
 * 1、2018-7-2 09:29:54 新增继承{@link BaseMainActivity}的Activity虚拟导航栏功能
 * 2、2018-11-29 11:49:46 {@link #setStatusBar(Activity)}增加topView background 空判断
 * 3、2018-11-29 11:50:58 {@link #onActivityDestroyed(Activity)} 出栈方法调用{@link StackUtil#pop(Activity, boolean)} 第二个参数设置为false避免因Activity状态切换进入生命周期造成状态无法保存问题
 * 4、2019-3-25 14:27:33 新增下拉刷新功能处理及管理并删除原Fragment生命周期注销方法
 * 5、2019-4-8 17:03:25 删除关于{@link IRefreshLoadView}与{@link IRefreshView}重复判断相关代码
 */
public class FrameLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private String TAG = getClass().getSimpleName();
    private ActivityFragmentControl mActivityFragmentControl;
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
    private FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallbacks;


    @Override
    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
        TourCooLogUtil.i("onActivityCreated:" + activity.getClass().getSimpleName() + ";contentView:" + CommonUtil.getRootView(activity));
        //回调实现自定义逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityCreated(activity, savedInstanceState);
        }
        if (activity instanceof LicenseCameraActivity) {
            return;
        }
        getControl();
        //统一Activity堆栈管理
        StackUtil.getInstance().push(activity);
        //统一横竖屏操作
        if (mActivityFragmentControl != null) {
            mActivityFragmentControl.setRequestedOrientation(activity);
        }
        //统一Fragment生命周期处理
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            fragmentManager.registerFragmentLifecycleCallbacks(this, true);
            if (mFragmentLifecycleCallbacks != null) {
                fragmentManager.registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks, true);
            }
        }
        //设置检测滑动返回是否加载
      /*  if (CommonUtil.isClassExist(FastConstant.BGA_SWIPE_BACK_HELPER_CLASS)
                && !(activity instanceof BGASwipeBackHelper.Delegate)) {
            setSwipeBack(activity);
        }*/
        
    }

    @Override
    public void onActivityStarted(@NotNull Activity activity) {
        if(activity instanceof LicenseCameraActivity){
            return;
        }
        View contentView = CommonUtil.getRootView(activity);
        TourCooLogUtil.i("onActivityStarted:" + activity.getClass().getSimpleName() + ";contentView:" + contentView);
        boolean isSet = activity.getIntent().getBooleanExtra(FrameConstant.IS_SET_CONTENT_VIEW_BACKGROUND, false);
        if (!isSet) {
            setContentViewBackground(CommonUtil.getRootView(activity), activity.getClass());
        }
        //设置状态栏
        setStatusBar(activity);
        //设置虚拟导航栏功能
        setNavigationBar(activity);
        //设置TitleBarView-先设置TitleBarView避免多状态将布局替换
        if (activity instanceof ITitleView
                && !(activity instanceof IRefreshLoadView)
                && !activity.getIntent().getBooleanExtra(FrameConstant.IS_SET_TITLE_BAR_VIEW, false)
                && contentView != null) {
            DelegateManager.getInstance().putFastTitleDelegate(activity.getClass(),
                    new TitleDelegate(contentView, (ITitleView) activity, activity.getClass()));
            activity.getIntent().putExtra(FrameConstant.IS_SET_TITLE_BAR_VIEW, true);
        }
        //配置下拉刷新
        if (activity instanceof IRefreshView
                && !(BaseRefreshLoadActivity.class.isAssignableFrom(activity.getClass()))
                && !activity.getIntent().getBooleanExtra(FrameConstant.IS_SET_REFRESH_VIEW, false)) {
            IRefreshView refreshView = (IRefreshView) activity;
            if (contentView != null
                    || refreshView.getContentView() != null) {
                DelegateManager.getInstance().putFastRefreshDelegate(activity.getClass(),
                        new RefreshDelegate(
                                refreshView.getContentView() != null ? refreshView.getContentView() : contentView,
                                (IRefreshView) activity));
                activity.getIntent().putExtra(FrameConstant.IS_SET_REFRESH_VIEW, true);
            }
        }
        //回调给开发者实现自己应用逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityStarted(activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        TourCooLogUtil.i("onActivityResumed:" + activity.getClass().getSimpleName());
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        TourCooLogUtil.i("onActivityPaused:" + activity.getClass().getSimpleName() + ";isFinishing:" + activity.isFinishing());
        //Activity销毁前的时机需要关闭软键盘-在onActivityStopped及onActivityDestroyed生命周期内已无法关闭
        if (activity.isFinishing()) {
            KeyboardHelper.closeKeyboard(activity);
        }
        //回调给开发者实现自己应用逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        TourCooLogUtil.i("onActivityStopped:" + activity.getClass().getSimpleName() + ";isFinishing:" + activity.isFinishing());
        //回调给开发者实现自己应用逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityStopped(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        TourCooLogUtil.i("onActivitySaveInstanceState:" + activity.getClass().getSimpleName());
        //回调给开发者实现自己应用逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivitySaveInstanceState(activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //横竖屏会重绘将状态重置
        if (activity.getIntent() != null) {
            activity.getIntent().removeExtra(FrameConstant.IS_SET_STATUS_VIEW_HELPER);
            activity.getIntent().removeExtra(FrameConstant.IS_SET_NAVIGATION_VIEW_HELPER);
            activity.getIntent().removeExtra(FrameConstant.IS_SET_CONTENT_VIEW_BACKGROUND);
            activity.getIntent().removeExtra(FrameConstant.IS_SET_REFRESH_VIEW);
            activity.getIntent().removeExtra(FrameConstant.IS_SET_TITLE_BAR_VIEW);
        }
        TourCooLogUtil.i("onActivityDestroyed:" + activity.getClass().getSimpleName() + ";isFinishing:" + activity.isFinishing());
        StackUtil.getInstance().pop(activity, false);

        //清除下拉刷新代理FastRefreshDelegate
        DelegateManager.getInstance().removeFastRefreshDelegate(activity.getClass());
        //清除标题栏代理类FastTitleDelegate
        DelegateManager.getInstance().removeFastTitleDelegate(activity.getClass());
        //清除BasisHelper
        DelegateManager.getInstance().removeBasisHelper(activity);
        //回调给开发者实现自己应用逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityDestroyed(activity);
        }
    }

    @Override
    public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        boolean isSet = f.getArguments() != null && f.getArguments().getBoolean(FrameConstant.IS_SET_CONTENT_VIEW_BACKGROUND, false);
        if (!isSet) {
            setContentViewBackground(v, f.getClass());
        }
        //设置TitleBarView-先设置TitleBarView避免多状态将布局替换
        if (f instanceof ITitleView && !(f instanceof IRefreshLoadView)) {
            DelegateManager.getInstance().putFastTitleDelegate(f.getClass(),
                    new TitleDelegate(v, (ITitleView) f, f.getClass()));
        }
        //刷新功能处理
        if (f instanceof IRefreshView
                && !(BaseRefreshLoadFragment.class.isAssignableFrom(f.getClass()))) {
            IRefreshView refreshView = (IRefreshView) f;
            DelegateManager.getInstance().putFastRefreshDelegate(f.getClass(),
                    new RefreshDelegate(
                            refreshView.getContentView() != null ? refreshView.getContentView() : f.getView(),
                            refreshView));
        }
    }

    @Override
    public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentViewDestroyed(fm, f);
        if (f.getArguments() != null) {
            f.getArguments().putBoolean(FrameConstant.IS_SET_CONTENT_VIEW_BACKGROUND, false);
        }
        DelegateManager.getInstance().removeFastRefreshDelegate(f.getClass());
        DelegateManager.getInstance().removeFastTitleDelegate(f.getClass());
    }

    /**
     * 实时获取回调
     */

    private void getControl() {
        mActivityFragmentControl = UiManager.getInstance().getActivityFragmentControl();
        if (mActivityFragmentControl == null) {
            return;
        }
        mActivityLifecycleCallbacks = mActivityFragmentControl.getActivityLifecycleCallbacks();
        mFragmentLifecycleCallbacks = mActivityFragmentControl.getFragmentLifecycleCallbacks();
    }

    /**
     * 回调设置Activity/Fragment背景
     *
     * @param v
     * @param cls
     */
    private void setContentViewBackground(View v, Class<?> cls) {
        if (mActivityFragmentControl != null && v != null) {
            mActivityFragmentControl.setContentViewBackground(v, cls);
        }
    }


    /**
     * 设置状态栏
     *
     * @param activity 目标Activity
     */
    private void setStatusBar(Activity activity) {
        boolean isSet = activity.getIntent().getBooleanExtra(FrameConstant.IS_SET_STATUS_VIEW_HELPER, false);
        if (isSet) {
            return;
        }
        TitleBarView titleBarView = FindViewUtil.getTargetView(activity.getWindow().getDecorView(), TitleBarView.class);
        //不包含TitleBarView处理
        if (titleBarView == null && !(activity instanceof BaseMainActivity)) {
            View topView = getTopView(CommonUtil.getRootView(activity));
            TourCooLogUtil.i("其它三方库设置状态栏沉浸");
            StatusViewHelper statusViewHelper = StatusViewHelper.with(activity)
                    .setControlEnable(true)
                    .setPlusStatusViewEnable(false)
                    .setTransEnable(true)
                    .setTopView(topView);
            if (topView != null && topView.getBackground() != null) {
                Drawable drawable = topView.getBackground().mutate();
                statusViewHelper.setStatusLayoutDrawable(drawable);
            }
            boolean isInit = mActivityFragmentControl == null || mActivityFragmentControl.setStatusBar(activity, statusViewHelper, topView);
            if (activity instanceof IStatusBar) {
                isInit = ((IStatusBar) activity).setStatusBar(activity, statusViewHelper, topView);
            }
            if (isInit) {
                //状态栏黑色文字图标flag被覆盖问题--临时解决
                RxJavaManager.getInstance().setTimer(10)
                        .subscribe(new BaseObserver<Long>() {
                            @Override
                            public void onSuccessNext(Long entity) {
                                if (activity.isFinishing()) {
                                    return;
                                }
                                statusViewHelper.init();
                                activity.getIntent().putExtra(FrameConstant.IS_SET_STATUS_VIEW_HELPER, true);
                            }
                        });
            }
        }
    }

    /**
     * 设置全局虚拟导航栏功能
     *
     * @param activity 目标Activity
     */
    private void setNavigationBar(Activity activity) {
        boolean isSet = activity.getIntent().getBooleanExtra(FrameConstant.IS_SET_NAVIGATION_VIEW_HELPER, false);
        if (isSet) {
            return;
        }
        TourCooLogUtil.i("setNavigationBars:设置虚拟导航栏");
        View bottomView = CommonUtil.getRootView(activity);
        //继承FastMainActivity底部View处理
        if (BaseMainActivity.class.isAssignableFrom(activity.getClass())) {
            CommonTabLayout tabLayout = FindViewUtil.getTargetView(bottomView, CommonTabLayout.class);
            if (tabLayout != null) {
                bottomView = tabLayout;
            }
        }
        //设置虚拟导航栏控制
        NavigationViewHelper navigationViewHelper = NavigationViewHelper.with(activity)
                .setWhiteStyle();
        if (activity instanceof KeyboardHelper.OnKeyboardVisibilityChangedListener) {
            navigationViewHelper.setOnKeyboardVisibilityChangedListener((KeyboardHelper.OnKeyboardVisibilityChangedListener) activity);
        }
        boolean isInit = mActivityFragmentControl == null || mActivityFragmentControl.setNavigationBar(activity, navigationViewHelper, bottomView);
        if (activity instanceof INavigationBar) {
            isInit = ((INavigationBar) activity).setNavigationBar(activity, navigationViewHelper, bottomView);
        }
        if (isInit) {
            activity.getIntent().putExtra(FrameConstant.IS_SET_NAVIGATION_VIEW_HELPER, true);
            navigationViewHelper.init();
        }
    }

    /**
     * 获取Activity 顶部View(用于延伸至状态栏下边)
     *
     * @param target
     * @return
     */
    private View getTopView(View target) {
        if (target instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) target;
            if (group.getChildCount() > 0) {
                target = ((ViewGroup) target).getChildAt(0);
            }
        }
        return target;
    }
}

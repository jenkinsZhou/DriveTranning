package com.tourcoo.training.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


import androidx.core.content.ContextCompat;

import com.apkfuns.logutils.LogUtils;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.tourcoo.training.R;
import com.tourcoo.training.core.app.MyApplication;
import com.tourcoo.training.core.constant.FrameConstant;
import com.tourcoo.training.core.impl.FrameLifecycleCallbacks;
import com.tourcoo.training.core.interfaces.ActivityDispatchEventControl;
import com.tourcoo.training.core.interfaces.ActivityFragmentControl;
import com.tourcoo.training.core.interfaces.ActivityKeyEventControl;
import com.tourcoo.training.core.interfaces.ObserverControl;
import com.tourcoo.training.core.interfaces.RecyclerViewControl;
import com.tourcoo.training.core.interfaces.HttpRequestControl;
import com.tourcoo.training.core.interfaces.LoadMoreFoot;
import com.tourcoo.training.core.interfaces.LoadingDialog;
import com.tourcoo.training.core.interfaces.MultiStatusView;
import com.tourcoo.training.core.interfaces.QuitAppControl;
import com.tourcoo.training.core.interfaces.TitleBarViewControl;
import com.tourcoo.training.core.interfaces.ToastControl;
import com.tourcoo.training.core.manager.GlideManager;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.dialog.LoadingDialogWrapper;
import com.tourcoo.training.core.widget.dialog.loading.IosLoadingDialog;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年12月26日16:14
 * @Email: 971613168@qq.com
 */
public class UiManager {
    public static final String TAG = "UiManager";

    private UiManager() {
    }

    //原本在Provider中默认进行初始化,如果app出现多进程使用该模式可避免调用异常出现
    static {
        Application application = MyApplication.getContext();
        if (application != null) {
            LogUtils.tag(TAG).i(TAG, "initSuccess");
            init(application);
        }else {
            LogUtils.tag(TAG).e(TAG, "初始化失败");
        }
    }

    private static volatile UiManager sInstance;


    public static UiManager getInstance() {
        if (sInstance == null) {
            throw new NullPointerException(FrameConstant.EXCEPTION_NOT_INIT_FAST_MANAGER);
        }
        return sInstance;
    }

    private static Application mApplication;


    /**
     * Adapter加载更多View
     */
    private LoadMoreFoot mLoadMoreFoot;
    /**
     * 全局设置列表
     */
    private RecyclerViewControl mRecyclerViewControl;
    /**
     * SmartRefreshLayout默认刷新头
     */
    private DefaultRefreshHeaderCreator mDefaultRefreshHeader;
    /**
     * 多状态布局--加载中/空数据/错误/无网络
     */
    private MultiStatusView mMultiStatusView;
    /**
     * 配置全局通用加载等待Loading提示框
     */
    private LoadingDialog mLoadingDialog;
    /**
     * 配置TitleBarView相关属性
     */
    private TitleBarViewControl mTitleBarViewControl;

    /**
     * 配置Activity/Fragment(背景+Activity强制横竖屏+Activity 生命周期回调+Fragment生命周期回调)
     */
    private ActivityFragmentControl mActivityFragmentControl;

    /**
     * 配置BasisActivity 子类前台时监听按键相关
     */
    private ActivityKeyEventControl mActivityKeyEventControl;

    /**
     * 配置BasisActivity 子类事件派发相关
     */
    private ActivityDispatchEventControl mActivityDispatchEventControl;
    /**
     * 配置网络请求
     */
    private HttpRequestControl mHttpRequestControl;

    /**
     * 配置{@link com.tourcoo.training.core.retrofit.BaseObserver#onError(Throwable)}全局处理
     */
    private ObserverControl mObserverControl;
    /**
     * Activity 主页点击返回键控制
     */
    private QuitAppControl mQuitAppControl;
    /**
     * ToastUtil相关配置
     */
    private ToastControl mToastControl;

    public Application getApplication() {
        return mApplication;
    }

    /**
     * 滑动返回基础配置查看{@link com.tourcoo.training.core.impl.FrameLifecycleCallbacks#onActivityCreated(Activity, Bundle)}
     * 不允许外部调用
     *
     * @param application Application 对象
     * @return
     */
    static UiManager init(Application application) {
        LogUtils.tag(TAG).i("init_mApplication:" + mApplication + ";application;" + application);
        //保证只执行一次初始化属性
        if (mApplication == null && application != null) {
            mApplication = application;
            sInstance = new UiManager();
            //预设置FrameLoadDialog属性
            sInstance.setLoadingDialog(activity -> new LoadingDialogWrapper(activity, new IosLoadingDialog(activity).setLoadingText("加载中...")));
          /*  //设置检测滑动返回是否导入
            if (CommonUtil.isClassExist(FrameConstant.BGA_SWIPE_BACK_HELPER_CLASS)) {
                //设置滑动返回监听
                BGASwipeBackHelper.init(mApplication, null);
            }*/
            //注册activity生命周期
            mApplication.registerActivityLifecycleCallbacks(new FrameLifecycleCallbacks());
            //初始化Toast工具
            ToastUtil.init(mApplication);
            //初始化Glide
            GlideManager.setPlaceholderColor(ContextCompat.getColor(mApplication, R.color.colorPlaceholder));
            GlideManager.setPlaceholderRoundRadius(mApplication.getResources().getDimension(R.dimen.dp_placeholder_radius));
        }
        return getInstance();
    }


    public LoadMoreFoot getLoadMoreFoot() {
        return mLoadMoreFoot;
    }

    /**
     * 设置Adapter统一加载更多相关脚布局
     * 最终调用{@link com.tourcoo.training.core.base.delegate.RefreshLoadDelegate()} ()}
     *
     * @param mLoadMoreFoot
     * @return
     */
    public UiManager setLoadMoreFoot(LoadMoreFoot mLoadMoreFoot) {
        this.mLoadMoreFoot = mLoadMoreFoot;
        return this;
    }

    public RecyclerViewControl getRecyclerViewControl() {
        return mRecyclerViewControl;
    }

    /**
     * 全局设置列表
     *
     * @param control
     * @return
     */
    public UiManager setRecyclerViewControl(RecyclerViewControl control) {
        this.mRecyclerViewControl = control;
        return this;
    }

    public DefaultRefreshHeaderCreator getDefaultRefreshHeader() {
        return mDefaultRefreshHeader;
    }

    /**
     * 设置SmartRefreshLayout 下拉刷新头
     * 最终调用{@link com.tourcoo.training.core.base.delegate.RefreshDelegate()}
     *
     * @param control
     * @return
     */
    public UiManager setDefaultRefreshHeader(DefaultRefreshHeaderCreator control) {
        this.mDefaultRefreshHeader = control;
        return sInstance;
    }

    public MultiStatusView getMultiStatusView() {
        return mMultiStatusView;
    }

    /**
     * 设置多状态布局--加载中/空数据/错误/无网络
     * 最终调用{@link com.tourcoo.training.core.base.delegate.RefreshDelegate()}
     *
     * @param control
     * @return
     */
    public UiManager setMultiStatusView(MultiStatusView control) {
        this.mMultiStatusView = control;
        return this;
    }

    public LoadingDialog getLoadingDialog() {
        return mLoadingDialog;
    }

    /**
     * 设置全局网络请求等待Loading提示框如登录等待loading
     * 最终调用{@link com.tourcoo.training.core.retrofit.BaseLoadingObserver#(Activity)}
     *
     * @param control
     * @return
     */
    public UiManager setLoadingDialog(LoadingDialog control) {
        if (control != null) {
            this.mLoadingDialog = control;
        }
        return this;
    }

    public TitleBarViewControl getTitleBarViewControl() {
        return mTitleBarViewControl;
    }

    public UiManager setTitleBarViewControl(TitleBarViewControl control) {
        mTitleBarViewControl = control;
        return this;
    }


    public ActivityFragmentControl getActivityFragmentControl() {
        return mActivityFragmentControl;
    }

    /**
     * 配置Activity/Fragment(背景+Activity强制横竖屏+Activity 生命周期回调+Fragment生命周期回调)
     *
     * @param control
     * @return
     */
    public UiManager setActivityFragmentControl(ActivityFragmentControl control) {
        mActivityFragmentControl = control;
        return this;
    }

    public ActivityKeyEventControl getActivityKeyEventControl() {
        return mActivityKeyEventControl;
    }

    /**
     * 配置BasisActivity 子类前台时监听按键相关
     *
     * @param control
     * @return
     */
    public UiManager setActivityKeyEventControl(ActivityKeyEventControl control) {
        mActivityKeyEventControl = control;
        return this;
    }

    public ActivityDispatchEventControl getActivityDispatchEventControl() {
        return mActivityDispatchEventControl;
    }

    /**
     * 配置BasisActivity 子类事件派发相关
     *
     * @param control
     * @return
     */
    public UiManager setActivityDispatchEventControl(ActivityDispatchEventControl control) {
        mActivityDispatchEventControl = control;
        return this;
    }

    public HttpRequestControl getHttpRequestControl() {
        return mHttpRequestControl;
    }

    /**
     * 配置Http请求成功及失败相关回调-方便全局处理
     *
     * @param control
     * @return
     */
    public UiManager setHttpRequestControl(HttpRequestControl control) {
        mHttpRequestControl = control;
        return this;
    }

    public ObserverControl getFastObserverControl() {
        return mObserverControl;
    }

    /**
     * 配置{@link com.tourcoo.training.core.retrofit.BaseObserver#onError(Throwable)}全局处理
     *
     * @param control FastObserverControl对象
     * @return
     */
    public UiManager setFastObserverControl(ObserverControl control) {
        mObserverControl = control;
        return this;
    }

    public QuitAppControl getQuitAppControl() {
        return mQuitAppControl;
    }

    /**
     * 配置Http请求成功及失败相关回调-方便全局处理
     *
     * @param control
     * @return
     */
    public UiManager setQuitAppControl(QuitAppControl control) {
        mQuitAppControl = control;
        return this;
    }

    public ToastControl getToastControl() {
        return mToastControl;
    }

    /**
     * 配置ToastUtil
     *
     * @param control
     * @return
     */
    public UiManager setToastControl(ToastControl control) {
        mToastControl = control;
        return this;
    }

}

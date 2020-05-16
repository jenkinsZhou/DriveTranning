package com.tourcoo.training.core.app;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.didichuxing.doraemonkit.DoraemonKit;
import com.didichuxing.doraemonkit.kit.webdoor.WebDoorManager;
import com.simple.spiderman.SpiderMan;
import com.tourcoo.training.BuildConfig;
import com.tourcoo.training.config.AppConfig;
import com.tourcoo.training.core.UiManager;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.impl.ActivityControlImpl;
import com.tourcoo.training.core.impl.AppImpl;
import com.tourcoo.training.core.impl.HttpRequestControlImpl;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.retrofit.RetrofitHelper;
import com.tourcoo.training.core.util.NotificationUtil;
import com.tourcoo.training.core.util.ToastUtils;
import com.tourcoo.training.entity.greendao.GreenDaoHelper;

import static com.tourcoo.training.core.log.cores.LogLevel.TYPE_VERBOSE;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年12月26日16:20
 * @Email: 971613168@qq.com
 */
public class MyApplication extends MultiDexApplication {
    public static final String TAG = "MyApplication";
    private static Application application;
    private static final String PREFIX_TAG_GLOBAL = "DriverTraining";

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        com.blankj.utilcode.util.LogUtils.getConfig().setBorderSwitch(false).setGlobalTag("DriveTraining");
        initSync();
    }


    /**
     * 同步初始化 （重要组件初始化）
     */
    private void initSync() {
        initLogConfig();
        SpiderMan.init(this);
        GreenDaoHelper.getInstance().initDatabase(this);
        ToastUtils.init(this);
        //初始化全局防重复点击
        GlobalClickCallbacks.init(this);
//        initDebugKit();
/*//# 支持写入日志到文件
        TourCooLogUtil.getLog2FileConfig().configLog2FileEnable(true)
                // targetSdkVersion >= 23 需要确保有写sdcard权限
                .configLog2FilePath("/sdcard/项目文件夹/logs/")
                .configLog2FileNameFormat("%d{yyyyMMdd}.log")
                .configLogFileEngine(new LogFileEngineFactory(application));*/
        AppImpl impl = new AppImpl(application);
        ActivityControlImpl activityControl = new ActivityControlImpl();
        UiManager.getInstance()
                //设置Adapter加载更多视图--默认设置了FastLoadMoreView
                .setLoadMoreFoot(impl)
                //全局设置RecyclerView
                .setRecyclerViewControl(impl)
                //设置RecyclerView加载过程多布局属性
                .setMultiStatusView(impl)
                //设置全局网络请求等待Loading提示框如登录等待loading--观察者必须为FastLoadingObserver及其子类
                .setLoadingDialog(impl)
                //设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                .setDefaultRefreshHeader(impl)
                //设置全局TitleBarView相关配置
                .setTitleBarViewControl(impl)
//                设置Activity滑动返回控制-默认开启滑动返回功能不需要设置透明主题
//                .setSwipeBackControl(new SwipeBackControlImpl())
                //设置Activity/Fragment相关配置(横竖屏+背景+虚拟导航栏+状态栏+生命周期)
                .setActivityFragmentControl(activityControl)
                //设置BasisActivity 子类按键监听
                .setActivityKeyEventControl(activityControl)
                //配置BasisActivity 子类事件派发相关
                .setActivityDispatchEventControl(activityControl)
                //设置http请求结果全局控制
                .setHttpRequestControl(new HttpRequestControlImpl())
                //配置{@link FastObserver#onError(Throwable)}全局处理
                .setFastObserverControl(impl)
                //设置主页返回键控制-默认效果为2000 毫秒时延退出程序
                .setQuitAppControl(impl)
                //设置ToastUtil全局控制
                .setToastControl(impl);

        //初始化Retrofit配置
        RetrofitHelper.getInstance()
                //配置全局网络请求BaseUrl
                .setBaseUrl(RequestConfig.BASE_URL)
                //信任所有证书--也可设置setCertificates(单/双向验证)
                .setCertificates()
                //设置统一请求头
//                .addHeader(header)
//                .addHeader(key,value)
                //设置请求全局log-可设置tag及Level类型
                .setLogEnable(true)
//                .setLogEnable(BuildConfig.DEBUG, TAG, HttpLoggingInterceptor.Level.BODY)
                //设置统一超时--也可单独调用read/write/connect超时(可以设置时间单位TimeUnit)
                //默认10
                .setTimeout(10);
        NotificationUtil.getInstance().init(this);
        //注意设置baseUrl要以/ 结尾 service 里的方法不要以/打头不然拦截到的url会有问题
        //以下为配置多BaseUrl--默认方式一优先级高 可通过FastRetrofit.getInstance().setHeaderPriorityEnable(true);设置方式二优先级
        //方式一 通过Service 里的method-(如:) 设置 推荐 使用该方式不需设置如方式二的额外Header
       /* RetrofitHelper.getInstance()
                .putBaseUrl(ApiConstant.API_UPDATE_APP, BuildConfig.BASE__UPDATE_URL);*/
    }


    public static Application getContext() {
        return application;
    }


    /**
     * 是否控制底部导航栏
     *
     * @return
     */
    public static boolean isControlNavigation() {
        return false;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initLogConfig() {
        // 配置可展示日志等级
        TourCooLogUtil.getLogConfig()
                // 是否在Logcat显示日志
                .configAllowLog(BuildConfig.DEBUG)
                // 配置统一的TAG 前缀
                .configTagPrefix(PREFIX_TAG_GLOBAL)
                // 首行显示信息(可配置日期，线程等等)
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")
                // 是否显示边框
                .configShowBorders(false)
                .configLevel(TYPE_VERBOSE);
        TourCooLogUtil.getLog2FileConfig()
                //不开启日志写入文件
                .configLog2FileEnable(false);
    }

    private void initDebugKit(){
           if (AppConfig.DEBUG_MODE) {
            //初始化哆啦A梦
            DoraemonKit.install(this);
            // H5任意门功能需要，非必须
            DoraemonKit.setWebDoorCallback(new WebDoorManager.WebDoorCallback() {
                @Override
                public void overrideUrlLoading(Context context, String s) {
                    // 使用自己的H5容器打开这个链接
//                    LoggerManager.i("overrideUrlLoading", "url:" + s);
//                    Web.start(StackUtil.getInstance().getCurrent(), s);
                }
            });
        }

    }
}

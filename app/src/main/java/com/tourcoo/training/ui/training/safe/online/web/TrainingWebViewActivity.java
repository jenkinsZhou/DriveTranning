package com.tourcoo.training.ui.training.safe.online.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.dyhdyh.support.countdowntimer.CountDownTimerSupport;
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.IVideo;
import com.just.agentweb.LogUtils;
import com.just.agentweb.VideoImpl;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.just.agentweb.download.DownloadingService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tourcoo.training.R;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.constant.TrainingConstant;
import com.tourcoo.training.core.base.activity.BaseWebActivity;
import com.tourcoo.training.core.base.activity.WebViewActivity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.manager.RxJavaManager;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.FileUtil;
import com.tourcoo.training.core.util.NotificationUtil;
import com.tourcoo.training.core.util.RomUtil;
import com.tourcoo.training.core.util.SPUtil;
import com.tourcoo.training.core.util.StackUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.navigation.NavigationBarUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.entity.course.CourseInfo;
import com.tourcoo.training.entity.training.Course;
import com.tourcoo.training.ui.MainActivity;
import com.tourcoo.training.ui.MainTabActivity;
import com.tourcoo.training.ui.face.OnLineFaceRecognitionActivity;
import com.tourcoo.training.ui.training.safe.online.PlayVideoActivity;
import com.tourcoo.training.widget.dialog.IosAlertDialog;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.io.File;

import static com.tourcoo.training.constant.TrainingConstant.EXTRA_COURSE_INFO;
import static com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID;

/**
 * @author :JenkinsZhou
 * @description :网页课件
 * * @company :途酷科技
 * @date 2020年04月27日17:26
 * @Email: 971613168@qq.com
 */
public class TrainingWebViewActivity extends BaseWebActivity {
    private String mFilePath = FileUtil.getCacheDir();
    private String mFormat = "保存图片<br><small><font color='#2394FE'>图片文件夹路径:%1s</font></small>";
    private static boolean mIsShowTitle = true;
    private RefreshLayout mRefreshLayout;
    private CountDownTimerSupport mTimerTask;
    private long faceVerifyInterval;
    private String trainingPlanID;
    private Course mCourse;
    public static final String TAG = "TrainingWebViewActivity";
    private TextView tvLimitTips;
    private long currentProgress;


    @Override
    public int getContentLayout() {
        return R.layout.activity_training_webview;
    }

    @Override
    protected int getProgressColor() {
        return super.getProgressColor();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String url = intent.getStringExtra("url");
        trainingPlanID = intent.getStringExtra(EXTRA_TRAINING_PLAN_ID);
        if (!TextUtils.isEmpty(url) && WebCourseTempHelper.getInstance().getCourse() != null) {
            mUrl = url;
            mCourse = WebCourseTempHelper.getInstance().getCourse();
            //todo 设置计时时间
//            faceVerifyInterval = mCourse.getProgress();
        } else {
            ToastUtil.show("未获取到课件");
        }
    }

    @Override
    protected int getProgressHeight() {
        return super.getProgressHeight();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        if (!mIsShowTitle) {
            titleBar.setStatusBarLightMode(false)
                    .setVisibility(View.GONE);
        }
        titleBar.setTitleMainTextMarquee(true)
//                .setOnRightTextClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        WebViewActivity.start(mContext,"www.baidu.com");
//                    }
//                })
                .setDividerVisible(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP);
    }

    @Override
    protected void setAgentWeb(AgentWeb.CommonBuilder mAgentBuilder) {
        super.setAgentWeb(mAgentBuilder);
        //设置 IAgentWebSettings
        mAgentBuilder.setAgentWebWebSettings(getSettings())
                .setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        if (newProgress == 100) {
                            mCurrentUrl = view.getUrl();
                        /*    int position = (int) SPUtil.get(mContext, mCurrentUrl, 0);
                            view.scrollTo(0, position);
                            ToastUtil.showSuccess("加载成功");*/
                            //网页加载成功 开始计时
                            //todo 计时器模块暂不添加
//                            initTimerAndStart();
                        }

                    }

                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        if (mTitleBar != null) {
                            mTitleBar.setTitleMainText(title);
                        }
                    }

                    @Override
                    public void onHideCustomView() {
                        super.onHideCustomView();
                        getIVideo().onHideCustomView();
                        //显示状态栏
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        NavigationBarUtil.setNavigationBarLightMode(mContext);
                    }

                    @Override
                    public void onShowCustomView(View view, CustomViewCallback callback) {
                        super.onShowCustomView(view, callback);
                        getIVideo().onShowCustomView(view, callback);
                        //隐藏状态栏
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        RxJavaManager.getInstance().setTimer(100)
                                .subscribe(new BaseObserver<Long>() {
                                    @Override
                                    public void onSuccessNext(Long entity) {

                                    }
                                });
                    }
                });
    }

    @Override
    protected void setAgentWeb(AgentWeb mAgentWeb) {
        super.setAgentWeb(mAgentWeb);
        WebView mWebView = mAgentWeb.getWebCreator().getWebView();
        mWebView.setOnLongClickListener(v -> {
            WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
            if (hitTestResult == null) {
                return false;
            }
            if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE
                    || hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            } else if (!mIsShowTitle) {
//                showActionSheet();
            }
            return true;
        });
    }


    public View getContentView() {
        if (mAgentWeb != null) {
            TourCooLogUtil.i("getContentView", "webView:" + mAgentWeb.getWebCreator().getWebView());
            return mAgentWeb.getWebCreator().getWebView();
        }
        return null;
    }


    protected boolean isTrans() {
        return (RomUtil.isEMUI() && (RomUtil.getEMUIVersion().compareTo("EmotionUI_4.1") > 0));
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvLimitTips = findViewById(R.id.tvLimitTips);
        tvLimitTips.setText("文件至少要学习5min");
    }

    @Override
    public void onBackPressed() {
       /* Activity activity = StackUtil.getInstance().getPrevious();
        if (activity == null) {
            CommonUtil.startActivity(mContext, MainTabActivity.class);
        }*/
        showExitDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerPause();
        WebView webView = mAgentWeb.getWebCreator().getWebView();
        SPUtil.put(mContext, webView.getUrl(), webView.getScrollY());
    }

    private IVideo mIVideo = null;

    private IVideo getIVideo() {
        if (mIVideo == null) {
            mIVideo = new VideoImpl(mContext, mAgentWeb.getWebCreator().getWebView());
        }
        return mIVideo;
    }

    /**
     * 更新于 AgentWeb  4.0.0
     */
    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {

        private DownloadingService mDownloadingService;

        /**
         *
         * @param url                下载链接
         * @param userAgent          UserAgent
         * @param contentDisposition ContentDisposition
         * @param mimetype           资源的媒体类型
         * @param contentLength      文件长度
         * @param extra              下载配置 ， 用户可以通过 Extra 修改下载icon ， 关闭进度条 ， 是否强制下载。
         * @return true 表示用户处理了该下载事件 ， false 交给 AgentWeb 下载
         */
        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            LogUtils.i(TAG, "onStart:" + url);
            // 是否开启断点续传
            extra.setOpenBreakPointDownload(true)
                    //下载通知的icon
                    .setIcon(R.drawable.ic_file_download_black_24dp)
                    // 连接最大时长
                    .setConnectTimeOut(6000)
                    // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
                    .setBlockMaxTime(10 * 60 * 1000)
                    // 下载最大时长
                    .setDownloadTimeOut(Long.MAX_VALUE)
                    // 串行下载更节省资源哦
                    .setParallelDownload(false)
                    // false 关闭进度通知
                    .setEnableIndicator(true)
                    // 自定义请求头
                    .addHeader("Cookie", "xx")
                    // 下载完成自动打开
                    .setAutoOpen(true)
                    // 强制下载，不管网络网络类型
                    .setForceDownload(true);
            return false;
        }

        /**
         *
         * 不需要暂停或者停止下载该方法可以不必实现
         * @param url
         * @param downloadingService  用户可以通过 DownloadingService#shutdownNow 终止下载
         */
        @Override
        public void onBindService(String url, DownloadingService downloadingService) {
            super.onBindService(url, downloadingService);
            mDownloadingService = downloadingService;
            LogUtils.i(TAG, "onBindService:" + url + "  DownloadingService:" + downloadingService);
        }

        /**
         * 回调onUnbindService方法，让用户释放掉 DownloadingService。
         * @param url
         * @param downloadingService
         */
        @Override
        public void onUnbindService(String url, DownloadingService downloadingService) {
            super.onUnbindService(url, downloadingService);
            mDownloadingService = null;
            LogUtils.i(TAG, "onUnbindService:" + url);
        }

        /**
         *
         * @param url  下载链接
         * @param loaded  已经下载的长度
         * @param length    文件的总大小
         * @param usedTime   耗时 ，单位ms
         * 注意该方法回调在子线程 ，线程名 AsyncTask #XX 或者 AgentWeb # XX
         */
        @Override
        public void onProgress(String url, long loaded, long length, long usedTime) {
            int mProgress = (int) ((loaded) / Float.valueOf(length) * 100);
            LogUtils.i(TAG, "onProgress:" + mProgress);
            //进度到100--主动调用关闭下载并重启下载因断点下载直接回调成功
            if (mProgress == 100) {
                mDownloadingService.shutdownNow().performReDownload();
            }
            super.onProgress(url, loaded, length, usedTime);
        }

        /**
         *
         * @param path 文件的绝对路径
         * @param url  下载地址
         * @param throwable    如果异常，返回给用户异常
         * @return true 表示用户处理了下载完成后续的事件 ，false 默认交给AgentWeb 处理
         */
        @Override
        public boolean onResult(String path, String url, Throwable throwable) {
            NotificationUtil.getInstance().cancelAll();
            //下载成功
            if (null == throwable) {
                if (path.endsWith("apk")) {
                    FileUtil.installApk(new File(path), getPackageName() + ".AgentWebFileProvider");
                }
            } else {//下载失败

            }
            // true  不会发出下载完成的通知 , 或者打开文件
            return path.endsWith("apk");
        }
    };

    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView,
                        DefaultDownloadImpl
                                .create((Activity) webView.getContext(),
                                        webView,
                                        mDownloadListenerAdapter,
                                        mDownloadListenerAdapter,
                                        this.mAgentWeb.getPermissionInterceptor()));
            }
        };
    }


    /**
     * 初始化计时器 并开始计时
     */
    private void initTimerAndStart() {
        cancelTimer();
        TourCooLogUtil.i(TAG, "间隔时间=" + faceVerifyInterval);
        //总时长 间隔时间
        if (faceVerifyInterval <= 0) {
            //取消计时器
            cancelTimer();
            return;
        }
        //初始化计时器
//        faceVerifyInterval = 12
        mTimerTask = new CountDownTimerSupport(faceVerifyInterval * 1000L, 1000L);
        mTimerTask.setOnCountDownTimerListener(new OnCountDownTimerListener() {


            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //计时完成 说明课件已看完
                //todo 处理认证逻辑
                skipRecognize();

            }
        });
        startTimer();
    }


    private void cancelTimer() {
        if (mTimerTask != null) {
            mTimerTask.stop();
        }
    }


    private void startTimer() {
        if (mTimerTask != null) {
            //先重置 在启动
            mTimerTask.reset();
            mTimerTask.start();
        }
    }

    private void timerPause() {
        if (mTimerTask != null) {
            //暂停
            mTimerTask.pause();
        }
    }

    private void timerResume() {
        if (mTimerTask != null) {
            //恢复
            mTimerTask.resume();
        }
    }


    private void skipRecognize() {
        //暂停计时器
        timerPause();
        //跳转到人脸认证
        skipFace();
    }

    private void skipFace() {
        //人脸认证
        Intent intent = new Intent(mContext, OnLineFaceRecognitionActivity.class);
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, CommonUtil.getNotNullValue(trainingPlanID));
        startActivityForResult(intent, PlayVideoActivity.REQUEST_CODE_FACE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerResume();
    }


    /**
     * 保存当前观看进度
     */
    private void requestSaveProgress(String courseId, String second) {
        ApiRepository.getInstance().requestSaveProgress(trainingPlanID, courseId, second).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(new BaseLoadingObserver<BaseResult>() {
            @Override
            public void onSuccessNext(BaseResult entity) {
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    //todo
                    ToastUtil.showSuccess("进度保存成功");
                } else {
                    ToastUtil.show(entity.msg);
                }
                finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                finish();
            }
        });
    }

    private void showExitDialog() {
        if (currentProgress <= 0) {
            finish();
            return;
        }
        IosAlertDialog dialog = new IosAlertDialog(mContext)
                .init()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setTitle("确认退出学习")
                .setMsg("退出前会保存当前学习进度")
                .setPositiveButton("确认退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doSaveProgressAndFinish();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        setStatusBarDarkMode(mContext, isStatusBarDarkMode());
                    }
                });
    }

    /**
     * 保存进度并关闭当前页面
     */
    private void doSaveProgressAndFinish() {
        if (mCourse == null) {
            return;
        }
        requestSaveProgress(mCourse.getID() + "", currentProgress + "");
    }


}

package com.tourcoo.training.ui.training.safe.online.web;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolindicator.sdk.CoolIndicator;
import com.dyhdyh.support.countdowntimer.CountDownTimerSupport;
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tourcoo.training.R;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.base.activity.BaseTitleActivity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.entity.training.Course;
import com.tourcoo.training.widget.dialog.IosAlertDialog;
import com.tourcoo.training.widget.web.RichWebView;
import com.trello.rxlifecycle3.android.ActivityEvent;

import static com.tourcoo.training.constant.TrainingConstant.COURSE_STATUS_FINISH;
import static com.tourcoo.training.constant.TrainingConstant.EXTRA_COURSE_INFO;
import static com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID;

/**
 * @author :JenkinsZhou
 * @description :播放网页课件
 * @company :途酷科技
 * @date 2020年05月09日17:12
 * @Email: 971613168@qq.com
 */
public class PlayHtmlWebActivity extends BaseTitleActivity {
    public static final String TAG = "计时器模块";
    private Course mCurrentCourse;
    private RichWebView webView;
    private CoolIndicator indicator;
    private String mUrl;
    /**
     * 课件本身时长
     */
    private long fixedDuration;
    private long duration;
    private String mTrainingPlanID;
    private CountDownTimerSupport mTimerTask;
    private TextView tvLimitTips;
    private LinearLayout llHeaderBar;
    /**
     * 剩余时长（进度）
     */
    private long mRemainProgress;

    @Override
    public int getContentLayout() {
        return R.layout.activity_webview_play_html;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mUrl = getIntent().getStringExtra("url");
        mCurrentCourse = getIntent().getParcelableExtra(EXTRA_COURSE_INFO);
        mTrainingPlanID = getIntent().getStringExtra(EXTRA_TRAINING_PLAN_ID);
        if (mCurrentCourse == null) {
            ToastUtil.show("未获取到网页课程");
            finish();
            return;
        }
        webView = findViewById(R.id.mWebView);
        tvLimitTips = findViewById(R.id.tvLimitTips);
        indicator = findViewById(R.id.indicator);
        llHeaderBar = findViewById(R.id.llHeaderBar);
        initDuration();
        initWebView();
    }

    @Override
    public void loadData() {
        webView.loadUrl(CommonUtil.getNotNullValue(mUrl));
        //初始化计时器
        initTimerAndStart();
        //计时开始
        startTimer();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("文件");
    }

    private void initWebView() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                indicator.start();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                indicator.complete();
            }
        });
    }

    @Override
    protected void onDestroy() {
        cancelTimer();
        if (webView != null) {
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearCache(true);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }


    /**
     * 初始化计时器 并开始计时
     */
    private void initTimerAndStart() {
        //总时长 间隔时间
        if (duration <= 0) {
            //取消计时器
            showHeaderTime(-1);
            cancelTimer();
            return;
        }
        showHeaderTime(duration);
        //初始化计时器
        mTimerTask = new CountDownTimerSupport(duration * 1000L, 1000L);
        mTimerTask.setOnCountDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
//                TourCooLogUtil.d("时间：" + millisUntilFinished / 1000);
                mRemainProgress = millisUntilFinished / 1000;
                showHeaderTime(mRemainProgress);
            }

            @Override
            public void onFinish() {
                //todo 计时结束
                doTimeUp();
            }
        });

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

    /**
     * 计时结束 说明课程学习结束
     */
    private void doTimeUp() {
        //剩余时间
        mRemainProgress = 0;
        //隐藏
        setViewGone(llHeaderBar, false);
        //当前课件学完
        requestComplete();
    }

    @Override
    protected void onPause() {
        timerPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        timerResume();
        super.onResume();
    }


    /**
     * 保存当前观看进度
     */
    /*private fun requestSaveProgress(courseId: String, second: String) {
        ApiRepository.getInstance().requestSaveProgress(trainingPlanID, courseId, second).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>?>() {
            override fun onSuccessNext(entity: BaseResult<Any>?) {
                if (entity?.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    //todo
                    ToastUtil.showSuccess("进度保存成功")
                } else {
                    ToastUtil.show(entity?.msg)
                }
                finish()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                finish()
            }
        })
    }*/


    /**
     * 保存当前观看进度
     */
    private void requestSaveProgress(String second) {
        TourCooLogUtil.i(TAG, "保存的进度：" + second);
        ApiRepository.getInstance().requestSaveProgress(mTrainingPlanID, mCurrentCourse.getID() + "", second).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(new BaseLoadingObserver<BaseResult>() {
            @Override
            public void onSuccessNext(BaseResult entity) {
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.showSuccess("进度保存成功");
                    //关键点：通知上个页面刷新
                    setResult(Activity.RESULT_OK);
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

    /**
     * 本次课程学习完成
     */
    private void requestComplete() {
        ApiRepository.getInstance().requestSaveProgress(mTrainingPlanID, mCurrentCourse.getID() + "", "-1").compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(new BaseLoadingObserver<BaseResult>() {
            @Override
            public void onSuccessNext(BaseResult entity) {
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    //重点：将本次课程完成状态置位已完成
                    mCurrentCourse.setCompleted(1);
                    setResult(Activity.RESULT_OK);
                } else {
                    ToastUtil.show(entity.msg);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    /*override fun onBackPressed() {
        if (smartVideoPlayer.playMode == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            smartVideoPlayer.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW)
            return
        }
        if (hasRequireExam) {
            finish()
        } else {
            showExit()
        }
    }*/

    @Override
    public void onBackPressed() {
        if (mCurrentCourse.getCompleted() == COURSE_STATUS_FINISH || mRemainProgress <= 0) {
            //表示课程已完成 所以直接退出
            finish();
        } else {
            showExitDialog();
        }
    }


    private void showExitDialog() {
        new IosAlertDialog(mContext)
                .init()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setOnDismissListener(dialog -> {
                    //计时恢复
                    timerResume();
                })
                .setTitle("确认退出学习")
                .setMsg("退出前会保存当前学习进度")
                .setPositiveButton("确认退出", v -> {
                    //保存进度 进度 = 总时长-剩余时长
                    long progress = fixedDuration - mRemainProgress;
                    if (progress <= 0) {
                        //说明学习结束
                        requestComplete();
                    } else {
//                        requestSaveProgress(progress + "");
                        requestComplete();
                    }

                })
                .setNegativeButton("取消", v -> {
                    setStatusBarDarkMode(mContext, isStatusBarDarkMode());
                }).show();
        //对话框弹出时 计时停止
        timerPause();
    }

    /**
     * 初始化学习进度
     */
    private void initDuration() {
        //获取课件固定的时长
        fixedDuration = mCurrentCourse.getDuration();
        if (mCurrentCourse.getCompleted() == COURSE_STATUS_FINISH) {
            //如果当前html课件本来就是已完成状态则将计时器时间置为0
            TourCooLogUtil.w(TAG, "当前课件已完成 不需要计时");
            duration = 0;
        } else {
            //说明当前课件还没有学完 需要判断进度是否为0 如果为0 说明从头开始计时 取时长字段 否则取progress
            if (mCurrentCourse.getProgress() <= 0) {
                //取getDuration()
                TourCooLogUtil.d(TAG, "当前课件还没有学完 并且进度为0 执行 duration 改为mCurrentCourse.getDuration()");
                duration = mCurrentCourse.getDuration();
            } else {
                //取progress后剩下的进度
                duration = mCurrentCourse.getDuration() - mCurrentCourse.getProgress();
                TourCooLogUtil.i(TAG, "取progress后剩下的进度 duration=" + duration);
                if (duration < 0) {
                    duration = 0;
                    TourCooLogUtil.e(TAG, "取progress后剩下的进度 但是 为负数 改成了0");
                }

            }
        }
    }

    /**
     * 显示头部倒计时剩余时间
     */
    private void showHeaderTime(long second) {
        if (second <= 0) {
            //隐藏时间
            setViewGone(llHeaderBar, false);
        }
        tvLimitTips.setText("文件至少要学习" + second + "秒");
    }
}

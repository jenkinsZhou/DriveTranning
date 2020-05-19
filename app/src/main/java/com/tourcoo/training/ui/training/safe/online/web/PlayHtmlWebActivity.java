package com.tourcoo.training.ui.training.safe.online.web;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.tourcoo.training.config.AppConfig;
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
import com.tourcoo.training.ui.face.OnLineFaceRecognitionActivity;
import com.tourcoo.training.utils.CustomCountDownTimer;
import com.tourcoo.training.widget.dialog.IosAlertDialog;
import com.tourcoo.training.widget.web.RichWebView;
import com.trello.rxlifecycle3.android.ActivityEvent;

import static com.tourcoo.training.constant.FaceConstant.FACE_CERTIFY_FAILED;
import static com.tourcoo.training.constant.FaceConstant.FACE_CERTIFY_SUCCESS;
import static com.tourcoo.training.constant.TrainingConstant.COURSE_STATUS_FINISH;
import static com.tourcoo.training.constant.TrainingConstant.EXTRA_COURSE_INFO;
import static com.tourcoo.training.constant.TrainingConstant.EXTRA_KEY_FACE_TIME;
import static com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID;
import static com.tourcoo.training.ui.training.safe.online.TencentPlayVideoActivity.REQUEST_CODE_FACE;

/**
 * @author :JenkinsZhou
 * @description :播放网页课件
 * @company :途酷科技
 * @date 2020年05月09日17:12
 * @Email: 971613168@qq.com
 */
public class PlayHtmlWebActivity extends BaseTitleActivity {
    public static final int RESULT_CODE_REFRESH_HTML = 1001;
    public static final String TAG = "计时器模块";
    private static final int TEST_TIME = 45;
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
    /**
     * 课程倒计时的使用的计时器
     */
    private CustomCountDownTimer mTimerTask;

    /**
     * 人脸认证间隔使用的计时器
     */
    private CustomCountDownTimer mFaceTimeTask;


    //后台配置的人脸验证间隔时间
    private int mFaceVerifyInterval = Integer.MAX_VALUE;

    /**
     * 人脸验证间隔时间
     */
    private int mFaceRemainTime = Integer.MAX_VALUE;
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
        //获取服务器返回的人脸间隔时间
        mFaceVerifyInterval = getIntent().getIntExtra(EXTRA_KEY_FACE_TIME, -1);

        if (mCurrentCourse == null) {
            ToastUtil.show("未获取到网页课程");
            finish();
            return;
        }
        webView = findViewById(R.id.mWebView);
        tvLimitTips = findViewById(R.id.tvLimitTips);
        indicator = findViewById(R.id.indicator);
        llHeaderBar = findViewById(R.id.llHeaderBar);
        if(AppConfig.DEBUG_MODE){
            mFaceVerifyInterval = 20;
        }
//        mFaceRemainTime = mCurrentCourse.
            mFaceRemainTime = mFaceVerifyInterval;
                initDuration();
        initWebView();
    }

    @Override
    public void loadData() {
        webView.loadUrl(CommonUtil.getNotNullValue(mUrl));
        //初始化计时器
        initCourseTimerAndStart();
        //计时开始
        startCourseTimer();
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
                //页面加载成功 启动人脸间隔计时器
                initFaceTimerAndStart(mFaceRemainTime);
            }
        });
    }

    @Override
    protected void onDestroy() {
        cancelCourseTimer();
        cancelFaceTimer();
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
     * 初始化课程计时器 并开始计时
     */
    private void initCourseTimerAndStart() {
        cancelCourseTimer();
        if (AppConfig.DEBUG_MODE) {
            duration = TEST_TIME;
        }
        //总时长 间隔时间
        if (duration <= 0) {
            //取消计时器
            showHeaderTime(-1);
            cancelCourseTimer();
            return;
        }
        showHeaderTime(duration);
        //初始化计时器
        mTimerTask = new CustomCountDownTimer(duration * 1000L, 1000L);
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


    private void cancelCourseTimer() {
        if (mTimerTask != null) {
            mTimerTask.stop();
            mTimerTask = null;
        }
    }

    private void cancelFaceTimer() {
        if (mFaceTimeTask != null) {
            mFaceTimeTask.stop();
            mFaceTimeTask = null;
            TourCooLogUtil.w("人脸计时器已经销毁");
        }
    }

    /**
     * 课程计时器启动
     */
    private void startCourseTimer() {
        if (mTimerTask != null) {
            //先重置 在启动
            mTimerTask.reset();
            mTimerTask.start();
        }
    }

    private void timerCoursePause() {
        if (mTimerTask != null) {
            //暂停
            mTimerTask.pause();
        }
    }

    private void timerCourseResume() {
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
        timerCoursePause();
        timerFacePause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        timerCourseResume();
        timerFaceResume();
        super.onResume();
    }


    /**
     * 保存当前观看进度
     */


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
                    //关键点：将本次课程完成状态置位已完成
                    mCurrentCourse.setCompleted(1);
                    // 通知上个页面刷新
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
                    timerCourseResume();
                    timerFaceResume();
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
                        //保存进度
                        requestSaveProgress(progress + "");
                    }

                })
                .setNegativeButton("取消", v -> {
                    setStatusBarDarkMode(mContext, isStatusBarDarkMode());
                }).show();
        //对话框弹出时 计时暂停
        timerCoursePause();
        //人脸验证计时器也暂停
        timerFacePause();
    }


    /**
     * 初始化学习进度
     */
    private void initDuration() {
        //获取课件固定的时长
        if(AppConfig.DEBUG_MODE  ){
            fixedDuration = TEST_TIME ;
        }else {
            fixedDuration = mCurrentCourse.getDuration();
        }

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


    /**
     * 初始化人脸间隔验证计时器并启动
     * @param faceVerifySecond
     */
    private void initFaceTimerAndStart(int faceVerifySecond ){
        cancelFaceTimer();
        if(faceVerifySecond <=0){
            TourCooLogUtil.w("人脸计时器间隔时间为0 计时器未能启动");
            return;
        }
        long faceVerifyMillisecond =faceVerifySecond*1000L;
        mFaceTimeTask = new CustomCountDownTimer(faceVerifyMillisecond, 1000L);
        mFaceTimeTask.setOnCountDownTimerListener( new  OnCountDownTimerListener (){
            @Override
            public void onTick(long millisUntilFinished) {
                mFaceRemainTime--;
                TourCooLogUtil.i("人脸计时器计时中：还剩："+mFaceRemainTime+"秒");
            }

            @Override
            public void onFinish() {
                TourCooLogUtil.i("计时完成");
                doSkipRecognize();
            }


        });
        startFaceTimer();
    }

    /**
     * 人脸计时器启动
     */
    private void startFaceTimer() {
        if (mFaceTimeTask != null) {
            //先重置 在启动
            mFaceTimeTask.reset();
            mFaceTimeTask.start();
            TourCooLogUtil.i("人脸计时器启动");
        }
    }

    /**
     * 人脸计时器暂停
     */
    private void timerFacePause() {
        if (mFaceTimeTask != null) {
            //暂停
            mFaceTimeTask.pause();
        }
    }

    /**
     * 人脸计时器恢复
     */
    private void timerFaceResume() {
        if (mFaceTimeTask != null) {
            //恢复
            mFaceTimeTask.resume();
            TourCooLogUtil.i("计时器恢复");
        }
    }


    private void doSkipRecognize() {
        //暂停课程倒计时计时器
        timerCoursePause();
        //
        timerFacePause();
        //跳转到人脸认证
        skipFace();
    }

    private void  skipFace() {
        //人脸认证
        Intent intent =new  Intent(this, OnLineFaceRecognitionActivity.class);
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, CommonUtil.getNotNullValue(mTrainingPlanID));
        startActivityForResult(intent, REQUEST_CODE_FACE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_FACE:
                //人脸验证状态回调处理
                switch (resultCode) {
                    case FACE_CERTIFY_SUCCESS:
                        //本次人脸验证通过 需要继续播放课件
                        //处理继续播放课件逻辑
                        doReadHtmlContinue();
                        break;

                    case  FACE_CERTIFY_FAILED :
                        //本次人脸验证失败 需要提示用户后 关闭页面并保存进度 不让用户学习了
                        handleRecognizeFailedCallback();

                    default:
                        //用户压根就没验证人脸 直接返回的 肯定关闭页面并保存进度 更不让用户学习了
                        handleRecognizeCancelCallback();
                        break;
                }


                break;
            default:
                break;
        }
    }



    /**
     * 人脸验证用户未通过的回调
     */
    private void handleRecognizeFailedCallback() {
        ToastUtil.show("您当前人脸验证未通过 本次学习结束");
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //保存进度并关闭
                doSaveProgressAndFinish();
            }
        },1000);
    }


    /**
     * 人脸验证用户未点击拍照的回调
     */
    private void handleRecognizeCancelCallback() {
        ToastUtil.show("您当前没有验证人脸 本次学习结束");
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //保存进度并关闭
                doSaveProgressAndFinish();
            }
        },1000);
    }

    /**
     * 保存进度并关闭当前页面
     */
    private void  doSaveProgressAndFinish() {
        //保存进度 进度 = 总时长-剩余时长
        if(mCurrentCourse == null){
            //课程都为空肯定直接finish
            finish();
        }
        long progress = fixedDuration - mRemainProgress;
        if(progress == 0){
            //浏览进度为0 直接关闭 不保存进度
            finish();
            return;
        }
        requestSaveProgress(progress+"");
    }


    /**
     * 人脸验证通过后 需要继续播放课件 计时又得开始了
     */
    private void doReadHtmlContinue(){
        TourCooLogUtil.i("开始新一轮计时");
        TourCooLogUtil.d("人脸验证通过 继续播放视频");
        //人脸验证成功 则课程恢复倒计时
        timerCourseResume();
        timerFaceResume();
        //重点来了 这里要将计时器重置到初始状态 并开始新一轮计时（只针对人脸计时器）
        mFaceRemainTime=mFaceVerifyInterval;
        initFaceTimerAndStart(mFaceRemainTime);
    }
}

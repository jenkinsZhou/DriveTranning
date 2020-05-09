package com.tourcoo.training.ui.training.safe.online.web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolindicator.sdk.CoolIndicator;
import com.dyhdyh.support.countdowntimer.CountDownTimerSupport;
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener;
import com.tourcoo.training.R;
import com.tourcoo.training.core.base.activity.BaseTitleActivity;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.entity.training.Course;
import com.tourcoo.training.widget.web.RichWebView;

import static com.tourcoo.training.constant.TrainingConstant.EXTRA_COURSE_INFO;

/**
 * @author :JenkinsZhou
 * @description :播放网页课件
 * @company :途酷科技
 * @date 2020年05月09日17:12
 * @Email: 971613168@qq.com
 */
public class PlayHtmlWebActivity extends BaseTitleActivity {

    private Course mCurrentCourse;
    private RichWebView webView;
    private CoolIndicator indicator;
    private String mUrl;
    private long duration;
    private CountDownTimerSupport mTimerTask;
    private TextView tvLimitTips;
    private LinearLayout llHeaderBar;

    @Override
    public int getContentLayout() {
        return R.layout.activity_webview_play_html;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mUrl = getIntent().getStringExtra("url");
        mCurrentCourse = getIntent().getParcelableExtra(EXTRA_COURSE_INFO);
        if (mCurrentCourse == null) {
            ToastUtil.show("未获取到网页课程");
            finish();
            return;
        }
        duration = mCurrentCourse.getDuration();
        webView = findViewById(R.id.mWebView);
        tvLimitTips = findViewById(R.id.tvLimitTips);
        indicator = findViewById(R.id.indicator);
        llHeaderBar = findViewById(R.id.llHeaderBar);
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
            cancelTimer();
            return;
        }
        tvLimitTips.setText("文件至少要学习" + duration + "秒");
        //初始化计时器
        mTimerTask = new CountDownTimerSupport(duration * 1000L, 1000L);
        mTimerTask.setOnCountDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
//                TourCooLogUtil.d("时间：" + millisUntilFinished / 1000);
                tvLimitTips.setText("文件至少要学习" + millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                //todo 计时结束
                showTimeUp();
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


    private void showTimeUp() {
        //隐藏
        setViewGone(llHeaderBar, false);
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
}

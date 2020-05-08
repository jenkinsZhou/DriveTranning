package com.tourcoo.training.ui.message;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coolindicator.sdk.CoolIndicator;
import com.tourcoo.training.R;
import com.tourcoo.training.adapter.news.NewsMultipleAdapter;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.base.activity.BaseTitleActivity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.entity.message.MessageDetail;
import com.tourcoo.training.entity.news.NewsDetailEntity;
import com.tourcoo.training.entity.news.NewsEntity;
import com.tourcoo.training.ui.home.news.NewsDetailHtmlActivity;
import com.tourcoo.training.widget.dialog.share.BottomShareDialog;
import com.tourcoo.training.widget.dialog.share.ShareEntity;
import com.tourcoo.training.widget.web.HeaderScrollHelper;
import com.tourcoo.training.widget.web.HeaderViewPager;
import com.tourcoo.training.widget.web.JavaScriptLog;
import com.tourcoo.training.widget.web.RichWebView;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.tourcoo.training.ui.home.news.NewsTabFragmentNew.EXTRA_NEWS_BEAN;
import static com.trello.rxlifecycle3.android.ActivityEvent.DESTROY;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年05月08日22:24
 * @Email: 971613168@qq.com
 */
public class MessageDetailActivity extends BaseTitleActivity {
    private RichWebView webView;
    //滚动控件父容器
//    private HeaderViewPager scrollableLayout;
    //进度条
    private CoolIndicator indicator;

    private TextView tvDate;
    private TextView tvTitle;




    @Override
    public int getContentLayout() {
        return R.layout.activity_message_detail;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        webView = findViewById(R.id.mWebView);
        tvDate = findViewById(R.id.tvDate);
        tvTitle = findViewById(R.id.tvTitle);
        indicator = findViewById(R.id.indicator);
        indicator = findViewById(R.id.indicator);
        initWebView();
        requestMessageDetail(getIntent().getIntExtra("id", -1));
    }

    private void initWebView() {


        //设置点击图片
        webView.addJavascriptInterface(new JavaScriptLog(this, new JavaScriptLog.ClickImageCallBack() {
            @Override
            public void clickImage(String src) {
            }
        }), "control");
        //设置图片加载失败回调
        webView.setLoadImgError();
        //添加点击图片脚本事件
        webView.setImageClickListener();

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("资讯");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearCache(true);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }


    private void requestMessageDetail(int id) {
        ApiRepository.getInstance().requestMessageDetail(id).compose(bindUntilEvent(DESTROY)).subscribe(new BaseLoadingObserver<BaseResult<MessageDetail>>() {
            @Override
            public void onSuccessNext(BaseResult<MessageDetail> entity) {
                if (entity == null) {
                    return;
                }
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    //设置html内容
                    showDetail(entity.getData());
                } else {
                    ToastUtil.show(entity.getMsg());
                }
            }
        });
    }


    private void showDetail(MessageDetail data){
        if(data == null){
            webView.setShow(CommonUtil.getNotNullValue("无数据"));
            return;
        }
        tvDate.setText(CommonUtil.getNotNullValue(data.getTime()));
        tvTitle.setText(CommonUtil.getNotNullValue(data.getTitle()));
        webView.setShow(CommonUtil.getNotNullValue(data.getContent()));
    }


}

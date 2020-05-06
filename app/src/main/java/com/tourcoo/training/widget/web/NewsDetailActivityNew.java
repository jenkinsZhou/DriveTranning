package com.tourcoo.training.widget.web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coolindicator.sdk.CoolIndicator;
import com.tourcoo.training.R;
import com.tourcoo.training.adapter.news.NewsMultipleAdapter;
import com.tourcoo.training.core.base.activity.BaseTitleActivity;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;



public class NewsDetailActivityTest extends BaseTitleActivity {

    private RichWebView webView;
    private RecyclerView recyclerView;
    //滚动控件父容器
    private HeaderViewPager scrollableLayout;
    //进度条
    private CoolIndicator indicator;

    private NewsMultipleAdapter adapter;



    private void initWebView() {
        webView = findViewById(R.id.newsWebView);
        recyclerView = findViewById(R.id.recycler_view);
        scrollableLayout = findViewById(R.id.scrollableLayout);
        indicator = findViewById(R.id.indicator);
        webView.loadUrl("https://gitee.com/zip1234/HeaderViewPager");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //滚动绑定
        scrollableLayout.setCurrentScrollableContainer(new HeaderScrollHelper.ScrollableContainer(){
            @Override
            public View getScrollableView() {
                return recyclerView;
            }
        });
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
    public int getContentLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initWebView();
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

    private void requestNewsDetail(){
        ApiRepository.getInstance().requestCategory()
    }
}

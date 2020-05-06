package com.tourcoo.training.widget.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coolindicator.sdk.CoolIndicator;
import com.tourcoo.training.R;
import com.tourcoo.training.adapter.news.NewsMultipleAdapter;
import com.tourcoo.training.core.base.activity.BaseTitleActivity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.entity.news.NewsDetailEntity;
import com.tourcoo.training.entity.news.NewsEntity;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;

import static com.tourcoo.training.ui.home.news.NewsTabFragmentNew.EXTRA_NEWS_ID;
import static com.tourcoo.training.ui.home.news.NewsTabFragmentNew.EXTRA_NEWS_URL;


public class NewsDetailActivityNew extends BaseTitleActivity {

    private RichWebView webView;
    private RecyclerView recyclerView;
    //滚动控件父容器
    private HeaderViewPager scrollableLayout;
    //进度条
    private CoolIndicator indicator;

    private NewsMultipleAdapter adapter;
    private String mUrl;
    private String mId;


    private void initWebView() {
        webView = findViewById(R.id.newsWebView);
        recyclerView = findViewById(R.id.recycler_view);
        scrollableLayout = findViewById(R.id.scrollableLayout);
        indicator = findViewById(R.id.indicator);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsMultipleAdapter(new ArrayList<>());
        adapter.bindToRecyclerView(recyclerView);
        initItemClick();
        webView.loadUrl(mUrl);
        //滚动绑定
        scrollableLayout.setCurrentScrollableContainer(new HeaderScrollHelper.ScrollableContainer() {
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
        return R.layout.activity_news_detail_html;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mUrl = getIntent().getExtras().getString(EXTRA_NEWS_URL);
        mId = getIntent().getExtras().getString(EXTRA_NEWS_ID);
        if (TextUtils.isEmpty(mUrl) || TextUtils.isEmpty(mId)) {
            ToastUtil.show("暂无资讯");
            finish();
            return;
        }
        initWebView();
        requestNewsDetail(mId);
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

    private void requestNewsDetail(String id) {
        ApiRepository.getInstance().requestNewsDetail(id).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(new BaseLoadingObserver<BaseResult<NewsDetailEntity>>() {
            @Override
            public void onSuccessNext(BaseResult<NewsDetailEntity> entity) {
                showWebDetail(entity.getData());
            }
        });
    }

    private void showWebDetail(NewsDetailEntity detailEntity) {
        adapter.setNewData(detailEntity.getRecommendList());
    }

    private void initItemClick() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsEntity newsEntity = (NewsEntity) adapter.getData().get(position);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_NEWS_ID,newsEntity.getID());
                intent.putExtra(EXTRA_NEWS_URL,newsEntity.getUrl());
                intent.setClass(mContext,NewsDetailActivityNew.class);
                startActivity(intent);
            }
        });
    }
}

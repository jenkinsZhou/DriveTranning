package com.tourcoo.training.ui.home.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.entity.news.NewsDetailEntity;
import com.tourcoo.training.entity.news.NewsEntity;
import com.tourcoo.training.widget.dialog.share.BottomShareDialog;
import com.tourcoo.training.widget.dialog.share.ShareEntity;
import com.tourcoo.training.widget.web.HeaderScrollHelper;
import com.tourcoo.training.widget.web.HeaderViewPager;
import com.tourcoo.training.widget.web.RichWebView;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;

import static com.tourcoo.training.ui.home.news.NewsTabFragmentNew.EXTRA_NEWS_BEAN;

/**
 * @author :JenkinsZhou
 * @description :新闻详情（普通）
 * @company :途酷科技
 * @date 2020年05月06日19:02
 * @Email: 971613168@qq.com
 */
public class NewsDetailHtmlActivity extends BaseTitleActivity implements View.OnClickListener {

    private RichWebView webView;
    private RecyclerView recyclerView;
    //滚动控件父容器
    private HeaderViewPager scrollableLayout;
    //进度条
    private CoolIndicator indicator;

    private NewsMultipleAdapter adapter;

    private NewsEntity mNewsEntity;
    private TextView tvNewsLookCount;
    private TextView tvNewsLikeCount;
    private TextView tvNewsShareCount;
    private LinearLayout llLike;


    @Override
    public int getContentLayout() {
        return R.layout.activity_news_detail_html;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvNewsLookCount = findViewById(R.id.tvNewsLookCount);
        tvNewsLikeCount = findViewById(R.id.tvNewsLikeCount);
        tvNewsShareCount = findViewById(R.id.tvNewsShareCount);
        findViewById(R.id.llLike).setOnClickListener(this);
        findViewById(R.id.llRead).setOnClickListener(this);
        findViewById(R.id.llShare).setOnClickListener(this);
        webView = findViewById(R.id.newsWebView);
        recyclerView = findViewById(R.id.recycler_view);
        scrollableLayout = findViewById(R.id.scrollableLayout);
        indicator = findViewById(R.id.indicator);
        tvNewsLookCount.setOnClickListener(this);
        tvNewsLikeCount.setOnClickListener(this);
        tvNewsShareCount.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            ToastUtil.show("未获取到新闻详情");
            finish();
            return;
        }
        mNewsEntity = (NewsEntity) bundle.getSerializable(EXTRA_NEWS_BEAN);
        if (mNewsEntity == null) {
            ToastUtil.show("暂无资讯详情");
            finish();
            return;
        }
        showNewsDetail();
        initWebView();
        requestNewsDetail(mNewsEntity.getID());
    }

    private void initWebView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsMultipleAdapter(new ArrayList<>());
        adapter.bindToRecyclerView(recyclerView);
        initItemClick();
        webView.loadUrl(CommonUtil.getUrl(mNewsEntity.getUrl()));
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
                intent.putExtra(EXTRA_NEWS_BEAN, newsEntity);
                intent.setClass(mContext, NewsDetailHtmlActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showNewsDetail() {
        tvNewsLookCount.setText(mNewsEntity.getReadTotal() + "");
        tvNewsLikeCount.setText(mNewsEntity.getLikeTotal() + "");
        tvNewsShareCount.setText(mNewsEntity.getSharedNum() + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNewsLikeCount:
                break;
            case R.id.llShare:
                showShareDialog();
                break;
            default:
                break;
        }
    }


    private void showShareDialog() {
        ShareEntity wx = new ShareEntity("微信", R.mipmap.ic_share_type_wx);
        ShareEntity pyq = new ShareEntity("朋友圈", R.mipmap.ic_share_type_friend);
        BottomShareDialog dialog = new BottomShareDialog(mContext).create().addData(wx).addData(pyq);
        dialog.setItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        ToastUtil.show("分享到微信");
                        break;
                    case 1:
                        ToastUtil.show("分享到朋友圈");
                        break;
                }
            }
        });
        dialog.show();
    }
}

package com.tourcoo.training.ui.home.news;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coolindicator.sdk.CoolIndicator;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerView;
import com.tencent.rtmp.TXLiveConstants;
import com.tourcoo.training.R;
import com.tourcoo.training.adapter.news.NewsMultipleAdapter;
import com.tourcoo.training.core.base.activity.BaseTitleActivity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.entity.news.NewsDetailEntity;
import com.tourcoo.training.entity.news.NewsEntity;
import com.tourcoo.training.ui.training.safe.online.detail.common.CommonDetailContract;
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
 * @description :
 * @company :途酷科技
 * @date 2020年05月07日8:07
 * @Email: 971613168@qq.com
 */
public class NewsDetailVideoActivity extends BaseTitleActivity implements View.OnClickListener {
    public static final String TAG = "NewsDetailVideoActivity";
    private RichWebView webView;
    private RecyclerView recyclerView;
    //滚动控件父容器
    private HeaderViewPager scrollableLayout;
    //进度条
    private CoolIndicator indicator;
    private SuperPlayerView smartVideoPlayer;

    private NewsMultipleAdapter adapter;
    private NewsEntity mNewsEntity;
    private TextView tvNewsLookCount;
    private TextView tvNewsLikeCount;
    private TextView tvNewsShareCount;


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
    public int getContentLayout() {
        return R.layout.activity_news_detail_video;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
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
        findViewById(R.id.llRead).setOnClickListener(this);
        findViewById(R.id.llShare).setOnClickListener(this);
        webView = findViewById(R.id.newsWebView);
        smartVideoPlayer = findViewById(R.id.superPlayerView);
        recyclerView = findViewById(R.id.recycler_view);
        scrollableLayout = findViewById(R.id.scrollableLayout);
        indicator = findViewById(R.id.indicator);
        tvNewsLookCount = findViewById(R.id.tvNewsLookCount);
        tvNewsLikeCount = findViewById(R.id.tvNewsLikeCount);
        tvNewsShareCount = findViewById(R.id.tvNewsShareCount);
        tvNewsLookCount.setOnClickListener(this);
        tvNewsLikeCount.setOnClickListener(this);
        tvNewsShareCount.setOnClickListener(this);
        showNewsDetail();
        loadPlayerSettingAndPlay();
        initWebView();
        requestNewsDetail(mNewsEntity.getID());
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
        if (smartVideoPlayer != null) {
            // 释放
            smartVideoPlayer.release();
            if (smartVideoPlayer.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
                smartVideoPlayer.resetPlayer();
            }
        }
    }

    private void requestNewsDetail(String id) {
        ApiRepository.getInstance().requestNewsDetail(id).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(new BaseLoadingObserver<BaseResult<NewsDetailEntity>>() {
            @Override
            public void onSuccessNext(BaseResult<NewsDetailEntity> entity) {
                loadRecommend(entity.getData());
            }
        });
    }

    private void loadRecommend(NewsDetailEntity detailEntity) {
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

    private void loadPlayerSettingAndPlay() {
        if (smartVideoPlayer == null) {
            return;
        }
        smartVideoPlayer.setSeekEnable(true);
        smartVideoPlayer.setOnPlayStatusListener(new SuperPlayerView.OnPlayStatusListener() {
            @Override
            public void enableSeek() {

            }

            @Override
            public void onAutoPlayComplete() {

            }
        });
        // 播放器配置
        SuperPlayerGlobalConfig prefs = SuperPlayerGlobalConfig.getInstance();
        // 开启悬浮窗播放
        prefs.enableFloatWindow = false;
        // 设置悬浮窗的初始位置和宽高
        // 播放器默认缓存个数
        prefs.maxCacheItem = 5;
        // 设置播放器渲染模式
        prefs.enableHWAcceleration = true;
        prefs.renderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
        smartVideoPlayer.setPlayerViewCallback(new SuperPlayerView.OnSuperPlayerViewCallback() {
            @Override
            public void onStartFullScreenPlay() {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                if (mTitleBar.getVisibility() != View.GONE) {
                    mTitleBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStopFullScreenPlay() {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                if (mTitleBar.getVisibility() != View.VISIBLE) {
                    mTitleBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onClickFloatCloseBtn() {

            }

            @Override
            public void onClickSmallReturnBtn() {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setStatusBarDarkMode(mContext, isStatusBarDarkMode());
                //设置返回按键功能
                onBackPressed();
            }

            @Override
            public void onStartFloatWindowPlay() {

            }
        });
        SuperPlayerModel model = new SuperPlayerModel();
        model.url = mNewsEntity.getVideoUrl();
        model.title = CommonUtil.getNotNullValue(mNewsEntity.getTitle());
        smartVideoPlayer.playWithModel(model);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (smartVideoPlayer.getPlayMode() != SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            setStatusBarDarkMode(mContext, isStatusBarDarkMode());
        } else {
            setViewGone(mTitleBar, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (smartVideoPlayer != null) {
            // 停止播放
            if (smartVideoPlayer.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
                smartVideoPlayer.onPause();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (smartVideoPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            smartVideoPlayer.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
        } else {
            super.onBackPressed();
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        if (smartVideoPlayer != null) {
            // 重新开始播放
            if (smartVideoPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAY) {
                smartVideoPlayer.onResume();
                if (smartVideoPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FLOAT) {
                    smartVideoPlayer.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
                }
            }
        }
    }

    private void showNewsDetail() {
        tvNewsLookCount.setText(mNewsEntity.getReadTotal() + "");
        tvNewsLikeCount.setText(mNewsEntity.getLikeTotal() + "");
        tvNewsShareCount.setText(mNewsEntity.getSharedNum() + "");
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
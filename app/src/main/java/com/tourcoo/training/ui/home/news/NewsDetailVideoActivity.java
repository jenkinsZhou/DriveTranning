package com.tourcoo.training.ui.home.news;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
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
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.rtmp.TXLiveConstants;
import com.tourcoo.training.R;
import com.tourcoo.training.adapter.news.NewsMultipleAdapter;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.base.activity.BaseTitleActivity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.SizeUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.entity.news.NewsDetail;
import com.tourcoo.training.entity.news.NewsEntity;
import com.tourcoo.training.entity.pay.WxShareEvent;
import com.tourcoo.training.widget.dialog.share.BottomShareDialog;
import com.tourcoo.training.widget.dialog.share.ShareEntity;
import com.tourcoo.training.widget.web.HeaderScrollHelper;
import com.tourcoo.training.widget.web.HeaderViewPager;
import com.tourcoo.training.widget.web.JavaScriptLog;
import com.tourcoo.training.widget.web.RichWebView;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.tourcoo.training.constant.TrainingConstant.APP_ID;
import static com.tourcoo.training.ui.home.news.NewsTabFragment.EXTRA_NEWS_BEAN;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月07日8:07
 * @Email: 971613168@qq.com
 */
public class NewsDetailVideoActivity extends BaseTitleActivity implements View.OnClickListener {
    public static final String TAG = "NewsDetailVideoActivity";
    private IWXAPI api;
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
    private CheckBox cBoxLike;

    private void initWebView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        api = WXAPIFactory.createWXAPI(mContext, APP_ID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsMultipleAdapter(new ArrayList<>());
        adapter.bindToRecyclerView(recyclerView);
        initItemClick();
        //滚动绑定
        scrollableLayout.setCurrentScrollableContainer(new HeaderScrollHelper.ScrollableContainer() {
            @Override
            public View getScrollableView() {
                return recyclerView;
            }
        });
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
    public int getContentLayout() {
        return R.layout.activity_news_detail_video;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
        cBoxLike = findViewById(R.id.cBoxLike);
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
        if (smartVideoPlayer != null) {
            smartVideoPlayer.release();
        }
        if (api != null) {
            api.detach();
        }
        if (webView != null) {
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearCache(true);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    private void requestNewsDetail(String id) {
        ApiRepository.getInstance().requestNewsDetail(id).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(new BaseLoadingObserver<BaseResult<NewsDetail>>() {
            @Override
            public void onSuccessNext(BaseResult<NewsDetail> entity) {
                if (entity == null) {
                    return;
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    showWebDetail(entity.getData());
                } else {
                    ToastUtil.show(entity.getMsg());
                }


            }
        });
    }

    private void showWebDetail(NewsDetail detailEntity) {
        adapter.setNewData(detailEntity.getRecommendList());
        TourCooLogUtil.d("富文本:" + detailEntity.getContent());
        webView.setShow(CommonUtil.getNotNullValue(detailEntity.getContent()));
        showNewsDetail(detailEntity);
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
        //进度条拖动开关
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
            case R.id.llLike:
                //直接点赞
                requestNewsLike(mNewsEntity.getID(), !cBoxLike.isChecked());
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

    private void showNewsDetail(NewsDetail newsDetail) {
        tvNewsLookCount.setText(newsDetail.getSeeNum() + "");
        tvNewsLikeCount.setText(newsDetail.getLikeNum() + "");
        tvNewsShareCount.setText(newsDetail.getSharedNum() + "");
    }

    private void showShareDialog() {
        ShareEntity wx = new ShareEntity("微信", R.mipmap.ic_share_type_wx);
        ShareEntity pyq = new ShareEntity("朋友圈", R.mipmap.ic_share_type_friend);
        BottomShareDialog dialog = new BottomShareDialog(mContext).create().addData(wx).addData(pyq);
        dialog.setItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                dialog.dismiss();
                switch (position) {
                    case 0:
                        wxSharePic(true);
                        break;
                    case 1:
                        //朋友圈
                        wxSharePic(false);
                        break;
                }
            }
        });
        dialog.show();
    }


    public void wxSharePic(boolean isSession) {
        //初始化WXImageObject和WXMediaMessage对象
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = mNewsEntity.getUrl();
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = mNewsEntity.getTitle();
        msg.description = mNewsEntity.getTitle();
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_news_share_icon);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, SizeUtil.dp2px(41), SizeUtil.dp2px(40), true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ("webPage") + System.currentTimeMillis();
        req.message = msg;

       /* WXImageObject imageObject = new WXImageObject();
        WXMediaMessage msg = new WXMediaMessage();
        msg.title = "特大喜讯！濡江铺子试运营今日开启！";
        msg.mediaObject = imageObject;
        //设置缩略图
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        msg.thumbData = ImageUtils.bitmap2Bytes(scaledBitmap, Bitmap.CompressFormat.PNG);
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "微信分享" + (System.currentTimeMillis());
        req.message = msg;*/
        //表示发送给朋友圈  WXSceneTimeline  表示发送给朋友  WXSceneSession
        req.scene = isSession ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        //调用api接口发送数据到微信
        api.sendReq(req);
       /* bitmap.recycle();
        scaledBitmap.recycle();*/
    }


    public byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 收到消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxPayCallbackEvent(WxShareEvent event) {
        if (event == null) {
            return;
        }
        if (event.isShareSuccess()) {
            requestShareSuccess(mNewsEntity.getID());
        }
    }

    private void requestShareSuccess(String newsId) {
        ApiRepository.getInstance().requestShareSuccess(newsId + "").compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(new BaseLoadingObserver<BaseResult>() {
            @Override
            public void onSuccessNext(BaseResult entity) {
            }
        });
    }

    private void requestNewsLike(String id, boolean isLike) {
        ApiRepository.getInstance().requestNewsLike(id, isLike).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(new BaseLoadingObserver<BaseResult>() {
            @Override
            public void onSuccessNext(BaseResult entity) {
                if (entity == null) {
                    return;
                }
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
//                    showLikeCountCallBack();
                    //直接刷新
                    ToastUtil.show("点赞成功");
                    requestNewsDetail(mNewsEntity.getID());
                } else {
                    ToastUtil.show(entity.getMsg());
                }
            }
        });
    }
}

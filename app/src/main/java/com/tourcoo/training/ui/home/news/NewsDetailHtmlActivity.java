package com.tourcoo.training.ui.home.news;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coolindicator.sdk.CoolIndicator;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tourcoo.training.R;
import com.tourcoo.training.adapter.news.NewsMultipleAdapter;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.constant.TrainingConstant;
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
import com.tourcoo.training.ui.MainTabActivity;
import com.tourcoo.training.ui.SplashActivity;
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

import static com.tourcoo.training.constant.CommonConstant.EXTRA_KEY_POSITION;
import static com.tourcoo.training.constant.TrainingConstant.APP_ID;
import static com.tourcoo.training.ui.home.news.NewsTabFragment.EXTRA_NEWS_BEAN;

/**
 * @author :JenkinsZhou
 * @description :新闻详情（普通）
 * @company :途酷科技
 * @date 2020年05月06日19:02
 * @Email: 971613168@qq.com
 */
public class NewsDetailHtmlActivity extends BaseTitleActivity implements View.OnClickListener {
    private IWXAPI api;
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
    private CheckBox cBoxLike;
    private int mTotalLike;
    private int itemPosition;

    private int mShareCount;
    private int mReadCount;


    @Override
    public int getContentLayout() {
        return R.layout.activity_news_detail_html;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        api = WXAPIFactory.createWXAPI(mContext, APP_ID);
        itemPosition = getIntent().getIntExtra(EXTRA_KEY_POSITION, -1);
        tvNewsLookCount = findViewById(R.id.tvNewsLookCount);
        tvNewsLikeCount = findViewById(R.id.tvNewsLikeCount);
        tvNewsShareCount = findViewById(R.id.tvNewsShareCount);
        cBoxLike = findViewById(R.id.cBoxLike);
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
        initWebView();
        requestNewsDetail(mNewsEntity.getID());
    }

    private void initWebView() {
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
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("资讯");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (ActivityUtils.isActivityExistsInStack(MainTabActivity.class)) {
            super.onBackPressed();
        } else {
            Intent resultIntent = new Intent(this, SplashActivity.class);
            TaskStackBuilder.create(this)
                    .addParentStack(resultIntent.getComponent())
                    .addNextIntent(resultIntent)
                    .startActivities();
        }
    }

    @Override
    protected void onDestroy() {
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
                    if (entity.getData() != null) {
                        mTotalLike = entity.getData().getLikeNum();
                        mShareCount = entity.getData().getSharedNum();
                        mReadCount = entity.getData().getSeeNum();
                        showWebDetail(entity.getData());
                    }
                } else {
                    ToastUtil.show(entity.getMsg());
                }


            }
        });
    }

    private void showWebDetail(NewsDetail detailEntity) {
        adapter.setNewData(detailEntity.getRecommendList());
        webView.setShow(CommonUtil.getNotNullValue(detailEntity.getContent()));
        showNewsDetail(detailEntity);
        setSuccessCallback();
    }

    private void initItemClick() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsEntity newsEntity = (NewsEntity) adapter.getData().get(position);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_NEWS_BEAN, newsEntity);
                intent.putExtra(EXTRA_KEY_POSITION, position);
                intent.setClass(mContext, NewsDetailHtmlActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showNewsDetail(NewsDetail newsDetail) {
        tvNewsLookCount.setText(newsDetail.getSeeNum() + "");
        tvNewsLikeCount.setText(newsDetail.getLikeNum() + "");
        tvNewsShareCount.setText(newsDetail.getSharedNum() + "");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llShare:
                showShareDialog();
                break;
            case R.id.llLike:
                //直接点赞
                requestNewsLike(mNewsEntity.getID(), !cBoxLike.isChecked());
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


    private void requestNewsLike(String id, boolean isLike) {
        ApiRepository.getInstance().requestNewsLike(id, isLike).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(new BaseLoadingObserver<BaseResult>() {
            @Override
            public void onSuccessNext(BaseResult entity) {
                if (entity == null) {
                    return;
                }
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.show("点赞成功");
                    showLike(++mTotalLike);
                    setSuccessCallback();
                } else {
                    ToastUtil.show(entity.getMsg());
                }
            }
        });
    }


    public void wxSharePic(boolean isSession) {
        //初始化WXImageObject和WXMediaMessage对象
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = TrainingConstant.NEWS_SHARE_URL + "?id=" + mNewsEntity.getID() +
                "&TraineeID=" + SPUtils.getInstance().getString("TraineeID");
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
                //分享成功 说明用户既阅读了 又分享了 因此需要通知列表更新数据
                showShare(++mShareCount);
                setSuccessCallback();
            }
        });
    }


    private void showLike(int count) {
        tvNewsLikeCount.setText(count + "");
    }

    private void showShare(int share) {
        tvNewsShareCount.setText(share + "");
    }

    /**
     * 将数据更新到静态变量上
     */
    private void assignmentCount() {
        NewsTemp.newsShareCount = mShareCount;
        NewsTemp.newsReadCount = mReadCount;
    }

    private void setSuccessCallback() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_KEY_POSITION, itemPosition);
        assignmentCount();
        setResult(Activity.RESULT_OK, intent);
    }
}

package com.tourcoo.training.ui.message;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.coolindicator.sdk.CoolIndicator;
import com.tourcoo.training.R;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.base.activity.BaseTitleActivity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.entity.message.MessageDetail;
import com.tourcoo.training.widget.web.JavaScriptLog;
import com.tourcoo.training.widget.web.RichWebView;

import static com.trello.rxlifecycle3.android.ActivityEvent.DESTROY;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
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
    private boolean refreshEnable = true;




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
        //是否需要刷新
        refreshEnable =   getIntent().getIntExtra("readFlag", -1) == 0;
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
        titleBar.setTitleMainText("信息详情");
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
                    if(refreshEnable){
                        //沒有阅读过 才需要刷新
                        setResult(Activity.RESULT_OK);
                    }
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

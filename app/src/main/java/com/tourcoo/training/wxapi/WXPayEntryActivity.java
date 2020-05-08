package com.tourcoo.training.wxapi;


import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tourcoo.training.R;
import com.tourcoo.training.constant.TrainingConstant;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.util.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private static final String TAG = "微信支付回调";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, TrainingConstant.APP_ID,true);
        api.registerApp(TrainingConstant.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq resp) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int errCode = baseResp.errCode;
        ToastUtil.show(errCode+"");
        TourCooLogUtil.i(TAG,baseResp);
        if (errCode == 0) {
            // 成功

        } else if (errCode == -1) {
            // 失败

        } else if (errCode == -2) {
            // 取消

        }

        finish();

    }

}
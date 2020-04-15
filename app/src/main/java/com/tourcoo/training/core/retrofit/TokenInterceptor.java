package com.tourcoo.training.core.retrofit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.app.MyApplication;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.entity.account.AccountHelper;
import com.tourcoo.training.ui.account.LoginActivity;


import java.io.IOException;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.tourcoo.training.config.RequestConfig.KEY_TOKEN;


/**
 * @author :JenkinsZhou
 * @description :token验证拦截器
 * @company :途酷科技
 * @date 2019年10月29日9:54
 * @Email: 971613168@qq.com
 */
public class TokenInterceptor implements Interceptor {
    public static final String TAG = "TokenInterceptor";
    private static final String TOKEN_FLAG = "NeedToken";
    private static final String OPTIONAL_TOKEN_FLAG = "OptionalToken";
    private static final String SKIP_LOGIN_FLAG = "skipLogin";
    private static final String YES = "true";
    private static final String NO = "false";
    //有时需要token 有时不需要token
    public static final String HEADER_OPTIONAL_TOKEN = OPTIONAL_TOKEN_FLAG + ": " + YES;
    public static final String HEADER_NEED_TOKEN = TOKEN_FLAG + ": " + YES;
    public static final String HEADER_NO_NEED_TOKEN = TOKEN_FLAG + ": " + NO;
    public static final String HEADER_NOT_SKIP_LOGIN = SKIP_LOGIN_FLAG + ": " + NO;
    public static final String HEADER_SKIP_LOGIN = SKIP_LOGIN_FLAG + ": " + YES;
//    private static final String URL_REFRESH_TOKEN = Contacts.PRO_BASE_URL;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String needToken = originalRequest.header(TOKEN_FLAG);
        String optionalTokenFlag = originalRequest.header(OPTIONAL_TOKEN_FLAG);
        Response originalResponse = chain.proceed(originalRequest);
        if (!TextUtils.isEmpty(needToken) && needToken.equalsIgnoreCase(NO)) {
            //说明该接口不需要传token 不做任何处理
            TourCooLogUtil.d(TAG, "不需要token验证 不做任何处理");
            return originalResponse;
        } else if (optionalTokenFlag != null) {
            //根据用户的登录状态 决定是否传入token
            if (AccountHelper.getInstance().isLogin()) {
                String skipLoginFlag = originalRequest.header(SKIP_LOGIN_FLAG);
                //不传或者为true 表示 该接口需要token
                boolean skipLoginEnable = skipLoginFlag == null || skipLoginFlag.equalsIgnoreCase(YES);
                TourCooLogUtil.d(TAG, "需要token验证");
                //需要token校验
                Request tokenRequest = chain.request().newBuilder()
                        .removeHeader(KEY_TOKEN)
                        .addHeader(KEY_TOKEN, AccountHelper.getInstance().getUserInfo().getAccessToken())
                        .build();
                Response tokenResponse = chain.proceed(tokenRequest);
                if (tokenResponse.code() == RequestConfig.CODE_REQUEST_TOKEN_INVALID) {
                    //说明token过期
                    if (skipLoginEnable) {
                        skipLogin();
                        ToastUtils.showShort("登录已过期 请重新登录");
                    }
                    AccountHelper.getInstance().logout();
                    //todo 通知其他页面
//                    EventBus.getDefault().post(new UserInfoEvent());
                }
                return tokenResponse;

            } else {
                TourCooLogUtil.d(TAG, "未登录，不传Token");
                return originalResponse;
            }
        } else {
            //说明该接口要传token
            TourCooLogUtil.d(TAG, "该接口要传token");
            if (AccountHelper.getInstance().isLogin()) {
                //判断当前接口 token过期后是否要跳转至登录页面
                  TourCooLogUtil.e(TAG,AccountHelper.getInstance().getUserInfo());
                Request newTokenRequest = chain.request().newBuilder()
                        .removeHeader(KEY_TOKEN)
                        .addHeader(KEY_TOKEN, AccountHelper.getInstance().getUserInfo().getAccessToken())
                        .build();
                String skipLoginFlag = originalRequest.header(SKIP_LOGIN_FLAG);
                boolean skipLoginEnable = skipLoginFlag == null || skipLoginFlag.equalsIgnoreCase(YES);
               /* if (skipLoginEnable) {
                    //表示 token过期后 需要跳转至登录页
                    skipLogin();
                }*/
                return chain.proceed(newTokenRequest);
            } else {
                Request newTokenRequest = chain.request().newBuilder()
                        .removeHeader(KEY_TOKEN)
                        .build();
                return chain.proceed(newTokenRequest);
            }
        }

    }

    private void skipLogin() {
        startActivity(MyApplication.getContext(), LoginActivity.class);
    }


    private <T> T parseJavaBean(Object data, Class<T> tClass) {
        try {
            return JSON.parseObject(JSON.toJSONString(data), tClass);
        } catch (Exception e) {
            LogUtils.e("parseJavaBean()报错--->" + e.toString());
            return null;
        }
    }


    /**
     * @param context
     * @param activity 跳转Activity
     */
    public static void startActivity(Context context, Class<? extends Activity> activity) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, activity);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


}

package com.tourcoo.training.core.retrofit;


import android.app.Activity;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.tourcoo.training.config.AppConfig;
import com.tourcoo.training.core.UiManager;
import com.tourcoo.training.core.interfaces.IHttpRequestControl;
import com.tourcoo.training.core.util.StackUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.dialog.LoadingDialogWrapper;

/**
 * @Author: JenkinsZhou on 2018/7/23 14:08
 * @E-Mail: 971613168@qq.com
 * Function: 快速创建支持Loading的Retrofit观察者
 * Description:
 * 1、2017-11-16 13:38:16 JenkinsZhou增加多种构造用于实现父类全局设置网络请求错误码
 */
public abstract class BaseLoadingObserver<T> extends BaseObserver<T> {
    /**
     * CommonListDialog
     */
    private LoadingDialogWrapper mDialog;
    protected String loadingText = "";

    /**
     * 用于全局配置
     *
     * @param activity
     */
    public BaseLoadingObserver(@Nullable Activity activity, IHttpRequestControl httpRequestControl, @StringRes int resId) {
        this(UiManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(resId), httpRequestControl);
    }

    public BaseLoadingObserver(IHttpRequestControl httpRequestControl, @StringRes int resId) {
        this(StackUtil.getInstance().getCurrent(), httpRequestControl, resId);
    }

    public BaseLoadingObserver(@Nullable Activity activity, IHttpRequestControl httpRequestControl, CharSequence msg) {
        this(UiManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(msg), httpRequestControl);
    }

    public BaseLoadingObserver(IHttpRequestControl httpRequestControl, CharSequence msg) {
        this(StackUtil.getInstance().getCurrent(), httpRequestControl, msg);
    }

    public BaseLoadingObserver(@Nullable Activity activity, @StringRes int resId) {
        this(UiManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(resId));
    }

    public BaseLoadingObserver(@StringRes int resId) {
        this(StackUtil.getInstance().getCurrent(), resId);
    }

    public BaseLoadingObserver(@Nullable Activity activity, CharSequence msg) {
        this(UiManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(msg));
    }

    public BaseLoadingObserver(CharSequence msg) {
        this(StackUtil.getInstance().getCurrent(), msg);
    }

    public BaseLoadingObserver(@Nullable Activity activity, IHttpRequestControl httpRequestControl) {
        this(UiManager.getInstance().getLoadingDialog().createLoadingDialog(activity), httpRequestControl);
    }

    public BaseLoadingObserver(IHttpRequestControl httpRequestControl) {
        this(StackUtil.getInstance().getCurrent(), httpRequestControl);
    }

    public BaseLoadingObserver(@Nullable Activity activity) {
        this(UiManager.getInstance().getLoadingDialog().createLoadingDialog(activity));
    }

    public BaseLoadingObserver() {
        this(StackUtil.getInstance().getCurrent());
    }

    public BaseLoadingObserver(String loadingText) {
        this(StackUtil.getInstance().getCurrent());
        this.loadingText = loadingText;
    }

    public BaseLoadingObserver(LoadingDialogWrapper dialog) {
        this(dialog, null);
    }

    public BaseLoadingObserver(LoadingDialogWrapper dialog, IHttpRequestControl httpRequestControl) {
        super(httpRequestControl);
        this.mDialog = dialog;
    }

    @Override
    public void onNext(T entity) {
        dismissProgressDialog();
        super.onNext(entity);
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        super.onError(e);
        if (AppConfig.DEBUG_MODE) {
            ToastUtil.showFailed(e.toString());
        }
    }

    public void showProgressDialog() {
        if (mDialog != null) {
            if (!TextUtils.isEmpty(loadingText)) {
                mDialog.setMessage(loadingText);
            }
            mDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressDialog();
    }
}

package com.tourcoo.training.core;

import android.app.Activity;

import com.apkfuns.logutils.LogUtils;
import com.tourcoo.training.core.manager.DelegateManager;


/**
 * @Author: JenkinsZhou on 2019/8/7 14:22
 * @E-Mail: JenkinsZhou@126.com
 * @Function: 绑定Activity Helper
 * @Description:
 */
public class BaseHelper {
     public static final String TAG = "BaseHelper";
    protected Activity mContext;
    protected String mTag = getClass().getSimpleName();

    public BaseHelper(Activity context) {
        mContext = context;
        DelegateManager.getInstance().putBasisHelper(context, this);
    }

    /**
     * Activity 关闭onDestroy调用
     */
    public void onDestroy() {
        LogUtils.tag(TAG).d( "onDestroy");
    }
}

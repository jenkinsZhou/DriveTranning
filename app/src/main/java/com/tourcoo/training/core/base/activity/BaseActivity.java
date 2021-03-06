package com.tourcoo.training.core.base.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.aliyun.utils.VcPlayerLog;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tourcoo.training.core.UiManager;
import com.tourcoo.training.core.constant.FrameConstant;
import com.tourcoo.training.core.interfaces.ActivityDispatchEventControl;
import com.tourcoo.training.core.interfaces.ActivityKeyEventControl;
import com.tourcoo.training.core.interfaces.IBasicView;
import com.tourcoo.training.core.interfaces.IRefreshLoadView;
import com.tourcoo.training.core.interfaces.QuitAppControl;
import com.tourcoo.training.core.interfaces.ISideControl;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.manager.RxJavaManager;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.StackUtil;
import com.tourcoo.training.core.widget.dialog.loading.IosLoadingDialog;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年12月26日17:04
 * @Email: 971613168@qq.com
 */
public abstract class BaseActivity extends RxAppCompatActivity implements IBasicView , ISideControl {
    protected Activity mContext;
    protected View mContentView;
    protected Handler baseHandler  = new Handler();
    protected Bundle mSavedInstanceState;
    protected boolean mIsViewLoaded = false;
    protected boolean mIsFirstShow = true;
    protected boolean mIsFirstBack = true;
    protected long mDelayBack = 1000;
    protected String TAG = getClass().getSimpleName();
    private QuitAppControl mQuitAppControl;
    protected IosLoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isEventBusEnable()) {
            if (CommonUtil.isClassExist(FrameConstant.EVENT_BUS_CLASS)) {
                if (CommonUtil.haveEventBusAnnotation(this)) {
                    org.greenrobot.eventbus.EventBus.getDefault().register(this);
                }
            }
            if (CommonUtil.isClassExist(FrameConstant.ANDROID_EVENT_BUS_CLASS)) {
                EventBus.getDefault().register(this);
            }
        }
        super.onCreate(savedInstanceState);
        this.mSavedInstanceState = savedInstanceState;
        mContext = this;
        setStatusBarDarkMode(mContext, isStatusBarDarkMode());
        beforeSetContentView();
        mContentView = View.inflate(mContext, getContentLayout(), null);
        //解决StatusLayoutManager与SmartRefreshLayout冲突
        if (this instanceof IRefreshLoadView) {
            if (CommonUtil.isClassExist(FrameConstant.SMART_REFRESH_LAYOUT_CLASS)) {
                if (mContentView.getClass() == SmartRefreshLayout.class) {
                    FrameLayout frameLayout = new FrameLayout(mContext);
                    if (mContentView.getLayoutParams() != null) {
                        frameLayout.setLayoutParams(mContentView.getLayoutParams());
                    }
                    frameLayout.addView(mContentView);
                    mContentView = frameLayout;
                }
            }
        }
        setContentView(mContentView);
        mIsViewLoaded = true;
        beforeInitView(savedInstanceState);
        initView(savedInstanceState);
        loadingDialog = new IosLoadingDialog(mContext);
    }


    @Override
    protected void onResume() {
        beforeFastLazyLoad();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (isEventBusEnable()) {
            if (CommonUtil.isClassExist(FrameConstant.EVENT_BUS_CLASS)) {
                if (CommonUtil.haveEventBusAnnotation(this)) {
                    org.greenrobot.eventbus.EventBus.getDefault().unregister(this);
                }
            }
            if (CommonUtil.isClassExist(FrameConstant.ANDROID_EVENT_BUS_CLASS)) {
                EventBus.getDefault().unregister(this);
            }
        }
        super.onDestroy();

        mContentView = null;
        mContext = null;
        mSavedInstanceState = null;
        mQuitAppControl = null;
        TAG = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> list = getSupportFragmentManager().getFragments();
        if (list.size() == 0) {
            return;
        }
        for (Fragment f : list) {
            f.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ActivityKeyEventControl control = UiManager.getInstance().getActivityKeyEventControl();
        if (control != null) {
            if (control.onKeyDown(this, keyCode, event)) {
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        ActivityKeyEventControl control = UiManager.getInstance().getActivityKeyEventControl();
        if (control != null) {
            if (control.onKeyUp(this, keyCode, event)) {
                return true;
            }
            return super.onKeyUp(keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        ActivityKeyEventControl control = UiManager.getInstance().getActivityKeyEventControl();
        if (control != null) {
            return control.onKeyLongPress(this, keyCode, event);
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        ActivityKeyEventControl control = UiManager.getInstance().getActivityKeyEventControl();
        if (control != null) {
            if (control.onKeyShortcut(this, keyCode, event)) {
                return true;
            }
            return super.onKeyShortcut(keyCode, event);
        }
        return super.onKeyShortcut(keyCode, event);

    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        ActivityKeyEventControl control = UiManager.getInstance().getActivityKeyEventControl();
        if (control != null) {
            if (control.onKeyMultiple(this, keyCode, repeatCount, event)) {
                return true;
            }
            return super.onKeyMultiple(keyCode, repeatCount, event);
        }
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ActivityDispatchEventControl control = UiManager.getInstance().getActivityDispatchEventControl();
        if (control != null) {
            if (control.dispatchTouchEvent(this, ev)) {
                return true;
            }
            return super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        ActivityDispatchEventControl control = UiManager.getInstance().getActivityDispatchEventControl();
        if (control != null) {
            if (control.dispatchGenericMotionEvent(this, ev)) {
                return true;
            }
            return super.dispatchGenericMotionEvent(ev);
        }
        return super.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        ActivityDispatchEventControl control = UiManager.getInstance().getActivityDispatchEventControl();
        if (control != null) {
            if (control.dispatchKeyEvent(this, event)) {
                return true;
            }
            return super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        ActivityDispatchEventControl control = UiManager.getInstance().getActivityDispatchEventControl();
        if (control != null) {
            if (control.dispatchKeyShortcutEvent(this, event)) {
                return true;
            }
            return super.dispatchKeyShortcutEvent(event);
        }
        return super.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        ActivityDispatchEventControl control = UiManager.getInstance().getActivityDispatchEventControl();
        if (control != null) {
            if (control.dispatchTrackballEvent(this, ev)) {
                return true;
            }
            return super.dispatchTrackballEvent(ev);
        }
        return super.dispatchTrackballEvent(ev);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        ActivityDispatchEventControl control = UiManager.getInstance().getActivityDispatchEventControl();
        if (control != null) {
            if (control.dispatchPopulateAccessibilityEvent(this, event)) {
                return true;
            }
            return super.dispatchPopulateAccessibilityEvent(event);
        }
        return super.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        if (UiManager.getInstance().getActivityFragmentControl() != null) {
            UiManager.getInstance().getActivityFragmentControl().setContentViewBackground(mContentView, this.getClass());
        }
    }

    private void beforeFastLazyLoad() {
        //确保视图加载及视图绑定完成避免刷新UI抛出异常
        if (!mIsViewLoaded) {
            RxJavaManager.getInstance().setTimer(10)
                    .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new BaseObserver<Long>() {
                        @Override
                        public void onSuccessNext(Long entity) {
                            beforeFastLazyLoad();
                        }
                    });
        } else {
            lazyLoad();
        }
    }

    private void lazyLoad() {
        if (mIsFirstShow) {
            mIsFirstShow = false;
            loadData();
        }
    }

    /**
     * 退出程序
     */
    protected void quitApp() {
        mQuitAppControl = UiManager.getInstance().getQuitAppControl();
        mDelayBack = mQuitAppControl != null ? mQuitAppControl.quipApp(mIsFirstBack, this) : mDelayBack;
        //时延太小或已是第二次提示直接通知执行最终操作
        if (mDelayBack <= 0 || !mIsFirstBack) {
            if (mQuitAppControl != null) {
                mQuitAppControl.quipApp(false, this);
            } else {
                StackUtil.getInstance().exit();
            }
            return;
        }
        //编写逻辑
        if (mIsFirstBack) {
            mIsFirstBack = false;
            RxJavaManager.getInstance().setTimer(mDelayBack)
                    .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new BaseObserver<Long>() {
                        @Override
                        public void onSuccessNext(Long entity) {
                            mIsFirstBack = true;
                        }
                    });
        }
    }

    protected void setStatusBarColor(Activity activity, int color) {
        if (color <= 0) {
            return;
        }
        baseHandler.postDelayed(() -> ImmersionBar.with(activity)
                .statusBarColor(color)
                .init(), 50);
    }


    protected void setStatusBarDarkMode(Activity activity, boolean isDarkFont) {
        baseHandler.postDelayed(() -> ImmersionBar.with(activity)
                .statusBarDarkFont(isDarkFont, 0.2f)
                .init(), 50);
    }

    protected void setViewVisible(View view, boolean visible) {
        if (view == null) {
//            TourCooLogUtil.e(TAG, "setViewVisible()--->View==null！");
            return;
        }
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setViewGone(View view, boolean visible) {
        if (view == null) {
//            TourCooLogUtil.e(TAG, "setViewGone()--->View==null！");
            return;
        }
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    protected String getTextValue(TextView textView) {
        return textView != null ? textView.getText().toString() : "";
    }

    protected String getTextValue(EditText editText) {
        return editText != null ? editText.getText().toString() : "";
    }


    protected void showLoading(String msg) {
        if (loadingDialog != null ) {
            if (!TextUtils.isEmpty(msg)) {
                loadingDialog.setLoadingText(msg);
            }
            loadingDialog.show();
        }
    }


    protected void closeLoading() {
        if (loadingDialog != null ) {
            loadingDialog.dismiss();
        }
    }


    protected boolean isStrangePhone() {
        boolean strangePhone = "mx5".equalsIgnoreCase(Build.DEVICE)
                || "Redmi Note2".equalsIgnoreCase(Build.DEVICE)
                || "Z00A_1".equalsIgnoreCase(Build.DEVICE)
                || "hwH60-L02".equalsIgnoreCase(Build.DEVICE)
                || "hermes".equalsIgnoreCase(Build.DEVICE)
                || ("V4".equalsIgnoreCase(Build.DEVICE) && "Meitu".equalsIgnoreCase(Build.MANUFACTURER))
                || ("m1metal".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER));

        VcPlayerLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
        return strangePhone;
    }
}

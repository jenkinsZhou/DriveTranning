package com.tourcoo.training.core.base.activity;

import android.app.Activity;
import android.os.Bundle;

import com.tourcoo.training.core.interfaces.ITitleView;
import com.tourcoo.training.core.util.FindViewUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;


/**
 * @Author: JenkinsZhou on 2018/7/23 10:35
 * @E-Mail: 971613168@qq.com
 * Function: 包含TitleBarView的Activity
 * Description:
 * 1、2019-3-25 17:03:43 推荐使用{@link ITitleView}通过接口方式由FastLib自动处理{@link com.tourcoo.training.core.impl.FrameLifecycleCallbacks#onActivityStarted(Activity)}
 */
public abstract class BaseTitleActivity extends BaseActivity implements ITitleView {

    protected TitleBarView mTitleBar;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mTitleBar = FindViewUtil.getTargetView(mContentView, TitleBarView.class);
    }

    @Override
    protected void onDestroy() {
        mTitleBar = null;
        super.onDestroy();
    }
}

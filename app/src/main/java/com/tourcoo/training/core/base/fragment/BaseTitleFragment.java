package com.tourcoo.training.core.base.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tourcoo.training.core.interfaces.ITitleView;
import com.tourcoo.training.core.util.FindViewUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;


/**
 * @Author: JenkinsZhou on 2018/7/23 10:34
 * @E-Mail: JenkinsZhou@126.com
 * Function: 设置有TitleBar的Fragment
 * Description:
 * 1、2019-3-25 17:03:43 推荐使用{@link ITitleView}通过接口方式由框架自动处理{@link com.tourcoo.training.core.impl.FrameLifecycleCallbacks#onFragmentStarted(FragmentManager, Fragment)}
 */
public abstract class BaseTitleFragment extends BaseFragment implements ITitleView {

    protected TitleBarView mTitleBar;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mTitleBar = FindViewUtil.getTargetView(mContentView, TitleBarView.class);
    }
}

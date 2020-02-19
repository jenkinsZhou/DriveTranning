package com.tourcoo.training.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tourcoo.training.R;
import com.tourcoo.training.core.base.activity.QQTitleActivity;
import com.tourcoo.training.core.base.fragment.BaseFragment;
import com.tourcoo.training.core.base.fragment.BaseTitleFragment;
import com.tourcoo.training.core.util.StatusBarUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年12月27日17:39
 * @Email: 971613168@qq.com
 */
public class MineFragment extends BaseTitleFragment {
    @Override
    public int getContentLayout() {
        return R.layout.fragement_mine;
    }
    private RelativeLayout rlTitle;
    private SmartRefreshLayout smartRefreshLayoutCommon;
    @Override
    public void initView(Bundle savedInstanceState) {
        rlTitle = mContentView.findViewById(R.id.rlTitle);
        smartRefreshLayoutCommon = mContentView.findViewById(R.id.smartRefreshLayoutCommon);
        setMarginTop();
        smartRefreshLayoutCommon.setRefreshHeader(new ClassicsHeader(mContext));
     /*   mContentView.findViewById(R.id.ivContent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, QQTitleActivity.class);
                startActivity(intent);
            }
        });*/
    }



    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setVisibility(View.GONE);
    }


    private void setMarginTop( ) {
        ViewGroup.LayoutParams layoutParams = rlTitle.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).setMargins(0, getMarginTop(), 0, 0);
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).setMargins(0, getMarginTop(), 0, 0);
        } else if (layoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) layoutParams).setMargins(0, getMarginTop(), 0, 0);
        }
    }

    @Override
    public int getMarginTop() {
        return StatusBarUtil.getStatusBarHeight();
    }


}

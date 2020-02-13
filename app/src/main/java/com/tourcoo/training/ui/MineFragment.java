package com.tourcoo.training.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tourcoo.training.R;
import com.tourcoo.training.core.base.activity.QQTitleActivity;
import com.tourcoo.training.core.base.fragment.BaseFragment;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年12月27日17:39
 * @Email: 971613168@qq.com
 */
public class MineFragment extends BaseFragment {
    @Override
    public int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mContentView.findViewById(R.id.ivContent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, QQTitleActivity.class);
                startActivity(intent);
            }
        });
    }

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

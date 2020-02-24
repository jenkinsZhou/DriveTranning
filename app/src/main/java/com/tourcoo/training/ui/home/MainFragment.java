package com.tourcoo.training.ui.home;

import android.os.Bundle;
import android.view.View;

import com.tourcoo.training.R;
import com.tourcoo.training.core.base.fragment.BaseTitleFragment;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月24日16:05
 * @Email: 971613168@qq.com
 */
public class MainFragment extends BaseTitleFragment implements View.OnClickListener  {
    @Override
    public void onClick(View v) {

    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

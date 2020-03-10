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
public class MainTabFragment extends BaseTitleFragment implements View.OnClickListener  {
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



    public static MainTabFragment newInstance() {
        Bundle args = new Bundle();
        MainTabFragment fragment = new MainTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("主页");
    }



}

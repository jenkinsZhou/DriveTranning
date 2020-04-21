package com.tourcoo.training.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tourcoo.training.R;
import com.tourcoo.training.core.base.activity.BaseMainActivity;
import com.tourcoo.training.core.base.entity.FrameTabEntity;
import com.tourcoo.training.core.widget.view.tab.CommonTabLayout;
import com.tourcoo.training.ui.home.MainTabFragment;
import com.tourcoo.training.ui.home.MineTabFragment;
import com.tourcoo.training.ui.home.MineTabFragmentNew;
import com.tourcoo.training.ui.home.NewsTabFragment;
import com.tourcoo.training.ui.home.StudyTabFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年12月27日17:31
 * @Email: 971613168@qq.com
 */
public class MainTabActivity extends BaseMainActivity {

    private CommonTabLayout mTabLayout;
    private ArrayList<FrameTabEntity> mTabEntities;

    @Nullable
    @Override
    public List<FrameTabEntity> getTabList() {
        mTabEntities = new ArrayList<>();
        mTabEntities.add(new FrameTabEntity("发现", R.drawable.ic_home_normal, R.drawable.ic_home_selected, NewsTabFragment.Companion.newInstance()));
        mTabEntities.add(new FrameTabEntity("主页", R.drawable.ic_home_normal, R.drawable.ic_home_selected, StudyTabFragment.Companion.newInstance()));
        mTabEntities.add(new FrameTabEntity("我的", R.drawable.ic_home_normal, R.drawable.ic_home_selected, MineTabFragmentNew.Companion.newInstance()));
        return mTabEntities;
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public boolean isSlideEnable() {
        return false;
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
        mTabLayout = tabLayout;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTabLayout = findViewById(R.id.commonTabLayout);
        mTabLayout.setCurrentTab(1);
    }






}

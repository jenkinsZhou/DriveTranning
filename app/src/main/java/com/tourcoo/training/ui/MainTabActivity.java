package com.tourcoo.training.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tourcoo.training.R;
import com.tourcoo.training.core.base.activity.BaseMainActivity;
import com.tourcoo.training.core.base.entity.FrameTabEntity;
import com.tourcoo.training.core.widget.view.tab.CommonTabLayout;

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
    @Override
    public void initView(Bundle savedInstanceState) {
        mTabLayout = findViewById(R.id.commonTabLayout);
    }

    @Nullable
    @Override
    public List<FrameTabEntity> getTabList() {
        mTabEntities = new ArrayList<>();
        mTabEntities.add(new FrameTabEntity("主页", R.drawable.ic_home_normal, R.drawable.ic_home_selected, MineFragment.newInstance()));
        mTabEntities.add(new FrameTabEntity("我的", R.drawable.ic_home_normal, R.drawable.ic_home_selected, MineFragment.newInstance()));
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
      /*  ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.ic_github);
        tabLayout.setCenterView(imageView, SizeUtil.dp2px(42), SizeUtil.dp2px(42), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.start(mContext, "https://github.com/JenkinsZhou/FastLib/blob/master/README.md");
            }
        });*/
    }
}

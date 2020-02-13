package com.tourcoo.training.core.base.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.tourcoo.training.R;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;


/**
 * @Author: JenkinsZhou on 2018/9/19 10:37
 * @E-Mail: JenkinsZhou@126.com
 * Function: QQ默认主题Title背景渐变
 * Description:
 */
public class QQTitleActivity extends BaseTitleActivity  {

    @Override
    public int getContentLayout() {
        return R.layout.activity_qq_title;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setLeftTextDrawable(R.drawable.ic_back_white)
                .setStatusBarLightMode(false)
                .setTitleMainTextColor(Color.WHITE)
                .setBgResource(R.drawable.shape_qq_bg);
    }


}

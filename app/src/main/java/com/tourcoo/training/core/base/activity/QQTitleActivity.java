package com.tourcoo.training.core.base.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

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
        return R.layout.activity_register;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setLeftTextDrawable(R.drawable.icon_return)
                .setStatusBarLightMode(false)
                .setTitleMainTextColor(Color.BLACK)
                .setBgResource(R.color.white)
              ;
    }


}

package com.tourcoo.training.core.base.fragment;

import com.tourcoo.training.R;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.SizeUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;

import static com.tourcoo.training.config.AppConfig.TITLE_MAIN_TITLE_SIZE;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月06日14:50
 * @Email: 971613168@qq.com
 */
public abstract class BaseBlueBgTitleFragment extends BaseTitleFragment {

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        if (titleBar != null) {
//            setMarginTop(titleBar);
            titleBar.setBgResource(R.drawable.bg_gradient_blue_53c2ff_4e52ff);
            titleBar.setLeftTextDrawable(R.drawable.ic_back_white);
            titleBar.setTitleMainTextColor(CommonUtil.getColor(R.color.white));
            titleBar.setHeight(SizeUtil.dp2px(45f));
        }
    }
}

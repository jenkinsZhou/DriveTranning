package com.tourcoo.training.core.base.delegate;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;

import com.tourcoo.training.R;
import com.tourcoo.training.core.UiManager;
import com.tourcoo.training.core.interfaces.ITitleView;
import com.tourcoo.training.core.interfaces.TitleBarViewControl;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.FindViewUtil;
import com.tourcoo.training.core.util.SizeUtil;
import com.tourcoo.training.core.util.StackUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;


/**
 * @Author: JenkinsZhou on 2018/7/13 17:53
 * @E-Mail: 971613168@qq.com
 * Function: 带TitleBarView 的Activity及Fragment代理类
 * Description:
 * 1、2018-4-20 13:53:57 简化全局属性设置通过接口暴露实现
 * 2、2018-6-22 14:06:50 设置通用基础数据
 * 3、2018-7-23 09:47:16 修改TitleBarView设置主标题逻辑
 * ({@link Activity#getTitle()}获取不和应用名称一致才进行设置-因Manifest未设置Activity的label属性获取的是应用名称)
 * 4、2018-11-19 11:27:42 设置全局Tint颜色资源
 * 5、2019-1-23 16:17:37 修改返回键操作逻辑避免快速点击造成页面崩溃问题
 */
public class TitleDelegate {
     public static final String TAG = "TitleDelegate";
    public TitleBarView mTitleBar;
    private TitleBarViewControl mTitleBarViewControl;

    public TitleDelegate(View rootView, ITitleView iTitleBarView, final Class<?> cls) {
        mTitleBar = rootView.findViewById(R.id.titleBarHead);
        if (mTitleBar == null) {
            mTitleBar = FindViewUtil.getTargetView(rootView, TitleBarView.class);
        }
        if (mTitleBar == null) {
            return;
        }
       TourCooLogUtil.i("class:" + cls.getSimpleName());
        //默认的MD风格返回箭头icon如使用该风格可以不用设置
        final Activity activity = StackUtil.getInstance().getActivity(cls);
        //设置TitleBarView 所有TextView颜色
        mTitleBar.setLeftTextDrawable(activity != null ? R.mipmap.ic: 0)
                //.setLeftTextDrawableTintResource(R.color.colorTitleText)
                .setOnLeftTextClickListener(activity == null ? null : new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity = StackUtil.getInstance().getActivity(cls);
                        //增加判断避免快速点击返回键造成崩溃
                        if (activity == null) {
                            return;
                        }
                        activity.onBackPressed();
                    }
                })
                .setTextColorResource(R.color.colorTitleText)
                //.setRightTextDrawableTintResource(R.color.colorTitleText)
                //.setActionTintResource(R.color.colorTitleText)
                .setTitleMainText(getTitle(activity));
        mTitleBarViewControl = UiManager.getInstance().getTitleBarViewControl();
        if (mTitleBarViewControl != null) {
            mTitleBarViewControl.createTitleBarViewControl(mTitleBar, cls);
        }
        iTitleBarView.beforeSetTitleBar(mTitleBar);
        iTitleBarView.setTitleBar(mTitleBar);
        if(iTitleBarView.isMainTitleBold()){
            mTitleBar.getMainTitleTextView().setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else {
            mTitleBar.getMainTitleTextView().setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }

    /**
     * 获取Activity 标题({@link Activity#getTitle()}获取不和应用名称一致才进行设置-因Manifest未设置Activity的label属性获取的是应用名称)
     *
     * @param activity
     * @return
     */
    private CharSequence getTitle(Activity activity) {
        if (activity != null) {
            CharSequence appName = CommonUtil.getAppName(activity);
            CharSequence label = activity.getTitle();
            if (label != null && !label.equals(appName)) {
                return label;
            }
        }
        return "";
    }

    public void onDestroy() {
        mTitleBar = null;
        mTitleBarViewControl = null;
       TourCooLogUtil.i(  "onDestroy");
    }
}

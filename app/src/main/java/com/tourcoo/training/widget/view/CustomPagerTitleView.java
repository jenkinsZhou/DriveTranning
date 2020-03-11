package com.tourcoo.training.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tourcoo.training.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月11日17:17
 * @Email: 971613168@qq.com
 */
public class CustomPagerTitleView extends View implements IMeasurablePagerTitleView {

    private TextView tvPageTitle;
    private RelativeLayout rlParentLayout;
    public CustomPagerTitleView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_pager_change_view,null);
        rlParentLayout = (RelativeLayout) view;
        tvPageTitle = view.findViewById(R.id.tvPageTitle);
    }

    public CustomPagerTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPagerTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomPagerTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getContentLeft() {
        return 0;
    }

    @Override
    public int getContentTop() {
        return 0;
    }

    @Override
    public int getContentRight() {
        return 0;
    }

    @Override
    public int getContentBottom() {
        return 0;
    }

    @Override
    public void onSelected(int index, int totalCount) {

    }

    @Override
    public void onDeselected(int index, int totalCount) {

    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

    }
}

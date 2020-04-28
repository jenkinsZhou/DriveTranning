package com.tourcoo.training.ui.guide;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月28日20:47
 * @Email: 971613168@qq.com
 */
public class GuidePagerAdapterNew extends PagerAdapter {
    private List<View> viewList;
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //当前滑动到的ViewPager页面
        View view = viewList.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//每次划出当前页面的时候就销毁
        //super.destroyItem(container, position, object);
        container.removeView(viewList.get(position));
    }

    @Override
    public int getCount() {
        //设置ViewPager有几个滑动页面
        return viewList.size();
        //用图片资源数组的length也可以，用图片容器的size也可以
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;//官方固定写法
    }

    public GuidePagerAdapterNew(List<View> viewList) {
        this.viewList = viewList;
    }
}

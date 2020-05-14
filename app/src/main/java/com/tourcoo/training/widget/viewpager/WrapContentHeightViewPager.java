package com.tourcoo.training.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * @author :JenkinsZhou
 * @description :高度自适应内容的ViewPager
 * @company :途酷科技
 * @date 2019年03月20日14:20
 * @Email: 971613168@qq.com
 */
public class WrapContentHeightViewPager extends ViewPager {

    /**
     * 左右滑动是否可用（默认可用）
     */
    private boolean slidingEnable;

    public WrapContentHeightViewPager(Context context) {
        this(context, null);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                requestLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取当前子view
        View view = getChildAt(getCurrentItem());
        if (view != null) {
            // 测量当前子view宽高
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));
    }

    /**
     * 决定控件高度
     *
     * @param measureSpec
     * @param view
     * @return
     */
    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            // set the height from the base view if available
//            如果view可用 则获取view高度
            if (view != null) {
                result = view.getMeasuredHeight();
            }
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!slidingEnable){
            return false;
        }else{
            return super.onTouchEvent(motionEvent);
        }
    }

    public boolean isSlidingEnable() {
        return slidingEnable;
    }

    public void setSlidingEnable(boolean slidingEnable) {
        this.slidingEnable = slidingEnable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!slidingEnable){
            return false;
        }else{
            return super.onInterceptTouchEvent(motionEvent);
        }
    }
}

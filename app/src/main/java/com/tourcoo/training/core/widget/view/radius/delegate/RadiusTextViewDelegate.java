package com.tourcoo.training.core.widget.view.radius.delegate;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @Author: JenkinsZhou on 2018/7/19 10:00
 * @E-Mail: 971613168@qq.com
 * Function:TextView及EditText代理类
 * Description:
 * 1、2018-5-25 11:27:05 新增代理解决泛型链式调用BUG
 */
public class RadiusTextViewDelegate extends RadiusTextDelegate<RadiusTextViewDelegate> {

    public RadiusTextViewDelegate(TextView view, Context context, AttributeSet attrs) {
        super(view, context, attrs);
    }

    @Override
    public void init() {
        super.init();
    }
}

package com.tourcoo.training.core.widget.view.alpha;

import android.content.Context;
import android.util.AttributeSet;

import com.tourcoo.training.core.widget.view.alpha.delegate.AlphaDelegate;


/**
 * @Author: JenkinsZhou on 2018/7/19 9:56
 * @E-Mail: 971613168@qq.com
 * Function: 控制Alpha 按下效果
 * Description:
 */
public class AlphaTextView extends androidx.appcompat.widget.AppCompatTextView {

    private AlphaDelegate delegate;

    public AlphaTextView(Context context) {
        this(context, null);
    }

    public AlphaTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        delegate = new AlphaDelegate(this);
    }

    public AlphaDelegate getDelegate() {
        return delegate;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        delegate.getAlphaViewHelper().onPressedChanged(this, pressed);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        delegate.getAlphaViewHelper().onEnabledChanged(this, enabled);
    }
}

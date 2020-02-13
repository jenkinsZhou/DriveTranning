package com.tourcoo.training.core.interfaces;

import android.widget.Toast;

import com.tourcoo.training.core.widget.view.radius.RadiusTextView;


/**
 * @Author: JenkinsZhou on 2019/1/18 17:49
 * @E-Mail: JenkinsZhou@126.com
 * @Description:
 */
public interface ToastControl {

    /**
     * 处理其它异常情况
     *
     * @return
     */
    Toast getToast();

    /**
     * 设置Toast
     *
     * @param toast    ToastUtil 中的Toast
     * @param textView ToastUtil 中的Toast设置的View
     */
    void setToast(Toast toast, RadiusTextView textView);
}

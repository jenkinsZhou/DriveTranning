package com.tourcoo.training.widget.aliplayer.listener;

/**
 * 清晰度切换监听
 */
public interface OnChangeQualityListener {

    void onChangeQualitySuccess(String quality);

    void onChangeQualityFail(int code, String msg);
}

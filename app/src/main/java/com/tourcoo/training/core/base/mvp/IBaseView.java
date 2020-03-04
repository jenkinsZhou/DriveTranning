package com.tourcoo.training.core.base.mvp;

import android.content.Context;

import com.trello.rxlifecycle3.LifecycleTransformer;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月24日17:58
 * @Email: 971613168@qq.com
 */
public interface IBaseView {

    /**
     * 显示加载框
     */
    void showLoading();
    /**
     * 隐藏加载框
     */
    void closeLoading();

    /**
     * 空数据
     *
     * @param tag TAG
     */
    void onEmpty(Object tag);

    /**
     * 错误数据
     *
     * @param tag      TAG
     * @param errorMsg 错误信息
     */
    void onError(Object tag, String errorMsg);

    /**
     * 上下文
     *
     * @return context
     */
    Context getContext();


    <T> LifecycleTransformer<T> bindUntilEvent();


}

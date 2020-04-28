package com.tourcoo.training.core.base.fragment;



import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tourcoo.training.core.base.activity.BaseTitleRefreshLoadActivity;
import com.tourcoo.training.core.base.mvp.BasePresenter;
import com.tourcoo.training.core.base.mvp.IBaseView;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.FragmentEvent;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月28日10:35
 * @Email: 971613168@qq.com
 */
public abstract class BaseTitleMvpRefreshLoadFragment<P extends BasePresenter, T> extends BaseTitleRefreshLoadFragment<T> implements IBaseView {
    protected P presenter;

    protected abstract void loadPresenter();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
        loadPresenter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void loadData() {
        super.loadData();
    }


    //***************************************IBaseView方法实现*************************************
    @Override
    public void showLoading() {
        showLoading("");
    }



    @Override
    public void closeLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onEmpty(Object tag) {

    }

    @Override
    public void onError(Object tag, String errorMsg) {

    }

    @Override
    public Context getContext() {
        return mContext;
    }


    /**
     * 创建Presenter
     *
     * @return p
     */
    protected abstract P createPresenter();


    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent() {
        return bindUntilEvent(FragmentEvent.DESTROY);
    }
}

package com.tourcoo.training.ui.account.register;


import com.tourcoo.training.core.base.mvp.BasePresenter;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月07日14:47
 * @Email: 971613168@qq.com
 */
public class DriverRegisterPresenter extends BasePresenter<DriverRegisterContract.RegisterModel, DriverRegisterContract.RegisterView> implements DriverRegisterContract.RegisterPresenter {
    @Override
    protected DriverRegisterContract.RegisterModel createModule() {
        return null;
    }

    @Override
    public void start() {

    }


    @Override
    public void selectImages() {
        mProxyView.getImages();
    }

    @Override
    public void uploadImage() {
        mProxyView.uploadImage(mProxyView.getImages());
    }
}

package com.tourcoo.training.ui.account.register;

import com.tourcoo.training.config.AppConfig;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.base.mvp.BasePresenter;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.entity.account.UserInfo;
import com.tourcoo.training.entity.account.register.IndustryCategory;

import java.util.List;
import java.util.Map;

/**
 * @author :JenkinsZhou
 * @description :个体工商户注册Presenter
 * @company :途酷科技
 * @date 2020年04月09日14:18
 * @Email: 971613168@qq.com
 */
public class IndustryRegisterPresenter extends BasePresenter<IndustryRegisterContract.RegisterModel, IndustryRegisterContract.RegisterView> implements IndustryRegisterContract.RegisterPresenter {


    @Override
    protected IndustryRegisterContract.RegisterModel createModule() {
        return new IndustryRegisterModel();
    }

    @Override
    public void start() {
        doGetIndustryCategory();
    }

    @Override
    public void doRegister(Map<String, Object> values) {
        if (!isViewAttached()) {
            return;
        }
        getModule().requestRegister(values, new BaseLoadingObserver<BaseResult<Object>>() {
            @Override
            public void onSuccessNext(BaseResult<Object> entity) {
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    getView().registerSuccess(entity.getData());
                } else {
                    ToastUtil.show(entity.msg);
                }
            }

            @Override
            public void onFailedNext(Throwable e) {
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed(e.toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed(e.toString());
                }
            }
        });
    }

    @Override
    public void doGetIndustryCategory() {
        if (!isViewAttached()) {
            return;
        }
        getModule().requestCategory(new BaseLoadingObserver<BaseResult<List<IndustryCategory>>>() {
            @Override
            public void onSuccessNext(BaseResult<List<IndustryCategory>> entity) {
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    getView().initIndustry(entity.getData());
                } else {
                    ToastUtil.show(entity.msg);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed(e.toString());
                }
            }
        });
    }

    @Override
    public void doLogin(String idCard, String password) {
        if (!isViewAttached()) {
            return;
        }
        getModule().requestLogin(idCard, password,new BaseLoadingObserver<BaseResult<UserInfo>>() {
            @Override
            public void onSuccessNext(BaseResult<UserInfo> entity) {
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    getView().loginSuccess(entity.getData());
                } else {
                    ToastUtil.show(entity.msg);
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.show(e.toString());
            }
        });
    }
}

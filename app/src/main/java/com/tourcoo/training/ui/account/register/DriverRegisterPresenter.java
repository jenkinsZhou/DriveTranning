package com.tourcoo.training.ui.account.register;



import com.tourcoo.training.config.AppConfig;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.base.mvp.BasePresenter;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.entity.account.UserInfo;

import java.util.Map;


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
        return new DriverRegisterModel();
    }

    @Override
    public void start() {
//        mProxyView.showIdCardInfo(getIdCardInfo());
    }




    @Override
    public void doRegister(Map<String, Object> values) {
        if (!isViewAttached()) {
            return;
        }
        getModule().requestRegister(values, new BaseLoadingObserver<BaseResult<UserInfo>>() {
            @Override
            public void onSuccessNext(BaseResult<UserInfo> entity) {
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    getView().registerSuccess(entity.getData());
                } else {
                    ToastUtil.show(entity.msg);
                }
            }
        });
    }


}

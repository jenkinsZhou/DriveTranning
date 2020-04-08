package com.tourcoo.training.ui.account.register;

import android.widget.EditText;

import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.base.mvp.IBaseModel;
import com.tourcoo.training.core.base.mvp.IBaseView;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.entity.account.IdCardInfo;
import com.tourcoo.training.entity.account.UserInfo;

import java.util.Map;


/**
 * @author :JenkinsZhou
 * @description :驾驶员注册Contract
 * @company :途酷科技
 * @date 2020年04月07日14:22
 * @Email: 971613168@qq.com
 */
public interface DriverRegisterContract {

    interface RegisterView extends IBaseView {
        /*void showIdCardInfo(IdCardInfo idCardInfo);*/

        String showCompanyByKeyword(String keyWord);

        void registerSuccess(UserInfo userInfo);
    }

    interface RegisterModel extends IBaseModel {
        void requestRegister(Map<String, Object> values, BaseObserver<BaseResult<UserInfo>> observer);
    }

    interface RegisterPresenter {
        //        IdCardInfo getIdCardInfo();
        void doRegister(Map<String, Object> values);

    }
}

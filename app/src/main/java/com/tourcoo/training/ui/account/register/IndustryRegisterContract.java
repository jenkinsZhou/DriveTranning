package com.tourcoo.training.ui.account.register;

import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.base.mvp.IBaseModel;
import com.tourcoo.training.core.base.mvp.IBaseView;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.entity.account.UserInfo;
import com.tourcoo.training.entity.account.register.IndustryCategory;

import java.util.List;
import java.util.Map;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月09日14:16
 * @Email: 971613168@qq.com
 */
public interface IndustryRegisterContract {

    interface RegisterView extends IBaseView {
        /*void showIdCardInfo(IdCardInfo idCardInfo);*/

//        String showCompanyByKeyword(String keyWord);

        void registerSuccess(UserInfo userInfo);

        void initIndustry(List<IndustryCategory> list);

        void loginSuccess(UserInfo userInfo);

    }

    interface RegisterModel extends IBaseModel {

        void requestCategory(BaseObserver<BaseResult<List<IndustryCategory>>> observer);

        void requestRegister(Map<String, Object> values, BaseObserver<BaseResult<UserInfo>> observer);

        void requestLogin(String idCard, String password,BaseObserver<BaseResult<UserInfo>> observer);
    }

    interface RegisterPresenter {
        void doRegister(Map<String, Object> values);

        void doGetIndustryCategory();

        void doLogin(String idCard, String password);
    }
}

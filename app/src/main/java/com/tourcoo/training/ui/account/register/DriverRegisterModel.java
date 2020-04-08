package com.tourcoo.training.ui.account.register;


import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.entity.account.UserInfo;

import java.util.Map;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月07日14:52
 * @Email: 971613168@qq.com
 */
public class DriverRegisterModel implements DriverRegisterContract.RegisterModel {




    @Override
    public void requestRegister(Map<String, Object> values, BaseObserver<BaseResult<UserInfo>> observer) {
        ApiRepository.getInstance().requestRegisterDriver(values).subscribe(observer);
    }
}

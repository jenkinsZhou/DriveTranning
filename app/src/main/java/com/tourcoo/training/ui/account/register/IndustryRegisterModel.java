package com.tourcoo.training.ui.account.register;

import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.util.StackUtil;
import com.tourcoo.training.entity.account.UserInfo;
import com.tourcoo.training.entity.account.register.IndustryCategory;

import java.util.List;
import java.util.Map;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月09日14:24
 * @Email: 971613168@qq.com
 */
public class IndustryRegisterModel implements IndustryRegisterContract.RegisterModel {


    @Override
    public void requestCategory(BaseObserver<BaseResult<List<IndustryCategory>>> observer) {
        ApiRepository.getInstance().requestCategory().subscribe(observer);

    }

    @Override
    public void requestRegister(Map<String, Object> values, BaseObserver<BaseResult<Object>> observer) {
        ApiRepository.getInstance().requestRegisterIndustry(values).subscribe(observer);
    }
}

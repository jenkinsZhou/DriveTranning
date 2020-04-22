package com.tourcoo.training.ui.pay;


import com.tourcoo.training.core.base.entity.BaseMovieEntity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.entity.account.PayInfo;
import com.tourcoo.training.entity.account.UserInfo;
import com.tourcoo.training.entity.pay.CoursePayInfo;
import com.tourcoo.training.ui.account.register.DriverRegisterContract;

import java.util.Map;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月07日14:52
 * @Email: 971613168@qq.com
 */
public class BuyNowModel implements BuyNowContract.BuyNowModel {

    @Override
    public void getPayinfo(String trainingPlanID, BaseObserver<BaseResult<CoursePayInfo>> observer) {
        ApiRepository.getInstance().requestGetCoursePayInfo(trainingPlanID).subscribe(observer);
    }

    @Override
    public void setPayinfo(String trainingPlanID, int payType, BaseObserver<BaseResult<PayInfo>> observer) {
        ApiRepository.getInstance().requestPayCourse(trainingPlanID, payType).subscribe(observer);
    }
}

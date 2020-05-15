package com.tourcoo.training.ui.pay;


import com.tourcoo.training.core.base.entity.BaseMovieEntity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.base.mvp.IBaseModel;
import com.tourcoo.training.core.base.mvp.IBaseView;
import com.tourcoo.training.core.interfaces.IHttpRequestControl;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.entity.account.PayInfo;
import com.tourcoo.training.entity.account.UserInfo;
import com.tourcoo.training.entity.pay.CoursePayInfo;

import java.util.Map;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月13日17:34
 * @Email: 971613168@qq.com
 */
public interface BuyNowContract {


    interface BuyNowModel extends IBaseModel {
        void getPayinfo(String trainingPlanID, BaseObserver<BaseResult<CoursePayInfo>> observer);
        //提交订单，获取支付参数
        void setPayinfo(String trainingPlanID, int payType,BaseObserver<BaseResult<PayInfo>> observer);
    }

    interface View extends IBaseView {
        void getPayInfoSuccess(CoursePayInfo payInfo);
        void setPayInfo(int payType, PayInfo payInfo);
        void payFailed(String message);
    }

    interface Presenter {
        void getPayInfo(String trainingPlanID);
        void buyCourse(String trainingPlanID,int payType);
    }
}

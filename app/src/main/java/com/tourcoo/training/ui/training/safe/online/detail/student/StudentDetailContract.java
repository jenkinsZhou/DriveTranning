package com.tourcoo.training.ui.training.safe.online.detail.student;

import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.base.mvp.IBaseModel;
import com.tourcoo.training.core.base.mvp.IBaseView;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.entity.account.PayInfo;
import com.tourcoo.training.entity.pay.CoursePayInfo;
import com.tourcoo.training.entity.training.TrainingPlanDetail;

import java.util.Map;

import io.reactivex.Observable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月22日12:42
 * @Email: 971613168@qq.com
 */
public interface StudentDetailContract {
    interface TrainDetailModel extends IBaseModel {
        void requestTrainDetail(String trainingPlanID,  BaseObserver<BaseResult<TrainingPlanDetail>> observer);

        void requestTurnOnline(String trainingPlanID,  BaseObserver<BaseResult> observer);

    }

    interface View extends IBaseView {
        void doShowTrainPlan(TrainingPlanDetail planDetail);

        /**
         * 转线上
         */
        void  doTurnOnline();

        void showTurnOnlineSuccess();

        void showTurnOnlineFailed();
    }

    interface TrainDetailPresenter {
        void getTrainDetail(String trainingPlanID);

        void getTurnOnline(String trainingPlanID);
    }
}

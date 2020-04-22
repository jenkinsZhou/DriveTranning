package com.tourcoo.training.ui.training.safe.online.detail.teacher;

import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.base.mvp.IBaseModel;
import com.tourcoo.training.core.base.mvp.IBaseView;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.entity.training.TrainingPlanDetail;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月22日20:09
 * @Email: 971613168@qq.com
 */
public interface TeacherDetailContract {
    interface TrainDetailModel extends IBaseModel {
        void requestTrainDetail(String trainingPlanID,  BaseObserver<BaseResult<TrainingPlanDetail>> observer);
    }

    interface View extends IBaseView {
        void doShowTrainPlan(TrainingPlanDetail planDetail);
    }

    interface TrainDetailPresenter {
        void getTrainDetail(String trainingPlanID);
    }
}

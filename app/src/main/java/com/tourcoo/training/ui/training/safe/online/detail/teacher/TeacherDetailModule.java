package com.tourcoo.training.ui.training.safe.online.detail.teacher;

import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.entity.training.TrainingPlanDetail;
import com.tourcoo.training.ui.training.safe.online.detail.student.StudentDetailContract;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月22日12:55
 * @Email: 971613168@qq.com
 */
public class TeacherDetailModule implements StudentDetailContract.TrainDetailModel {

    @Override
    public void requestTrainDetail(String trainingPlanID, BaseObserver<BaseResult<TrainingPlanDetail>> observer) {
        ApiRepository.getInstance().trainingPlanID(trainingPlanID).subscribe(observer);
    }

    @Override
    public void requestTurnOnline(String trainingPlanID, BaseObserver<BaseResult> observer) {
        ApiRepository.getInstance().requestTurnOnline(trainingPlanID).subscribe(observer);
    }
}

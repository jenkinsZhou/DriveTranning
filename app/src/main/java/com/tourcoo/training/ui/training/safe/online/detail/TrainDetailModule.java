package com.tourcoo.training.ui.training.safe.online.detail;

import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.entity.training.TrainingPlanDetail;

import io.reactivex.Observable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月22日12:55
 * @Email: 971613168@qq.com
 */
public class TrainDetailModule implements TrainDetailContract.TrainDetailModel {

    @Override
    public void requestTrainDetail(String trainingPlanID, BaseObserver<BaseResult<TrainingPlanDetail>> observer) {

    }
}

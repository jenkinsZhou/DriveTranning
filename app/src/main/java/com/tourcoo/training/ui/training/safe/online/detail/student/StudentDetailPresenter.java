package com.tourcoo.training.ui.training.safe.online.detail.student;

import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.base.mvp.BasePresenter;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.entity.training.TrainingPlanDetail;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月22日12:41
 * @Email: 971613168@qq.com
 */
public class StudentDetailPresenter extends BasePresenter<StudentDetailContract.TrainDetailModel, StudentDetailContract.View> implements StudentDetailContract.TrainDetailPresenter {
    @Override
    public void getTrainDetail(String trainingPlanID) {
        if (!isViewAttached()) {
            return;
        }
        getModule().requestTrainDetail(trainingPlanID, new BaseLoadingObserver<BaseResult<TrainingPlanDetail>>(){

            @Override
            public void onSuccessNext(BaseResult<TrainingPlanDetail> entity) {
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    getView().doShowTrainPlan(entity.getData());
                } else {
                    ToastUtil.show(entity.msg);
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.show(e.toString());
            }
        });

    }

    @Override
    protected StudentDetailContract.TrainDetailModel createModule() {
        return new StudentDetailModule();
    }

    @Override
    public void start() {

    }


}

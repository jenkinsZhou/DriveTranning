package com.tourcoo.training.ui.training.safe.online.detail.teacher;

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
public class TeacherDetailPresenter extends BasePresenter<TeacherDetailContract.TrainDetailModel, TeacherDetailContract.View> implements TeacherDetailContract.TrainDetailPresenter {
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
    public void getTurnOnline(String trainingPlanID) {
        if (!isViewAttached()) {
            return;
        }
        getModule().requestTurnOnline(trainingPlanID, new BaseLoadingObserver<BaseResult>() {
            @Override
            public void onSuccessNext(BaseResult entity) {
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    getView().showTurnOnlineSuccess();
                } else {
                    getView().showTurnOnlineFailed();
                    ToastUtil.show(entity.msg);
                }
            }
        });

    }


    @Override
    protected TeacherDetailContract.TrainDetailModel createModule() {
        return new TeacherDetailModule();
    }

    @Override
    public void start() {

    }



}

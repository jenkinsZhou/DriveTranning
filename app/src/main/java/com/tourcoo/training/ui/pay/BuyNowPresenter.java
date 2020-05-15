package com.tourcoo.training.ui.pay;



import com.tourcoo.training.config.AppConfig;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.base.mvp.BasePresenter;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.entity.account.PayInfo;
import com.tourcoo.training.entity.pay.CoursePayInfo;



/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月07日14:47
 * @Email: 971613168@qq.com
 */
public class BuyNowPresenter extends BasePresenter<BuyNowContract.BuyNowModel, BuyNowContract.View> implements BuyNowContract.Presenter {

    @Override
    protected BuyNowContract.BuyNowModel createModule() {
        return new BuyNowModel();
    }



    @Override
    public void start() {
//        mProxyView.showIdCardInfo(getIdCardInfo());
    }


    @Override
    public void getPayInfo(String trainingPlanID) {
        if (!isViewAttached()) {
            return;
        }

        getModule().getPayinfo(trainingPlanID, new BaseLoadingObserver<BaseResult<CoursePayInfo>>() {
            @Override
            public void onSuccessNext(BaseResult<CoursePayInfo> entity) {
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    getView().getPayInfoSuccess(entity.getData());
                } else {
                    getView().payFailed(entity.msg);
                    ToastUtil.show(entity.msg);
                }
            }

            @Override
            public void onFailedNext(Throwable e) {
                super.onFailedNext(e);
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed(e.toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed(e.toString());
                }
            }
        });


    }

    @Override
    public void buyCourse(String trainingPlanID, int payType) {
        if (!isViewAttached()) {
            return;
        }

        getModule().setPayinfo(trainingPlanID, payType,new BaseLoadingObserver<BaseResult<PayInfo>>() {
            @Override
            public void onSuccessNext(BaseResult<PayInfo> entity) {
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    getView().setPayInfo(payType,entity.getData());
                } else {
                    ToastUtil.show(entity.msg);
                }
            }

            @Override
            public void onFailedNext(Throwable e) {
                super.onFailedNext(e);
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed(e.toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed(e.toString());
                }
            }
        });


    }


}

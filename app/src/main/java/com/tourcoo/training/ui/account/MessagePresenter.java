package com.tourcoo.training.ui.account;





import com.tourcoo.training.core.UiManager;
import com.tourcoo.training.core.base.entity.BaseMovieEntity;
import com.tourcoo.training.core.base.mvp.BasePresenter;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.ui.account.contract.MessageContract;
import com.tourcoo.training.ui.account.contract.MessageModel;

import java.util.ArrayList;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月13日17:34
 * @Email: 971613168@qq.com
 */
public class MessagePresenter extends BasePresenter<MessageContract.MessageModel, MessageContract.View> implements MessageContract.Presenter {


    @Override
    protected MessageContract.MessageModel createModule() {
        return new MessageModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void getMovie(int start, int count) {
        if (!isViewAttached()) {
            return;
        }
        getModule().getMovie(new BaseLoadingObserver<BaseMovieEntity>() {
            @Override
            public void onSuccessNext(BaseMovieEntity entity) {
                UiManager.getInstance().getHttpRequestControl().httpRequestSuccess(getView().getIHttpRequestControl(), entity == null || entity.subjects == null ? new ArrayList<>() : entity.subjects, null);
            }
        },start,count);
    }
}

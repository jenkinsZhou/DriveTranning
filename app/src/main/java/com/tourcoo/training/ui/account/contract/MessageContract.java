package com.tourcoo.training.ui.account.contract;


import com.tourcoo.training.core.base.entity.BaseMovieEntity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.base.mvp.IBaseModel;
import com.tourcoo.training.core.base.mvp.IBaseView;
import com.tourcoo.training.core.interfaces.IHttpRequestControl;
import com.tourcoo.training.core.retrofit.BaseObserver;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月13日17:34
 * @Email: 971613168@qq.com
 */
public interface MessageContract {


    interface MessageModel extends IBaseModel {
        void getMovie(BaseObserver<BaseMovieEntity> observer, int start, int count);
    }

    interface View extends IBaseView {
        IHttpRequestControl getIHttpRequestControl();

    }

    interface Presenter {
        void getMovie(int start, int count);
    }
}

package com.tourcoo.training.ui.home.news;

import com.tourcoo.training.core.base.entity.BasePageResult;
import com.tourcoo.training.core.base.mvp.IBaseModel;
import com.tourcoo.training.core.base.mvp.IBaseView;
import com.tourcoo.training.core.interfaces.IHttpRequestControl;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.entity.account.UserInfo;
import com.tourcoo.training.entity.news.NewsEntity;
import com.tourcoo.training.entity.training.TrainingPlanDetail;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月28日10:06
 * @Email: 971613168@qq.com
 */
public interface NewsListContract {

    interface NewsListModel extends IBaseModel {

        void requestNewsList(int page,BaseObserver<BasePageResult<NewsEntity>> observer);

    }

    interface NewsListView extends IBaseView {
        IHttpRequestControl getIHttpRequestControl();
    }

    interface NewsListPresenter {
        void getNewsList(int page);

    }
}

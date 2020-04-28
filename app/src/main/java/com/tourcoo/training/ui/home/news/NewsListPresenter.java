package com.tourcoo.training.ui.home.news;

import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.UiManager;
import com.tourcoo.training.core.base.entity.BasePageResult;
import com.tourcoo.training.core.base.mvp.BasePresenter;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.entity.news.NewsEntity;

import java.util.ArrayList;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月28日10:20
 * @Email: 971613168@qq.com
 */
public class NewsListPresenter extends BasePresenter<NewsListContract.NewsListModel, NewsListContract.NewsListView> implements NewsListContract.NewsListPresenter {


    @Override
    protected NewsListContract.NewsListModel createModule() {
        return new NewsModule();
    }

    @Override
    public void start() {
    }

    @Override
    public void getNewsList(int page) {
        if (!isViewAttached()) {
            return;
        }
        getModule().requestNewsList(page, new BaseLoadingObserver<BasePageResult<NewsEntity>>() {
            @Override
            public void onSuccessNext(BasePageResult<NewsEntity> entity) {
                if (entity == null) {
                    ToastUtil.show("数据有误");
                    return;
                }
                if (entity.getData() != null) {
                    UiManager.getInstance().getHttpRequestControl().httpRequestSuccess(getView().getIHttpRequestControl(), entity.getData().getRows()  == null ? new ArrayList<>() :entity.getData().getRows(), null);
                } else {
                    ToastUtil.show(entity.msg);
                }


            }
        });

    }
}

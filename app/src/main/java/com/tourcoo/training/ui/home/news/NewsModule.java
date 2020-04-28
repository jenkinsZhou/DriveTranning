package com.tourcoo.training.ui.home.news;

import com.tourcoo.training.core.base.entity.BasePageResult;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.entity.news.NewsEntity;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月28日10:14
 * @Email: 971613168@qq.com
 */
public class NewsModule implements NewsListContract.NewsListModel {
    @Override
    public void requestNewsList(int page, BaseObserver<BasePageResult<NewsEntity>> observer) {
        ApiRepository.getInstance().requestNewsList(page).subscribe(observer);
    }


}

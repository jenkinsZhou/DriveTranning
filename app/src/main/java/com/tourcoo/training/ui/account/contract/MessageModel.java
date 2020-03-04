package com.tourcoo.training.ui.account.contract;


import com.tourcoo.training.core.base.entity.BaseMovieEntity;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月15日18:23
 * @Email: 971613168@qq.com
 */
public class MessageModel implements MessageContract.MessageModel {


    @Override
    public void getMovie(BaseObserver<BaseMovieEntity> observer, int start, int count) {
        ApiRepository.getInstance().getMovie(start , count).
                subscribe(observer);
    }
}

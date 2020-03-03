package com.tourcoo.training.core.retrofit.repository;

import android.accounts.NetworkErrorException;


import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.config.RequestConfig;
import com.tourcoo.training.core.retrofit.CommonTransformer;
import com.tourcoo.training.core.retrofit.FrameNullException;
import com.tourcoo.training.core.retrofit.RetryWhen;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @Author: JenkinsZhou on 2018/10/10 17:24
 * @E-Mail: 971613168@qq.com
 * @Function: retrofit使用基类封装
 * @Description:
 */
public abstract class BaseRepository {

    /**
     * @param observable 用于解析 统一返回实体统一做相应的错误码--如登录失效
     * @param <T>
     * @return
     */
    protected <T> Observable<T> transform(Observable<BaseResult<T>> observable) {
        return CommonTransformer.switchSchedulers(
                observable.retryWhen(new RetryWhen())
                        .flatMap((Function<BaseResult<T>, ObservableSource<T>>) result -> {
                            if (result == null) {
                                return Observable.error(new NetworkErrorException());
                            } else {
                                if (result.getStatus() == RequestConfig.CODE_REQUEST_SUCCESS) {
                                    return result.getData() != null ? Observable.just(result.getData())
                                            : Observable.error(new FrameNullException());
                                } else {
                                    return Observable.error(new NetworkErrorException());
                                }
                            }
                        }));
    }
}

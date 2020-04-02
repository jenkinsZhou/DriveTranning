package com.tourcoo.training.core.base.mvp;


/**
 * @author :JenkinsZhou
 * @description :空presenter
 * @company :途酷科技
 * @date 2020年04月02日15:16
 * @Email: 971613168@qq.com
 */

public class NullPresenter extends BasePresenter<NullContract.NullModel, NullContract.NullView> implements NullContract.Presenter {


    @Override
    protected NullContract.NullModel createModule() {
        return new NullModule();
    }

    @Override
    public void start() {

    }
}

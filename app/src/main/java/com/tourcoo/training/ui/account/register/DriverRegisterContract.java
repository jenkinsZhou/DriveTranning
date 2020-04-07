package com.tourcoo.training.ui.account.register;

import com.tourcoo.training.core.base.mvp.IBaseModel;
import com.tourcoo.training.core.base.mvp.IBaseView;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :驾驶员注册Contract
 * @company :途酷科技
 * @date 2020年04月07日14:22
 * @Email: 971613168@qq.com
 */
public interface DriverRegisterContract {

    interface RegisterView extends IBaseView {
        List<String> getImages();

        void uploadImage(List<String> images);

        int getProgress();

    }

    interface RegisterModel extends IBaseModel {
        void recognizeIdCard();
    }

    interface RegisterPresenter {
        void selectImages();

        void uploadImage();
    }
}

package com.tourcoo.training.ui.training.trainingpackage;

import com.tourcoo.training.core.base.mvp.IBaseModel;
import com.tourcoo.training.core.base.mvp.IBaseView;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月28日14:11
 * @Email: 971613168@qq.com
 */
public interface TrainingPackageList {

    interface PackageModule  extends IBaseModel{

    }


    interface PackageView  extends IBaseView {

    }


    interface PackagePresenter   {
           void  getPackageList();
    }
}

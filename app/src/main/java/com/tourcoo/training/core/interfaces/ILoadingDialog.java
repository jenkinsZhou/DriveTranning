package com.tourcoo.training.core.interfaces;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.tourcoo.training.core.widget.dialog.LoadingDialogWrapper;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年12月26日16:39
 * @Email: 971613168@qq.com
 */
public interface ILoadingDialog {
    /**
     * 设置快速Loading CommonListDialog
     *
     * @param activity
     * @return
     */
    @Nullable
    LoadingDialogWrapper createLoadingDialog(@Nullable Activity activity);
}

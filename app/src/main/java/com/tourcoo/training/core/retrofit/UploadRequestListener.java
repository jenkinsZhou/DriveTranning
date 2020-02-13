package com.tourcoo.training.core.retrofit;

/**
 * @Author: JenkinsZhou on 2018/7/23 14:25
 * @E-Mail: JenkinsZhou@126.com
 * Function: 上传进度监听
 * Description:
 */
public interface UploadRequestListener {

    /**
     * 上传进度回调
     *
     * @param progress 进度
     * @param current  已上传字节数
     * @param total    总上传字节数
     */
    void onProgress(float progress, long current, long total);

    /**
     * 上传失败回调
     *
     * @param e 错误
     */
    void onFail(Throwable e);
}

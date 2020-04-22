package com.tourcoo.training.widget.player;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年04月20日23:19
 * @Email: 971613168@qq.com
 */
public interface OnPlayStatusListener {
    void onPlayComplete(int courseId);

    void onAutoPlayComplete(int courseId);

    void onPlayPause(int courseId);

    void onPlayResume(int courseId);
}

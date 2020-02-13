package com.tourcoo.training.core.widget.view.tab.listener;

/**
 * @Author: JenkinsZhou on 2018/12/3 13:19
 * @E-Mail: JenkinsZhou@126.com
 * @Function: tab 选中监听
 * @Description:
 */
public interface OnTabSelectListener {

    /**
     * tab首次选中
     *
     * @param position
     */
    void onTabSelect(int position);

    /**
     * tab选中状态再点击
     *
     * @param position
     */
    void onTabReselect(int position);
}
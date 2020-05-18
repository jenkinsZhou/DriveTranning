package com.tourcoo.training.event;

import com.tourcoo.training.entity.account.UserInfo;

/**
 * @author :JenkinsZhou
 * @description :专项相关页面刷新事件
 * @company :途酷科技
 * @date 2020/5/18 16:36
 * @Email: 971613168@qq.com
 */

public class ProfessionRefreshEvent {
    public int type;
    public UserInfo userInfo;

    public ProfessionRefreshEvent(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}

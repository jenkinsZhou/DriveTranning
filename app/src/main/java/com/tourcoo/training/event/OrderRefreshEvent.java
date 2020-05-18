package com.tourcoo.training.event;

import com.tourcoo.training.entity.account.UserInfo;

/**
 * @author :JenkinsZhou
 * @description :订单相关事件
 * @company :途酷科技
 * @date 2020/5/18 18:54
 * @Email: 971613168@qq.com
 */

public class OrderRefreshEvent {
    public int type;
    public UserInfo userInfo;

    public OrderRefreshEvent() {
    }

    public OrderRefreshEvent(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}

package com.tourcoo.training.entity.pay;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月09日10:57
 * @Email: 971613168@qq.com
 */
public class WxPayEvent {
    private int type;
    private boolean paySuccess;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getPaySuccess() {
        return paySuccess;
    }

    public void setPaySuccess(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }


    public WxPayEvent(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }
}

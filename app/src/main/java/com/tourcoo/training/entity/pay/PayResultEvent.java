package com.tourcoo.training.entity.pay;

/**
 * @author :JenkinsZhou
 * @description :支付结果回调
 * @company :途酷科技
 * @date 2020年05月10日14:51
 * @Email: 971613168@qq.com
 */
public class PayResultEvent {

    private boolean paySuccess;

    public PayResultEvent() {
    }

    public PayResultEvent(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }

    public boolean isPaySuccess() {
        return paySuccess;
    }

    public void setPaySuccess(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }
}

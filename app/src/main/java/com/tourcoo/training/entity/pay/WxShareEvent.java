package com.tourcoo.training.entity.pay;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月09日15:54
 * @Email: 971613168@qq.com
 */
public class WxShareEvent {
    private boolean shareSuccess;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isShareSuccess() {
        return shareSuccess;
    }

    public void setShareSuccess(boolean shareSuccess) {
        this.shareSuccess = shareSuccess;
    }

    public WxShareEvent(boolean shareSuccess) {
        this.shareSuccess = shareSuccess;
    }
}

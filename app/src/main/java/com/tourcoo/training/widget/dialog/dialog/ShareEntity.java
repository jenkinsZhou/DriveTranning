package com.tourcoo.training.widget.dialog.dialog;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月20日19:32
 * @Email: 971613168@qq.com
 */
public class ShareEntity {
    private String shareItemName;
    private int shareIcon;

    public ShareEntity(String shareItemName, int shareIcon) {
        this.shareItemName = shareItemName;
        this.shareIcon = shareIcon;
    }

    public String getShareItemName() {
        return shareItemName;
    }

    public void setShareItemName(String shareItemName) {
        this.shareItemName = shareItemName;
    }

    public int getShareIcon() {
        return shareIcon;
    }

    public void setShareIcon(int shareIcon) {
        this.shareIcon = shareIcon;
    }
}

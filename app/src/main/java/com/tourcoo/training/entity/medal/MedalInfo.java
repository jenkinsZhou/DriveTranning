package com.tourcoo.training.entity.medal;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月06日15:57
 * @Email: 971613168@qq.com
 */
public class MedalInfo implements MultiItemEntity {
    /*标题*/
    public final static int ITEM_TYPE_TITLE = 0;
    /*item*/
    public final static int ITEM_TYPE_CONTENT = 1;
    private int ID;
    private int Status;
    private boolean header;
    private int iconId;
    private int headerIcon;

    public int getHeaderIcon() {
        return headerIcon;
    }

    public void setHeaderIcon(int headerIcon) {
        this.headerIcon = headerIcon;
    }

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    @Override
    public int getItemType() {
        if (header) {
            return ITEM_TYPE_TITLE;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }


}

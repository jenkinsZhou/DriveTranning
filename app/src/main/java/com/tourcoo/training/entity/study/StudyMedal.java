package com.tourcoo.training.entity.study;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年03月05日23:37
 * @Email: 971613168@qq.com
 */
public class StudyMedal implements MultiItemEntity {

    /*标题*/
    public final static int ITEM_TYPE_TITLE = 0;
    /*item*/
    public final static int ITEM_TYPE_CONTENT = 1;

    public boolean isHeader ;

    public boolean isLock;
    @Override
    public int getItemType() {
        return itemType;
    }

public int itemType;
    public int iconId;

    public String medalDesc;

    public String groupName;
}

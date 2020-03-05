package com.tourcoo.training.entity.study;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年03月05日23:30
 * @Email: 971613168@qq.com
 */
public class StudyMedalGroup implements MultiItemEntity {

    /*标题*/
    public final static int ITEM_TYPE_TITLE = 0;
    /*item*/
    public final static int ITEM_TYPE_CONTENT = 1;

    public  String groupName;

    public List<StudyMedal> medalList;

    @Override
    public int getItemType() {
        return 0;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<StudyMedal> getMedalList() {
        return medalList;
    }

    public void setMedalList(List<StudyMedal> medalList) {
        this.medalList = medalList;
    }
}

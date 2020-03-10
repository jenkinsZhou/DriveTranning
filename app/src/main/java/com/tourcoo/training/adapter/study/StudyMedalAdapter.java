package com.tourcoo.training.adapter.study;


import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.entity.study.StudyMedal;

import java.util.List;

import static com.tourcoo.training.entity.study.StudyMedalGroup.ITEM_TYPE_CONTENT;
import static com.tourcoo.training.entity.study.StudyMedalGroup.ITEM_TYPE_HEADER;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年03月05日23:29
 * @Email: 971613168@qq.com
 */
public class StudyMedalAdapter extends BaseMultiItemQuickAdapter<StudyMedal, BaseViewHolder> {

    public StudyMedalAdapter(List<StudyMedal> data) {
        super(data);
        //标题布局
        addItemType(ITEM_TYPE_HEADER, R.layout.item_study_medel_title);
        //内容布局
        addItemType(ITEM_TYPE_CONTENT, R.layout.item_study_medal);
    }

    @Override
    public int getItemViewType(int position) {
        if(getData().get(position).isHeader){
            return ITEM_TYPE_HEADER;
        }else {
            return ITEM_TYPE_CONTENT;
        }

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, StudyMedal item) {
        switch (helper.getItemViewType()) {
            case ITEM_TYPE_HEADER:
                helper.setText(R.id.tvMedalGroupName, item.groupName);
                break;
            case ITEM_TYPE_CONTENT:
                helper.setText(R.id.tvMedalDesc, item.medalDesc);
                break;
        }
    }

}

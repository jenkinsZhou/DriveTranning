package com.tourcoo.training.adapter.study;


import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.entity.medal.MedalInfo;
import com.tourcoo.training.entity.study.StudyMedal;
import com.tourcoo.training.widget.aliplayer.utils.Common;

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
public class StudyMedalAdapter extends BaseMultiItemQuickAdapter<MedalInfo, BaseViewHolder> {

    public StudyMedalAdapter(List<MedalInfo> data) {
        super(data);
        //标题布局
        addItemType(ITEM_TYPE_HEADER, R.layout.item_study_medel_title);
        //内容布局
        addItemType(ITEM_TYPE_CONTENT, R.layout.item_study_medal);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MedalInfo item) {
        switch (helper.getItemViewType()) {
            case ITEM_TYPE_HEADER:
                ImageView ivHeaderIcon = helper.getView(R.id.ivHeaderIcon);
                ivHeaderIcon.setImageResource(item.getHeaderIcon());
                helper.setText(R.id.tvMedalGroupName, CommonUtil.getNotNullValue(item.getTitle()));
                break;
            case ITEM_TYPE_CONTENT:
                ImageView ivMedalIcon = helper.getView(R.id.ivMedalIcon);
                helper.setText(R.id.tvMedalDesc, CommonUtil.getNotNullValue(item.getDesc()));
                ivMedalIcon.setImageResource(item.getIconId());
                break;
        }
    }

}

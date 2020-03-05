package com.tourcoo.training.adapter.study;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;
import com.tourcoo.training.entity.study.StudyMedal;
import com.tourcoo.training.entity.study.StudyMedalGroup;

import java.util.List;

import static com.tourcoo.training.entity.study.StudyMedalGroup.ITEM_TYPE_CONTENT;
import static com.tourcoo.training.entity.study.StudyMedalGroup.ITEM_TYPE_TITLE;

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
        addItemType(ITEM_TYPE_TITLE, R.layout.item_study_medel_title);
        //内容布局
        addItemType(ITEM_TYPE_CONTENT, R.layout.item_study_medal);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, StudyMedal item) {
        switch (helper.getItemViewType()) {
            case ITEM_TYPE_TITLE:
                helper.setText(R.id.tvMedalGroupName, item.groupName);
                break;
            case ITEM_TYPE_CONTENT:
                helper.setText(R.id.tvMedalDesc, item.medalDesc);
               /* ImageView ivModuleStatus = helper.getView(R.id.ivModuleStatus);
                helper.addOnClickListener(R.id.ivModuleStatus);
                ImageView ivModuleIcon = helper.getView(R.id.ivModuleIcon);
                GlideManager.loadRoundImg(item.getIcon(), ivModuleIcon, 5);
                HomeChildItem childItem = item.getChildList().get(helper.getLayoutPosition());*/
               /* if (childItem.isParentGroup()) {
                    //全部应用
                    if (childItem.isSelect()) {
                        ivModuleStatus.setImageResource(R.mipmap.icon_app_select);
                    } else {
                        ivModuleStatus.setImageResource(R.mipmap.icon_app_add);
                    }
                } else {
                    ivModuleStatus.setImageResource(R.mipmap.icon_app_remove);
                }*/


                break;
        }
    }

}

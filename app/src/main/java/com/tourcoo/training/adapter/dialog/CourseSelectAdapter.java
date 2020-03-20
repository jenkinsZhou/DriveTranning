package com.tourcoo.training.adapter.dialog;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.util.ResourceUtil;
import com.tourcoo.training.entity.course.CourseEntity;


/**
 * @author :JenkinsZhou
 * @description :课程选择
 * @company :途酷科技
 * @date 2020年03月18日10:17
 * @Email: 971613168@qq.com
 */
public class CourseSelectAdapter extends BaseQuickAdapter<CourseEntity, BaseViewHolder> {


    public CourseSelectAdapter() {
        super(R.layout.item_dialog_course_select);
    }
    @Override
    protected void convert(@NonNull BaseViewHolder helper, CourseEntity item) {
        if(item == null){
            return;
        }
        helper.setText(R.id.tvItemName,item.getCourseDurationDesc());
        ImageView ivItemSelect =  helper.getView(R.id.ivItemSelect);
        ImageView ivItemIcon =  helper.getView(R.id.ivItemIcon);

        if(item.isSelect()){
            ivItemSelect.setImageResource(R.mipmap.ic_select_small_blue);
            ivItemIcon.setImageResource(R.mipmap.ic_class_type_select);
        }else {
            ivItemSelect.setImageDrawable(ResourceUtil.getDrawable(R.drawable.shape_circle_white_hollow_gray_bfbfbf));
            ivItemIcon.setImageResource(R.mipmap.ic_class_type);
        }
    }
}

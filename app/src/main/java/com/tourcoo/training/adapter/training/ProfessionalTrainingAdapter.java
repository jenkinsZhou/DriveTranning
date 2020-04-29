package com.tourcoo.training.adapter.training;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.entity.course.CourseInfo;
import com.tourcoo.training.entity.training.ProfessionTrainingEntity;

/**
 * @author :JenkinsZhou
 * @description : 现场培训
 * @company :途酷科技
 * @date 2020年03月10日23:05
 * @Email: 971613168@qq.com
 */
public class ProfessionalTrainingAdapter extends BaseQuickAdapter<ProfessionTrainingEntity, BaseViewHolder> {


    public ProfessionalTrainingAdapter() {
        super(R.layout.item_training_professional);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ProfessionTrainingEntity item) {
        GlideManager.loadImg(item.getCoverUrl(), helper.getView(R.id.ivCover),R.drawable.ic_rect_default);
    }

}

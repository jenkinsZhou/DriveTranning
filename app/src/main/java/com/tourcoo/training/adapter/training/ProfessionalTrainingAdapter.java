package com.tourcoo.training.adapter.training;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.entity.training.ProfessionTrainingEntity;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年03月10日23:05
 * @Email: 971613168@qq.com
 */
public class ProfessionalTrainingAdapter extends BaseQuickAdapter<ProfessionTrainingEntity, BaseViewHolder> {

    public ProfessionalTrainingAdapter() {
        super(R.layout.item_tab_training_professional);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ProfessionTrainingEntity item) {

    }

   /* @Override
    protected void convert(@NonNull BaseViewHolder helper, String url) {
        ImageView ivNewsImage =  helper.getView(R.id.ivNewsImage);
        GlideManager.loadImageAuto(url,ivNewsImage);
    }*/
}

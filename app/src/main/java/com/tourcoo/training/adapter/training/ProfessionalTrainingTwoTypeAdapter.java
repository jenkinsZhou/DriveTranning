package com.tourcoo.training.adapter.training;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;
import com.tourcoo.training.entity.training.ProfessionTrainingEntity;
import com.tourcoo.training.entity.training.ProfessionalTwoTypeModel;

/**
 * @author :JenkinsZhou
 * @description : 现场培训
 * @company :途酷科技
 * @date 2020年03月10日23:05
 * @Email: 971613168@qq.com
 */
public class ProfessionalTrainingTwoTypeAdapter extends BaseQuickAdapter<ProfessionalTwoTypeModel, BaseViewHolder> {


    public ProfessionalTrainingTwoTypeAdapter() {
        super(R.layout.item_professional_training_two_type);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ProfessionalTwoTypeModel item) {
        int index = helper.getAdapterPosition() % 3;
        switch (index) {
            case 0:
                helper.setTextColor(R.id.btnSubmit, Color.parseColor("#50AAFE"));
                helper.getView(R.id.llRoot).setBackgroundResource(R.mipmap.ic_bg_training_item_blue);
                break;
            case 1:
                helper.setTextColor(R.id.btnSubmit, Color.parseColor("#9D7BFE"));
                helper.getView(R.id.llRoot).setBackgroundResource(R.mipmap.ic_bg_training_item_purple);
                break;
            case 2:
                helper.setTextColor(R.id.btnSubmit, Color.parseColor("#FB7494"));
                helper.getView(R.id.llRoot).setBackgroundResource(R.mipmap.ic_bg_training_item_red);
                break;
        }

        helper.setText(R.id.tvTitle,item.getTitle());
        helper.setText(R.id.tvDetails,item.getSubTitle());
        helper.setText(R.id.tvCoins,"学币："+item.getCoins());

        if(item.getStatus() == 1){ //已购买
            helper.setText(R.id.btnSubmit,"立即学习");
        }else {
            helper.setText(R.id.btnSubmit,"立即购买");
        }

        helper.addOnClickListener(R.id.btnSubmit);

    }

}

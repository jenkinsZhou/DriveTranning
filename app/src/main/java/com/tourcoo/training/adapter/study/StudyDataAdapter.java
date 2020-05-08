package com.tourcoo.training.adapter.study;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.entity.study.StudyDataEntity;
import com.tourcoo.training.entity.study.StudyDataInfo;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月08日9:44
 * @Email: 971613168@qq.com
 */
public class StudyDataAdapter extends BaseQuickAdapter<StudyDataInfo, BaseViewHolder> {
    public StudyDataAdapter() {
        super(R.layout.item_study_data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, StudyDataInfo item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tvStudyDate, CommonUtil.getNotNullValue(item.getTime()));
        helper.setText(R.id.tvStudyLength, CommonUtil.getNotNullValue(item.getStudyHour() + "小时"));
    }
}

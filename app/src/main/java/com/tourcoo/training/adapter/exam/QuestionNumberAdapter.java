package com.tourcoo.training.adapter.exam;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.entity.exam.ExaminationEntityOld;
import com.tourcoo.training.entity.exam.Question;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年03月12日23:09
 * @Email: 971613168@qq.com
 */
public class QuestionNumberAdapter extends BaseQuickAdapter<Question, BaseViewHolder> {


    public QuestionNumberAdapter() {
        super(R.layout.item_question_number);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Question item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tvQuestionNum, "" + item.getQuestionNumber());
    }
}

package com.tourcoo.training.adapter.exam;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.constant.ExamConstant;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.ResourceUtil;
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
        TextView tvQuestionNum = helper.getView(R.id.tvQuestionNum);
        tvQuestionNum.setText(item.getQuestionNumber());
        if(item.isCurrentShow()){
            tvQuestionNum.setBackground(ResourceUtil.getDrawable(R.drawable.shape_circle_blue_5087ff_hollow));
            tvQuestionNum.setTextColor(CommonUtil.getColor(R.color.blue5087FF));
        }else {
            switch (item.getAnswerStatus()) {
                case ExamConstant.STATUS_ANSWER_RIGHT:
                    tvQuestionNum.setBackground(ResourceUtil.getDrawable(R.drawable.shape_circle_green_67c23a));
                    tvQuestionNum.setTextColor(CommonUtil.getColor(R.color.white));
                    break;
                case ExamConstant.STATUS_ANSWER_WRONG:
                    tvQuestionNum.setBackground(ResourceUtil.getDrawable(R.drawable.shape_circle_red_d8e106));
                    tvQuestionNum.setTextColor(CommonUtil.getColor(R.color.white));
                    break;
                case ExamConstant.STATUS_NO_ANSWER:
                    tvQuestionNum.setBackground(ResourceUtil.getDrawable(R.drawable.shape_circle_gray_a2a2a2_hollow));
                    tvQuestionNum.setTextColor(CommonUtil.getColor(R.color.grayA2A2A2));
                    break;
            }
        }

    }
}

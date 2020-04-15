package com.tourcoo.training.adapter.exam;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.util.ResourceUtil;
import com.tourcoo.training.entity.exam.Answer;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月12日9:31
 * @Email: 971613168@qq.com
 */
public class QuestionAdapter extends BaseQuickAdapter<Answer, BaseViewHolder> {

    public QuestionAdapter() {
        super(R.layout.item_examnation);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Answer item) {
        TextView tvAnswerLabel = helper.getView(R.id.tvAnswerLabel);
        tvAnswerLabel.setBackgroundResource(R.drawable.shape_circle_white_dp_28);
        tvAnswerLabel.setText(item.getAnswerId());
        TextView tvAnswer = helper.getView(R.id.tvAnswer);
        tvAnswer.setText(item.getAnswerName());
       if(item.isHasAnswered()){
           //已经回答过 先判断当前答案是否被选中
           if(item.isSelect()){
               //答案被选中 再判断选中的这个是否是正确答案
                if(item.isCorrectAnswer()){
                    //显示答题正确
                    showAnswerCorrect(tvAnswerLabel,tvAnswer);
                }else {
                    //显示答题错误
                    showWrong(tvAnswerLabel,tvAnswer);
                }
           }else {
                //未选中
                //当前答案 需要判断当前是否是正确的答案
               if(item.isCorrectAnswer()){
                   //显示正确图标
                   showCorrect(tvAnswerLabel,tvAnswer);
               }else {
                   //显示答题默认图标
                   showUnSelect(item,tvAnswerLabel,tvAnswer);
               }
           }
       }else {
           //未回答过
           if(item.isSelect()){
               //选中 显示选中图标
               showSelect(item,tvAnswerLabel,tvAnswer);
           }else {
               //未选中 显示默认
               showUnSelect(item,tvAnswerLabel,tvAnswer);
           }
       }
    }

    private void showWrong(TextView labelTextView,TextView textView){
//        GlideManager.loadImg(R.mipmap.ic_answer_wrong,imageView);
        labelTextView.setBackgroundResource(R.mipmap.ic_answer_wrong);
        labelTextView.setText("");
        textView.setTextColor(ResourceUtil.getColor(R.color.redF35757));
    }

    private void showAnswerCorrect(TextView labelTextView,TextView textView){
        labelTextView.setBackgroundResource(R.mipmap.ic_answer_correct);
        labelTextView.setText("");
//        GlideManager.loadImg(R.mipmap.ic_answer_correct,imageView);
        textView.setTextColor(ResourceUtil.getColor(R.color.green42AF3D));
    }

    private void showCorrect(TextView labelTextView,TextView textView){
        labelTextView.setBackgroundResource(R.mipmap.ic_answer_correct_blue);
        labelTextView.setText("");
//        GlideManager.loadImg(R.mipmap.ic_answer_correct,imageView);
        textView.setTextColor(ResourceUtil.getColor(R.color.blue007AFF));
    }

    private void showUnSelect(Answer answer, TextView labelTextView, TextView textView){
        textView.setTextColor(ResourceUtil.getColor(R.color.black333333));
        labelTextView.setBackgroundResource(R.drawable.ic_answer_bg_white);
        labelTextView.setTextColor(ResourceUtil.getColor(R.color.black333333));
        labelTextView.setText(answer.getAnswerId());
    }

    private void showSelect(Answer answer, TextView labelTextView, TextView textView){
//        GlideManager.loadImg(answer.getSelectedIcon(),imageView);
        labelTextView.setBackgroundResource(R.drawable.ic_answer_bg_blue);
        labelTextView.setText(answer.getAnswerId());
        labelTextView.setTextColor(ResourceUtil.getColor(R.color.white));
        textView.setTextColor(ResourceUtil.getColor(R.color.black333333));
    }
}

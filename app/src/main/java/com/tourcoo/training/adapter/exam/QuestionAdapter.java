package com.tourcoo.training.adapter.exam;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;
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
        ImageView ivAnswer = helper.getView(R.id.ivAnswer);
        TextView tvAnswer = helper.getView(R.id.tvAnswer);
        tvAnswer.setText(item.getAnswerContent());
       if(item.isHasAnswered()){
           //已经回答过 先判断当前答案是否被选中
           if(item.isSelected()){
               //答案被选中 再判断选中的这个是否是正确答案
                if(item.isCorrectAnswer()){
                    //显示答题正确
                    showAnswerCorrect(ivAnswer,tvAnswer);
                }else {
                    //显示答题错误
                    showWrong(ivAnswer,tvAnswer);
                }
           }else {
                //未选中
                //当前答案 需要判断当前是否是正确的答案
               if(item.isCorrectAnswer()){
                   //显示正确图标
                   showCorrect(ivAnswer,tvAnswer);
               }else {
                   //显示答题默认图标
                   showUnSelect(item,ivAnswer,tvAnswer);
               }
           }
       }else {
           //未回答过
           if(item.isSelected()){
               //选中 显示选中图标
               showSelect(item,ivAnswer,tvAnswer);
           }else {
               //未选中 显示默认
               showUnSelect(item,ivAnswer,tvAnswer);
           }
       }
    }

    private void showWrong(ImageView imageView,TextView textView){
//        GlideManager.loadImg(R.mipmap.ic_answer_wrong,imageView);
        imageView.setImageResource(R.mipmap.ic_answer_wrong);
        textView.setTextColor(ResourceUtil.getColor(R.color.redF35757));
    }

    private void showAnswerCorrect(ImageView imageView,TextView textView){
        imageView.setImageResource(R.mipmap.ic_answer_correct);
//        GlideManager.loadImg(R.mipmap.ic_answer_correct,imageView);
        textView.setTextColor(ResourceUtil.getColor(R.color.green42AF3D));
    }

    private void showCorrect(ImageView imageView,TextView textView){
        imageView.setImageResource(R.mipmap.ic_answer_correct_blue);
//        GlideManager.loadImg(R.mipmap.ic_answer_correct,imageView);
        textView.setTextColor(ResourceUtil.getColor(R.color.blue007AFF));
    }

    private void showUnSelect(Answer answer,ImageView imageView,TextView textView){
        textView.setTextColor(ResourceUtil.getColor(R.color.black333333));
        imageView.setImageResource(answer.getUnSelectedIcon());
    }

    private void showSelect(Answer answer,ImageView imageView,TextView textView){
//        GlideManager.loadImg(answer.getSelectedIcon(),imageView);
        imageView.setImageResource(answer.getSelectedIcon());
        textView.setTextColor(ResourceUtil.getColor(R.color.black333333));
    }
}

package com.tourcoo.training.adapter.study;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.ResourceUtil;
import com.tourcoo.training.entity.study.StudyRecord;

import java.util.List;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月07日11:30
 * @Email: 971613168@qq.com
 */
public class StudyRecordAdapter extends BaseMultiItemQuickAdapter<StudyRecord, BaseViewHolder> {
    /*标题*/
    public final static int ITEM_TYPE_HEADER = 0;
    /*item*/
    public final static int ITEM_TYPE_CONTENT = 1;

    public StudyRecordAdapter(List<StudyRecord> data) {
        super(data);
        //标题布局
        addItemType(ITEM_TYPE_HEADER, R.layout.item_study_record_header);
        //内容布局
        addItemType(ITEM_TYPE_CONTENT, R.layout.item_study_record);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, StudyRecord item) {
        if (getData().isEmpty()) {
            return;
        }
        switch (helper.getItemViewType()) {
            case ITEM_TYPE_HEADER:
                helper.setText(R.id.tvTrainDate, CommonUtil.getNotNullValue(item.getTitle()));
                break;
            case ITEM_TYPE_CONTENT:
                ImageView ivStudyTag = helper.getView(R.id.ivStudyTag);
                TextView tvExamScore = helper.getView(R.id.tvExamScore);
                helper.setText(R.id.tvTrainName, CommonUtil.getNotNullValue(item.getTrainingPlanName()));
                boolean isOffline = item.getIsOnlineLearning() == 0;
                if (isOffline) {
                    helper.setText(R.id.tvTrainType, "现场培训");
                } else {
                    helper.setText(R.id.tvTrainType, "线上培训");
                }
                String date = CommonUtil.getNotNullValue(item.getTrainingPlanStartTime());
                date += "—" + CommonUtil.getNotNullValue(item.getTrainingPlanEndTime());
                helper.setText(R.id.tvTrainDate, date);
                helper.addOnClickListener(R.id.llLookMore);
//                "TrainingPlanStatus":  学习状态(0 未开始, 1 进行时,2 已结束, 3 待考试),
                switch (item.getTrainingPlanStatus()) {
                    case 0:
                        ivStudyTag.setImageResource(R.mipmap.ic_training_state_no_start);
                        break;
                    case 1:
                        ivStudyTag.setImageResource(R.mipmap.ic_training_state_doing);
                        break;
                    case 2:
                        ivStudyTag.setImageResource(R.mipmap.ic_training_state_end);
                        break;
                    case 3:
                        ivStudyTag.setImageResource(R.mipmap.ic_training_state_wait_exam);
                        break;
                    default:
                        break;
                }
                helper.setText(R.id.tvTrainName, CommonUtil.getNotNullValue(item.getTrainingPlanName()));
                tvExamScore.setText(CommonUtil.getNotNullValue(item.getExamScore()));
                tvExamScore.setTextColor(ResourceUtil.getColor(R.color.green69C33C));
                //折叠
                if (item.isFolding()) {
                    //隐藏考试成绩
                    helper.setGone(R.id.llExamScore, false);
                    //隐藏培训时间
                    helper.setGone(R.id.llTrainDate, false);
                    //显示查看更多
                    helper.setGone(R.id.llLookMore, true);
                    helper.setGone(R.id.ivStudyTag, false);
                } else {
                    //显示考试成绩
                    helper.setGone(R.id.llExamScore, true);
                    //显示培训时间
                    helper.setGone(R.id.llTrainDate, true);
                    //隐藏查看更多
                    helper.setGone(R.id.llLookMore, false);
                    helper.setGone(R.id.ivStudyTag, true);
                }
                break;
        }
    }


    public String getNotNullValue(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        return number;
    }

    @Override
    public int getItemViewType(int position) {
        if (getData().isEmpty()) {
            return super.getItemViewType(position);
        }
        if (getData().get(position).isHeader()) {
            return ITEM_TYPE_HEADER;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }
}

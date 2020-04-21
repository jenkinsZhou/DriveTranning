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
public class ProfessionalTrainingAdapter extends BaseQuickAdapter<CourseInfo, BaseViewHolder> {


    public static final int COURSE_STATUS_NOT_START = 0;
    public static final int COURSE_STATUS_ALREADY_SIGN = 1;
    public static final int COURSE_STATUS_ALREADY_SIGN_OUT = 2;
    public static final int COURSE_STATUS_CONVERT_ONLINE = 3;
    public static final int COURSE_STATUS_FINISHED = 4;
    public static final int COURSE_STATUS_UNRIGHT = 5;
    public static final int COURSE_STATUS_ALREADY_TEST = 6;

    public ProfessionalTrainingAdapter() {
        super(R.layout.item_training_safe_locale);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CourseInfo item) {

        helper.setText(R.id.tvCourseTimeRange, CommonUtil.getNotNullValue(item.getTimeRange()));
        helper.setText(R.id.tvCourseTitle, CommonUtil.getNotNullValue(item.getTitle()));
        helper.setText(R.id.tvAddress, CommonUtil.getNotNullValue(item.getAddress()));

        ImageView ivCourseStatus = helper.getView(R.id.ivCourseStatus);
        switch (item.getStatus()) {
            case COURSE_STATUS_NOT_START:
                ivCourseStatus.setImageResource(R.mipmap.ic_training_state_no_start);
                break;
            case COURSE_STATUS_ALREADY_SIGN:
                ivCourseStatus.setImageResource(R.mipmap.ic_training_state_signed);
                break;
            case COURSE_STATUS_ALREADY_SIGN_OUT:
                ivCourseStatus.setImageResource(R.mipmap.ic_training_state_sign_back);
                break;
            case COURSE_STATUS_CONVERT_ONLINE:
                ivCourseStatus.setImageResource(R.mipmap.ic_training_state_turn_online);
                break;
            case COURSE_STATUS_FINISHED:
                ivCourseStatus.setImageResource(R.mipmap.ic_training_state_end);
                break;
            case COURSE_STATUS_UNRIGHT:
                ivCourseStatus.setImageResource(R.mipmap.ic_training_state_no_pass);
                break;
            case COURSE_STATUS_ALREADY_TEST:
                ivCourseStatus.setImageResource(R.mipmap.ic_training_state_already_test);
                break;
        }

    }

}

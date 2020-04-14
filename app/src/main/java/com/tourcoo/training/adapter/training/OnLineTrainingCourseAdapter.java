package com.tourcoo.training.adapter.training;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.entity.course.CourseInfo;

/**
 * @author :JenkinsZhou
 * @description :线上培训
 * @company :途酷科技
 * @date 2020年04月13日14:56
 * @Email: 971613168@qq.com
 */
public class OnLineTrainingCourseAdapter extends BaseQuickAdapter<CourseInfo, BaseViewHolder> {
    public static final int COURSE_STATUS_FINISHED = 0;
    public static final int COURSE_STATUS_CONTINUE = 1;
    public static final int COURSE_STATUS_NEED_PAY = 2;
    public static final int COURSE_STATUS_WAIT_EXAM = 3;
  /*  case finished = 0
            case continues = 1
            case immdiate = 2
            case preText = 3*/
    public OnLineTrainingCourseAdapter() {
        super(R.layout.item_tab_training_work_before);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CourseInfo item) {
        ImageView ivVideoThumbnail = helper.getView(R.id.ivVideoThumbnail);
        GlideManager.loadImg(CommonUtil.getUrl(item.getCoverUrl()), ivVideoThumbnail, R.drawable.img_training_free_video);
        helper.setText(R.id.tvCourseTimeRange, CommonUtil.getNotNullValue(item.getTimeRange()));
        helper.setText(R.id.tvCourseTitle, CommonUtil.getNotNullValue(item.getTitle()));

        ImageView ivCourseStatus = helper.getView(R.id.ivCourseStatus);
        switch (item.getStatus()) {
            case COURSE_STATUS_FINISHED:
                ivCourseStatus.setImageResource(R.mipmap.ic_course_status_finish);
                break;
            case COURSE_STATUS_CONTINUE:
                ivCourseStatus.setImageResource(R.mipmap.ic_course_status_continue);
                break;
            case COURSE_STATUS_NEED_PAY:
                ivCourseStatus.setImageResource(R.mipmap.ic_course_status_need_pay);
                break;
            case COURSE_STATUS_WAIT_EXAM:
                ivCourseStatus.setImageResource(R.mipmap.ic_course_status_wait_exam);
                break;
        }
    }





}

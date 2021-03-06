package com.tourcoo.training.adapter.training;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.ResourceUtil;
import com.tourcoo.training.entity.course.CourseInfo;
import com.tourcoo.training.widget.aliplayer.utils.Common;

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

        if (StringUtils.isEmpty(item.getCoverUrl())) {
            GlideManager.loadGrayImg(CommonUtil.getUrl(item.getCoverUrl()), ivVideoThumbnail, R.drawable.ic_rect_default);
        } else {
            GlideManager.loadImg(CommonUtil.getUrl(item.getCoverUrl()), ivVideoThumbnail, R.drawable.ic_rect_default);
        }

        helper.setText(R.id.tvCourseTimeRange, CommonUtil.getNotNullValue(item.getTimeRange()));
        helper.setText(R.id.tvCourseTitle, CommonUtil.getNotNullValue(item.getTitle()));
        ProgressBar progressBarOnLine = helper.getView(R.id.progressBarOnLine);

        switch (item.getTag()) {
            case 0:
                helper.setVisible(R.id.ivTag, false);
                break;
            case 1:
                helper.setVisible(R.id.ivTag, true);
                helper.setImageResource(R.id.ivTag, R.drawable.icon_video_free);
                break;
            case 2:
                helper.setVisible(R.id.ivTag, true);
                helper.setImageResource(R.id.ivTag, R.mipmap.icon_video_new_tag);
                break;
            default:
                helper.setVisible(R.id.ivTag, false);
                break;
        }

        if (item.getType() == 3 || item.getType() == 4) {
            helper.setVisible(R.id.ivVideoTag, false);
        } else {
            helper.setVisible(R.id.ivVideoTag, true);
        }

        TextView tvCourseTitle = helper.getView(R.id.tvCourseTitle);
        ImageView ivCourseStatus = helper.getView(R.id.ivCourseStatus);
        TextView tvCourseTimeRange = helper.getView(R.id.tvCourseTimeRange);
        helper.setText(R.id.tvStudyProgress, "学习进度" + CommonUtil.doubleTransStringZhen(item.getProgress()) + "%");
        switch (item.getStatus()) {
            case COURSE_STATUS_FINISHED:
                progressBarOnLine.setVisibility(View.GONE);
                ivCourseStatus.setImageResource(R.mipmap.ic_course_status_finish);
                tvCourseTitle.setTextColor(ResourceUtil.getColor(R.color.gray999999));
                tvCourseTimeRange.setTextColor(ResourceUtil.getColor(R.color.grayB6B6B6));
                helper.setGone(R.id.llStudyProgress, false);
                helper.setGone(R.id.tvCourseTimeRange, true);
                break;
            case COURSE_STATUS_CONTINUE:
                progressBarOnLine.setVisibility(View.VISIBLE);
                progressBarOnLine.setProgress((int) item.getProgress());
                ivCourseStatus.setImageResource(R.mipmap.ic_course_status_continue);
                helper.setGone(R.id.llStudyProgress, true);
                helper.setGone(R.id.tvCourseTimeRange, false);
                tvCourseTitle.setTextColor(ResourceUtil.getColor(R.color.black333333));
                tvCourseTimeRange.setTextColor(ResourceUtil.getColor(R.color.gray999999));
                break;
            case COURSE_STATUS_NEED_PAY:
                progressBarOnLine.setVisibility(View.GONE);
                ivCourseStatus.setImageResource(R.mipmap.ic_course_status_need_pay);
                helper.setGone(R.id.llStudyProgress, false);
                helper.setGone(R.id.tvCourseTimeRange, true);
                tvCourseTitle.setTextColor(ResourceUtil.getColor(R.color.black333333));
                tvCourseTimeRange.setTextColor(ResourceUtil.getColor(R.color.gray999999));
                break;
            case COURSE_STATUS_WAIT_EXAM:
                progressBarOnLine.setVisibility(View.GONE);
                ivCourseStatus.setImageResource(R.mipmap.ic_course_status_wait_exam);
                helper.setGone(R.id.llStudyProgress, false);
                helper.setGone(R.id.tvCourseTimeRange, true);
                tvCourseTitle.setTextColor(ResourceUtil.getColor(R.color.black333333));
                tvCourseTimeRange.setTextColor(ResourceUtil.getColor(R.color.gray999999));
                break;
        }
    }


}

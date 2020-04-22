package com.tourcoo.training.ui.training.safe.online.detail.student

import android.os.Bundle
import android.text.TextUtils
import com.tourcoo.training.R
import com.tourcoo.training.constant.TrainingConstant.*
import com.tourcoo.training.constant.UserConstant.*
import com.tourcoo.training.core.base.mvp.BaseMvpTitleActivity
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.training.TrainingPlanDetail
import kotlinx.android.synthetic.main.activity_training_detail_student.*
import kotlinx.android.synthetic.main.activity_training_detail_teacher.tvCourseName
import kotlinx.android.synthetic.main.activity_training_detail_teacher.tvCoursePlanTime
import kotlinx.android.synthetic.main.activity_training_detail_teacher.tvLocate
import kotlinx.android.synthetic.main.activity_training_detail_teacher.tvRole

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月22日12:39
 * @Email: 971613168@qq.com
 */
class StudentPlanDetailActivity : BaseMvpTitleActivity<StudentDetailPresenter>(), StudentDetailContract.View {
    private var trainingPlanId = ""
    override fun loadPresenter() {
        presenter.start()
        presenter.getTrainDetail(trainingPlanId)
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_training_detail_student
    }

    override fun createPresenter(): StudentDetailPresenter {
        return StudentDetailPresenter()
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("现场培训")
    }

    override fun initView(savedInstanceState: Bundle?) {
        trainingPlanId = intent.getStringExtra(EXTRA_TRAINING_PLAN_ID)
        if (TextUtils.isEmpty(trainingPlanId)) {
            ToastUtil.show("未获取培训计划")
            finish()
            return
        }


    }


    override fun doShowTrainPlan(planDetail: TrainingPlanDetail?) {
        showTrainPlan(planDetail)
    }

    private fun showTrainPlan(planDetail: TrainingPlanDetail?) {
        if (planDetail == null) {
            return
        }
        tvCourseName.text = CommonUtil.getNotNullValue(planDetail.title)
        tvCoursePlanTime.text = CommonUtil.getNotNullValue(planDetail.cTime)
        tvLocate.text = CommonUtil.getNotNullValue(planDetail.classroomName)
        tvRole.text = "学员"
            //todo
//        planDetail.status = 5
        when (planDetail.status)
            /**
             * 未开始
             */
        {
            TRAIN_STATUS_NO_START -> {
                //未开始 只有转线上按钮
                //todo
                setViewGone(rlButtonLayout, true)
                //转线上按钮
                setViewGone(ivStudentToOnline, true)
                //签到按钮
                setViewGone(ivSignStudent, false)
                //扫码按钮
                setViewGone(ivScanCode, false)
                //签退按钮
                setViewGone(ivSignOut, false)
                //签到相关信息
                setViewGone(llTrainStatusLayout, false)
                //底部按钮信息
                setViewGone(llBottomButtonLayout, false)

            }

            /**
             * 已签到
             */
            TRAIN_STATUS_SIGNED -> {
                //转线上按钮
                setViewGone(ivStudentToOnline, true)
                //签到按钮
                setViewGone(ivSignStudent, false)
                //扫码按钮
                setViewGone(ivScanCode, false)
                //签退按钮
                setViewGone(ivSignOut, true)
                //显示签到时间
                setViewGone(llSignedTime, true)
                //隐藏签退时间
                setViewGone(llSignedOutTime, false)
                //隐藏标签
                setViewGone(ivStatusTag, false)
                ivStatusTag.setImageResource(R.mipmap.ic_training_state_signed)
                setViewGone(llBottomButtonLayout, false)
            }
            /**
             * 已签退
             */
            TRAIN_STATUS_SIGN_OUT -> {
                //隐藏所有按钮
                setViewGone(rlButtonLayout, false)
                //显示签到签退时间信息
                setViewGone(llTrainStatusLayout, true)
                //隐藏标签
                setViewGone(ivStatusTag, false)
                setViewGone(llBottomButtonLayout, false)
            }
            /**
             * 已经转线上
             */
            TRAIN_STATUS_TO_ONLINE -> {
                //隐藏所有按钮
                setViewGone(rlButtonLayout, false)
                //显示签到签退时间信息
                setViewGone(llTrainStatusLayout, true)
                //显示转线上标签
                ivStatusTag.setImageResource(R.mipmap.ic_training_state_turn_online)
                setViewGone(ivStatusTag, true)
                setViewGone(llBottomButtonLayout, false)
            }

            /**
             * 已结束
             */
            TRAIN_STATUS_END -> {
                //隐藏所有按钮
                setViewGone(rlButtonLayout, false)
                //显示签到签退时间信息
                setViewGone(llTrainStatusLayout, true)
                //显示已结束标签
                ivStatusTag.setImageResource(R.mipmap.ic_training_state_end)
                setViewGone(ivStatusTag, true)
                setViewGone(llBottomButtonLayout, false)
            }

            /**
             * 不合格
             */
            TRAIN_STATUS_NO_PASS -> {
                //隐藏所有按钮
                setViewGone(rlButtonLayout, false)
                //显示签到签退时间信息
                setViewGone(llTrainStatusLayout, true)
                //显示已结束标签
                ivStatusTag.setImageResource(R.mipmap.ic_training_state_no_pass)
                setViewGone(ivStatusTag, true)
                setViewGone(llBottomButtonLayout, false)
            }

            /**
             * 待考试
             */
            TRAIN_STATUS_WAIT_EXAM -> {
                //隐藏所有按钮
                setViewGone(rlButtonLayout, false)
                //显示签到签退时间信息
                setViewGone(llTrainStatusLayout, true)
                //隐藏标签
                setViewGone(ivStatusTag, false)
                setViewGone(llBottomButtonLayout, true)
                setViewGone(ivWaitExam, true)

            }

            else -> {
            }
        }


    }
}
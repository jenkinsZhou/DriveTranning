package com.tourcoo.training.ui.training.safe.online.detail.student

import android.os.Bundle
import android.text.TextUtils
import com.tourcoo.training.R
import com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID
import com.tourcoo.training.constant.UserConstant.*
import com.tourcoo.training.core.base.mvp.BaseMvpTitleActivity
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.training.TrainingPlanDetail
import kotlinx.android.synthetic.main.activity_student_training_detail.*

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
        return R.layout.activity_student_training_detail
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
        when (planDetail.role) {
            ROLE_STUDENT -> {
                tvRole.text = "学员"
            }
            ROLE_TEACHER -> {
                tvRole.text = "安全员"
            }
            ROLE_TEACHER_AND_STUDENT -> {
                tvRole.text = "安全员+学员"
            }

        }
    }
}
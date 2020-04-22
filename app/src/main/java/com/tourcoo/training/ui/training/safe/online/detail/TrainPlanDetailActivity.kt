package com.tourcoo.training.ui.training.safe.online.detail

import android.os.Bundle
import com.tourcoo.training.R
import com.tourcoo.training.constant.UserConstant
import com.tourcoo.training.constant.UserConstant.*
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.mvp.BaseMvpTitleActivity
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.training.TrainingPlanDetail
import kotlinx.android.synthetic.main.activity_training_detail.*
import kotlinx.android.synthetic.main.activity_training_detail.view.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月22日12:39
 * @Email: 971613168@qq.com
 */
class TrainPlanDetailActivity : BaseMvpTitleActivity<TrainPlanDetailPresenter>(), TrainDetailContract.View {
    override fun loadPresenter() {
        presenter.start()
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_training_detail
    }

    override fun createPresenter(): TrainPlanDetailPresenter {
        return TrainPlanDetailPresenter()
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("现场培训")
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun doShowTrainPlan(planDetail: TrainingPlanDetail?) {

    }

    private fun showTrainPlan(planDetail: TrainingPlanDetail?) {
        if (planDetail == null) {
            return
        }
        tvCourseName.text =CommonUtil.getNotNullValue(planDetail.title)
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
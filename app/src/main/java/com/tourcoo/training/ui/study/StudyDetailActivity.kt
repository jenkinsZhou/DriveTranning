package com.tourcoo.training.ui.study

import android.os.Bundle
import android.text.TextUtils
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant
import com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.study.StudyDetail
import com.tourcoo.training.entity.study.StudyRecord
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_study_detail.*

/**
 *@description :学习详情
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年05月07日16:46
 * @Email: 971613168@qq.com
 */
class StudyDetailActivity : BaseTitleActivity() {
    private var trainingPlanID = 0
    override fun getContentLayout(): Int {
        return R.layout.activity_study_detail
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("培训详情")
    }

    override fun initView(savedInstanceState: Bundle?) {
        trainingPlanID = intent.getIntExtra(EXTRA_TRAINING_PLAN_ID, -1)
        requestDetail(trainingPlanID.toString())
    }


    private fun requestDetail(trainingPlanID: String) {
        ApiRepository.getInstance().requestStudyDetail(trainingPlanID).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<StudyDetail>>() {
            override fun onSuccessNext(entity: BaseResult<StudyDetail>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    showDetail(entity.data)
                }
            }
        })
    }


    private fun showDetail(detail: StudyDetail?) {
        if (detail == null) {
            return
        }
        if(detail.start ==null){
            //todo
//            setViewGone()
            tvStartTime.text =CommonUtil.getNotNullValue(detail.start)
        }else{

        }


    }
}
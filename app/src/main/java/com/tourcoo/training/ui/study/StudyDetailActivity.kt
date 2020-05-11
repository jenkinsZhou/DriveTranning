package com.tourcoo.training.ui.study

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.study.StudyDetail
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
    private var trainingPlanID = ""
    override fun getContentLayout(): Int {
        return R.layout.activity_study_detail
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("培训记录")
    }

    override fun initView(savedInstanceState: Bundle?) {
        trainingPlanID = intent.getStringExtra(EXTRA_TRAINING_PLAN_ID)
        trainingPlanID =CommonUtil.getNotNullValue(trainingPlanID)
        requestDetail(trainingPlanID)
    }


    private fun requestDetail(trainingPlanID: String) {
        ApiRepository.getInstance().requestStudyDetail(trainingPlanID).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<StudyDetail>>() {
            override fun onSuccessNext(entity: BaseResult<StudyDetail>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    showDetail(entity.data)
                } else {
                    ToastUtil.show(entity.msg)
                }
            }
        })
    }


    private fun showDetail(detail: StudyDetail?) {
        if (detail == null) {
            return
        }
        tvTrainName.text = CommonUtil.getNotNullValue(detail.title)
        if (detail.start == null) {
            setViewGone(llStartTime, false)
        } else {
            setViewGone(llStartTime, true)
            //起始时间
            tvStartTime.text = CommonUtil.getNotNullValue(detail.start.time)
            //位置
            tvLocate.text = CommonUtil.getNotNullValue(detail.start.address)
            //安全员
            tvTeacherName.text = CommonUtil.getNotNullValue(detail.start.securityOfficer)
            //课时
            val length = detail.start.classDuration / 60
            tvCourseLength.text = CommonUtil.getNotNullValue(length.toString() + "分钟")
            setLineHeight(llStartTimeRight, startLineView)
        }
        //签到模块
        if (detail.signIn == null || TextUtils.isEmpty(detail.signIn.time)) {
            setViewGone(llSignIn, false)
        } else {
            setViewGone(llSignIn, true)
            tvSignInTime.text = CommonUtil.getNotNullValue(detail.signIn.time)
            setLineHeight(llSignInRight, lineViewSignIn)
        }

        //签退模块
        if (detail.signOut == null || TextUtils.isEmpty(detail.signOut.time)) {
            setViewGone(llSignOut, false)
        } else {
            setViewGone(llSignOut, true)
            tvSignOutTime.text = CommonUtil.getNotNullValue(detail.signOut.time)
            setLineHeight(llSignOutRight, lineViewSignOut)
        }

        //线上模块
        if (detail.onLine == null || TextUtils.isEmpty(detail.onLine.time)) {
            setViewGone(llOnline, false)
        } else {
            setViewGone(llOnline, true)
            tvOnLineTime.text = CommonUtil.getNotNullValue(detail.onLine.time)
            //课时
            val length = detail.onLine.classDuration / 60
            tvCourseTime.text = CommonUtil.getNotNullValue(length.toString() + "分钟")
            tvStudyProgress.text = CommonUtil.getNotNullValue(detail.onLine.progress.toString())
            setLineHeight(llOnLineRight, lineViewOnline)
        }

        //考试模块
        if (detail.exam == null || TextUtils.isEmpty(detail.exam.time)) {
            setViewGone(llExam, false)
        } else {
            setViewGone(llExam, true)
            tvExamTime.text = CommonUtil.getNotNullValue(detail.exam.time)
            tvExamScore.text = CommonUtil.getNotNullValue(detail.exam.score)
            setLineHeight(llExamRight, lineViewExam)
        }

        //考试模块
        if (detail.end == null || TextUtils.isEmpty(detail.end.time)) {
            setViewGone(llEnd, false)
        } else {
            setViewGone(llEnd, true)
            tvEndTime.text = CommonUtil.getNotNullValue(detail.end.time)
        }
        addDirectory(detail)
    }

    private fun setLineHeight(view: View, lineView: View) {
        val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(w, h)
        val params = lineView.layoutParams
        params.height = view.measuredHeight
        lineView.layoutParams = params
    }

    private fun addDirectory(detail: StudyDetail) {
        if (detail.subjects == null) {
            return
        }
        llDirectory.removeAllViews()
        for (subject in detail.subjects) {
            val view = LayoutInflater.from(mContext).inflate(R.layout.textview_padding_layout, null)
            val textView = view.findViewById<TextView>(R.id.tvText)
            textView.setTextColor(ResourceUtil.getColor(R.color.black333333))
            if (!TextUtils.isEmpty(subject)) {
                textView.text = subject
                llDirectory.addView(view)
            }
        }

    }
}
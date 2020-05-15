package com.tourcoo.training.ui.training.professional

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.tourcoo.training.R
import com.tourcoo.training.constant.TrainingConstant.EXTRA_KEY_EXAM_ID
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.ui.exam.ProfessionalExamActivityNew
import com.tourcoo.training.ui.face.ProfessionalFaceRecognitionActivity
import kotlinx.android.synthetic.main.activity_professional_exam_select_child.*

class ProfessionalExamSelectChildActivity : BaseTitleActivity() {

    /**
     * 是否是正式考试
     */
    private var formal = true
    private var mTrainingPlanId = ""
    private var mExamId = ""

    override fun getContentLayout(): Int {
        return R.layout.activity_professional_exam_select_child
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("考试")
    }

    override fun initView(savedInstanceState: Bundle?) {
        mTrainingPlanId = intent.getStringExtra("trainingPlanId") as String
        mExamId = intent.getStringExtra("examId")


        rlExamSimulation.setOnClickListener {
            formal = false
            //模拟考试不需要人脸验证 直接跳到考试页面
            skipExamByType(mTrainingPlanId, mExamId, formal)

        }
        //正式测试
        rlExamFormal.setOnClickListener {
            if (CommonUtil.getNotNullValue(mExamId).isEmpty() || mExamId == "0") {
                ToastUtil.show("请先完成模拟测试")
            } else {
                formal = true
                //正式考试要先人脸认证
                skipFaceCertify()
            }
        }

    }


    private fun skipExamByType(trainingPlanId: String?, examId: String?, formal: Boolean) {
//        val intent = Intent(this, ProfessionalExamActivityOld::class.java)
        val intent = Intent(this, ProfessionalExamActivityNew::class.java)
        intent.putExtra("trainingPlanId", CommonUtil.getNotNullValue(trainingPlanId))
        intent.putExtra("examId", CommonUtil.getNotNullValue(examId))

        if (formal) {//正式考试
            intent.putExtra("type", 0)
            startActivityForResult(intent,300)
        } else {
            intent.putExtra("type", 1)
            startActivityForResult(intent, 300)
        }

    }

    /**
     * 跳转到专项模块的人脸验证
     */
    private fun skipFaceCertify() {
        val intent = Intent(this, ProfessionalFaceRecognitionActivity::class.java)
        intent.putExtra(EXTRA_KEY_EXAM_ID, CommonUtil.getNotNullValue(mExamId))
        startActivityForResult(intent, 2017)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                300 -> {
                    finish()
                }

                2017 -> {
                    //人脸认证成功 则开始考试
                    skipExamByType(mTrainingPlanId, mExamId, formal)
                }

            }

        }
    }
}

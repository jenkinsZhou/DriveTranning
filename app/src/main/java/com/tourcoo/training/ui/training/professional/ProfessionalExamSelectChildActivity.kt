package com.tourcoo.training.ui.training.professional

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.ui.exam.ProfessionalExamActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_professional_exam_select_child.*

class ProfessionalExamSelectChildActivity : BaseTitleActivity() {


    override fun getContentLayout(): Int {
        return R.layout.activity_professional_exam_select_child
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("考试")
    }

    override fun initView(savedInstanceState: Bundle?) {
        val trainingPlanId = intent.getStringExtra("trainingPlanId")
        val examId = intent.getStringExtra("examId")

        LogUtils.e(examId)

        rlExamSimulation.setOnClickListener {
            val intent = Intent(this, ProfessionalExamActivity::class.java)
            intent.putExtra("trainingPlanId", trainingPlanId)
            intent.putExtra("type", 1)
            intent.putExtra("examId", examId)
            startActivity(intent)
        }


        //正式测试
        rlExamFormal.setOnClickListener {
            val intent = Intent(this, ProfessionalExamActivity::class.java)
            intent.putExtra("trainingPlanId", trainingPlanId)
            intent.putExtra("type", 0)
            intent.putExtra("examId", examId)
            startActivity(intent)
        }

    }

}

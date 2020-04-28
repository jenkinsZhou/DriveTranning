package com.tourcoo.training.ui.training.professional

import android.os.Bundle
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.widget.dialog.CommonBellDialog
import kotlinx.android.synthetic.main.activity_professional_exam_select.*

/**
 *@description :专项测试选择页面
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月28日14:22
 * @Email: 971613168@qq.com
 */
class ProfessionalExamSelectActivity : BaseTitleActivity(), View.OnClickListener {
    override fun getContentLayout(): Int {
        return R.layout.activity_professional_exam_select
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("考试")
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvBuy.setOnClickListener(this)
        tvExamSimulation.setOnClickListener(this)
        tvExamFormal.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvBuy -> {
                ToastUtil.show("购买")
            }
            R.id.tvExamSimulation -> {
                ToastUtil.show("模拟测试")
            }
            R.id.tvExamFormal -> {
                val dialog = CommonBellDialog(mContext)
                dialog.create().setContent("尊敬的学员用户，您还未购买此项目，暂不可进行学习。支付学币之后，方可使用。").setPositiveButton("立即购买",object : View.OnClickListener{
                    override fun onClick(v: View?) {
                    }

                })
                dialog.show()
            }
            else -> {
            }
        }
    }
}
package com.tourcoo.training.ui.training.professional

import android.os.Bundle
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.widget.dialog.CommonBellDialog
import com.trello.rxlifecycle3.android.ActivityEvent
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

    private var id = ""
    private var coins = ""
    private var childModuleId = ""
    private var currentPlanId: String? = null

    //是否需要购买
    private var needBuy = true

    override fun initView(savedInstanceState: Bundle?) {
        val title = intent.getStringExtra("title")
        childModuleId = intent.getStringExtra("childModuleId")
        coins = intent.getStringExtra("coins")
        id = intent.getStringExtra("id")
        needBuy = intent.getIntExtra("status", 0) == 0  //0(暂未购买)  1（已购买）

        if (needBuy) { //已购买
            llHeaderBar.visibility = View.GONE
        } else { //暂未购买
            llHeaderBar.visibility = View.VISIBLE
        }

        mTitleBar.setTitleMainText(title)

        tvBuy.setOnClickListener(this)
        rlExamSimulation.setOnClickListener(this)
        rlExamFormal.setOnClickListener(this)
    }

    private fun requestPayInfo() {
        ApiRepository.getInstance().requestTwoPayInfo(id, childModuleId, coins).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any?>>() {
            override fun onSuccessNext(entity: BaseResult<Any?>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.showSuccess("支付完成")
                    needBuy = false
                    //todo  dasdfasdf
                } else {
                    ToastUtil.show(entity.msg)
                }
            }
        })
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvBuy -> {
                ToastUtil.show("点击购买")
//                if (needBuy) {
                    val dialog = CommonBellDialog(mContext)
                    dialog.create().setContent("尊敬的学员用户，您还未购买此项目，暂不可进行学习。支付学币之后，方可使用。").setPositiveButton("立即购买", object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            requestPayInfo()
                            dialog.dismiss()
                        }
                    })
                    dialog.show()
                    return
//                }
            }
            R.id.rlExamSimulation -> {
                ToastUtil.show("模拟测试")
            }
            R.id.rlExamFormal -> {

            }
            else -> {
            }
        }
    }
}
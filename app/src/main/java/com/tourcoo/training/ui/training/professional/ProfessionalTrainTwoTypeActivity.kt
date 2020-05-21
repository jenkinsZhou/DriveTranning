package com.tourcoo.training.ui.training.professional

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tourcoo.training.R
import com.tourcoo.training.adapter.training.ProfessionalTrainingTwoTypeAdapter
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.activity.BaseTitleRefreshLoadActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.training.ProfessionalTwoTypeModel
import com.tourcoo.training.utils.RecycleViewDivider
import com.tourcoo.training.widget.dialog.CommonBellDialog
import com.trello.rxlifecycle3.android.ActivityEvent

/**
 *@description :两类人员专项
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月27日16:32
 * @Email: 971613168@qq.com
 */
class ProfessionalTrainTwoTypeActivity : BaseTitleRefreshLoadActivity<ProfessionalTwoTypeModel>() {

    override fun getContentLayout(): Int {
        return R.layout.frame_layout_title_refresh_recycler
    }

    private var adapter: ProfessionalTrainingTwoTypeAdapter? = null
    override fun getAdapter(): BaseQuickAdapter<ProfessionalTwoTypeModel, BaseViewHolder> {
        adapter = ProfessionalTrainingTwoTypeAdapter()
        return adapter!!
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("两类人员专项")
    }

    private var id = ""
    override fun initView(savedInstanceState: Bundle?) {
        val title = intent.getStringExtra("title")
        id = intent.getStringExtra("id")

        mTitleBar.setTitleMainText(title)

        mRecyclerView.addItemDecoration(RecycleViewDivider(this, LinearLayout.VERTICAL, ConvertUtils.dp2px(10f), resources.getColor(R.color.grayFBF8FB), true))


        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val info = adapter.data[position] as ProfessionalTwoTypeModel
            jumpByModelType(info)
        }

        adapter!!.setOnItemChildClickListener { adapter, view, position ->
            val info = adapter.data[position] as ProfessionalTwoTypeModel
            when (view.id) {
                R.id.btnSubmit -> {
                    if (info.status == 1) { //已购买
                        jumpByModelType(info)
                    } else {
                        // TrainingPlanStatus":计划时间是否开始 0未开始 1进行中 2已过期 （说明：未开始和已过期状态下不允许支付学币）
                        if (info.trainingPlanStatus == 0 || info.trainingPlanStatus == 2) {
                            ToastUtil.show("当前计划未开始或已过期")
                        }else{
                            val dialog = CommonBellDialog(mContext)
                            dialog.create().setContent("尊敬的学员用户，您还未购买此项目，暂不可进行学习。支付学币之后，方可使用。").setPositiveButton("立即购买", object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    requestPayInfo(info.specialId, info.childModuleId, info.coins)
                                    dialog.dismiss()
                                }
                            })
                            dialog.show()
                        }

                    }
                }
            }
        }

    }


    private fun jumpByModelType(info: ProfessionalTwoTypeModel) {
        if (info.type == "0") {//直接跳转到课程列表
            val intent = Intent(this, ProfessionalSelectActivity::class.java)
            intent.putExtra("id", info.specialId)
            intent.putExtra("childModuleId", info.childModuleId)
            intent.putExtra("title", info.title)
            intent.putExtra("coins", info.coins)
            intent.putExtra("planStatus", info.planStatus)
            intent.putExtra("trainingPlanStatus", info.trainingPlanStatus)
            startActivity(intent)

        } else {//直接跳转到考试分类列表
            if (info.planStatus == 1) {
                //说明计划完成 无需考试 直接拦截
                ToastUtil.show("当前计划已完成 无需考试")
                return
            }
            val intent = Intent(this, ProfessionalExamSelectListActivity::class.java)
            intent.putExtra("id", info.specialId)
            intent.putExtra("childModuleId", info.childModuleId)
            intent.putExtra("title", info.title)
            intent.putExtra("coins", info.coins)
            intent.putExtra("status", info.status)
            intent.putExtra("planStatus", info.planStatus)
            intent.putExtra("trainingPlanStatus", info.trainingPlanStatus)
            startActivity(intent)
        }
    }


    private var isFirst = false
    override fun onResume() {
        super.onResume()
        if (isFirst) {
            requestTwoTypeDatas()
        }
    }

    override fun loadData(page: Int) {
        isFirst = true
        requestTwoTypeDatas()
    }


    private fun requestTwoTypeDatas() {
        ApiRepository.getInstance().requestTwoType(id).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<List<ProfessionalTwoTypeModel>>>(iHttpRequestControl) {
            override fun onSuccessNext(entity: BaseResult<List<ProfessionalTwoTypeModel>>?) {
                UiManager.getInstance().httpRequestControl.httpRequestSuccess(iHttpRequestControl, if (entity!!.data == null) ArrayList<ProfessionalTwoTypeModel>() else entity.data, null)
            }
        })
    }


    private fun requestPayInfo(id: String, childModuleId: String, coins: String) {
        ApiRepository.getInstance().requestTwoPayInfo(id, childModuleId, coins).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any?>>() {
            override fun onSuccessNext(entity: BaseResult<Any?>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.showSuccess("支付完成")
                    requestTwoTypeDatas()
                } else {
                    ToastUtil.show(entity.msg)
                }
            }
        })
    }


}
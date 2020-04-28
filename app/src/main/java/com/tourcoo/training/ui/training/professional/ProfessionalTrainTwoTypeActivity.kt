package com.tourcoo.training.ui.training.professional

import android.os.Bundle
import android.widget.LinearLayout
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tourcoo.training.R
import com.tourcoo.training.adapter.training.ProfessionalTrainingTwoTypeAdapter
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.activity.BaseTitleRefreshLoadActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.training.ProfessionalTwoTypeModel
import com.tourcoo.training.utils.RecycleViewDivider
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

    override fun getAdapter(): BaseQuickAdapter<ProfessionalTwoTypeModel, BaseViewHolder> {
        return ProfessionalTrainingTwoTypeAdapter()
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


        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val info = adapter.data[position] as ProfessionalTwoTypeModel
            if (info.type == "0") {//直接跳转到课程列表
                ToastUtil.show("直接跳转到课程列表")
            } else {//直接跳转到考试分类列表

            }

//            val intent = Intent(this,CertificationDetailsActivity::class.java)
//            intent.putExtra("id",info.id)
//            startActivity(intent)
        }


    }

    override fun loadData(page: Int) {
        requestTwoTypeDatas()
    }


    private fun requestTwoTypeDatas() {
        ApiRepository.getInstance().requestTwoType(id).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<List<ProfessionalTwoTypeModel>>>() {
            override fun onSuccessNext(entity: BaseResult<List<ProfessionalTwoTypeModel>>?) {
                UiManager.getInstance().httpRequestControl.httpRequestSuccess(iHttpRequestControl, if (entity!!.data == null) ArrayList<ProfessionalTwoTypeModel>() else entity.data, null)
            }
        })
    }


}
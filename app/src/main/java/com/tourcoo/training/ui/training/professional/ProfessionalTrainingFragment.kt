package com.tourcoo.training.ui.training.professional

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tourcoo.training.R
import com.tourcoo.training.adapter.training.ProfessionalTrainingAdapter
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.entity.BasePageResult
import com.tourcoo.training.core.base.fragment.BaseTitleRefreshLoadFragment
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.course.CourseInfo
import com.tourcoo.training.entity.training.ProfessionTrainingEntity
import com.tourcoo.training.utils.RecycleViewDivider
import com.trello.rxlifecycle3.android.FragmentEvent

/**
 *@description : 专项培训列表
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年03月10日20:29
 * @Email: 971613168@qq.com
 */
class ProfessionalTrainingFragment : BaseTitleRefreshLoadFragment<ProfessionTrainingEntity>() {


    private var adapter: ProfessionalTrainingAdapter? = null

    override fun getAdapter(): BaseQuickAdapter<ProfessionTrainingEntity, BaseViewHolder> {
        adapter = ProfessionalTrainingAdapter()
        return adapter!!
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.visibility = View.GONE
    }

    override fun loadData(page: Int) {
        requestCourseProfessionalTraining(page)
    }


    override fun getContentLayout(): Int {
        return R.layout.fragment_training_profressional_new
    }


    override fun initView(savedInstanceState: Bundle?) {
        mRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, ConvertUtils.dp2px(10f), resources.getColor(R.color.grayFBF8FB), true))

        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val courseInfo = adapter!!.data[position] as ProfessionTrainingEntity

            if (!AccountHelper.getInstance().isLogin) {
                ToastUtil.show("请先登录")
                return@OnItemClickListener
            }

            val intent = Intent(context,ProfessionalTrainTwoTypeActivity::class.java)
            intent.putExtra("id",courseInfo.id)
            intent.putExtra("title",courseInfo.name)
            startActivity(intent)
        }

    }


    companion object {
        fun newInstance(): ProfessionalTrainingFragment {
            val args = Bundle()
            val fragment = ProfessionalTrainingFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private fun requestCourseProfessionalTraining(page: Int) {
        ApiRepository.getInstance().requestCourseProfessionalTraining(page).compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BasePageResult<ProfessionTrainingEntity>?>() {
            override fun onSuccessNext(entity: BasePageResult<ProfessionTrainingEntity>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
//                    handleOnLineCourseList(entity.data)
                    UiManager.getInstance().httpRequestControl.httpRequestSuccess(iHttpRequestControl, if (entity.data == null) ArrayList<ProfessionTrainingEntity>() else entity.data.rows, null)


                } else {
                    ToastUtil.show(entity.msg)
                }

            }

        })

    }


    private fun initTrainingPlanClick() {
        if (adapter == null) {
            return
        }
        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val courseInfo = adapter!!.data[position] as CourseInfo

            if (!AccountHelper.getInstance().isLogin) {
                ToastUtil.show("请先登录")
                return@OnItemClickListener
            }

//            when (AccountHelper.getInstance().userInfo.isAuthenticated) {
//                0 -> {  //未认证
//                    currentPlanId = courseInfo.trainingPlanID
//                    showRecognize(currentPlanId)
//                }
//
//                1 -> {  //已认证
//                    verifyByStatus(courseInfo)
//                }
//
//                2 -> {  //认证失败
//                    val dialog = LocalTrainingConfirmDialog(mContext)
//                    dialog.setContent("请确认是否为本人学习？")
//                            .setPositiveButtonClick("确认") {
//                                verifyByStatus(courseInfo)
//                                dialog.dismiss()
//                            }
//                            .setNegativeButtonClick("取消") {
//                                dialog.dismiss()
//                            }
//                            .create()
//                            .show()
//                }
//            }

        }
    }


}
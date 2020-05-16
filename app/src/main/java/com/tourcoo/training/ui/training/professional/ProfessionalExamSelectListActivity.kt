package com.tourcoo.training.ui.training.professional

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tourcoo.training.R
import com.tourcoo.training.adapter.training.OnLineTrainingCourseAdapter
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.activity.BaseTitleRefreshLoadActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.course.CourseInfo
import com.tourcoo.training.entity.training.TwoTypeModel
import com.tourcoo.training.utils.RecycleViewDivider
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
class ProfessionalExamSelectListActivity : BaseTitleRefreshLoadActivity<CourseInfo>(), View.OnClickListener {


    private var adapter: OnLineTrainingCourseAdapter? = null

    companion object {
        const val REQUEST_CODE_FACE_RECORD = 201

        const val REQUEST_CODE_FACE_COMPARE = 202

        const val REQUEST_CODE_FACE_VERIFY = 203
    }

    override fun getAdapter(): BaseQuickAdapter<CourseInfo, BaseViewHolder> {
        adapter = OnLineTrainingCourseAdapter()
        return adapter!!
    }


    private var isFirst = false
    override fun onResume() {
        super.onResume()
        if (isFirst) {
            requestData()
        }
    }

    override fun loadData(page: Int) {
        isFirst = true
        requestData()
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_professional_exam_select
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("考试")
    }

    private var id = ""
    private var coins = ""
    private var childModuleId = ""

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

        mRecyclerView.addItemDecoration(RecycleViewDivider(this, LinearLayout.VERTICAL, ConvertUtils.dp2px(10f), resources.getColor(R.color.grayFBF8FB), true))

        adapter!!.setOnItemClickListener { adapter, view, position ->
            val info = adapter.data[position] as CourseInfo
            if (needBuy) {
                showBuyDialog()
                return@setOnItemClickListener
            }
            // public static final int COURSE_STATUS_FINISHED = 0;
            //    public static final int COURSE_STATUS_CONTINUE = 1;
            //    public static final int COURSE_STATUS_NEED_PAY = 2;
            //    public static final int COURSE_STATUS_WAIT_EXAM = 3;
            if(info.status == 0){
                //已完成状态下 屏蔽点击事件
                return@setOnItemClickListener
            }
            val intent = Intent(this, ProfessionalExamSelectChildActivity::class.java)
            intent.putExtra("trainingPlanId", CommonUtil.getNotNullValue(info.trainingPlanID))
            intent.putExtra("examId", CommonUtil.getNotNullValue(info.examId))
            startActivity(intent)
        }


        tvBuy.setOnClickListener(this)

    }


    private fun requestData() {
        ApiRepository.getInstance().requestTwoTypeDetailsList(id, childModuleId).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<TwoTypeModel>>(iHttpRequestControl) {
            override fun onSuccessNext(entity: BaseResult<TwoTypeModel>?) {
                if (entity == null) {
                    return
                }
                mRefreshLayout.finishRefresh()

                needBuy = entity.data.status == 0
                if (entity.data.status == 1) { //已购买
                    llHeaderBar.visibility = View.GONE
                } else { //暂未购买
                    llHeaderBar.visibility = View.VISIBLE
                }

                UiManager.getInstance().httpRequestControl.httpRequestSuccess(iHttpRequestControl, if (entity.data == null) ArrayList<CourseInfo>() else entity.data.planData, null)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                mRefreshLayout.finishRefresh()
            }
        })
    }

    private fun requestPayInfo() {
        ApiRepository.getInstance().requestTwoPayInfo(id, childModuleId, coins).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any?>>() {
            override fun onSuccessNext(entity: BaseResult<Any?>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.showSuccess("支付完成")
                    requestData()
                } else {
                    ToastUtil.show(entity.msg)
                }
            }
        })
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvBuy -> {
                if (needBuy) {
                    showBuyDialog()
                    return
                }
            }

            else -> {
            }
        }
    }


    private fun showBuyDialog() {
        val dialog = CommonBellDialog(mContext)
        dialog.create().setContent("尊敬的学员用户，您还未购买此项目，暂不可进行学习。支付学币之后，方可使用。").setPositiveButton("立即购买", object : View.OnClickListener {
            override fun onClick(v: View?) {
                requestPayInfo()
                dialog.dismiss()
            }
        })
        dialog.show()
    }
}
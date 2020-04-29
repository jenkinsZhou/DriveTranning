package com.tourcoo.training.ui.training.professional

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tourcoo.training.R
import com.tourcoo.training.adapter.training.OnLineTrainingCourseAdapter
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.activity.BaseTitleRefreshLoadActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.course.CourseInfo
import com.tourcoo.training.entity.training.TwoTypeModel
import com.tourcoo.training.ui.face.FaceRecognitionActivity
import com.tourcoo.training.ui.pay.BuyNowActivity
import com.tourcoo.training.ui.training.safe.online.TencentPlayVideoActivity
import com.tourcoo.training.ui.training.safe.online.aliyun.AliYunPlayVideoActivity
import com.tourcoo.training.utils.RecycleViewDivider
import com.tourcoo.training.widget.dialog.CommonBellDialog
import com.tourcoo.training.widget.dialog.recognize.RecognizeStepDialog
import com.tourcoo.training.widget.dialog.training.LocalTrainingConfirmDialog
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_professional_exam_select.*

/**
 *@description :专项测试选择页面
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月28日14:22
 * @Email: 971613168@qq.com
 */
class ProfessionalExamSelectActivity : BaseTitleRefreshLoadActivity<CourseInfo>(), View.OnClickListener {

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

    override fun loadData(page: Int) {
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
    private var currentPlanId: String? = null
    private var dialog: RecognizeStepDialog? = null

    //是否需要购买
    private var needBuy = true

    override fun initView(savedInstanceState: Bundle?) {
        val title = intent.getStringExtra("title")
        childModuleId = intent.getStringExtra("childModuleId")
        coins = intent.getStringExtra("coins")
        id = intent.getStringExtra("id")

        mTitleBar.setTitleMainText(title)

        mRecyclerView.addItemDecoration(RecycleViewDivider(this, LinearLayout.VERTICAL, ConvertUtils.dp2px(10f), resources.getColor(R.color.grayFBF8FB), true))


        initTrainingPlanClick()

        tvBuy.setOnClickListener(this)
//        tvExamSimulation.setOnClickListener(this)
//        tvExamFormal.setOnClickListener(this)
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

            when (AccountHelper.getInstance().userInfo.isAuthenticated) {
                0 -> {  //未认证
                    currentPlanId = courseInfo.trainingPlanID
                    showRecognize(currentPlanId)
                }

                1 -> {  //已认证
                    verifyByStatus(courseInfo)
                }

                2 -> {  //认证失败
                    val dialog = LocalTrainingConfirmDialog(mContext)
                    dialog.setContent("请确认是否为本人学习？")
                            .setPositiveButtonClick("确认") {
                                verifyByStatus(courseInfo)
                                dialog.dismiss()
                            }
                            .setNegativeButtonClick("取消") {
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                }
            }

        }
    }


    private fun showRecognize(trainingId: String?) {
        if (TextUtils.isEmpty(trainingId)) {
            ToastUtil.show("'未获取到培训信息")
            return
        }
        dialog = RecognizeStepDialog(mContext).create().setPositiveButton {
            skipFaceRecord(trainingId!!)
        }
        dialog?.show()
    }


    private fun skipFaceRecord(trainingId: String) {
        val intent = Intent(mContext, FaceRecognitionActivity::class.java)
        intent.putExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID, trainingId)
        startActivityForResult(intent, REQUEST_CODE_FACE_RECORD)
    }

    /**
     * 已通过身份验证的后续判断流程
     */
    private fun verifyByStatus(courseInfo: CourseInfo) {
        doSkipByStatus(courseInfo)
    }

    private fun doSkipByStatus(courseInfo: CourseInfo?) {
        if (courseInfo == null) {
            return
        }

        if (needBuy) {
            val dialog = CommonBellDialog(mContext)
            dialog.create().setContent("尊敬的学员用户，您还未购买此项目，暂不可进行学习。支付学币之后，方可使用。").setPositiveButton("立即购买", object : View.OnClickListener {
                override fun onClick(v: View?) {
                    requestPayInfo()
                    dialog.dismiss()
                }
            })
            dialog.show()
            return
        }


        when (courseInfo.status) {

            OnLineTrainingCourseAdapter.COURSE_STATUS_CONTINUE -> {
                skipPlayVideoByType(courseInfo.trainingPlanID, courseInfo.type)
            }

            OnLineTrainingCourseAdapter.COURSE_STATUS_WAIT_EXAM -> {
                skipPlayVideoByType(courseInfo.trainingPlanID, courseInfo.type)
            }

            OnLineTrainingCourseAdapter.COURSE_STATUS_FINISHED -> {

            }

            else -> {
                skipPlayVideoByType(courseInfo.trainingPlanID, courseInfo.type)
            }
        }
    }


    private fun skipPlayVideoByType(trainingId: String?, courseType: Int) {
        var intent: Intent? = null
        when (courseType) {
            TrainingConstant.TYPE_COURSE_HTML -> {
                //todo 全部为html
                intent = Intent(mContext, TencentPlayVideoActivity::class.java)
            }
            TrainingConstant.TYPE_COURSE_TYPE_DRIVE -> {
                //车学堂 使用腾讯播放器
                intent = Intent(mContext, TencentPlayVideoActivity::class.java)
            }
            TrainingConstant.TYPE_TYPE_COURSE_HLS -> {
                //hls 使用阿里播放器
                intent = Intent(mContext, AliYunPlayVideoActivity::class.java)
            }
            TrainingConstant.TYPE_COURSE_OTHER -> {
                //混合非加密 使用腾讯播放器
                intent = Intent(mContext, TencentPlayVideoActivity::class.java)
            }

        }
        intent?.putExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID, trainingId)
        startActivity(intent)
    }


    private fun requestData() {
        ApiRepository.getInstance().requestTwoTypeDetailsList(id, childModuleId).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<TwoTypeModel>>(iHttpRequestControl) {
            override fun onSuccessNext(entity: BaseResult<TwoTypeModel>?) {
                if (entity == null) {
                    return
                }

                needBuy = entity.data.status == 0
                if (entity.data.status == 1) { //已购买
                    llHeaderBar.visibility = View.GONE
                } else { //暂未购买
                    llHeaderBar.visibility = View.VISIBLE
                }

                UiManager.getInstance().httpRequestControl.httpRequestSuccess(iHttpRequestControl, if (entity.data == null) ArrayList<CourseInfo>() else entity.data.planData, null)
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
                ToastUtil.show("购买")
            }
//            R.id.tvExamSimulation -> {
//                ToastUtil.show("模拟测试")
//            }
//            R.id.tvExamFormal -> {
//
//            }
            else -> {
            }
        }
    }
}
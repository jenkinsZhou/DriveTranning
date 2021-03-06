package com.tourcoo.training.ui.training.professional

import android.app.Activity
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
import com.tourcoo.training.adapter.training.OnLineTrainingCourseAdapter.COURSE_STATUS_CONTINUE
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
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.entity.course.CourseInfo
import com.tourcoo.training.entity.training.TwoTypeModel
import com.tourcoo.training.ui.account.LoginActivity
import com.tourcoo.training.ui.account.register.RecognizeIdCardActivity
import com.tourcoo.training.ui.face.FaceRecognitionActivity
import com.tourcoo.training.ui.pay.BuyNowActivity
import com.tourcoo.training.ui.training.safe.online.TencentPlayVideoActivity
import com.tourcoo.training.ui.training.safe.online.aliyun.AliYunPlayVideoActivity
import com.tourcoo.training.ui.training.safe.online.web.HtmlBrowserActivity
import com.tourcoo.training.utils.RecycleViewDivider
import com.tourcoo.training.widget.dialog.CommonBellAlert
import com.tourcoo.training.widget.dialog.CommonBellDialog
import com.tourcoo.training.widget.dialog.recognize.RecognizeStepDialog
import com.tourcoo.training.widget.dialog.training.LocalTrainingConfirmDialog
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_professional_select.*

/**
 *@description :专项测试选择页面
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月28日14:22
 * @Email: 971613168@qq.com
 */
class ProfessionalTwoCourseSelectActivity : BaseTitleRefreshLoadActivity<CourseInfo>(), View.OnClickListener {

    private var adapter: OnLineTrainingCourseAdapter? = null

    companion object {
        const val REQUEST_CODE_FACE_RECORD = 201

        const val REQUEST_CODE_FACE_COMPARE = 202

        const val REQUEST_CODE_FACE_VERIFY = 203

        const val REQUEST_CODE_AUTH = 204

    }
    private var mPlanStatus = -1
    private var mTrainingPlanStatus = -1
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
        return R.layout.activity_professional_select
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("两类人员专项")
    }

    private var id = ""
    private var coins = ""
    private var childModuleId = ""
    private var currentCourseInfo: CourseInfo? = null
    private var dialog: RecognizeStepDialog? = null

    //是否需要购买
    private var needBuy = true

    override fun initView(savedInstanceState: Bundle?) {
        val title = intent.getStringExtra("title")
        childModuleId = intent.getStringExtra("childModuleId")
        coins = intent.getStringExtra("coins")
        id = intent.getStringExtra("id")
        mPlanStatus = intent.getIntExtra("planStatus", -1)
        mTrainingPlanStatus = intent.getIntExtra("trainingPlanStatus", -1)
        mTitleBar.setTitleMainText(title)

        mRecyclerView.addItemDecoration(RecycleViewDivider(this, LinearLayout.VERTICAL, ConvertUtils.dp2px(10f), resources.getColor(R.color.grayFBF8FB), true))


        initTrainingPlanClick()

        tvBuy.setOnClickListener(this)
    }


    private fun initTrainingPlanClick() {
        if (adapter == null) {
            return
        }
        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            currentCourseInfo = adapter!!.data[position] as CourseInfo

            if (!AccountHelper.getInstance().isLogin) {
                ToastUtil.show("请先登录")
                return@OnItemClickListener
            }
//            TrainingPlanStatus":计划时间是否开始 0未开始 1进行中 2已过期
            if (mTrainingPlanStatus == 0) {
                //mPlanStatus = 0 说明计划未开始 直接拦截
                ToastUtil.show("课程还未开始")
                return@OnItemClickListener
            }
            if (mTrainingPlanStatus == 2) {
                //mPlanStatus = 0 说明计划未开始 直接拦截
                ToastUtil.show("课程已过期")
                return@OnItemClickListener
            }
            /*  case finished = 0
             case continues = 1
             case immdiate = 2
             case preText = 3*/
            if (currentCourseInfo!!.status == 0) {
                //已完成状态下 不可点击
                return@OnItemClickListener
            }

            if (AccountHelper.getInstance().userInfo.isAuthenticated == 1 || AccountHelper.getInstance().userInfo.isAuthenticated == 3) {
                val dialog = CommonBellAlert(mContext)
                dialog.create().setContent("驾驶员自主注册，需等待企业管理员审核。").setPositiveButton("知道了", object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        dialog.dismiss()
                    }
                })
                dialog.show()
                return@OnItemClickListener
            }


            when (AccountHelper.getInstance().userInfo.status) {
                0 -> {  //未认证
                    showRecognize(currentCourseInfo!!.trainingPlanID)
                }

                1 -> {  //已认证
                    verifyByStatus(currentCourseInfo!!)
                }

                2 -> {  //认证失败
                    val dialog = LocalTrainingConfirmDialog(mContext)
                    dialog.create()
                            .setContent("请确认是否为本人学习？")
                            .setPositiveButtonClick("确认") {
                                verifyByStatus(currentCourseInfo!!)
                                dialog.dismiss()
                            }
                            .setNegativeButtonClick("取消") {
                                dialog.dismiss()
                            }
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
        intent.putExtra("OnlyBase64", true)
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
            if (mTrainingPlanStatus == 0 || mTrainingPlanStatus == 2) {
                ToastUtil.show("当前计划未开始或已过期")
                return
            }
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
                skipFaceVefify(courseInfo.trainingPlanID)
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

    private fun skipFaceVefify(trainingId: String) {
        val intent = Intent(mContext, FaceRecognitionActivity::class.java)
        intent.putExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID, trainingId)
        startActivityForResult(intent, REQUEST_CODE_FACE_VERIFY)
    }


    private fun skipPlayVideoByType(trainingId: String?, courseType: Int) {
        var intent: Intent? = null
        when (courseType) {
            TrainingConstant.TYPE_COURSE_HTML -> {
                intent = Intent(mContext, HtmlBrowserActivity::class.java)
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
                //todo 暂时跳转到阿里播放器
//                intent = Intent(mContext, AliYunPlayVideoActivity::class.java)
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
                if (mTrainingPlanStatus == 0 || mTrainingPlanStatus == 2) {
                    ToastUtil.show("当前计划未开始或已过期")
                    return
                }
                val dialog = CommonBellDialog(mContext)
                dialog.create().setContent("尊敬的学员用户，您还未购买此项目，暂不可进行学习。支付学币之后，方可使用。").setPositiveButton("立即购买", object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        requestPayInfo()
                        dialog.dismiss()
                    }
                })
                dialog.show()
            }

            else -> {
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_AUTH -> {
                    currentCourseInfo!!.status = COURSE_STATUS_CONTINUE
                    verifyByStatus(currentCourseInfo!!)
                }

                REQUEST_CODE_FACE_RECORD -> {
                    if (data != null) {
                        handleStepOneSuccess()
                    }
                }
                REQUEST_CODE_FACE_COMPARE -> {
                    AccountHelper.getInstance().userInfo.status = 1
                    ToastUtil.showSuccess("验证通过")
                    closeFaceDialog()
                    verifyByStatus(currentCourseInfo!!)
                }

                REQUEST_CODE_FACE_VERIFY -> {
                    skipPlayVideoByType(currentCourseInfo!!.trainingPlanID, currentCourseInfo!!.type)
                }
            }
        } else {
            baseHandler.postDelayed({
                closeFaceDialog()
            }, 500)
        }
    }


    private fun handleStepOneSuccess() {
        dialog?.showStepOneSuccess()
        dialog?.setPositiveButton(View.OnClickListener {
            skipIdCardRecognize(currentCourseInfo!!.trainingPlanID)
        })
    }


    /**
     * 人脸和身份证比对
     */
    private fun skipIdCardRecognize(trainingId: String?) {
        if (TextUtils.isEmpty(trainingId)) {
            ToastUtil.show("'未获取到培训信息")
            return
        }
        val bundle = Bundle()
        bundle.putInt(LoginActivity.EXTRA_KEY_REGISTER_TYPE, LoginActivity.EXTRA_REGISTER_TYPE_DRIVER)
        AccountTempHelper.getInstance().recognizeType = LoginActivity.EXTRA_TYPE_RECOGNIZE_COMPARE
        val intent = Intent(mContext, RecognizeIdCardActivity::class.java)
        intent.putExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID, trainingId)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_FACE_COMPARE)
    }


    private fun closeFaceDialog() {
        dialog?.dismiss()
    }


}
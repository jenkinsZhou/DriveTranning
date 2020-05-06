package com.tourcoo.training.ui.training.safe.online.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tourcoo.training.R
import com.tourcoo.training.adapter.training.OnLineTrainingCourseAdapter
import com.tourcoo.training.adapter.training.OnLineTrainingCourseAdapter.*
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant.*
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.base.fragment.BaseFragment
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.entity.account.UserInfoEvent
import com.tourcoo.training.entity.course.CourseInfo
import com.tourcoo.training.ui.account.LoginActivity
import com.tourcoo.training.ui.account.LoginActivity.Companion.EXTRA_TYPE_RECOGNIZE_COMPARE
import com.tourcoo.training.ui.account.register.RecognizeIdCardActivity
import com.tourcoo.training.ui.face.FaceRecognitionActivity
import com.tourcoo.training.ui.pay.BuyNowActivity
import com.tourcoo.training.ui.training.safe.online.web.HtmlBrowserActivity
import com.tourcoo.training.ui.training.safe.online.TencentPlayVideoActivity
import com.tourcoo.training.ui.training.safe.online.aliyun.AliYunPlayVideoActivity
import com.tourcoo.training.utils.RecycleViewDivider
import com.tourcoo.training.widget.dialog.recognize.RecognizeStepDialog
import com.tourcoo.training.widget.dialog.training.InputPayNumAlert
import com.tourcoo.training.widget.dialog.training.LocalTrainingConfirmDialog
import com.trello.rxlifecycle3.android.FragmentEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *@description :线上培训
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月11日10:55
 * @Email: 971613168@qq.com
 */
class OnlineTrainFragment : BaseFragment() {
    private var adapter: OnLineTrainingCourseAdapter? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var currentCourseInfo: CourseInfo? = null
    private var dialog: RecognizeStepDialog? = null
    private var inputAlert: InputPayNumAlert? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_training_profressional
    }


    override fun initView(savedInstanceState: Bundle?) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        refreshLayout = mContentView.findViewById(R.id.smartRefreshLayoutCommon)
        refreshLayout?.setRefreshHeader(ClassicsHeader(mContext))
        refreshLayout?.setOnRefreshListener {
            requestCourseOnLine()
        }


        recyclerView = mContentView.findViewById(R.id.rvCommon)
        recyclerView?.layoutManager = LinearLayoutManager(mContext)
        recyclerView?.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, ConvertUtils.dp2px(10f), resources.getColor(R.color.grayFBF8FB), true))
        adapter = OnLineTrainingCourseAdapter()
        adapter?.bindToRecyclerView(recyclerView)

        //个体工商户
        if (AccountHelper.getInstance().isLogin && AccountHelper.getInstance().userInfo.userType == 1) {
            val view = View.inflate(context, R.layout.empty_individual_business_layout, null)
            val tvBuy = view.findViewById<TextView>(R.id.tvBuy)
            tvBuy.setOnClickListener {
                showInputDialog()
            }

            adapter?.emptyView = view
        } else {
            adapter?.setEmptyView(R.layout.empty_driver_layout)
        }


        initTrainingPlanClick()

    }


    companion object {
        fun newInstance(): OnlineTrainFragment {
            val args = Bundle()
            val fragment = OnlineTrainFragment()
            fragment.arguments = args
            return fragment
        }

        const val REQUEST_CODE_FACE_RECORD = 201

        const val REQUEST_CODE_FACE_COMPARE = 202

        const val REQUEST_CODE_FACE_VERIFY = 203

        const val REQUEST_CODE_AUTH = 204
    }


    private fun requestCourseOnLine() {
        if (!AccountHelper.getInstance().isLogin) {
            removeData()
            refreshLayout?.finishRefresh()
            return
        }
        ApiRepository.getInstance().requestOnLineTrainingList().compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<CourseInfo>?>?>() {
            override fun onSuccessNext(entity: BaseResult<MutableList<CourseInfo>?>?) {
                refreshLayout?.finishRefresh()
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    handleOnLineCourseList(entity.data)
                } else {
                    ToastUtil.show(entity.msg)
                }

            }

            override fun onError(e: Throwable) {
                refreshLayout?.finishRefresh(false)
                dismissProgressDialog()
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed(e.toString())
                }
            }

        })

    }

    override fun loadData() {
        super.loadData()
        requestCourseOnLine()
    }

    private fun handleOnLineCourseList(list: MutableList<CourseInfo>?) {
        if (list == null) {
            return
        }
        adapter?.setNewData(list)
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

            when (AccountHelper.getInstance().userInfo.isAuthenticated) {
                0 -> {  //未认证
                    showRecognize(currentCourseInfo!!.trainingPlanID)
                }

                1 -> {  //已认证
                    verifyByStatus(currentCourseInfo!!)
                }

                2 -> {  //认证失败
                    val dialog = LocalTrainingConfirmDialog(mContext)
                    dialog.setContent("请确认是否为本人学习？")
                            .setPositiveButtonClick("确认") {
                                verifyByStatus(currentCourseInfo!!)
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
        when (courseInfo.status) {
            COURSE_STATUS_NEED_PAY -> {

                val intent = Intent(context, BuyNowActivity::class.java)
                intent.putExtra("trainingPlanID", courseInfo.trainingPlanID)
                startActivityForResult(intent, REQUEST_CODE_AUTH)
            }

            COURSE_STATUS_CONTINUE -> {
                skipFaceVefify(courseInfo.trainingPlanID)
            }

            COURSE_STATUS_WAIT_EXAM ->{
                skipPlayVideoByType(courseInfo.trainingPlanID,courseInfo.type)
            }

            COURSE_STATUS_FINISHED -> {

            }

            else -> {
                skipPlayVideoByType(courseInfo.trainingPlanID, courseInfo.type)
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
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, trainingId)
        intent.putExtra("OnlyBase64",true)
        startActivityForResult(intent, REQUEST_CODE_FACE_RECORD)
    }


    private fun skipFaceVefify(trainingId: String) {
        val intent = Intent(mContext, FaceRecognitionActivity::class.java)
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, trainingId)
        startActivityForResult(intent, REQUEST_CODE_FACE_VERIFY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_AUTH ->{
                    currentCourseInfo!!.status = COURSE_STATUS_CONTINUE
                    verifyByStatus(currentCourseInfo!!)
                }

                REQUEST_CODE_FACE_RECORD -> {
                    if (data != null) {
                        handleStepOneSuccess()
                    }
                }
                REQUEST_CODE_FACE_COMPARE -> {
                    AccountHelper.getInstance().userInfo.isAuthenticated = 1
                    ToastUtil.showSuccess("验证通过")
                    closeFaceDialog()
                    verifyByStatus(currentCourseInfo!!)
                }

                REQUEST_CODE_FACE_VERIFY ->{
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
        AccountTempHelper.getInstance().recognizeType = EXTRA_TYPE_RECOGNIZE_COMPARE
        val intent = Intent(mContext, RecognizeIdCardActivity::class.java)
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, trainingId)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_FACE_COMPARE)
    }


    private fun closeFaceDialog() {
        dialog?.dismiss()
    }


    private fun skipPlayVideoByType(trainingId: String?, courseType: Int) {
        var intent: Intent? = null
        when (courseType) {
            TYPE_COURSE_HTML -> {
                //todo 全部为html
                intent = Intent(mContext, HtmlBrowserActivity::class.java)
            }
            TYPE_COURSE_TYPE_DRIVE -> {
                //车学堂 使用腾讯播放器
                intent = Intent(mContext, TencentPlayVideoActivity::class.java)
            }
            TYPE_TYPE_COURSE_HLS -> {
                //hls 使用阿里播放器
                intent = Intent(mContext, AliYunPlayVideoActivity::class.java)
            }
            TYPE_COURSE_OTHER -> {
                //混合非加密 使用腾讯播放器
                intent = Intent(mContext, TencentPlayVideoActivity::class.java)
            }

        }
        intent?.putExtra(EXTRA_TRAINING_PLAN_ID, trainingId)
        startActivity(intent)
    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    /**
     * 收到消息
     *
     * @param userInfoEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserInfoRefreshEvent(userInfoEvent: UserInfoEvent?) {
        if (userInfoEvent == null) {
            return
        }
        if (userInfoEvent.userInfo == null) {
            removeData()
        } else {
            requestCourseOnLine()
        }

    }

    private fun removeData() {
        adapter?.data?.clear()
        adapter?.notifyDataSetChanged()
        refreshLayout?.finishRefresh()
    }


    private fun requestBusinessPayInfo(num: Int) {
        ApiRepository.getInstance().requestBusinessPayInfo(num).compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any?>>() {
            override fun onSuccessNext(entity: BaseResult<Any?>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.showSuccess("支付完成")
                    requestCourseOnLine()
                    inputAlert?.dismiss()
                } else {
                    ToastUtil.show(entity.msg)
                    inputAlert?.dismiss()
                }
            }
        })
    }


    private fun showInputDialog() {
        inputAlert = InputPayNumAlert(mContext)
        inputAlert!!.create().setConfirmClick {
            if (inputAlert!!.inputCount <= 0) {
                ToastUtil.show("请选择课程时长")
                return@setConfirmClick
            }
            requestBusinessPayInfo(inputAlert!!.inputCount)
            inputAlert!!.dismiss()
        }
                .setLimitCount(0, 12, 1).show()
    }


}
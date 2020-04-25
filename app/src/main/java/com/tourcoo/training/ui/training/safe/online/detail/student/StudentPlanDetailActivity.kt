package com.tourcoo.training.ui.training.safe.online.detail.student

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.alibaba.fastjson.JSON
import com.didichuxing.doraemonkit.zxing.activity.CaptureActivity
import com.tourcoo.training.R
import com.tourcoo.training.constant.TrainingConstant.*
import com.tourcoo.training.core.app.MyApplication
import com.tourcoo.training.core.base.mvp.BaseMvpTitleActivity
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.training.QrScanResult
import com.tourcoo.training.entity.training.TrainingPlanDetail
import com.tourcoo.training.ui.training.safe.online.TrainFaceCertifyActivity
import com.tourcoo.training.widget.dialog.training.CommonSuccessAlert
import com.tourcoo.training.widget.dialog.training.LocalTrainingConfirmDialog
import com.tourcoo.training.widget.websocket.SocketListener
import com.tourcoo.training.widget.websocket.WebSocketHandler
import com.tourcoo.training.widget.websocket.WebSocketSetting
import com.tourcoo.training.widget.websocket.response.ErrorResponse
import kotlinx.android.synthetic.main.activity_training_detail_student.*
import org.java_websocket.framing.Framedata
import java.nio.ByteBuffer
import java.text.SimpleDateFormat

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月22日12:39
 * @Email: 971613168@qq.com
 */
class StudentPlanDetailActivity : BaseMvpTitleActivity<StudentDetailPresenter>(), View.OnClickListener, StudentDetailContract.View, SocketListener {
    private var trainingPlanId = ""
    private val mTag = "StudentPlanDetailActivity"
    private var confirmDialog: LocalTrainingConfirmDialog? = null
    private var currentAction = ""

    companion object {
        const val REQUEST_CODE_SCAN = 1007
        const val REQUEST_CODE_SIGN_STUDENT = 1008
        const val EXTRA_PHOTO_PATH = "EXTRA_PHOTO_PATH"
        const val MSG_CODE_PROGRESS = 1
        const val MSG_CODE_CLOSE_PROGRESS = 201
    }

    override fun loadPresenter() {
        presenter.start()
        presenter.getTrainDetail(trainingPlanId)
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_training_detail_student
    }

    override fun createPresenter(): StudentDetailPresenter {
        return StudentDetailPresenter()
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("现场培训")
    }

    override fun initView(savedInstanceState: Bundle?) {
        trainingPlanId = intent.getStringExtra(EXTRA_TRAINING_PLAN_ID)
        if (TextUtils.isEmpty(trainingPlanId)) {
            ToastUtil.show("未获取培训计划")
            finish()
            return
        }
        if (!AccountHelper.getInstance().isLogin) {
            ToastUtil.show("请先登录")
            finish()
            return
        }
        ivStudentToOnline.setOnClickListener(this)
        ivSignStudent.setOnClickListener(this)
    }

    override fun showTurnOnlineSuccess() {
        //todo
        //转线上成功
        presenter.getTrainDetail(trainingPlanId)
    }

    override fun showTurnOnlineFailed() {
        //转线上失败回调
        closeConfirmDialog()
    }

    override fun doTurnOnline() {
        turnOnline()
    }


    override fun doShowTrainPlan(planDetail: TrainingPlanDetail?) {
        if (planDetail == null) {
            ToastUtil.show("未获取到计划详情")
            return
        }
        showTrainPlan(planDetail)
        setViewGone(ivSignStudent, true)
    }


    private fun showTrainPlan(planDetail: TrainingPlanDetail?) {
        if (planDetail == null) {
            return
        }
        tvTrainTitle.text = CommonUtil.getNotNullValue(planDetail.title)
        tvCoursePlanTime.text = CommonUtil.getNotNullValue(planDetail.cTime)
        tvLocate.text = CommonUtil.getNotNullValue(planDetail.classroomName)
        //todo
//        planDetail.status = 5
        when (planDetail.status)
            /**
             * 未开始
             */
        {
            TRAIN_STATUS_NO_START -> {
                //未开始 只有转线上按钮
                //todo
                setViewGone(rlButtonLayout, true)
                //转线上按钮
                setViewGone(ivStudentToOnline, true)
                //签到按钮
                setViewGone(ivSignStudent, false)
                //扫码按钮
                setViewGone(ivScanCode, false)
                //签退按钮
                setViewGone(ivSignOut, false)
                //签到相关信息
                setViewGone(llTrainStatusLayout, false)
                //底部按钮信息
                setViewGone(llBottomButtonLayout, false)

            }

            /**
             * 已签到
             */
            TRAIN_STATUS_SIGNED -> {
                //转线上按钮
                setViewGone(ivStudentToOnline, true)
                //签到按钮
                setViewGone(ivSignStudent, false)
                //扫码按钮
                setViewGone(ivScanCode, false)
                //签退按钮
                setViewGone(ivSignOut, true)
                //显示签到时间
                setViewGone(llSignedTime, true)
                //隐藏签退时间
                setViewGone(llSignedOutTime, false)
                //隐藏标签
                setViewGone(ivStatusTag, false)
                ivStatusTag.setImageResource(R.mipmap.ic_training_state_signed)
                setViewGone(llBottomButtonLayout, false)
            }
            /**
             * 已签退
             */
            TRAIN_STATUS_SIGN_OUT -> {
                //隐藏所有按钮
                setViewGone(rlButtonLayout, false)
                //显示签到签退时间信息
                setViewGone(llTrainStatusLayout, true)
                //隐藏标签
                setViewGone(ivStatusTag, false)
                setViewGone(llBottomButtonLayout, false)
            }
            /**
             * 已经转线上
             */
            TRAIN_STATUS_TO_ONLINE -> {
                //隐藏所有按钮
                setViewGone(rlButtonLayout, false)
                //显示签到签退时间信息
                setViewGone(llTrainStatusLayout, true)
                //显示转线上标签
                ivStatusTag.setImageResource(R.mipmap.ic_training_state_turn_online)
                setViewGone(ivStatusTag, true)
                setViewGone(llBottomButtonLayout, false)
            }

            /**
             * 已结束
             */
            TRAIN_STATUS_END -> {
                //隐藏所有按钮
                setViewGone(rlButtonLayout, false)
                //显示签到签退时间信息
                setViewGone(llTrainStatusLayout, true)
                //显示已结束标签
                ivStatusTag.setImageResource(R.mipmap.ic_training_state_end)
                setViewGone(ivStatusTag, true)
                setViewGone(llBottomButtonLayout, false)
            }

            /**
             * 不合格
             */
            TRAIN_STATUS_NO_PASS -> {
                //隐藏所有按钮
                setViewGone(rlButtonLayout, false)
                //显示签到签退时间信息
                setViewGone(llTrainStatusLayout, true)
                //显示已结束标签
                ivStatusTag.setImageResource(R.mipmap.ic_training_state_no_pass)
                setViewGone(ivStatusTag, true)
                setViewGone(llBottomButtonLayout, false)
            }

            /**
             * 待考试
             */
            TRAIN_STATUS_WAIT_EXAM -> {
                //隐藏所有按钮
                setViewGone(rlButtonLayout, false)
                //显示签到签退时间信息
                setViewGone(llTrainStatusLayout, true)
                //隐藏标签
                setViewGone(ivStatusTag, false)
                setViewGone(llBottomButtonLayout, true)
                setViewGone(ivWaitExam, true)

            }

            else -> {
            }
        }


    }

    /**
     * 转线上弹窗
     */
    private fun turnOnline() {
        confirmDialog = LocalTrainingConfirmDialog(mContext)
        confirmDialog!!.create().setContent("确定从现场学习转到线上学习吗？").setPositiveButton {
            presenter.getTurnOnline(trainingPlanId)
        }.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivStudentToOnline -> {
                //执行转线上
                doTurnOnline()
            }
            R.id.ivSignStudent -> {
                //学生签到
                studentSign()
            }

            else -> {
            }
        }
    }


    private fun closeConfirmDialog() {
        if (confirmDialog != null) {
            confirmDialog!!.dismiss()

        }
    }


    private fun initWebSocket(url: String) {
        val setting = WebSocketSetting()
        //连接地址，必填，例如 wss://echo.websocket.org
        setting.connectUrl = url //必填
        TourCooLogUtil.i(mTag, "connectUrl = $url")
        //设置连接超时时间
        setting.connectTimeout = 15 * 1000
        //设置心跳间隔时间
        setting.connectionLostTimeout = 60
        //设置断开后的重连次数，可以设置的很大，不会有什么性能上的影响
        setting.reconnectFrequency = 60
        //网络状态发生变化后是否重连，
//需要调用 WebSocketHandler.registerNetworkChangedReceiver(context) 方法注册网络监听广播
        setting.setReconnectWithNetworkChanged(true)
        //通过 init 方法初始化默认的 WebSocketManager 对象
        WebSocketHandler.init(setting)
        WebSocketHandler.getDefault().addListener(this)
        WebSocketHandler.getDefault().start()
        //注意，需要在 AndroidManifest 中配置网络状态获取权限
//注册网路连接状态变化广播
        WebSocketHandler.registerNetworkChangedReceiver(MyApplication.getContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (WebSocketHandler.getDefault() != null) {
            WebSocketHandler.getDefault().removeListener(this)
            WebSocketHandler.getDefault().disConnect()
        }

    }

    override fun onConnectFailed(e: Throwable?) {
    }

    override fun onSendDataError(errorResponse: ErrorResponse?) {
    }

    override fun onConnected() {
        TourCooLogUtil.i(mTag, "连接成功")
    }

    override fun <T : Any?> onMessage(message: String?, data: T) {
        ToastUtil.showFailed("webSocket:onMessage---" + data + "message =" + message)
        TourCooLogUtil.i(mTag, "webSocket:onMessage---" + data + "message =" + message)
    }

    override fun <T : Any?> onMessage(bytes: ByteBuffer?, data: T) {
    }

    override fun onDisconnect() {
        TourCooLogUtil.d(mTag, "断开连接")
    }

    override fun onPong(framedata: Framedata?) {
    }

    override fun onPing(framedata: Framedata?) {
    }


    private fun scanCode() {
        val intent = Intent(mContext, CaptureActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_SCAN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SCAN -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN)
                    if (result != null) {
                        when (currentAction) {
                            ACTION_STUDENT_SIGN -> {
                                handleScanSignCallback(result)
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
            /**
             * 签到成功回调
             */
            REQUEST_CODE_SIGN_STUDENT -> {
                //签到成功后 连接socket
                val socketUrl = BASE_SOCKET_URL_ + AccountHelper.getInstance().userInfo.accessToken + "&trainingPlanId=" + trainingPlanId
                initWebSocket(socketUrl)
                showSignSuccess()
            }
            else -> {
            }
        }
    }

    /**
     * 学生签到
     */
    private fun studentSign() {
        //学生签到扫码
        currentAction = ACTION_STUDENT_SIGN
        scanCode()
    }


    private fun skipSignFaceCertify(qrScanResult: QrScanResult?) {
        if (qrScanResult == null) {
            ToastUtil.show("未获取到相应数据")
            return
        }
        //跳转到培训相关的人脸识别
        val intent = Intent(this, TrainFaceCertifyActivity::class.java)
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, qrScanResult.trainingPlanID)
        intent.putExtra(EXTRA_KEY_QR_SCAN_RESULT, qrScanResult)
        //学生签到的action
        intent.putExtra(EXTRA_TRAIN_ACTION_KEY, ACTION_STUDENT_SIGN)
        startActivityForResult(intent, REQUEST_CODE_SIGN_STUDENT)
    }

    private fun handleScanSignCallback(result: String) {
        try {
            val scanResult = JSON.parseObject(result, QrScanResult::class.java)
            skipSignFaceCertify(scanResult)
        } catch (e: Exception) {
            ToastUtil.show("当前二维码无效")
        }
    }


    private fun showSignSuccess() {
        val dialog = CommonSuccessAlert(mContext)
        dialog.create().setAlertTitle("学员签到成功")
        val currentTime = System.currentTimeMillis()
        val timeNow: String = SimpleDateFormat("yyyy-MM-dd HH:mm").format(currentTime)
        dialog.setContent(timeNow).show()

    }
    /*  //初始化WebSocket连接
      initWebSocket(socketUrl)*/
}
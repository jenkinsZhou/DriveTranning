package com.tourcoo.training.ui.training.safe.online.detail.teacher

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.SpanUtils
import com.didichuxing.doraemonkit.zxing.activity.CaptureActivity
import com.tourcoo.training.R
import com.tourcoo.training.constant.TrainingConstant
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
import com.tourcoo.training.widget.dialog.CommonBellAlert
import com.tourcoo.training.widget.dialog.training.CommonSuccessAlert
import com.tourcoo.training.widget.dialog.training.LocalTrainingConfirmDialog
import com.tourcoo.training.widget.websocket.SocketListener
import com.tourcoo.training.widget.websocket.WebSocketHandler
import com.tourcoo.training.widget.websocket.WebSocketSetting
import com.tourcoo.training.widget.websocket.response.ErrorResponse
import kotlinx.android.synthetic.main.activity_training_detail_teacher.*
import org.java_websocket.framing.Framedata
import java.nio.ByteBuffer
import java.text.SimpleDateFormat

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月23日14:54
 * @Email: 971613168@qq.com
 */
class TeacherPlanDetailActivity : BaseMvpTitleActivity<TeacherDetailPresenter>(), View.OnClickListener, TeacherDetailContract.View, SocketListener {
    private var trainingPlanId = ""
    private val mTag = "TeacherPlanDetailActivity"
    private var confirmDialog: LocalTrainingConfirmDialog? = null


    companion object {
        const val REQUEST_CODE_SCAN = 1007
        const val REQUEST_CODE_SIGN_STUDENT = 1008
        const val REQUEST_CODE_SIGN_OUT_STUDENT = 1009
        const val REQUEST_CODE_CHECK_STATUS_STUDENT = 1010
        const val EXTRA_PHOTO_PATH = "EXTRA_PHOTO_PATH"
        const val MSG_CODE_PROGRESS = 1
        const val MSG_CODE_CLOSE_PROGRESS = 201
    }

    override fun loadPresenter() {
        presenter.start()
        presenter.getTrainDetail(trainingPlanId)

        val socketUrl = TrainingConstant.BASE_SOCKET_URL_ + AccountHelper.getInstance().userInfo.accessToken + "&trainingPlanId=" + trainingPlanId
        //初始化WebSocket连接
        initWebSocket(socketUrl)
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_training_detail_teacher
    }

    override fun createPresenter(): TeacherDetailPresenter {
        return TeacherDetailPresenter()
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("现场培训")
    }

    override fun initView(savedInstanceState: Bundle?) {
        trainingPlanId = intent.getStringExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID)
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

        ivSignTeacher.setOnClickListener(this)
        ivToOnline.setOnClickListener(this)

    }

    override fun showTurnOnlineSuccess() {
        //转线上成功
        presenter.getTrainDetail(trainingPlanId)
        //转线上成功回调
        closeConfirmDialog()
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
    }


    private fun showTrainPlan(planDetail: TrainingPlanDetail?) {
        if (planDetail == null) {
            return
        }
        tvCourseName.text = CommonUtil.getNotNullValue(planDetail.title)
        tvCoursePlanTime.text = CommonUtil.getNotNullValue(planDetail.sTime)
        tvLocate.text = CommonUtil.getNotNullValue(planDetail.classroomName)
        tvRole.text = "安全员"
        tvCourseTime.text = CommonUtil.getNotNullValue("" + planDetail.courseTime + "课时")

        when (planDetail.safetyManagerStatus)
            /**
             * 未开始
             */
        {
            TrainingConstant.TRAIN_STATUS_NO_START -> {
                //未开始 安全员只有一个签到按钮
                //只显示签到按钮
                setViewGone(rlButtonTeacherLayout, true)
                //显示转线上
                setViewGone(ivToOnline, false)
                setViewGone(ivSignTeacher, true)
                //隐藏签到时间相关信息
                setViewGone(llTrainStatusTeacherLayout, false)

            }

            /**
             * 已签到 显示预计结束时间 隐藏结束时间 和 标签图片
             */
            TrainingConstant.TRAIN_STATUS_SIGNED -> {
                // 安全员已签到 隐藏签到模块布局按钮
                setViewGone(rlButtonTeacherLayout, false)
                setViewVisible(ivSignTeacher, false)
                //显示转线上
                setViewGone(ivToOnline, false)

                setViewGone(llTrainStatusTeacherLayout, true)

                //显示签到时间 和预计结束时间
                setViewGone(llSignedTime, true)
                setViewGone(llPreEndTime, true)
                //隐藏结束时间
                setViewGone(llEndTime, false)

                //设置签到时间
                tvTeacherSignTime.text = CommonUtil.getNotNullValue(planDetail.saftyManagerSignIn)
                //显示安全员预计结束时间
                tvPreEndTime.text = CommonUtil.getNotNullValue(planDetail.eTime)
                //隐藏标签图片
                setViewGone(ivStatusTagTeacher, false)
            }


            /**
             * 已签退
             */
            TrainingConstant.TRAIN_STATUS_SIGN_OUT -> {
                // 安全员已签退 隐藏签到模块布局按钮
                setViewGone(rlButtonTeacherLayout, false)
                setViewVisible(ivSignTeacher, false)
                //显示转线上
                setViewGone(ivToOnline, false)

                setViewGone(llTrainStatusTeacherLayout, true)

                //显示签到时间 和预计结束时间
                setViewGone(llSignedTime, true)
                setViewGone(llPreEndTime, true)
                //隐藏结束时间
                setViewGone(llEndTime, false)

                //设置签到时间
                tvTeacherSignTime.text = CommonUtil.getNotNullValue(planDetail.saftyManagerSignIn)
                //显示安全员预计结束时间
                tvPreEndTime.text = CommonUtil.getNotNullValue(planDetail.eTime)
                //隐藏标签图片
                setViewGone(ivStatusTagTeacher, false)
            }


            /**
             * 已结束
             */
            TrainingConstant.TRAIN_STATUS_END -> {
                // 安全员已签到 隐藏签到模块布局按钮
                setViewGone(rlButtonTeacherLayout, false)
                setViewGone(llTrainStatusTeacherLayout, true)
                //显示签到时间 和结束时间
                setViewGone(llSignedTime, true)
                //隐藏预计结束时间
                setViewGone(llPreEndTime, false)
                setViewGone(llEndTime, true)
                //设置签到时间
                tvTeacherSignTime.text = CommonUtil.getNotNullValue(planDetail.saftyManagerSignIn)
                tvTeacherEndTime.text = CommonUtil.getNotNullValue(planDetail.eTime)

                //显示标签图片
                setViewGone(ivStatusTagTeacher, true)
                ivStatusTagTeacher.setImageResource(R.mipmap.ic_training_state_end)
            }


            /**
             * 已转线上
             */
            TrainingConstant.TRAIN_STATUS_TO_ONLINE -> {
                // 安全员已签到 隐藏签到模块布局按钮
                setViewGone(rlButtonTeacherLayout, false)
                setViewGone(llTrainStatusTeacherLayout, true)
                //显示签到时间 和结束时间
                setViewGone(llSignedTime, true)
                setViewGone(llPreEndTime, false)
                //隐藏预计结束时间
                setViewGone(llEndTime, false)

                //显示标签图片
                setViewGone(ivStatusTagTeacher, true)
                ivStatusTagTeacher.setImageResource(R.mipmap.ic_training_state_turn_online)
            }


            else -> {
                // 安全员已签到 隐藏签到模块布局按钮
                setViewGone(rlButtonTeacherLayout, false)
                setViewGone(llTrainStatusTeacherLayout, true)
                //显示签到时间 和结束时间
                setViewGone(llSignedTime, true)
                setViewGone(llPreEndTime, false)
                //隐藏预计结束时间
                setViewGone(llEndTime, false)

                //隐藏标签图片
                setViewGone(ivStatusTagTeacher, false)
            }
        }


    }

    /**
     * 转线上弹窗
     */
    private fun turnOnline() {
        confirmDialog = LocalTrainingConfirmDialog(mContext)

        confirmDialog!!.create()
                .setContent(SpanUtils()
                        .append("您是当前计划的 ").setForegroundColor(Color.parseColor("#333333")).setFontSize(15, true)
                        .append("『安全员』").setForegroundColor(Color.parseColor("#FF736C")).setFontSize(15, true)
                        .append(",确定从现场学习转到线上学习吗？").setForegroundColor(Color.parseColor("#333333")).setFontSize(15, true)
                        .create())
                .setPositiveButton {
                    presenter.getTurnOnline(trainingPlanId)
                }.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivToOnline -> {
                //执行转线上
                doTurnOnline()
            }
            R.id.ivSignTeacher -> {
                //安全员签到逻辑
                safeManagerSign()
            }

            else -> {
            }
        }
    }

    private var currentAction = ""
    /**
     * 安全员签到，后台默认学员身份已签到
     */
    private fun safeManagerSign() {
        //学生签到扫码
        currentAction = TrainingConstant.ACTION_SAFE_MANAGER_SIGN
        scanCode()
    }

    private fun scanCode() {
        val intent = Intent(mContext, CaptureActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_SCAN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (data == null) {
            return
        }

        when (requestCode) {
            REQUEST_CODE_SCAN -> {
                val result = data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN)
                if (result != null) {
                    when (currentAction) {

                        TrainingConstant.ACTION_SAFE_MANAGER_SIGN -> {
                            handleScanSignCallback(result, TrainingConstant.SCENE_SAFE_MANAGER_SIGN_IN)
                        }

                        else -> {
                        }
                    }
                }
            }
            /**
             * 签到成功回调
             */
            REQUEST_CODE_SIGN_STUDENT -> {
                showSafeManagerSignSuccess()
            }
            else -> {
            }
        }
    }


    private fun showSafeManagerSignSuccess() {
        presenter.getTrainDetail(
                trainingPlanId)
        val dialog = CommonSuccessAlert(mContext)
        dialog.create().setAlertTitle("安全员签到成功")
        val currentTime = System.currentTimeMillis()
        val timeNow: String = SimpleDateFormat("yyyy-MM-dd HH:mm").format(currentTime)
        dialog.setContent(timeNow).show()
    }


    private fun handleScanSignCallback(result: String, scene: Int) {
        try {
            val scanResult = JSON.parseObject(result, QrScanResult::class.java)
            when (scene) {
                TrainingConstant.SCENE_SAFE_MANAGER_SIGN_IN -> {
                    if (scanResult.scene.toInt() != TrainingConstant.SCENE_SAFE_MANAGER_SIGN_IN) {
                        ToastUtil.show("请扫描正确的场景二维码")
                    } else {
                        skipSignFaceCertify(scanResult)
                    }
                }

                else -> {
                    ToastUtil.show("请扫描正确的场景二维码")
                }
            }


        } catch (e: Exception) {
            ToastUtil.show("当前二维码无效")
        }
    }


    private fun skipSignFaceCertify(qrScanResult: QrScanResult?) {
        if (qrScanResult == null) {
            ToastUtil.show("未获取到相应数据")
            return
        }
        //跳转到培训相关的人脸识别
        val intent = Intent(this, TrainFaceCertifyActivity::class.java)
        intent.putExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID, qrScanResult.trainingPlanID)
        intent.putExtra(TrainingConstant.EXTRA_KEY_QR_SCAN_RESULT, qrScanResult)

        startActivityForResult(intent, REQUEST_CODE_SIGN_STUDENT)
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
        WebSocketHandler.getDefault().removeListener(this)
        WebSocketHandler.getDefault().disConnect()
    }

    override fun onConnectFailed(e: Throwable?) {
    }

    override fun onSendDataError(errorResponse: ErrorResponse?) {
    }

    override fun onConnected() {
        TourCooLogUtil.i(mTag, "连接成功")
    }

    override fun <T : Any?> onMessage(message: String?, data: T) {
        ToastUtil.showSuccess("onMessage---" + data + "message =" + message)
        presenter.getTrainDetail(trainingPlanId)
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
}
package com.tourcoo.training.ui.training.safe.online.detail.teacher

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.constant.TrainingConstant
import com.tourcoo.training.core.app.MyApplication
import com.tourcoo.training.core.base.mvp.BaseMvpTitleActivity
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.training.TrainingPlanDetail
import com.tourcoo.training.widget.dialog.training.LocalTrainingConfirmDialog
import com.tourcoo.training.widget.websocket.SocketListener
import com.tourcoo.training.widget.websocket.WebSocketHandler
import com.tourcoo.training.widget.websocket.WebSocketSetting
import com.tourcoo.training.widget.websocket.response.ErrorResponse
import kotlinx.android.synthetic.main.activity_training_detail_teacher.*
import org.java_websocket.framing.Framedata
import java.nio.ByteBuffer

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月23日14:54
 * @Email: 971613168@qq.com
 */
class TeacherPlanDetailActivity: BaseMvpTitleActivity<TeacherDetailPresenter>() ,View.OnClickListener, TeacherDetailContract.View, SocketListener {
    private var trainingPlanId = ""
    private val mTag = "TeacherPlanDetailActivity"
    private var confirmDialog: LocalTrainingConfirmDialog? = null
    override fun loadPresenter() {
        presenter.start()
        presenter.getTrainDetail(trainingPlanId)
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
        val socketUrl = TrainingConstant.BASE_SOCKET_URL_ + AccountHelper.getInstance().userInfo.accessToken + "&trainingPlanId=" + trainingPlanId
        //初始化WebSocket连接
        initWebSocket(socketUrl)
        setViewGone(ivSignTeacher, true)
    }


    private fun showTrainPlan(planDetail: TrainingPlanDetail?) {
        if (planDetail == null) {
            return
        }
        tvCourseName.text = CommonUtil.getNotNullValue(planDetail.title)
        tvCoursePlanTime.text = CommonUtil.getNotNullValue(planDetail.cTime)
        tvLocate.text = CommonUtil.getNotNullValue(planDetail.classroomName)
        tvRole.text = "安全员"
        //todo
//        planDetail.status = 5
        when (planDetail.status)
            /**
             * 未开始
             */
        {
            TrainingConstant.TRAIN_STATUS_NO_START -> {
                //未开始 安全员只有一个签到按钮
                //todo
                //只显示签到按钮
                setViewGone(rlButtonTeacherLayout, true)
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
                setViewGone(llTrainStatusTeacherLayout, true)
                //显示签到时间 和预计结束时间
                setViewGone(llPreEndTime, true)
                //隐藏结束时间
                setViewGone(llEndTime, false)
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
                setViewGone(llPreEndTime, false)
                //隐藏结束时间
                setViewGone(llEndTime, true)
                //隐藏标签图片
                setViewGone(ivStatusTagTeacher, true)
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
                //学员签到逻辑
                WebSocketHandler.getDefault().start()!!.send("测试数据")
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
package com.tourcoo.training.ui.pay

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import com.alipay.sdk.app.PayTask
import com.google.gson.Gson
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tourcoo.training.R
import com.tourcoo.training.constant.TrainingConstant
import com.tourcoo.training.core.base.mvp.BaseMvpTitleActivity
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.PayInfo
import com.tourcoo.training.entity.account.PayResult
import com.tourcoo.training.entity.pay.WxPayModel
import com.tourcoo.training.entity.pay.CoursePayInfo
import com.tourcoo.training.entity.pay.PayResultEvent
import com.tourcoo.training.entity.pay.WxPayEvent
import com.tourcoo.training.widget.dialog.common.CommonWaringAlert
import com.tourcoo.training.widget.dialog.training.CommonSuccessAlert
import kotlinx.android.synthetic.main.activity_pay_buy_now.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 *@description :立即购买（支付）
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月02日15:11
 * @Email: 971613168@qq.com
 */
class BuyNowActivity : BaseMvpTitleActivity<BuyNowPresenter>(), BuyNowContract.View {

    private var wxApi: IWXAPI? = null
    override fun createPresenter(): BuyNowPresenter {
        return BuyNowPresenter()
    }

    override fun loadPresenter() {
        presenter.start()
        presenter.getPayInfo(trainingPlanId)
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_pay_buy_now
    }


    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("立即购买")
    }

    private lateinit var trainingPlanId: String
    override fun initView(savedInstanceState: Bundle?) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        trainingPlanId = intent.getStringExtra("trainingPlanID")
        // 将该app注册到微信
        wxApi = WXAPIFactory.createWXAPI(this, TrainingConstant.APP_ID)
    }

    override fun getPayInfoSuccess(payInfo: CoursePayInfo) {

        tvCompanyRemain.text = "剩余学币：" + payInfo.companyCoinRemain + "个"
        tvCoinRemain.text = "剩余学币：" + payInfo.coinRemain + "个"

        radio_group.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbCompany -> {
                    tvTitle.text = "支付学币"
                    tvTagYuan.visibility = View.GONE
                    tvTagUnit.visibility = View.VISIBLE
                    tvAmount.text = "" + payInfo.coins

                    btnBuy.isEnabled = payInfo.companyCoinRemain >= payInfo.coins
                }

                R.id.rbUs -> {
                    tvTitle.text = "支付学币"
                    tvTagYuan.visibility = View.GONE
                    tvTagUnit.visibility = View.VISIBLE
                    tvAmount.text = "" + payInfo.coins

                    btnBuy.isEnabled = payInfo.coinRemain >= payInfo.coins
                }

                R.id.rbAlipay, R.id.rbWx -> {
                    tvTitle.text = "支付金额"
                    tvTagYuan.visibility = View.VISIBLE
                    tvTagUnit.visibility = View.GONE
                    tvAmount.text = "" + CommonUtil.doubleTransStringZhen(payInfo.price / 100)
                    btnBuy.isEnabled = true
                }
            }

        }

        when {
            payInfo.companyCoinRemain >= payInfo.coins -> {
                rbCompany.isChecked = true
            }
            payInfo.coinRemain >= payInfo.coins -> {
                rbUs.isChecked = true
            }
            else -> {
                rbAlipay.isChecked = true
            }
        }

        btnBuy.setOnClickListener {
            doBuyNow()
        }
        when (payInfo.paymentMode) {
            1 -> {
                //自付
                setViewGone(llCoinGroup, false)
                //只显示现金支付
                setViewGone(llCashGroup, true)
            }
            2 -> {
                //个人学币
                setViewGone(llCoinGroup, true)
                //隐藏企业学币支付
                setViewGone(ffCompany, false)
                //屏蔽现金支付
                setViewGone(llCashGroup, false)
            }
            3 -> {
                //自付+个人学币支付
                setViewGone(llCoinGroup, true)
                setViewGone(llCashGroup, true)
                //隐藏企业学币支付
                setViewGone(ffCompany, false)
                //显示个人学币支付
                setViewGone(ffUser, true)
            }
            4 -> {
                //企业学币支付
                setViewGone(llCoinGroup, true)
                setViewGone(ffCompany, true)
                setViewGone(llCashGroup, false)
                //隐藏现金支付
                setViewGone(llCashGroup, false)
                setViewGone(ffUser, false)
            }
            5 -> {
                //自付 + 企业学币
                setViewGone(llCoinGroup, true)
                setViewGone(llCashGroup, true)
                setViewGone(ffCompany, true)
                setViewGone(ffUser, false)
            }
            6 -> {
                //个人学币 + 企业学币
                setViewGone(llCoinGroup, true)
                setViewGone(llCashGroup, false)
                setViewGone(ffCompany, true)
                setViewGone(ffUser, true)
            }

            else -> {
                //全部都显示
                setViewGone(llCoinGroup, true)
                setViewGone(llCashGroup, true)
                setViewGone(ffCompany, true)
                setViewGone(ffUser, true)
            }
        }


    }


    override fun setPayInfo(payType: Int, payInfo: PayInfo?) {
        if (payType == 1 || payType == 2) {
            //学币支付成功
            doHandlePaySuccess()
        } else if (payType == 3) {
            if (payInfo == null) {
                ToastUtil.show("支付参数异常")
                return
            }
            payByAlipay(payInfo.thirdPayInfo.toString())
        } else if (payType == 4) {
            if (payInfo == null) {
                ToastUtil.show("支付参数异常")
                return
            }
            payByWx(payInfo.thirdPayInfo)
        }

    }

    override fun payFailed(message: String?) {
        showPayFailed()
    }

    private val SDK_PAY_FLAG = 1
    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String, String>)
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo = payResult.getResult()// 同步返回需要验证的信息
                    val resultStatus = payResult.getResultStatus()

                    TourCooLogUtil.d(resultInfo)

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        doHandlePaySuccess()
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show(payResult.memo)
                    }
                }
                else -> {
                }
            }
        }
    }


    private fun payByAlipay(orderInfo: String) {

        val payRunnable = Runnable {
            val alipay = PayTask(this)
            val result = alipay.payV2(orderInfo, true)

            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }
        // 必须异步调用
        val payThread = Thread(payRunnable)
        payThread.start()
    }


    private fun payByWx(orderInfo: Any) {
        val wxPayModelStr = Gson().toJson(orderInfo)
        val wxPayModel = Gson().fromJson<WxPayModel>(wxPayModelStr, WxPayModel::class.java)
        if (wxPayModel == null) {
            ToastUtil.show("微信支付数据异常")
            return
        }

        if (!wxApi!!.isWXAppInstalled) {
            ToastUtil.show("您尚未安装微信客户端")
            return
        }

        val request = PayReq()
        request.appId = wxPayModel.appid
        request.partnerId = wxPayModel.partnerId
        request.prepayId = wxPayModel.prepayid
        request.packageValue = "Sign=WXPay"
        request.nonceStr = wxPayModel.noncestr
        request.sign = wxPayModel.sign
        //todo 微信支付
        request.timeStamp = "" + wxPayModel.timestamp

        wxApi!!.sendReq(request)

    }


    /**
     * 收到消息
     *
     * @param payEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWxPayCallbackEvent(payEvent: WxPayEvent?) {
        if (payEvent == null) {
            return
        }
        if (payEvent.paySuccess) {
            doHandlePaySuccess()
        } else {
            ToastUtil.show("支付未完成")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        wxApi?.detach()
        EventBus.getDefault().unregister(this)
    }

    /**
     * 执行支付成功回调的地方 支付宝微信 学币支付通用
     */
    private fun doHandlePaySuccess() {
        showPaySuccessDialog()
    }

    /**
     * 真正处理支付成功回调的地方
     */
    private fun handlerPaySuccessResult() {
        //支付成功 发送消息通知其他页面
        EventBus.getDefault().post(PayResultEvent(true))
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun showPaySuccessDialog() {
        val dialog = CommonSuccessAlert(mContext)
        dialog.create().setAlertTitle("支付成功")
        dialog.setCancelTouchOutSide(false)
        val successAlert: String = "现在您可以开始学习了~"
        dialog.setConfirmClick(View.OnClickListener {
            baseHandler.postDelayed(Runnable {
                handlerPaySuccessResult()
            }, 500)
        })
        dialog.setContent(successAlert).show()
    }


    private fun showPayFailed() {
        val alert = CommonWaringAlert(mContext)
        alert.create().setTitle("支付失败").setContent("如未完成支付请重新支付").setPositiveButtonClick("继续支付", object : View.OnClickListener {
            override fun onClick(v: View?) {
                doBuyNow()
            }
        })
        alert.show()
    }


    private fun doBuyNow() {
        val payType = if (rbCompany.isChecked) 1
        else if (rbUs.isChecked) 2
        else if (rbAlipay.isChecked) 3
        else if (rbWx.isChecked) 4
        else 1
        presenter.buyCourse(trainingPlanId, payType)
    }
}
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
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tourcoo.training.R
import com.tourcoo.training.constant.TrainingConstant
import com.tourcoo.training.core.base.mvp.BaseMvpTitleActivity
import com.tourcoo.training.core.base.mvp.NullPresenter
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.PayInfo
import com.tourcoo.training.entity.account.PayResult
import com.tourcoo.training.entity.account.WxPayModel
import com.tourcoo.training.entity.pay.CoursePayInfo
import kotlinx.android.synthetic.main.activity_pay_buy_now.*

/**
 *@description :立即购买（支付）
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月02日15:11
 * @Email: 971613168@qq.com
 */
class BuyNowActivity : BaseMvpTitleActivity<BuyNowPresenter>(), BuyNowContract.View {

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
        trainingPlanId = intent.getStringExtra("trainingPlanID")
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
                    tvAmount.text = "" + payInfo.price

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
            val payType = if (rbCompany.isChecked) 1
            else if (rbUs.isChecked) 2
            else if (rbAlipay.isChecked) 3
            else if (rbWx.isChecked) 4
            else 1

            presenter.buyCourse(trainingPlanId, payType)
        }

    }


    override fun setPayInfo(payType: Int, payInfo: PayInfo?) {
        if (payType == 1 || payType == 2) {
            setResult(Activity.RESULT_OK)
            finish()
        } else if (payType == 3) {
            payByAlipay(payInfo!!.thirdPayInfo)
        } else if (payType == 4) {
            payByWx(payInfo!!.thirdPayInfo)
        }

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
                        ToastUtil.showSuccess("支付成功")
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.showFailed(payResult.memo)
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


    private fun payByWx(orderInfo: String) {
        val wxPayModel = Gson().fromJson<WxPayModel>(orderInfo, WxPayModel::class.java)

        if (wxPayModel == null) {
            ToastUtil.show("微信支付数据异常")
            return
        }

        // 将该app注册到微信
        val wxapi = WXAPIFactory.createWXAPI(this, TrainingConstant.APP_ID);

        if (!wxapi.isWXAppInstalled) {
            ToastUtil.show("您尚未安装微信客户端")
            return
        }

        val request = PayReq()
        request.appId = wxPayModel.appid
        request.partnerId = wxPayModel.mch_id
        request.prepayId = wxPayModel.prepay_id
        request.packageValue = "Sign=WXPay"
        request.nonceStr = wxPayModel.nonce_str
        request.sign = wxPayModel.sign
        request.timeStamp = "" + System.currentTimeMillis() / 1000

        wxapi.sendReq(request)

    }

}
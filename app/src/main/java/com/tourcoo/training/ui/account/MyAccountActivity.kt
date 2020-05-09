package com.tourcoo.training.ui.account

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.alipay.sdk.app.PayTask
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.gson.Gson
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tourcoo.training.R
import com.tourcoo.training.adapter.account.RechargeAmountAdapter
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.AppConfig.TEXT_REQUEST_ERROR
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.PayInfo
import com.tourcoo.training.entity.account.PayResult
import com.tourcoo.training.entity.account.RechargeEntity
import com.tourcoo.training.entity.account.UserInfoEvent
import com.tourcoo.training.entity.pay.WxPayEvent
import com.tourcoo.training.entity.pay.WxPayModel
import com.tourcoo.training.entity.recharge.CoinInfo
import com.tourcoo.training.entity.recharge.CoinPackageEntity
import com.tourcoo.training.widget.dialog.pay.BottomPayDialog
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_my_account.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月05日11:50
 * @Email: 971613168@qq.com
 */
class MyAccountActivity : BaseTitleActivity(), View.OnClickListener {
    private val mTag = "MyAccountActivity"
    private var mRechargeAmountAdapter: RechargeAmountAdapter? = null
    private var etCustomAmount: EditText? = null
    private var tvCustomAmount: TextView? = null
    private var rlContentView: RelativeLayout? = null
    private var payDialog: BottomPayDialog? = null
    private val mRechargeEntityList: MutableList<RechargeEntity> = ArrayList()
    private var mCoinList: MutableList<CoinInfo>? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_my_account
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("我的账户")
    }

    override fun initView(savedInstanceState: Bundle?) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        rvRecharge.layoutManager = GridLayoutManager(mContext, 3)
        tvConfirmPay.setOnClickListener(this)
        requestCoinPackage()
    }


    private fun loadPackageData(list: MutableList<CoinInfo>) {
        mRechargeEntityList.clear()
        mRechargeEntityList.addAll(transform(list))
        mRechargeAmountAdapter = RechargeAmountAdapter(mRechargeEntityList)
        mRechargeAmountAdapter?.bindToRecyclerView(rvRecharge)
        mRechargeAmountAdapter?.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            setSelect(position)
        })

    }


    /**
     * 设置选中属性
     *
     * @param position
     */
    private fun setSelect(position: Int) {
        if (mCoinList == null) {
            return
        }
        if (position >= mRechargeEntityList.size || position >= mCoinList!!.size) {
            return
        }
        val size = mRechargeEntityList.size
        var rechargeEntity: RechargeEntity
        var coin: CoinInfo
        for (i in 0 until size) {
            rechargeEntity = mRechargeEntityList[i]
            coin = mCoinList!![i]
            rechargeEntity.selected = i == position
            coin.isSelected = i == position

        }
        mRechargeAmountAdapter?.notifyDataSetChanged()
    }


    private fun requestEditFocus() {
        if (etCustomAmount == null) {
            return
        }
        etCustomAmount?.requestFocus()
        etCustomAmount?.isFocusable = true
        etCustomAmount?.isFocusableInTouchMode = true
        etCustomAmount?.requestFocus()
        rlContentView?.background = ContextCompat.getDrawable(mContext, R.drawable.selector_bg_radius_7_blue_hollow)
        etCustomAmount?.setTextColor(ResourceUtil.getColor(R.color.blue5087FF))
        setViewGone(tvCustomAmount, false)
        setViewGone(etCustomAmount, true)
        showInputMethod()

    }

    private fun showInputMethod() {
        //自动弹出键盘
        etCustomAmount?.inputType = InputType.TYPE_CLASS_NUMBER
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        //强制隐藏Android输入法窗口
// inputManager.hideSoftInputFromWindow(edit.getWindowToken(),0);
    }

    private fun loadCustomRechargeView() {
        if (mRechargeAmountAdapter!!.data.isEmpty()) {
            return
        }
        //View加载完成时回调
        rvRecharge.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                rvRecharge.viewTreeObserver.removeOnGlobalLayoutListener(this)
                loadCustomInputView()
            }

        })

    }

    private fun loadCustomInputView() {
        val view: View = rvRecharge.layoutManager?.getChildAt(0) ?: return
        val footView = LayoutInflater.from(mContext).inflate(R.layout.layout_custom_recharge, null)
        etCustomAmount = footView.findViewById(R.id.etCustomAmount)
        tvCustomAmount = footView.findViewById(R.id.tvCustomAmount)
        rlContentView = footView.findViewById(R.id.rlContentView)
        mRechargeAmountAdapter!!.addFooterView(footView)
        val layoutParams = footView.layoutParams
        val paddingTop = SizeUtil.dp2px(12f)
        val paddingStart = SizeUtil.dp2px(5f)
        layoutParams.height = view.height + paddingTop
        layoutParams.width = view.width + paddingStart
        footView.setPadding(paddingStart, paddingTop, 0, 0)
        footView.layoutParams = layoutParams
        footView.setOnClickListener(View.OnClickListener {
            requestEditFocus()
        })
    }

    /**
     * 移除EditText焦点
     */
    private fun clearEditFocus() {
        etCustomAmount?.setText("")
        setViewGone(tvCustomAmount, true)
        setViewGone(etCustomAmount, false)
        rlContentView?.background = ContextCompat.getDrawable(mContext, R.drawable.bg_radius_7_white_fffeff)
        etCustomAmount?.setTextColor(ResourceUtil.getColor(R.color.gray999999))
    }

    private fun transform(list: MutableList<CoinInfo>): MutableList<RechargeEntity> {
        val recharges = ArrayList<RechargeEntity>()
        for (coinInfo in list) {
            val rechargeEntity = RechargeEntity()
            rechargeEntity.rechargeDesc = coinInfo.desc
            rechargeEntity.accountBalance = coinInfo.coins
            rechargeEntity.selected = coinInfo.isSelected
            rechargeEntity.id = coinInfo.id
            rechargeEntity.rechargeMoney = coinInfo.price
            recharges.add(rechargeEntity)
        }
        //选中第一个
        if (recharges.isNotEmpty()) {
            recharges[0].selected = true
            list[0].isSelected = true
        }
        return recharges
    }

    private fun requestCoinPackage() {
        ApiRepository.getInstance().requestCoinPackage().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<CoinPackageEntity>>() {
            override fun onSuccessNext(entity: BaseResult<CoinPackageEntity>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS && entity.data.coinPackages != null) {
                    mCoinList = entity.data.coinPackages
                    loadPackageData(entity.data.coinPackages)
                    tvCurrentCoin.text = entity.data.coinsTotal.toString()
                } else {
                    ToastUtil.show(entity.msg)
                }
            }
        })
    }


    private fun showPay(coinInfo: CoinInfo?) {
        payDialog = BottomPayDialog(mContext).create()
        payDialog!!.show()
        payDialog!!.setPositiveButton(View.OnClickListener {
            requestRecharge(coinInfo)
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvConfirmPay -> {
                TourCooLogUtil.d(mTag, mRechargeEntityList)
                val coinInfo = getSelectPackage()
                if (coinInfo == null) {
                    ToastUtil.show("请先选择充值套餐")
                    return
                }
                showPay(coinInfo)
            }
            else -> {
            }
        }
    }

    /**
     * 充值
     */
    private fun requestRecharge(coinInfo: CoinInfo?) {
        if (payDialog == null || coinInfo == null) {
            ToastUtil.show("请先选择套餐类型")
            return
        }

        ApiRepository.getInstance().requestRecharge(coinInfo.id.toString(), payDialog!!.payType, ""+CommonUtil.doubleTransStringZhen(coinInfo.price), "1").compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<PayInfo>>() {
            override fun onSuccessNext(entity: BaseResult<PayInfo>?) {
                if (entity?.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    if (payDialog!!.payType == 1) {
                        payByAlipay(entity.data.thirdPayInfo.toString())
                    } else {
                        payByWx(entity.data.thirdPayInfo, entity.data)
                    }
                } else {
                    ToastUtil.show(entity?.msg)
                }
                payDialog?.dismiss()
            }

            override fun onError(e: Throwable) {
                dismissProgressDialog()
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed(e.toString())
                } else {
                    ToastUtil.show(TEXT_REQUEST_ERROR)
                }
                payDialog?.dismiss()
            }
        })
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
                    // 同步返回需要验证的信息
                    val resultInfo = payResult.getResult()
                    val resultStatus = payResult.getResultStatus()

                    TourCooLogUtil.d(resultInfo)

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.showSuccess("支付成功")
                        //刷新学币
                        requestCoinPackage()
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


    private fun payByWx(orderInfo: Any, patInfo: PayInfo) {
        val wxPayModelStr = Gson().toJson(orderInfo)
//        JSON.parseObject(wxPayModelStr,WxPayModel::class.java)
        val wxPayModel = Gson().fromJson<WxPayModel>(wxPayModelStr, WxPayModel::class.java)
        TourCooLogUtil.d("微信支付", wxPayModel)
        TourCooLogUtil.i("微信支付", wxPayModelStr)
        if (wxPayModel == null) {
            ToastUtil.show("微信支付数据异常")
            return
        }
        TourCooLogUtil.w("微信支付参数", wxPayModel)
        // 将该app注册到微信
        val wxapi = WXAPIFactory.createWXAPI(this, TrainingConstant.APP_ID);

        if (!wxapi.isWXAppInstalled) {
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
        request.timeStamp = "" + wxPayModel.timestamp
        TourCooLogUtil.d("微信支付参数", request)
        wxapi.sendReq(request)

    }

    private fun getSelectPackage(): CoinInfo? {
        if (mCoinList == null) {
            return null
        }
        for (coin in mCoinList!!) {
            if (coin.isSelected) {
                return coin
            }
        }
        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
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
            ToastUtil.showSuccess("支付成功")
            requestCoinPackage()
        } else {
            ToastUtil.show("支付未完成")
        }
    }
}
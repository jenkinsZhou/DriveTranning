package com.tourcoo.training.ui.account

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tourcoo.training.R
import com.tourcoo.training.adapter.account.RechargeAmountAdapter
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.RechargeEntity
import com.tourcoo.training.entity.recharge.CoinInfo
import com.tourcoo.training.entity.recharge.CoinPackageEntity
import com.tourcoo.training.widget.dialog.pay.BottomPayDialog
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_my_account.*
import java.util.*
import kotlin.collections.ArrayList


/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月05日11:50
 * @Email: 971613168@qq.com
 */
class MyAccountActivity : BaseTitleActivity(), View.OnClickListener {
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
        rvRecharge.layoutManager = GridLayoutManager(this, 3)
        tvConfirmPay.setOnClickListener(this)
        requestCoinPackage()
    }


    private fun loadPackageData(list: MutableList<CoinInfo>) {
        /*  mRechargeEntityList.add(RechargeEntity(20.00, true))
          mRechargeEntityList.add(RechargeEntity(30.00))
          mRechargeEntityList.add(RechargeEntity(50.00))
          mRechargeEntityList.add(RechargeEntity(70.00))
          mRechargeEntityList.add(RechargeEntity(90.00))
          mRechargeEntityList.add(RechargeEntity(120.00))*/
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
        if (position >= mRechargeEntityList.size) {
            return
        }
        if (position < 0) {
            for (entity in mRechargeEntityList) {
                entity.selected = false
            }

        } else {
            var rechargeEntity: RechargeEntity
            for (i in mRechargeEntityList.indices) {
                rechargeEntity = mRechargeEntityList[i]
                rechargeEntity.selected = i == position
            }
            clearEditFocus()
        }
        mRechargeAmountAdapter?.notifyDataSetChanged()
    }


    private fun requestEditFocus() {
        if (etCustomAmount == null) {
            return
        }
        setSelect(-1)
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
                    loadCustomRechargeView()
                } else {
                    ToastUtil.show(entity.msg)
                }
            }
        })
    }


    private fun showPay() {
        payDialog = BottomPayDialog(mContext).create()
        payDialog!!.show()
        payDialog!!.setPositiveButton(View.OnClickListener {
            if (mCoinList != null || mCoinList!!.isEmpty()) {
                //todo 后面再改
                requestRecharge(mCoinList!![0])
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvConfirmPay -> {
                showPay()
            }
            else -> {
            }
        }
    }


    private fun requestRecharge(coinInfo: CoinInfo?) {
        if (payDialog == null || coinInfo == null) {
            ToastUtil.show("请先选择套餐类型")
            return
        }

        ApiRepository.getInstance().requestRecharge(coinInfo.id.toString(), payDialog!!.payType, "1", "1").compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>>() {
            override fun onSuccessNext(entity: BaseResult<Any>?) {
                if (entity?.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.showSuccess("'充值成功")
                    payDialog?.dismiss()
                } else {
                    ToastUtil.show(entity?.msg)
                }
            }
        })
    }

    /*private fun getCurrentSelectCoin(): CoinInfo {
        //todo
        val dataList = mRechargeAmountAdapter?.data
        for (coin in dataList!!) {
            *//*  if(coin){

              }*//*
        }
        return null
    }*/
}
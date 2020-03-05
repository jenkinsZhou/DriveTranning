package com.tourcoo.training.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tourcoo.training.R
import com.tourcoo.training.adapter.account.RechargeAmountAdapter
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.RechargeEntity
import kotlinx.android.synthetic.main.activity_my_account.*
import java.util.*


/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月05日11:50
 * @Email: 971613168@qq.com
 */
class MyAccountActivity : BaseTitleActivity() {
    private var mRechargeAmountAdapter: RechargeAmountAdapter? = null
    private var etCustomAmount :EditText ?= null
    private var state =false
    private val mRechargeEntityList: MutableList<RechargeEntity> = ArrayList()
    override fun getContentLayout(): Int {
        return R.layout.activity_my_account
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("我的账户")
    }

    override fun initView(savedInstanceState: Bundle?) {
        rvRecharge.layoutManager = GridLayoutManager(this, 3)
        initData()
        loadCustomRechargeView()
    }


    private fun initData() {
        mRechargeEntityList.add(RechargeEntity(20.00, true))
        mRechargeEntityList.add(RechargeEntity(30.00))
        mRechargeEntityList.add(RechargeEntity(50.00))
        mRechargeEntityList.add(RechargeEntity(70.00))
        mRechargeEntityList.add(RechargeEntity(90.00))
        mRechargeEntityList.add(RechargeEntity(120.00))
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
            if(!state){
                mRechargeAmountAdapter?.notifyDataSetChanged()
            }
            requestEditFocus()
        }else{
            var rechargeEntity: RechargeEntity
            for (i in mRechargeEntityList.indices) {
                rechargeEntity = mRechargeEntityList[i]
                rechargeEntity.selected = i == position
            }
            mRechargeAmountAdapter?.notifyDataSetChanged()
        }
    }


    private fun requestEditFocus(){
        if(etCustomAmount == null){
            return
        }
        etCustomAmount?.requestFocus()
        etCustomAmount?.isFocusable = true
        etCustomAmount?.isFocusableInTouchMode = true
        etCustomAmount?.requestFocus()
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
        mRechargeAmountAdapter!!.addFooterView(footView)
        val layoutParams = footView.layoutParams
        val paddingTop = SizeUtil.dp2px(12f)
        val paddingStart = SizeUtil.dp2px(5f)
        layoutParams.height = view.height + paddingTop
        layoutParams.width = view.width + paddingStart
        footView.setPadding(paddingStart, paddingTop, 0, 0)
        footView.layoutParams = layoutParams
        etCustomAmount?.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                state = hasFocus
                baseHandler.postDelayed(Runnable {
                    ToastUtil.show("点击了")
                    setSelect(-1)
                },300)
            }
        })

    }

}
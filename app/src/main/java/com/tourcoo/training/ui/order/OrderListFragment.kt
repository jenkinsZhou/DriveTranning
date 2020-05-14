package com.tourcoo.training.ui.order

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tourcoo.training.R
import com.tourcoo.training.adapter.order.OrderAdapter
import com.tourcoo.training.adapter.order.OrderAdapter.ORDER_TYPE_COST
import com.tourcoo.training.adapter.order.OrderAdapter.ORDER_TYPE_RECHARGE
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.entity.BasePageResult
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.base.fragment.BaseRefreshLoadFragment
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.StackUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.entity.exam.Question
import com.tourcoo.training.entity.order.OrderEntity
import com.tourcoo.training.ui.account.MyAccountActivity
import com.trello.rxlifecycle3.android.FragmentEvent

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年05月08日14:53
 * @Email: 971613168@qq.com
 */
class OrderListFragment : BaseRefreshLoadFragment<OrderEntity>() {
    private var mType = 0
    private var adapter: OrderAdapter? = null
    private var hostActivity: Activity? = null

    override fun getAdapter(): BaseQuickAdapter<OrderEntity, BaseViewHolder> {
        adapter = OrderAdapter()
        return adapter as OrderAdapter
    }

    override fun initView(savedInstanceState: Bundle?) {
        mType = arguments!!.getInt("type")
    }

    override fun getContentLayout(): Int {
        return R.layout.frame_layout_refresh_recycler
    }

    override fun loadData(page: Int) {
        initItemClick()
        requestOrderList(page)
    }


    private fun requestOrderList(page: Int) {
        ApiRepository.getInstance().requestOrderList(page, mType).compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<OrderEntity>>?>(iHttpRequestControl) {
            override fun onSuccessNext(entity: BaseResult<MutableList<OrderEntity>>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    UiManager.getInstance().httpRequestControl.httpRequestSuccess(iHttpRequestControl, if (entity.data == null) ArrayList() else transformList(entity.data), null)
                } else {
                    ToastUtil.show(entity.msg)
                }

            }

        })
    }

    private fun transformList(list: MutableList<OrderEntity>): MutableList<OrderEntity> {
        for (orderEntity in list) {
            if (orderEntity.title.contains("充值")) {
                orderEntity.orderType = ORDER_TYPE_RECHARGE
            } else {
                orderEntity.orderType = ORDER_TYPE_COST
            }
        }
        return list
    }


    companion object {
        fun newInstance(type: Int): OrderListFragment {
            val args = Bundle()
            args.putInt("type", type)
            val fragment = OrderListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun initItemClick() {
        adapter!!.setOnItemChildClickListener { adapter, view, position ->
            when (view?.id) {
                R.id.btnOne -> {
                    skipRecharge()
                }
                else -> {
                }
            }
        }
    }

    private fun skipRecharge() {
        val intent = Intent(mContext, MyAccountActivity::class.java)
        startActivityForResult(intent, 2012)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
//            mRefreshLayout.autoRefresh()
            hostActivity = StackUtil.getInstance().getActivity(OrderListActivity::class.java)
            if (hostActivity != null) {
                hostActivity!!.finish()
            }
        }
    }

}
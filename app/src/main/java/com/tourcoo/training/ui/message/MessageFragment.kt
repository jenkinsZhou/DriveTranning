package com.tourcoo.training.ui.message

import android.content.Intent
import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.entity.BasePageResult
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.base.fragment.BaseRefreshLoadFragment
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.entity.message.MessageEntity
import com.tourcoo.training.entity.order.OrderEntity
import com.tourcoo.training.ui.order.OrderListFragment
import com.tourcoo.training.ui.training.RichWebViewActivity
import com.trello.rxlifecycle3.RxLifecycle
import com.trello.rxlifecycle3.RxLifecycle.bindUntilEvent
import com.trello.rxlifecycle3.android.FragmentEvent

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年05月08日19:27
 * @Email: 971613168@qq.com
 */
class MessageFragment : BaseRefreshLoadFragment<MessageEntity>() {
    private var adapter: MessageAdapter? = null
    private var mType = 0

    override fun getAdapter(): BaseQuickAdapter<MessageEntity, BaseViewHolder> {
        adapter = MessageAdapter()
        return adapter as MessageAdapter
    }

    override fun initView(savedInstanceState: Bundle?) {
        mType = arguments!!.getInt("type")
        initItemClick()
    }

    override fun getContentLayout(): Int {
        return R.layout.frame_layout_refresh_recycler
    }

    override fun loadData(page: Int) {
        requestMessageList(page)
    }


    private fun requestMessageList(page: Int) {
        ApiRepository.getInstance().requestMessageList(page, mType).compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BasePageResult<MessageEntity>?>(iHttpRequestControl) {
            override fun onSuccessNext(entity: BasePageResult<MessageEntity>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    UiManager.getInstance().httpRequestControl.httpRequestSuccess(iHttpRequestControl, if (entity.data == null) ArrayList() else (entity.data.rows), null)
                } else {
                    ToastUtil.show(entity.msg)
                }

            }

        })
    }

    companion object {
        fun newInstance(type: Int): MessageFragment {
            val args = Bundle()
            args.putInt("type", type)
            val fragment = MessageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun skipWebDetail(id: Int) {
        val intent = Intent(mContext, MessageWebViewActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    private fun initItemClick() {
        adapter!!.setOnItemClickListener { adapter, view, position ->
            var entity = adapter!!.data.get(position) as MessageEntity
            skipWebDetail(entity.id)
        }
    }


}






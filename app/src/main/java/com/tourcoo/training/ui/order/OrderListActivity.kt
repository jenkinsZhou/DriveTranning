package com.tourcoo.training.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tourcoo.training.R
import com.tourcoo.training.adapter.order.OrderAdapter.*
import com.tourcoo.training.adapter.page.CommonFragmentPagerAdapter
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.event.OrderRefreshEvent
import com.tourcoo.training.event.ProfessionRefreshEvent
import kotlinx.android.synthetic.main.activity_order_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *@description :订单列表
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年05月08日14:43
 * @Email: 971613168@qq.com
 */
class OrderListActivity : BaseTitleActivity() {
    private var pagerAdapter: CommonFragmentPagerAdapter? = null
    private var fragmentList: MutableList<Fragment>? = null
    private val titles = arrayOf("全部", "充值", "消费")
    override fun getContentLayout(): Int {
        return R.layout.activity_order_list
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("我的订单")
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        fragmentList = ArrayList()
        fragmentList!!.add(OrderListFragment.newInstance(ORDER_TYPE_ALL))
        fragmentList!!.add(OrderListFragment.newInstance(ORDER_TYPE_RECHARGE))
        fragmentList!!.add(OrderListFragment.newInstance(ORDER_TYPE_COST))
        pagerAdapter = CommonFragmentPagerAdapter(supportFragmentManager, fragmentList)
        orderViewPager.adapter = pagerAdapter
        orderViewPager.offscreenPageLimit = fragmentList!!.size + 1
        orderTabLayout.setupWithViewPager(orderViewPager)
        initTabTitle()
    }

    private fun initTabTitle() {
        for (i in titles.indices) {
            val tab = orderTabLayout.getTabAt(i)
            if (tab != null) {
                tab.text = titles[i]
            }
        }
    }

    /**
     *  重要的刷新机制
     *
     * @param orderEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onOrderRefreshEvent(orderEvent: OrderRefreshEvent?) {
        if (orderEvent == null) {
            return
        }
        //只要offLineRefreshEvent不为空 就刷新数据
        TourCooLogUtil.i("", "收到刷新事件")
        val fragment = fragmentList!![orderViewPager.currentItem] as OrderListFragment
        baseHandler.postDelayed(Runnable {
            fragment.autoRefresh()
        },500)

    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}
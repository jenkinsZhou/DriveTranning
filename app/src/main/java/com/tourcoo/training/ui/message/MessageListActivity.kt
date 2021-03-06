package com.tourcoo.training.ui.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tourcoo.training.R
import com.tourcoo.training.adapter.page.CommonFragmentPagerAdapter
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import kotlinx.android.synthetic.main.activity_order_list.*

/**
 *@description :消息列表
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年05月08日17:06
 * @Email: 971613168@qq.com
 */
class MessageListActivity :BaseTitleActivity() {
    private var fragmentList: MutableList<Fragment>? = null
    private val titles = arrayOf("企业通知", "公文公告")
    private var pagerAdapter: CommonFragmentPagerAdapter? = null
    override fun getContentLayout(): Int {
        return  R.layout.activity_order_list
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("消息通知")
    }

    override fun initView(savedInstanceState: Bundle?) {
        fragmentList = ArrayList()
        fragmentList!!.add(MessageFragment.newInstance(0))
        fragmentList!!.add(MessageFragment.newInstance(1))
        pagerAdapter = CommonFragmentPagerAdapter(supportFragmentManager, fragmentList)
        orderViewPager.adapter = pagerAdapter
        orderViewPager.offscreenPageLimit = fragmentList!!.size+1
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
}
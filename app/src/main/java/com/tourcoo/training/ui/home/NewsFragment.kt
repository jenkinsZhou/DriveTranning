package com.tourcoo.training.ui.home

import android.os.Bundle
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tourcoo.training.R
import com.tourcoo.training.adapter.news.NewsMultipleAdapter
import com.tourcoo.training.core.base.fragment.BaseBlueBgTitleFragment
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.news.NewsEntity
import com.tourcoo.training.entity.news.NewsEntity.NEWS_TYPE_IMAGE_HORIZONTAL
import com.tourcoo.training.entity.news.NewsEntity.NEWS_TYPE_IMAGE_VERTICAL
import kotlinx.android.synthetic.main.frame_layout_recycler.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月06日14:55
 * @Email: 971613168@qq.com
 */
class NewsFragment : BaseBlueBgTitleFragment() {
    private var adapter: NewsMultipleAdapter? = null
    private var rvCommon: RecyclerView? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_tab_news
    }

    override fun initView(savedInstanceState: Bundle?) {
        rvCommon = mContentView.findViewById(R.id.rvCommon)
        rvCommon?.layoutManager = LinearLayoutManager(mContext)
        adapter = NewsMultipleAdapter(ArrayList())
        adapter?.bindToRecyclerView(rvCommon)
        adapter?.setNewData(testData())
    }

    companion object {
        fun newInstance(): NewsFragment {
            val args = Bundle()
            val fragment = NewsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        val leftView = titleBar?.getTextView(Gravity.START)
        titleBar?.setTitleMainText("资讯")
        setViewGone(leftView, false)
    }

    private fun testData(): ArrayList<NewsEntity> {
        val dataList = ArrayList<NewsEntity>()
        for (index in 0 until 11) {
            val news = NewsEntity()
            if (index % 3 == 0) {
                news.newsType = NEWS_TYPE_IMAGE_VERTICAL
                news.newsTitle =
                        "货车追尾 致司机受伤被困冬夜里 驾驶室变身温急救室 实现快速救援,驾驶室变身温急救室 实现快速救援"
            } else {
                news.newsType = NEWS_TYPE_IMAGE_HORIZONTAL
                news.newsTitle =
                        "货车追尾 致司机受伤被困冬夜里 驾驶室变身温情"
//                                "急救室 实现快速救援货车追尾 致司机受伤被困冬夜里 驾驶室变身温急救室 实现快速救援,驾驶室变身温急救室 实现快速救援"
            }
            dataList.add(news)
        }
        return dataList
    }
}
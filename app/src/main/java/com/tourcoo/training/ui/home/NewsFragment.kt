package com.tourcoo.training.ui.home

import android.os.Bundle
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tourcoo.training.R
import com.tourcoo.training.adapter.news.NewsMultipleAdapter
import com.tourcoo.training.core.base.fragment.BaseBlueBgTitleFragment
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.news.NewsEntity
import com.tourcoo.training.entity.news.NewsEntity.NEWS_TYPE_IMAGE_HORIZONTAL
import com.tourcoo.training.entity.news.NewsEntity.NEWS_TYPE_IMAGE_VERTICAL

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
    private var mSmartRefreshLayout: SmartRefreshLayout? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_tab_news
    }

    override fun initView(savedInstanceState: Bundle?) {
        rvCommon = mContentView.findViewById(R.id.rvCommon)
        mSmartRefreshLayout = mContentView.findViewById(R.id.smartRefreshLayoutCommon)
        mSmartRefreshLayout?.setRefreshHeader(ClassicsHeader(mContext))
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
                val newsImageList  =  ArrayList<String>()
                news.imagesList = newsImageList
                newsImageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583509054521&di=efe3f40e5d225cbb51" +
                        "f2073732bf68b1&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201401%2F11%2F145825zn1sxa8anrg11gt1.jpg")
                newsImageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583509054522&di=d5dab6938c21683c20a27cbd0ed48742&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2" +
                        "Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1212%2F10%2Fc1%2F16491670_1355126816487.jpg")
                if(index %2 == 0){
                    newsImageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583509127874&di=2dd95a4fc752eb4b72a461ef342bebb6&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201510%2F14%2F20151014183417_2PJ8N.jpeg")
                }

            } else {
                news.imageId = R.drawable.img_bg
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
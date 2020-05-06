package com.tourcoo.training.ui.home.news

import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tourcoo.training.R
import com.tourcoo.training.adapter.news.NewsMultipleAdapter
import com.tourcoo.training.core.base.fragment.BaseTitleMvpRefreshLoadFragment
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.news.NewsEntity
import com.tourcoo.training.widget.web.NewsDetailActivityNew

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月28日10:02
 * @Email: 971613168@qq.com
 */
class NewsTabFragmentNew : BaseTitleMvpRefreshLoadFragment<NewsListPresenter, NewsEntity>(), NewsListContract.NewsListView {
    private var adapter: NewsMultipleAdapter? = null
    override fun getAdapter(): BaseQuickAdapter<NewsEntity, BaseViewHolder> {
        val list: MutableList<NewsEntity>? = ArrayList()
        adapter = NewsMultipleAdapter(list)
        return adapter!!
    }

    override fun loadPresenter() {

    }


    override fun setTitleBar(titleBar: TitleBarView?) {
        if (titleBar != null) {
            titleBar.setBgResource(R.drawable.bg_gradient_blue_53c2ff_4e52ff)
//            titleBar.setLeftTextDrawable(R.drawable.ic_back_white)
            titleBar.setTitleMainTextColor(CommonUtil.getColor(R.color.white))
            titleBar.height = SizeUtil.dp2px(45f)
            titleBar.setTitleMainText("资讯")
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        adapter!!.setOnItemClickListener { adapter, view, position ->
            //todo
            val news =      adapter.data[position] as NewsEntity
            val bundle =  Bundle()
            bundle.putString(EXTRA_NEWS_ID,news.id)
            bundle.putString(EXTRA_NEWS_URL,news.url)
            CommonUtil.startActivity(mContext, NewsDetailActivityNew::class.java,bundle)


        }
    }

    override fun getContentLayout(): Int {
        return R.layout.frame_layout_title_refresh_recycler
    }

    override fun loadData(page: Int) {
        presenter.getNewsList(page)
    }


    override fun createPresenter(): NewsListPresenter {
        return NewsListPresenter()
    }


    companion object {
        fun newInstance(): NewsTabFragmentNew {
            val args = Bundle()
            val fragment = NewsTabFragmentNew()
            fragment.arguments = args
            return fragment
        }

        const val EXTRA_NEWS_ID = "EXTRA_NEWS_ID"
        const val EXTRA_NEWS_URL = "EXTRA_NEWS_URL"
    }
}




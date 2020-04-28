package com.tourcoo.training.ui.home.news

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tourcoo.training.R
import com.tourcoo.training.adapter.news.NewsMultipleAdapter
import com.tourcoo.training.adapter.news.NewsMultipleAdapterOld
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.fragment.BaseTitleMvpRefreshLoadFragment
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.news.NewsEntity
import com.tourcoo.training.ui.home.NewsTabFragmentOld
import kotlinx.android.synthetic.main.frame_layout_recycler.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月28日10:02
 * @Email: 971613168@qq.com
 */
class NewsTabFragmentNew : BaseTitleMvpRefreshLoadFragment<NewsListPresenter, NewsEntity>(), NewsListContract.NewsListView {
    override fun getAdapter(): BaseQuickAdapter<NewsEntity, BaseViewHolder> {
        val list: MutableList<NewsEntity>? = ArrayList()
        return NewsMultipleAdapter(list)
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
        rvCommon?.layoutManager = LinearLayoutManager(mContext)
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
    }
}



package com.tourcoo.training.ui.home

import android.os.Bundle
import android.view.Gravity
import com.tourcoo.training.R
import com.tourcoo.training.core.base.fragment.BaseBlueBgTitleFragment
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.widget.banner.BannerEntity
import com.tourcoo.training.widget.banner.ImageBannerAdapter
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_tab_study.*
import java.util.*

/**
 *@description :学习tab页
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月10日9:34
 * @Email: 971613168@qq.com
 */
class StudyTabFragment : BaseBlueBgTitleFragment() {
    override fun getContentLayout(): Int {
        return R.layout.fragment_tab_study
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("交通安培在线课堂")
        val leftView = titleBar?.getTextView(Gravity.START)
        setViewGone(leftView, false)
        learnBanner.adapter =ImageBannerAdapter(getTestData3())
        learnBanner.indicator = CircleIndicator(mContext)
    }

    override fun initView(savedInstanceState: Bundle?) {

    }


    companion object {
        fun newInstance(): StudyTabFragment {
            val args = Bundle()
            val fragment = StudyTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun getTestData3(): List<BannerEntity>? {
        val list: MutableList<BannerEntity> = ArrayList()
        list.add(BannerEntity("https://img.zcool.cn/community/011ad05e27a173a801216518a5c505.jpg", null, 1))
        list.add(BannerEntity("https://img.zcool.cn/community/0148fc5e27a173a8012165184aad81.jpg", null, 1))
        list.add(BannerEntity("https://img.zcool.cn/community/013c7d5e27a174a80121651816e521.jpg", null, 1))
        list.add(BannerEntity("https://img.zcool.cn/community/01b8ac5e27a173a80120a895be4d85.jpg", null, 1))
        list.add(BannerEntity("https://img.zcool.cn/community/01a85d5e27a174a80120a895111b2c.jpg", null, 1))
        list.add(BannerEntity("https://img.zcool.cn/community/01085d5e27a174a80120a8958791c4.jpg", null, 1))
        list.add(BannerEntity("https://img.zcool.cn/community/01f8735e27a174a8012165188aa959.jpg", null, 1))
        return list
    }
}
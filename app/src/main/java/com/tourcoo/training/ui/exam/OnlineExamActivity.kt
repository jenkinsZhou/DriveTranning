package com.tourcoo.training.ui.exam

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tourcoo.training.R
import com.tourcoo.training.adapter.exam.ExamFragmentPagerAdapter
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import kotlinx.android.synthetic.main.activity_exam_online.*

/**
 *@description :线上考试
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月09日17:14
 * @Email: 971613168@qq.com
 */
class OnlineExamActivity : BaseTitleActivity(), View.OnClickListener {
    private var fragmentExamAdapter: ExamFragmentPagerAdapter? = null
    private var list: ArrayList<Fragment>? = null
    private var currentPosition = 0
    override fun getContentLayout(): Int {
        return R.layout.activity_exam_online
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("线上考试")

    }

    override fun initView(savedInstanceState: Bundle?) {
        tvNextQuestion.setOnClickListener(this)
        tvLastQuestion.setOnClickListener(this)
        list = ArrayList()
        fragmentExamAdapter = ExamFragmentPagerAdapter(supportFragmentManager, list)
        testLoadData()
        vpExamOnline.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                val currentPage = position+1
                ToastUtil.show("当前为第"+currentPage+"共"+list?.size+"页")
            }

        })
    }

    private fun testLoadData() {
        list?.clear()
        for (i in 0 until 10) {
            list?.add(OnlineExamFragment.newInstance())
        }
        vpExamOnline.adapter = fragmentExamAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvNextQuestion -> {
                if (currentPosition < list!!.size - 1) {
                    vpExamOnline.setCurrentItem(currentPosition + 1, true)
                }

            }
            R.id.tvLastQuestion -> {
                if (currentPosition > 0) {
                    vpExamOnline.setCurrentItem(currentPosition -1, true)
                }
            }
            else -> {
            }
        }
    }


}
package com.tourcoo.training.ui.exam

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tourcoo.training.R
import com.tourcoo.training.adapter.exam.ExamFragmentPagerAdapter
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import kotlinx.android.synthetic.main.activity_exam_online.*

/**
 *@description :线上考试
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月09日17:14
 * @Email: 971613168@qq.com
 */
class OnlineExamActivity : BaseTitleActivity() {
    private var fragmentExamAdapter : ExamFragmentPagerAdapter? = null
    private var list : ArrayList<Fragment> ?= null
    override fun getContentLayout(): Int {
        return R.layout.activity_exam_online
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("线上考试")

    }

    override fun initView(savedInstanceState: Bundle?) {
        fragmentExamAdapter = ExamFragmentPagerAdapter(supportFragmentManager,list)
        testLoadData()
    }

    private fun testLoadData(){
        list = ArrayList()
        for (i in 0 until  10){
            list?.add(OnlineExamFragment.newInstance())
        }
        vpExamOnline.adapter = fragmentExamAdapter
    }
}
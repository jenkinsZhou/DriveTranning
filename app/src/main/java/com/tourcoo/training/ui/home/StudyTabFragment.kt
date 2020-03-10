package com.tourcoo.training.ui.home

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tourcoo.training.R
import com.tourcoo.training.adapter.exam.ExamFragmentPagerAdapter
import com.tourcoo.training.core.base.fragment.BaseBlueBgTitleFragment
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.ui.training.ProfessionalTrainingFragment
import com.tourcoo.training.ui.training.SafeTrainingFragment
import com.tourcoo.training.ui.training.WorkProTrainingFragment
import com.tourcoo.training.widget.banner.BannerEntity
import com.tourcoo.training.widget.banner.ImageBannerAdapter
import com.tourcoo.training.widget.viewpager.AutoHeightViewPager
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
    private var fragmentExamAdapter: ExamFragmentPagerAdapter? = null
    private var list: ArrayList<Fragment>? = null
    private var currentPosition = 0
    private var trainingViewPager: AutoHeightViewPager?= null
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
        trainingViewPager = mContentView.findViewById(R.id.trainingViewPager)
        list = ArrayList()
        val activity = mContext as AppCompatActivity
        fragmentExamAdapter = ExamFragmentPagerAdapter(activity.supportFragmentManager, list)
        testLoadData()
        trainingViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                // 切换到当前页面，重置高度
                trainingViewPager?.requestLayout();
            }

        })
    }


    companion object {
        fun newInstance(): StudyTabFragment {
            val args = Bundle()
            val fragment = StudyTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

  private  fun getTestData3(): List<BannerEntity>? {
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


    private fun testLoadData() {
        list?.clear()
      /*  for (i in 0 until 10) {
            list?.add(OnlineExamFragment.newInstance())
        }*/
        list?.add(ProfessionalTrainingFragment.newInstance())
        list?.add(WorkProTrainingFragment.newInstance())
        list?.add(SafeTrainingFragment.newInstance())
        trainingViewPager?.offscreenPageLimit = 4
        trainingViewPager?.adapter = fragmentExamAdapter
    }


}
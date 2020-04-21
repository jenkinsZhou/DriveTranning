package com.tourcoo.training.ui.home

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tourcoo.training.R
import com.tourcoo.training.adapter.page.CommonFragmentPagerAdapter
import com.tourcoo.training.core.base.fragment.BaseBlueBgTitleFragment
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.ui.training.ProfessionalTrainingFragment
import com.tourcoo.training.ui.training.WorkProTrainingFragment
import com.tourcoo.training.ui.training.safe.SafeTrainingFragment
import com.tourcoo.training.widget.banner.BannerEntity
import com.tourcoo.training.widget.banner.ImageBannerAdapter
import com.tourcoo.training.widget.viewpager.AutoHeightViewPager
import com.tourcoo.training.widget.viewpager.SwitchScrollViewPager
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
class StudyTabFragment : BaseBlueBgTitleFragment(),View.OnClickListener {
    private var fragmentCommonAdapter: CommonFragmentPagerAdapter? = null
    private var list: ArrayList<Fragment>? = null
    private var currentPosition = 0
    private var trainingViewPager: SwitchScrollViewPager?= null
    override fun getContentLayout(): Int {
        return R.layout.fragment_tab_training
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("交通安培在线课堂")
        val leftView = titleBar?.getTextView(Gravity.START)
        setViewGone(leftView, false)
        showPageChange(0)
        learnBanner?.adapter =ImageBannerAdapter(getTestData3())
        learnBanner?.indicator = CircleIndicator(mContext)
        llTrainingSafe.setOnClickListener(this)
        llTrainingWorkBefore.setOnClickListener(this)
        llTrainingProfession.setOnClickListener(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        trainingViewPager = mContentView.findViewById(R.id.trainingViewPager)
        trainingViewPager?.setScollEnable(false)
        list = ArrayList()
        fragmentCommonAdapter = CommonFragmentPagerAdapter(this.childFragmentManager, list)
        testLoadData()
        trainingViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                showPageChange(position)
            }

        })
    }

    override fun loadData() {
        super.loadData()
        setStatusBarModeWhite(this)
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
        list?.add(SafeTrainingFragment.newInstance())
        list?.add(WorkProTrainingFragment.newInstance())
        list?.add(ProfessionalTrainingFragment.newInstance())
        trainingViewPager?.offscreenPageLimit = 100
        trainingViewPager?.adapter = fragmentCommonAdapter
    }



    private fun showSelect(llContainer : LinearLayout?,rlBg: RelativeLayout?){
        llContainer?.background = ResourceUtil.getDrawable(R.color.blue5087FF)
        rlBg?.background = ResourceUtil.getDrawable(R.drawable.shape_circle_white)
    }

    private fun showUnSelect(llContainer : LinearLayout?,rlBg: RelativeLayout?){
        llContainer?.background = ResourceUtil.getDrawable(R.color.white)
        rlBg?.background = ResourceUtil.getDrawable(R.drawable.shape_circle_grayf7f4f8)
    }


    private fun showPageChange(position : Int){
        when (position) {
            0 -> {
                showSelect(llTrainingSafe,rlCircleTrainingSafe)
                showUnSelect(llTrainingWorkBefore,rlCircleTrainingBeforeWork)
                showUnSelect(llTrainingProfession,rlCircleTrainingProfession)
            }
            1 -> {
                showUnSelect(llTrainingSafe,rlCircleTrainingSafe)
                showSelect(llTrainingWorkBefore,rlCircleTrainingBeforeWork)
                showUnSelect(llTrainingProfession,rlCircleTrainingProfession)
            }
            2 -> {
                showUnSelect(llTrainingSafe,rlCircleTrainingSafe)
                showUnSelect(llTrainingWorkBefore,rlCircleTrainingBeforeWork)
                showSelect(llTrainingProfession,rlCircleTrainingProfession)
            }
            else -> {
            }
        }
    }



    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llTrainingSafe -> {
                trainingViewPager?.setCurrentItem(0,true)
            }
            R.id.llTrainingWorkBefore -> {
                trainingViewPager?.setCurrentItem(1,true)
            }
            R.id.llTrainingProfession -> {
                trainingViewPager?.setCurrentItem(2,true)
            }
            else -> {
            }
        }
    }


    override fun onVisibleChanged(isVisibleToUser: Boolean) {
        super.onVisibleChanged(isVisibleToUser)
//        if(isVisibleToUser){
            setStatusBarModeWhite(this)
//        }
    }
}
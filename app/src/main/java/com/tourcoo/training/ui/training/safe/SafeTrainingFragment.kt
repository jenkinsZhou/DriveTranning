package com.tourcoo.training.ui.training.safe

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tourcoo.training.R
import com.tourcoo.training.adapter.page.CommonFragmentPagerAdapter
import com.tourcoo.training.core.base.fragment.BaseFragment
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.widget.view.CustomPagerTitleView
import com.tourcoo.training.widget.viewpager.AutoHeightViewPager
import kotlinx.android.synthetic.main.fragment_training_safe.*
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import java.util.*

/**
 *@description :安全培训
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年03月10日20:37
 * @Email: 971613168@qq.com
 */
class SafeTrainingFragment : BaseFragment() {

    private var fragmentCommonAdapter: CommonFragmentPagerAdapter? = null
    private var list: ArrayList<Fragment>? = null
    private var currentPosition = 0
    private val titleList = ArrayList<String>()
    private var safeTrainingViewPager: AutoHeightViewPager?= null
    override fun getContentLayout(): Int {
        return R.layout.fragment_training_safe
    }



    override fun initView(savedInstanceState: Bundle?) {
        setStatusBarModeWhite(this)
        safeTrainingViewPager = mContentView.findViewById(R.id.safeTrainingViewPager)
        list = ArrayList()
    }

    override fun loadData() {
        super.loadData()
        safeTrainingViewPager?.offscreenPageLimit = 2
        fragmentCommonAdapter = CommonFragmentPagerAdapter(this.childFragmentManager, list)
        safeTrainingViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                // 切换到当前页面，重置高度
//                safeTrainingViewPager?.requestLayout()
            }

        })
        loadFragment()
        initMagicIndicator()
    }
    companion object {
        fun newInstance(): SafeTrainingFragment {
            val args = Bundle()
            val fragment = SafeTrainingFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun loadFragment(){
        list?.clear()
        list?.add(OnlineTrainFragment.newInstance())
        list?.add(OfflineTrainFragment.newInstance())
        titleList.add("线上培训")
        titleList.add("现场培训")
        fragmentCommonAdapter = CommonFragmentPagerAdapter( this.childFragmentManager, list)
        safeTrainingViewPager?.adapter = fragmentCommonAdapter
    }


    private fun initMagicIndicator() {
        magicIndicator.setBackgroundResource(R.drawable.bg_radius_10_white)
        val commonNavigator = CommonNavigator(mContext)
        commonNavigator.isAdjustMode = true
        commonNavigator.scrollPivotX = 0.11f
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return if (list == null) 0 else list!!.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorTransitionPagerTitleView(context)
                simplePagerTitleView.setText(titleList[index])
                simplePagerTitleView.normalColor = ResourceUtil.getColor(R.color.redE54E3F)
                simplePagerTitleView.selectedColor = ResourceUtil.getColor(R.color.black000000)
                simplePagerTitleView.setOnClickListener { safeTrainingViewPager!!.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = WrapPagerIndicator(context)
                indicator.roundRadius = SizeUtil.dp2px(5f).toFloat()
                        indicator.fillColor = ResourceUtil.getColor(R.color.grayF5F5F5)
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, safeTrainingViewPager)
    }

}
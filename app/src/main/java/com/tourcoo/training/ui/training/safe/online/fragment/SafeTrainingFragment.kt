package com.tourcoo.training.ui.training.safe.online.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tourcoo.training.R
import com.tourcoo.training.adapter.page.CommonFragmentPagerAdapter
import com.tourcoo.training.core.base.fragment.BaseFragment
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.account.UserInfoEvent
import com.tourcoo.training.ui.MainTabActivity
import kotlinx.android.synthetic.main.fragment_training_safe.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 *@description :安全培训
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月10日20:37
 * @Email: 971613168@qq.com
 */
class SafeTrainingFragment : BaseFragment() {

    private var fragmentCommonAdapter: CommonFragmentPagerAdapter? = null
    private var list: ArrayList<Fragment>? = null
    private var currentPosition = 0
    private val titleList = ArrayList<String>()
    private var safeTrainingViewPager: ViewPager? = null

    //    private var safeTrainingViewPager: AutoHeightViewPager? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_training_safe
    }


    override fun initView(savedInstanceState: Bundle?) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        setStatusBarModeWhite(this)
        safeTrainingViewPager = mContentView.findViewById(R.id.safeTrainingViewPager)
        list = ArrayList()
    }

    override fun loadData() {
        super.loadData()
        safeTrainingViewPager?.offscreenPageLimit = 2
        fragmentCommonAdapter = CommonFragmentPagerAdapter(this.childFragmentManager, list)
        safeTrainingViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

    private fun loadFragment() {
        list?.clear()

        if (!AccountHelper.getInstance().isLogin) {
            return
        }

        titleList.add("线上培训")
        list?.add(OnlineTrainFragment.newInstance())

        //userType 1(个体工商户)   2（驾驶员）
        if (AccountHelper.getInstance().userInfo.userType == 2) {
            titleList.add("现场培训")
            list?.add(OfflineTrainFragment.newInstance())
            magicIndicator.visibility = View.VISIBLE
        } else {
            magicIndicator.visibility = View.GONE
        }

        fragmentCommonAdapter = CommonFragmentPagerAdapter(this.childFragmentManager, list)
        safeTrainingViewPager?.adapter = fragmentCommonAdapter
    }


    private fun initMagicIndicator() {
//        magicIndicator.setBackgroundResource(R.drawable.bg_radius_10_white)
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
                simplePagerTitleView.normalColor = ResourceUtil.getColor(R.color.gray999999)
                simplePagerTitleView.selectedColor = ResourceUtil.getColor(R.color.black000000)
                simplePagerTitleView.setOnClickListener { safeTrainingViewPager!!.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                val navigatorHeight = context.resources.getDimension(R.dimen.dp_30)
                indicator.lineHeight = navigatorHeight
                indicator.roundRadius = context.resources.getDimension(R.dimen.dp_5)
                indicator.setColors(Color.WHITE)

                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, safeTrainingViewPager)
    }

    /**
     * 收到消息
     *
     * @param userInfoEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserInfoRefreshEvent(userInfoEvent: UserInfoEvent?) {
        if (userInfoEvent == null) {
            return
        }
        if (userInfoEvent.userInfo != null) {
            loadFragment()
        }

    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}
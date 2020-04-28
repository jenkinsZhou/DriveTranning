package com.tourcoo.training.ui.guide

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.SPUtils
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.ui.MainTabActivity
import com.tourcoo.training.ui.account.LoginActivity
import kotlinx.android.synthetic.main.activity_guide.*
import net.lucode.hackware.magicindicator.ViewPagerHelper

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月28日19:30
 * @Email: 971613168@qq.com
 */
class GuideActivity : BaseTitleActivity() {
    override fun getContentLayout(): Int {
        return R.layout.activity_guide
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        setViewGone(titleBar, false)
    }

    override fun initView(savedInstanceState: Bundle?) {
        initAdapter()
        tvSkip.setOnClickListener(View.OnClickListener {
            doGoHome()
        })
    }




    private fun initMagicIndicator(size: Int, viewPager: ViewPager) {
        val scaleCircleNavigator = ScaleCircleNavigator(this)
        scaleCircleNavigator.setCircleCount(size)
        scaleCircleNavigator.setNormalCircleColor(ResourceUtil.getColor(R.color.blue9FBBF8))
        scaleCircleNavigator.setSelectedCircleColor(ResourceUtil.getColor(R.color.blue5087FF))
        scaleCircleNavigator.setCircleClickListener { index -> viewPager.currentItem = index }
        guideMagicIndicator.navigator = scaleCircleNavigator
        ViewPagerHelper.bind(guideMagicIndicator, viewPager)
    }


    private fun initAdapter() {
        val guideViewList: MutableList<View> = ArrayList()
        val adapter = GuidePagerAdapterNew(guideViewList)
        val guideView1 = LayoutInflater.from(mContext).inflate(R.layout.item_guide_layout, null)
        val ivGuide1 = guideView1.findViewById<ImageView>(R.id.ivGuide)
        ivGuide1.setImageResource(R.drawable.img_guide)
        val tvGuideTitle1 = guideView1.findViewById<TextView>(R.id.tvGuideTitle)
        val tvGuideContent1 = guideView1.findViewById<TextView>(R.id.tvGuideContent)
        tvGuideTitle1.text = "课程权威 严谨 专业"
        tvGuideContent1.text = "交通运输部安委会“安全创新”特别推荐"
        guideViewList.add(guideView1)


        val guideView2 = LayoutInflater.from(mContext).inflate(R.layout.item_guide_layout, null)
        val tvGuideTitle2 = guideView2.findViewById<TextView>(R.id.tvGuideTitle)
        val tvGuideContent2 = guideView2.findViewById<TextView>(R.id.tvGuideContent)
        val ivGuide2 = guideView2.findViewById<ImageView>(R.id.ivGuide)
        ivGuide2.setImageResource(R.drawable.img_guide_02)
        tvGuideTitle2.text = "培训先进 真实 全面 "
        tvGuideContent2.text = "通过人脸识别 实名认证保障学员的学习数据"
        guideViewList.add(guideView2)


        val guideView3 = LayoutInflater.from(mContext).inflate(R.layout.item_guide_layout, null)
        val tvGuideTitle3 = guideView3.findViewById<TextView>(R.id.tvGuideTitle)
        val tvGuideContent3 = guideView3.findViewById<TextView>(R.id.tvGuideContent)
        val ivGuide3 = guideView3.findViewById<ImageView>(R.id.ivGuide)
        ivGuide3.setImageResource(R.drawable.img_guide_03)
        val tvGo = guideView3.findViewById<TextView>(R.id.tvGo)
        setViewGone(tvGo, true)
        tvGo.setOnClickListener(View.OnClickListener {
            ToastUtil.show("前往")
            doGoHome()
        })
        tvGuideTitle3.text = "行业资讯 一览无余"
        tvGuideContent3.text = "最新资讯热点 您关心的话题新闻都在这里"
        guideViewList.add(guideView3)
        vpGuide.adapter = adapter
        //指示器初始化
        initMagicIndicator(guideViewList.size, vpGuide)
    }

    private fun doGoHome() {
        if (AccountHelper.getInstance().isLogin) {
            CommonUtil.startActivity(mContext, MainTabActivity::class.java)
        } else {
            CommonUtil.startActivity(mContext, LoginActivity::class.java)
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 如果切换到后台，就设置下次不进入功能引导页
        SPUtils.getInstance().put("FIRST_OPEN", true)
    }
}
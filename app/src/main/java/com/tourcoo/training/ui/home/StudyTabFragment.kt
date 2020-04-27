package com.tourcoo.training.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tourcoo.training.R
import com.tourcoo.training.adapter.page.CommonFragmentPagerAdapter
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.WebViewActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.base.fragment.BaseBlueBgTitleFragment
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.study.BannerBean
import com.tourcoo.training.ui.training.professional.ProfessionalTrainingFragment
import com.tourcoo.training.ui.training.workpro.WorkProTrainingFragment
import com.tourcoo.training.ui.training.safe.online.fragment.SafeTrainingFragment
import com.tourcoo.training.widget.banner.BannerEntity
import com.tourcoo.training.widget.banner.ImageBannerAdapter
import com.tourcoo.training.widget.viewpager.SwitchScrollViewPager
import com.trello.rxlifecycle3.android.FragmentEvent
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.fragment_tab_training.*
import java.util.*

/**
 *@description :学习tab页
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月10日9:34
 * @Email: 971613168@qq.com
 */
class StudyTabFragment : BaseBlueBgTitleFragment(), View.OnClickListener {

    private var fragmentCommonAdapter: CommonFragmentPagerAdapter? = null
    private var list: ArrayList<Fragment>? = null
    private var currentPosition = 0
    private var trainingViewPager: SwitchScrollViewPager? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_tab_training
    }


    private var tv_safe: TextView? = null
    private var tv_job: TextView? = null
    private var tv_special: TextView? = null
    override fun onStart() {
        super.onStart()
        tv_safe = view!!.findViewById(R.id.tv_safe)
        tv_job = view!!.findViewById(R.id.tv_job)
        tv_special = view!!.findViewById(R.id.tv_special)
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        super.setTitleBar(titleBar)
        titleBar?.setTitleMainText("交通安培在线课堂")
        val leftView = titleBar?.getTextView(Gravity.START)
        setViewGone(leftView, false)


        showPageChange(0)

        llTrainingSafe.setOnClickListener(this@StudyTabFragment)
        llTrainingWorkBefore.setOnClickListener(this@StudyTabFragment)
        llTrainingProfession.setOnClickListener(this@StudyTabFragment)


        val list: MutableList<BannerEntity> = ArrayList()

        ApiRepository.getInstance().requesListBanner().compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<List<BannerBean>>>() {
            override fun onSuccessNext(entity: BaseResult<List<BannerBean>>?) {
                if (entity != null) {
                    if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                        TourCooLogUtil.d(entity)
                        entity.data.forEach {
                            list.add(BannerEntity(it.imageUrl, it.url, 1))
                        }
                        learnBanner?.adapter = ImageBannerAdapter(list)
                        learnBanner?.indicator = CircleIndicator(mContext)
                        learnBanner?.setOnBannerListener(object : OnBannerListener<Any> {
                            override fun onBannerChanged(position: Int) {
                            }

                            override fun OnBannerClick(data: Any?, position: Int) {
                                if (list[position].url.isEmpty()) {
                                    return
                                }
                                WebViewActivity.start(context, list[position].url, true)
                            }

                        })

                    } else {
                        ToastUtil.show(entity.msg)
                    }
                }
            }
        })


    }


    override fun initView(savedInstanceState: Bundle?) {
        trainingViewPager = mContentView.findViewById(R.id.trainingViewPager)
        trainingViewPager?.setScollEnable(false)
        list = ArrayList()
        fragmentCommonAdapter = CommonFragmentPagerAdapter(this.childFragmentManager, list)
        testLoadData()
        trainingViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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


    private fun testLoadData() {
        list?.clear()
        list?.add(SafeTrainingFragment.newInstance())
        list?.add(WorkProTrainingFragment.newInstance())
        list?.add(ProfessionalTrainingFragment.newInstance())
        trainingViewPager?.offscreenPageLimit = 100
        trainingViewPager?.adapter = fragmentCommonAdapter
    }


    private fun showSelect(llContainer: LinearLayout?, rlBg: RelativeLayout?) {
        llContainer?.background = ResourceUtil.getDrawable(R.color.blue5087FF)
        rlBg?.background = ResourceUtil.getDrawable(R.drawable.shape_circle_white)
    }

    private fun showUnSelect(llContainer: LinearLayout?, rlBg: RelativeLayout?) {
        llContainer?.background = ResourceUtil.getDrawable(R.color.white)
        rlBg?.background = ResourceUtil.getDrawable(R.drawable.shape_circle_grayf7f4f8)
    }


    private fun showPageChange(position: Int) {
        when (position) {
            0 -> {

                tv_safe?.setTextColor(Color.WHITE)
                tv_job?.setTextColor(resources.getColor(R.color.black333333))
                tv_special?.setTextColor(resources.getColor(R.color.black333333))
                icTrainingSafe.setImageResource(R.mipmap.ic_training_safe_select)
                ivTrainingWorkPro.setImageResource(R.mipmap.ic_training_pre_work_unselect)
                ivTrainingProfession.setImageResource(R.mipmap.ic_training_professional_unselect)
                showSelect(llTrainingSafe, rlCircleTrainingSafe)
                showUnSelect(llTrainingWorkBefore, rlCircleTrainingBeforeWork)
                showUnSelect(llTrainingProfession, rlCircleTrainingProfession)
            }
            1 -> {
                tv_safe?.setTextColor(resources.getColor(R.color.black333333))
                tv_job?.setTextColor(Color.WHITE)
                tv_special?.setTextColor(resources.getColor(R.color.black333333))
                icTrainingSafe.setImageResource(R.mipmap.ic_training_safe)
                ivTrainingWorkPro.setImageResource(R.mipmap.ic_training_pre_work)
                ivTrainingProfession.setImageResource(R.mipmap.ic_training_professional_unselect)
                showUnSelect(llTrainingSafe, rlCircleTrainingSafe)
                showSelect(llTrainingWorkBefore, rlCircleTrainingBeforeWork)
                showUnSelect(llTrainingProfession, rlCircleTrainingProfession)
            }

            2 -> {
                tv_safe?.setTextColor(resources.getColor(R.color.black333333))
                tv_job?.setTextColor(resources.getColor(R.color.black333333))
                tv_special?.setTextColor(Color.WHITE)
                icTrainingSafe.setImageResource(R.mipmap.ic_training_safe)
                ivTrainingWorkPro.setImageResource(R.mipmap.ic_training_pre_work_unselect)
                ivTrainingProfession.setImageResource(R.mipmap.ic_training_profession)
                showUnSelect(llTrainingSafe, rlCircleTrainingSafe)
                showUnSelect(llTrainingWorkBefore, rlCircleTrainingBeforeWork)
                showSelect(llTrainingProfession, rlCircleTrainingProfession)
            }
            else -> {
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llTrainingSafe -> {
                trainingViewPager?.setCurrentItem(0, true)
            }
            R.id.llTrainingWorkBefore -> {
                trainingViewPager?.setCurrentItem(1, true)
            }
            R.id.llTrainingProfession -> {
                trainingViewPager?.setCurrentItem(2, true)
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


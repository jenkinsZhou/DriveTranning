package com.tourcoo.training.ui.exam

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tourcoo.training.R
import com.tourcoo.training.adapter.exam.QuestionNumberAdapter
import com.tourcoo.training.adapter.page.CommonFragmentPagerAdapter
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.exam.ExaminationEntity
import kotlinx.android.synthetic.main.activity_exam_online.*

/**
 *@description :线上考试
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月09日17:14
 * @Email: 971613168@qq.com
 */
class OnlineExamActivity : BaseTitleActivity(), View.OnClickListener {
    private var fragmentCommonAdapter: CommonFragmentPagerAdapter? = null
    private var questionNumAdapter: QuestionNumberAdapter? = null
    private var list: ArrayList<Fragment>? = null
    private var currentPosition = 0
    private val delayTime = 500L
    private val answerHandler = Handler()
    private var behavior: BottomSheetBehavior<NestedScrollView>? = null


    override fun getContentLayout(): Int {
        return R.layout.activity_exam_online
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("线上考试")

    }

    override fun initView(savedInstanceState: Bundle?) {
        tvNextQuestion.setOnClickListener(this)
        tvLastQuestion.setOnClickListener(this)
        questionNumRv.layoutManager = GridLayoutManager(mContext,6)
        behavior = BottomSheetBehavior.from(nsvBottom)
        llQuestionBar.setOnClickListener(this)
    }

    override fun loadData() {
        super.loadData()
        list = ArrayList()
        fragmentCommonAdapter = CommonFragmentPagerAdapter(supportFragmentManager, list)
        questionNumAdapter = QuestionNumberAdapter()
        questionNumAdapter?.bindToRecyclerView(questionNumRv)
        testLoadData()
        loadBottomSheetBar()
        vpExamOnline.offscreenPageLimit = list!!.size - 1
        vpExamOnline.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
            }

        })
    }

    private fun testLoadData() {
        list?.clear()
        for (i in 0 until 10) {
            list?.add(OnlineExamFragment.newInstance())
        }
        vpExamOnline.adapter = fragmentCommonAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvNextQuestion -> {
                doAnswerQuestion()
            }
            R.id.tvLastQuestion -> {
                skipLastQuestion()
            }
            R.id.llQuestionBar->{
                handleBottomBarClick()
            }
            else -> {
            }
        }
    }


    private fun skipLastQuestion() {
        if (currentPosition > 0) {
            vpExamOnline.setCurrentItem(currentPosition - 1, true)
        }
    }

    private fun skipNextQuestionNow() {
        skipNextQuestionDelay(1)
    }

    private fun skipNextQuestionDelay(delay: Long) {
        if (currentPosition < list!!.size - 1) {
            answerHandler.postDelayed({
                vpExamOnline.setCurrentItem(currentPosition + 1, true)
            }, delay)
        }
    }


    private fun doAnswerQuestion() {
        if (currentPosition < list!!.size - 1) {
            val fragment = list!![currentPosition] as OnlineExamFragment
            val selectCount = fragment.getSelectCount()
            if (fragment.isMultipleAnswer() && selectCount == 1) {
                ToastUtil.show("这道题是多选哦")
                return
            }
            val hasAnswer = fragment.answerQuestion()
            if (hasAnswer) {
                skipNextQuestionDelay(delayTime)
            } else {
                skipNextQuestionNow()
            }
        }
    }

    private fun getQuestionList() : ArrayList<ExaminationEntity>{
        val questionList = ArrayList<ExaminationEntity>()
        for (i in 0 until  30){
           val exam =  ExaminationEntity()
            exam.number = i+1
            questionList.add(exam)
        }
        return questionList
    }


    private fun loadBottomSheetBar(){
        if(list == null){
            return
        }
        questionNumAdapter?.setNewData(getQuestionList())
    }

    private fun handleBottomBarClick(){
        if(behavior!!.state == BottomSheetBehavior.STATE_COLLAPSED){
            behavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
        }else if(behavior!!.state ==BottomSheetBehavior.STATE_EXPANDED ){
            behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}
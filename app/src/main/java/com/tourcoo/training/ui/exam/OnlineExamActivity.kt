package com.tourcoo.training.ui.exam

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tourcoo.training.R
import com.tourcoo.training.adapter.exam.QuestionNumberAdapter
import com.tourcoo.training.adapter.page.CommonFragmentPagerAdapter
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.course.CourseInfo
import com.tourcoo.training.entity.exam.ExamEntity
import com.tourcoo.training.entity.exam.ExamTempHelper
import com.tourcoo.training.entity.exam.ExaminationEntityOld
import com.tourcoo.training.entity.exam.Question
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
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
    private var examId = ""
    private var trainPlanId = ""

    companion object {
        const val EXTRA_TRAINING_PLAN_ID = "EXTRA_TRAINING_PLAN_ID"
        const val EXTRA_EXAM_ID = "EXTRA_EXAM_ID"
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_exam_online
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("线上考试")

    }

    override fun initView(savedInstanceState: Bundle?) {
        if (intent != null) {
            trainPlanId = intent.getStringExtra(EXTRA_TRAINING_PLAN_ID) as String
            examId = intent!!.getStringExtra(EXTRA_EXAM_ID) as String
        }
        if (TextUtils.isEmpty(trainPlanId) || TextUtils.isEmpty(examId)) {
            ToastUtil.show("未获取到考试题数据")
            finish()
            return
        }
        tvNextQuestion.setOnClickListener(this)
        tvLastQuestion.setOnClickListener(this)
        questionNumRv.layoutManager = GridLayoutManager(mContext, 6)
        behavior = BottomSheetBehavior.from(nsvBottom)
        nsvBottom.isNestedScrollingEnabled = false
        llQuestionBar.setOnClickListener(this)
    }

    override fun loadData() {
        super.loadData()
        requestExam(trainPlanId, examId)
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
            R.id.llQuestionBar -> {
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

    private fun setQuestionNumber(questions: MutableList<Question>): MutableList<Question> {
        for (i in 0 until questions.size - 1) {
            questions[i].questionNumber = "" + i + 1
        }
        return questions
    }


    private fun loadBottomSheetBar(questions: MutableList<Question>?) {
        if (questions == null) {
            return
        }
        questionNumAdapter?.setNewData(setQuestionNumber(questions))
    }

    private fun handleBottomBarClick() {
        if (behavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else if (behavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }


    private fun requestExam(trainId: String, examId: String) {
        ApiRepository.getInstance().requestExam(trainId, examId).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<ExamEntity>?>() {
            override fun onSuccessNext(entity: BaseResult<ExamEntity>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    handleExamResult(entity.data)
                } else {
                    ToastUtil.show(entity.msg)
                }

            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed(e.toString())
                }
            }
        })
    }

    private fun handleExamResult(examEntity: ExamEntity?) {
        if (examEntity == null || examEntity.questions == null) {
            return
        }
        ExamTempHelper.getInstance().examInfo = examEntity
        list = ArrayList()
        fragmentCommonAdapter = CommonFragmentPagerAdapter(supportFragmentManager, list)
        questionNumAdapter = QuestionNumberAdapter()
        questionNumAdapter?.bindToRecyclerView(questionNumRv)
        testLoadData()
        loadBottomSheetBar(examEntity.questions)
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
}
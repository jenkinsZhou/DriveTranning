package com.tourcoo.training.ui.exam

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tourcoo.training.R
import com.tourcoo.training.adapter.exam.QuestionNumberAdapter
import com.tourcoo.training.adapter.page.CommonFragmentPagerAdapter
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.exam.*
import com.tourcoo.training.widget.dialog.exam.CommitAnswerDialog
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_exam_online.*
import org.apache.commons.lang.StringUtils

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
            val bundle = intent!!.extras
            trainPlanId = bundle!!.getString(EXTRA_TRAINING_PLAN_ID)!!
            examId =  bundle!!.getString(EXTRA_EXAM_ID)!!
        }
        if (TextUtils.isEmpty(trainPlanId) || TextUtils.isEmpty(examId)) {
            ToastUtil.show("未获取到考试题数据")
            finish()
            return
        }
        list = ArrayList()
        tvNextQuestion.setOnClickListener(this)
        tvLastQuestion.setOnClickListener(this)
        tvCommitExam.setOnClickListener(this)
        questionNumRv.layoutManager = GridLayoutManager(mContext, 6)
        behavior = BottomSheetBehavior.from(nsvBottom)
        nsvBottom.isNestedScrollingEnabled = false
        llQuestionBar.setOnClickListener(this)
    }

    override fun loadData() {
        super.loadData()
        //获取考试类型
        requestExamQuestions(trainPlanId, examId)
    }

    private fun loadQuestion(examEntity: ExamEntity?) {
        if (examEntity == null) {
            return
        }
        list?.clear()
        val questions = examEntity.questions
        for (i in 0 until questions.size ) {
            list?.add(ExamFragment.newInstance(questions[i]))
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
            R.id.tvCommitExam -> {
                //交卷
                doCommitExam()
            }
            else -> {
            }
        }
    }


    private fun skipLastQuestion() {
        if (currentPosition > 0) {
            vpExamOnline.setCurrentItem(currentPosition - 1, true)
            setViewGone(tvCommitExam, false)
            setViewGone(tvNextQuestion, true)
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
            setViewGone(tvCommitExam, false)
            setViewGone(tvNextQuestion, true)
            val fragment = list!![currentPosition] as ExamFragment
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
        } else {
            setViewGone(tvCommitExam, true)
            setViewGone(tvNextQuestion, false)
        }
    }

    private fun setQuestionNumber(questions: MutableList<Question>): MutableList<Question> {
        for (i in 0 until questions.size) {
            questions[i].questionNumber = (i + 1).toString()
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


    private fun requestExamQuestions(trainId: String, examId: String) {
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
        fragmentCommonAdapter = CommonFragmentPagerAdapter(supportFragmentManager, list)
        questionNumAdapter = QuestionNumberAdapter()
        questionNumAdapter?.bindToRecyclerView(questionNumRv)
        //处理底部题目弹窗
        questionNumAdapter?.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            vpExamOnline.setCurrentItem(position,false)
            handleBottomBarClick()
        })
        loadQuestion(examEntity)
        loadBottomSheetBar(examEntity.questions)
        vpExamOnline.offscreenPageLimit = list!!.size
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

    /**
     * 交卷
     */
    private fun doCommitExam() {
        val dialog = CommitAnswerDialog(mContext)
        dialog.create().show()
        dialog.setPositiveButtonListener(View.OnClickListener {
            commitExam(getAllQuestions())
            dialog.dismiss()
        })
    }

    private fun commitExam(questions: MutableList<Question>) {
        val commitList = ArrayList<CommitAnswer>()
        for (question in questions) {
            val commit = CommitAnswer()
            commit.id = question.id.toString()
            commit.answer = StringUtils.join(question.answer, ",")
            commitList.add(commit)
        }
        ApiRepository.getInstance().requestFinishExam(examId, commitList).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<ExamResultEntity>?>() {
            override fun onSuccessNext(entity: BaseResult<ExamResultEntity>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.showSuccess(entity.data.toString())
                } else {
                    ToastUtil.show(entity.msg)
                }
            }
        })
    }

    private fun getAllQuestions(): MutableList<Question> {
        val results = ArrayList<Question>()
        for (fragment in list!!) {
            val onlineExamFragment = fragment as ExamFragment?
            if (onlineExamFragment != null) {
                results.add(onlineExamFragment.getQuestion())
            }
        }
        return results
    }


    /**
     * 保存答题状态
     */
    private fun saveExam(questions: MutableList<Question>) {
        val commitList = ArrayList<CommitAnswer>()
        for (question in questions) {
            //如果回答过答案 才保存
            if(question.isHasAnswered){
                val commit = CommitAnswer()
                commit.id = question.id.toString()
                commit.answer = StringUtils.join(question.answer, "")
                commitList.add(commit)
            }
        }
        if(commitList.isEmpty()){
            return
        }
        ApiRepository.getInstance().requestSaveAnswer(examId, commitList).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>?>("正在保存答题..") {
            override fun onSuccessNext(entity: BaseResult<Any>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.showSuccess("答题进度已保存")
                } else {
                    ToastUtil.show(entity.msg)
                }
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        saveExam(getAllQuestions())
    }
}
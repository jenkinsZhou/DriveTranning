package com.tourcoo.training.ui.exam

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SpanUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.tourcoo.training.R
import com.tourcoo.training.adapter.exam.QuestionNumberAdapter
import com.tourcoo.training.adapter.page.CommonFragmentPagerAdapter
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.ExamConstant.*
import com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.Base64Util
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.exam.*
import com.tourcoo.training.ui.MainTabActivity
import com.tourcoo.training.ui.certificate.MyCertificationActivity
import com.tourcoo.training.widget.dialog.exam.ExamCommonDialog
import com.tourcoo.training.widget.dialog.exam.ExamNotPassDialog
import com.tourcoo.training.widget.dialog.exam.ExamPassDialog
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_exam_online.*
import org.apache.commons.lang.StringUtils
import java.text.SimpleDateFormat

/**
 *@description :线上考试
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月09日17:14
 * @Email: 971613168@qq.com
 */
class ExamActivity : BaseTitleActivity(), View.OnClickListener, QuestionClickListener {
    private val mTag = "OnlineExamActivity"
    private var fragmentCommonAdapter: CommonFragmentPagerAdapter? = null
    private var questionNumAdapter: QuestionNumberAdapter? = null
    private var list: ArrayList<Fragment>? = null
    private var currentPosition = 0
    private val delayTime = 800L
    private val answerHandler = Handler()
    private var behavior: BottomSheetBehavior<NestedScrollView>? = null
    private var examId = ""
    private var trainPlanId = ""
    private var lastQuestionIndex = -1
    /**
     * 用来计数是否答完所有试题 (没回答完不让提交答案)
     */
    private var answerCount = 0

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
            examId = bundle.getString(EXTRA_EXAM_ID)!!
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
        llBgGray.setOnClickListener(this)
        questionNumRv.layoutManager = GridLayoutManager(mContext, 6)
        behavior = BottomSheetBehavior.from(nsvBottom)
        behavior!!.addBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_DRAGGING->{
                        //不做任何处理
                    }
                    STATE_COLLAPSED -> {
                        //折叠了
                        setViewGone(llBgGray,false)
                    }
                    STATE_EXPANDED->{
                        //展开了
                        setViewGone(llBgGray,true)
                    } else -> {
                }

                }
            }

        })
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
        for (i in 0 until questions.size) {
            val currentFragment = ExamFragment.newInstance(questions[i])
            currentFragment.setOnQuestionListener(this)
            list?.add(currentFragment)
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
                showBottomBarInfo()
            }
            R.id.llQuestionBar -> {
                handleBottomBarBehavior()
            }
            R.id.tvCommitExam -> {
                //交卷之前 先把最后一道题回答了
                doAnswerQuestion()
                showBottomBarInfo()
                baseHandler.postDelayed(Runnable {
                    //交卷
                    doCommitExam()
                }, 500)
            }
            R.id.llBgGray->{
                behaviorClose()
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
                showBottomBarInfo()
            }, delay)
        }

    }

    private fun doAnswerQuestion() {
        showButtonByCurrentPage()
        val fragment = list!![currentPosition] as ExamFragment
        val selectCount = fragment.getSelectCount()
        if (selectCount <= 0) {
            //说明用户还没回答题目 不让进入下一题 所以直接拦截
            ToastUtil.show("请先回答当前题目")
            return
        }
        if (fragment.isMultipleAnswer() && selectCount == 1) {
            ToastUtil.show("这道题是多选哦")
            return
        }
        val hasAnswer = fragment.answerQuestion()
        //控制题解显示或隐藏
        fragment.showQuestionAnalysis(hasAnswer)
        if (hasAnswer) {


            skipNextQuestionDelay(delayTime)
        } else {
            skipNextQuestionNow()
        }
    }

    private fun setQuestionNumber(questions: MutableList<Question>): MutableList<Question> {
        for (i in 0 until questions.size) {
            val currentQuestion = questions[i]
            currentQuestion.questionNumber = (i + 1).toString()
        }
        loadNumberStatus(questions)
        return questions
    }

    private fun loadNumberStatus(questions: MutableList<Question>) {
        for (i in 0 until questions.size) {
            val currentQuestion = questions[i]
            if (currentQuestion.answerStatus == STATUS_NO_ANSWER) {
                if (i > 1) {
                    TourCooLogUtil.i("执行了")
                    //需要判断上一题是否已回答过 若上一题已经回答过则 单独设置个状态
                    val lastQuestion = questions[i - 1]
                    val hasAnswer = lastQuestion.answerStatus == STATUS_ANSWER_WRONG || lastQuestion.answerStatus == STATUS_ANSWER_RIGHT
                    if (hasAnswer) {
                        //说明上一题已经回答过
                        currentQuestion.answerStatus = STATUS_NO_ANSWER_FIRST
                        lastQuestionIndex = i
                    } else {
                        //否则 还是置为未回答状态
                        currentQuestion.answerStatus = STATUS_NO_ANSWER
                    }
                }
            }
        }
    }

    private fun loadBottomSheetBar(questions: MutableList<Question>?) {
        if (questions == null) {
            return
        }
        questionNumAdapter?.setNewData(setQuestionNumber(questions))
        showBottomBarInfo()
    }

    private fun handleBottomBarBehavior() {
        if (behavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
            setViewGone(llBgGray,true)
        } else if (behavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            setViewGone(llBgGray,false)
        }
    }


    private fun requestExamQuestions(trainId: String, examId: String) {
        ApiRepository.getInstance().requestExam(trainId, examId).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<ExamEntity>?>() {
            override fun onSuccessNext(entity: BaseResult<ExamEntity>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    rlBottomLayout.visibility = View.VISIBLE
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

            //如果这道题用户没有回答则 不让跳转
            val fragment = list!![position] as ExamFragment
            if (fragment.getQuestionStatus() == STATUS_NO_ANSWER) {
                //拦截点击
                return@OnItemClickListener
            }
            vpExamOnline.setCurrentItem(position, false)
            //响应底部题目列表点击事件
            showBottomBarInfo()
            handleBottomBarBehavior()
        })
        loadQuestion(examEntity)
        vpExamOnline.offscreenPageLimit = list!!.size
        vpExamOnline.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                showButtonByCurrentPage()
                showBottomBarInfo()
            }
        })
        loadBottomSheetBar(examEntity.questions)
        if (lastQuestionIndex >= 0) {
            vpExamOnline.setCurrentItem(lastQuestionIndex,false)
        }
    }

    /**
     * 交卷
     */
    private fun doCommitExam() {
        //先获取一下已答题数量
        val userAnswerList = getAllQuestions()
        if (answerCount < list!!.size) {
            ToastUtil.show("您还有题目没有答完哦")
            return
        }
        //显示交卷确认弹窗
        val dialog = ExamCommonDialog(mContext)
        dialog.create().show()
        dialog.setPositiveButtonListener(View.OnClickListener {
            isSubmit = true
            commitExam(userAnswerList)
            dialog.dismiss()
        })


    }

    /**
     *真正的卷逻辑
     */
    private fun commitExam(questions: MutableList<Question>) {
        val commitList = ArrayList<CommitAnswer>()
        for (question in questions) {
            val commit = CommitAnswer()
            commit.id = question.id.toString()
            commit.answer = StringUtils.join(question.answer, "")
            commitList.add(commit)
        }
        ApiRepository.getInstance().requestFinishExam(examId, commitList).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<ExamResultEntity>?>() {
            override fun onSuccessNext(entity: BaseResult<ExamResultEntity>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    if (entity.data.status == 0) { //合格

                        tvCertificateId.text = "证书编号：NO.${entity.data.data.certificateId}"
                        tvCreateTime.text = entity.data.data.createTime


                        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val startTime = simpleDateFormat.format(simpleDateFormat.parse(entity.data.data.startTime))
                        val endTime = simpleDateFormat.format(simpleDateFormat.parse(entity.data.data.endTime))

                        tvDetail.text = SpanUtils()
                                .append("学员 ").setForegroundColor(Color.parseColor("#999999")).setFontSize(13, true).setLineHeight((1.5 * 15).toInt(), SpanUtils.ALIGN_CENTER)
                                .append(entity.data.data.name).setForegroundColor(Color.parseColor("#333333")).setFontSize(14, true).setUnderline().setBold().setLineHeight((1.5 * 15).toInt(), SpanUtils.ALIGN_CENTER)
                                .append(" 身份证号 ").setForegroundColor(Color.parseColor("#999999")).setFontSize(13, true).setLineHeight((1.5 * 15).toInt(), SpanUtils.ALIGN_CENTER)
                                .append(entity.data.data.idCard).setForegroundColor(Color.parseColor("#333333")).setFontSize(14, true).setUnderline().setBold().setLineHeight((1.5 * 15).toInt(), SpanUtils.ALIGN_CENTER)
                                .append(" 于 ").setForegroundColor(Color.parseColor("#999999")).setFontSize(13, true).setLineHeight((1.5 * 15).toInt(), SpanUtils.ALIGN_CENTER)
                                .append(startTime + " - " + endTime).setForegroundColor(Color.parseColor("#333333")).setFontSize(14, true).setUnderline().setBold().setLineHeight((1.5 * 15).toInt(), SpanUtils.ALIGN_CENTER)
                                .append(" 完整学习了交通安培课程 ").setForegroundColor(Color.parseColor("#999999")).setFontSize(13, true).setLineHeight((1.5 * 15).toInt(), SpanUtils.ALIGN_CENTER)
                                .append(entity.data.data.trainingPlanName).setForegroundColor(Color.parseColor("#333333")).setFontSize(14, true).setUnderline().setBold().setLineHeight((1.5 * 15).toInt(), SpanUtils.ALIGN_CENTER)
                                .append(" 成绩合格，特授此证书。 ").setForegroundColor(Color.parseColor("#999999")).setFontSize(13, true).setLineHeight((1.5 * 15).toInt(), SpanUtils.ALIGN_CENTER)
                                .create()



                        baseHandler.postDelayed({
                            val bitmap = ImageUtils.view2Bitmap(flCertificate)
                            val base64Image = "data:image/jpeg;base64," + Base64Util.bitmapToBase64(bitmap)

                            uploadCertificate(entity.data.data.certificateId, base64Image, entity.data.tips)
                        }, 150)

                    } else { //不合格
                        val dialog = ExamNotPassDialog(mContext)
                        dialog.create()
                                .setTips(entity.data.tips)
                                .setPositiveButtonListener {
                                    dialog.dismiss()
                                    startActivity(Intent(this@ExamActivity, MainTabActivity::class.java))
                                    ActivityUtils.finishOtherActivities(MainTabActivity::class.java)
                                }
                                .show()
                    }

                } else {
                    ToastUtil.show(entity.msg)
                }
            }
        })
    }

    private fun uploadCertificate(id: String, base64Image: String, tips: String) {
        ApiRepository.getInstance().uploadCertificate(id, base64Image).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>?>("正在保存答题..") {
            override fun onSuccessNext(entity: BaseResult<Any>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    val dialog = ExamPassDialog(mContext)
                    dialog.create()
                            .setTips(tips)
                            .setPositiveButtonListener {
                                dialog.dismiss()
                                startActivity(Intent(this@ExamActivity, MainTabActivity::class.java))
                                ActivityUtils.finishOtherActivities(MainTabActivity::class.java)
                            }
                            .setNegativeButtonListener {
                                dialog.dismiss()
                                startActivity(Intent(this@ExamActivity, MyCertificationActivity::class.java))
                                finish()
                            }
                            .show()
                } else {
                    ToastUtil.show(entity.msg)
                }
            }
        })
    }


    private fun getAllQuestions(): MutableList<Question> {
        //先重置答题数量
        answerCount = 0
        val results = ArrayList<Question>()
        for (fragment in list!!) {
            val onlineExamFragment = fragment as ExamFragment?
            if (onlineExamFragment != null) {
                results.add(onlineExamFragment.getQuestion())
                if (onlineExamFragment.getQuestion().isHasAnswered) {
                    //回答过 则答题数量+1
                    answerCount++
                }
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
            if (question.isHasAnswered) {
                val commit = CommitAnswer()
                commit.id = question.id.toString()
                commit.answer = StringUtils.join(question.answer, "")
                commitList.add(commit)
            }
        }
        if (commitList.isEmpty()) {
            return
        }
        ApiRepository.getInstance().requestSaveAnswer(examId, commitList).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>?>("正在保存答题..") {
            override fun onSuccessNext(entity: BaseResult<Any>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                } else {
                    ToastUtil.show(entity.msg)
                }
                finish()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                finish()
            }
        })
    }


    //是否已交卷
    private var isSubmit = false

    override fun onBackPressed() {
        getAllQuestions()
        if (!isSubmit && answerCount < list!!.size) {
            //说明还没交卷并且还有题目没有答完 需要保存答题进度
            showExitExamAnswerDialog()
        } else {
            //不保存 直接退出
            super.onBackPressed()
        }
    }


    private fun showBottomBarInfo() {
        if (list == null) {
            return
        }
        showButtonByCurrentPage()
        var correctCount = 0
        var wrongCount = 0
        var hasAnswerCount = 0

        for (index in 0 until list!!.size) {
            val fragment = list!![index] as ExamFragment
            if (index == vpExamOnline.currentItem) {
                fragment.getQuestion().isCurrentShow = true
            } else {
                fragment.getQuestion().isCurrentShow = false
            }
        }
        for (index in 0 until list!!.size) {
            val fragment = list!![index] as ExamFragment
            when (fragment.getQuestion().answerStatus) {
                STATUS_ANSWER_WRONG -> {
                    //答错的
                    wrongCount++
                    hasAnswerCount++
                }
                STATUS_ANSWER_RIGHT -> {
                    //答对的
                    correctCount++
                    hasAnswerCount++
                }
            }

        }
        tvAnswerCorrectCount.text = correctCount.toString()
        tvAnswerErrorCount.text = wrongCount.toString()
        val current = vpExamOnline.currentItem + 1
        val info = "" + current + "/" + list!!.size
        tvCurrentAnswerResult.text = info
        //关键点：蓝色圆圈
        loadNumberStatus(questionNumAdapter!!.data)
        questionNumAdapter!!.notifyDataSetChanged()
    }


    private fun showButtonByCurrentPage() {
        if (vpExamOnline.currentItem == list!!.size - 1) {
            setViewGone(tvCommitExam, true)
            setViewGone(tvNextQuestion, false)
        } else {
            setViewGone(tvCommitExam, false)
            setViewGone(tvNextQuestion, true)
        }
    }


    /**
     * 显示是否退出考试
     */
    private fun showExitExamAnswerDialog() {
        baseHandler.postDelayed({
            val dialog = ExamCommonDialog(mContext)
            dialog.create().setContent("是否退出考试 ？").setPositiveButtonListener(View.OnClickListener {
                dialog.dismiss()
                isSubmit = true
                getAllQuestions()
                if (answerCount == 0) {
                    //说明用户没有回答题目 也不用保存题目了 直接返回
                    finish()
                    return@OnClickListener
                }
                saveExam(getAllQuestions())

            }).show()
        }, 100)
    }

    override fun onQuestionClick() {
        //如果是展开状态 就关闭
        behaviorClose()
    }


    private fun behaviorClose(){
        //如果是展开状态 就关闭
        if (behavior!!.state == STATE_EXPANDED) {
            behavior!!.state = STATE_COLLAPSED
        }
    }

}
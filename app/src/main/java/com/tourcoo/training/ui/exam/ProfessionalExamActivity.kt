package com.tourcoo.training.ui.exam

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SpanUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
import com.tourcoo.training.widget.dialog.exam.CommitAnswerDialog
import com.tourcoo.training.widget.dialog.exam.ExamNotPassDialog
import com.tourcoo.training.widget.dialog.exam.ExamPassDialog
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
class ProfessionalExamActivity : BaseTitleActivity(), View.OnClickListener {
    private val mTag = "ProfessionalExamActivity"
    private var fragmentCommonAdapter: CommonFragmentPagerAdapter? = null
    private var questionNumAdapter: QuestionNumberAdapter? = null
    private var list: ArrayList<Fragment>? = null
    private var currentPosition = 0
    private val delayTime = 500L
    private val answerHandler = Handler()
    private var behavior: BottomSheetBehavior<NestedScrollView>? = null
    private var type = 0
    private var trainPlanId = ""
    private var examId = ""

    companion object {
        const val EXTRA_EXAM_ID = "EXTRA_EXAM_ID"
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_exam_online
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("专项考试")
    }

    override fun initView(savedInstanceState: Bundle?) {
        trainPlanId = intent.getStringExtra("trainingPlanId")
        type = intent.getIntExtra("type", 0)


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
        requestExamQuestions(trainPlanId, type)
    }

    private fun loadQuestion(examEntity: ExamEntity?) {
        if (examEntity == null) {
            return
        }
        list?.clear()
        val questions = examEntity.questions
        for (i in 0 until questions.size) {
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
            questions[i].questionNumber = (i + 1).toString()
        }
        return questions
    }


    private fun loadBottomSheetBar(questions: MutableList<Question>?) {
        if (questions == null) {
            return
        }
        questionNumAdapter?.setNewData(setQuestionNumber(questions))
        baseHandler.postDelayed(Runnable {
            showBottomBarInfo()
        }, 500)
    }

    private fun handleBottomBarBehavior() {
        if (behavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else if (behavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }


    private fun requestExamQuestions(trainId: String, type: Int) {
        ApiRepository.getInstance().requestProfessionalExamInfo(trainId, type).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<ExamEntity>?>() {
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

        examId = "" + examEntity.examID

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
    }

    /**
     * 交卷
     */
    private fun doCommitExam() {

        val dialog = CommitAnswerDialog(mContext)
        dialog.create().show()
        dialog.setPositiveButtonListener(View.OnClickListener {
            isSubmit = true
            commitExam(getAllQuestions())
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
            commit.answer = StringUtils.join(question.answer, ",")
            commitList.add(commit)
        }

        ApiRepository.getInstance().requestProfessionalFinishExam(examId, commitList).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<ExamResultEntity>?>() {
            override fun onSuccessNext(entity: BaseResult<ExamResultEntity>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    if (entity.data.status == 0) { //合格

                        if (type == 0) { //正式考试
                            createCertificate(entity)
                        } else {
                            val dialog = ExamPassDialog(mContext)
                            dialog.create()
                                    .setTips(entity.data.tips)
                                    .setPositiveButtonListener {
                                        dialog.dismiss()
                                        finish()
                                    }
                                    .setNegativeGone(false)
                                    .show()
                        }

                    } else { //不合格
                        val dialog = ExamNotPassDialog(mContext)
                        dialog.create()
                                .setTips(entity.data.tips)
                                .setPositiveButtonListener {
                                    dialog.dismiss()
                                    finish()
                                }
                                .show()
                    }

                } else {
                    ToastUtil.show(entity.msg)
                }
            }
        })
    }

    /**
     * 创建证书，提交到后台
     */
    private fun createCertificate(entity: BaseResult<ExamResultEntity>) {
        tvCertificateId.text = "证书编号：NO." + entity.data.data.certificateId
        tvCreateTime.text = entity.data.data.createTime
        tvDetail.text = SpanUtils()
                .append("学员 ").setForegroundColor(Color.parseColor("#999999")).setFontSize(13, true)
                .append(entity.data.data.name).setForegroundColor(Color.parseColor("#333333")).setFontSize(14, true).setUnderline()
                .append(" 身份证号 ").setForegroundColor(Color.parseColor("#999999")).setFontSize(13, true)
                .append(entity.data.data.idCard).setForegroundColor(Color.parseColor("#333333")).setFontSize(14, true).setUnderline()
                .append(" 于 ").setForegroundColor(Color.parseColor("#999999")).setFontSize(13, true)
                .append(entity.data.data.startTime + " - " + entity.data.data.endTime).setForegroundColor(Color.parseColor("#333333")).setFontSize(14, true).setUnderline()
                .append(" 完整学习了交通安培课程 ").setForegroundColor(Color.parseColor("#999999")).setFontSize(13, true)
                .append(entity.data.data.trainingPlanName).setForegroundColor(Color.parseColor("#333333")).setFontSize(14, true).setUnderline()
                .append(" 成绩合格，特授此证书。 ").setForegroundColor(Color.parseColor("#999999")).setFontSize(13, true)
                .create()

        val bitmap = ImageUtils.view2Bitmap(flCertificate)
        val base64Image = "data:image/jpeg;base64," + Base64Util.bitmapToBase64(bitmap)

        uploadCertificate(entity.data.data.certificateId, base64Image, entity.data.tips)
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
                                startActivity(Intent(this@ProfessionalExamActivity, MainTabActivity::class.java))
                                ActivityUtils.finishOtherActivities(MainTabActivity::class.java)
                            }
                            .setNegativeButtonListener {
                                dialog.dismiss()
                                startActivity(Intent(this@ProfessionalExamActivity, MyCertificationActivity::class.java))
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
        val results = ArrayList<Question>()
        for (fragment in list!!) {
            val onlineExamFragment = fragment as ExamFragment?
            if (onlineExamFragment != null) {
                results.add(onlineExamFragment.getQuestion())
            }
        }
        TourCooLogUtil.i(mTag, results)
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
        ApiRepository.getInstance().requestProfessionalSaveAnswer(examId, commitList).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>?>("正在保存答题..") {
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


    //是否已交卷
    private var isSubmit = false

    override fun onBackPressed() {
        super.onBackPressed()
        if (!isSubmit) {
            saveExam(getAllQuestions())
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


}
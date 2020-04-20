package com.tourcoo.training.ui.exam

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tourcoo.training.R
import com.tourcoo.training.adapter.exam.QuestionAdapter
import com.tourcoo.training.core.base.fragment.BaseFragment
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.entity.exam.*
import com.tourcoo.training.entity.exam.Question.*
import kotlinx.android.synthetic.main.fragment_exam_content_online.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月09日17:31
 * @Email: 971613168@qq.com
 */
class OnlineExamFragment : BaseFragment(), View.OnClickListener {

    private var adapter: QuestionAdapter? = null
    private var questionRecyclerView: RecyclerView? = null
    private var tvCurrentQuestion: TextView? = null
    private var tvQuestionType: TextView? = null
    private var question: Question? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_exam_content_online
    }

    override fun initView(savedInstanceState: Bundle?) {
        question = arguments?.getParcelable("question")
        questionRecyclerView = mContentView.findViewById(R.id.questionRecyclerView)
        tvCurrentQuestion = mContentView.findViewById(R.id.tvCurrentQuestion)
        tvQuestionType = mContentView.findViewById(R.id.tvQuestionType)
        questionRecyclerView?.layoutManager = LinearLayoutManager(mContext)
        if (ExamTempHelper.getInstance().examInfo == null || question == null) {
            ToastUtil.show("未获取到题目信息")
            return
        }
        adapter = QuestionAdapter()
        adapter?.bindToRecyclerView(questionRecyclerView)
        showQuestion(question!!)
        loadCorrectAdapter(question!!)
//        testData(question!!)
        loadQuestionHistory(question!!)
        adapter?.setNewData(question!!.answerItems)
        loadItemClick(question!!)
    }


    companion object {
        fun newInstance(question: Question): OnlineExamFragment {
            val args = Bundle()
            args.putParcelable("question", question)
            val fragment = OnlineExamFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            /* R.id.tvNextQuestion -> {
             }
             else -> {
             }*/
        }
    }


    private fun getAllAnswerList(hasAnswer: Boolean): ArrayList<AnswerOld> {
        val answerList = ArrayList<AnswerOld>()
        val answerA = AnswerOld()
        answerA.answerId = "1"
        answerA.answerContent = "我是A选项"
        answerA.isHasAnswered = hasAnswer
        answerA.selectedIcon = R.drawable.img_a_selected
        answerA.unSelectedIcon = R.drawable.img_a
        answerList.add(answerA)
        val answerB = AnswerOld()
        answerB.answerId = "2"
        answerB.answerContent = "我是B选项"
        answerB.isHasAnswered = hasAnswer
        answerB.selectedIcon = R.drawable.img_b_selected
        answerB.unSelectedIcon = R.drawable.img_b
        answerList.add(answerB)
        val answerC = AnswerOld()
        answerC.answerId = "3"
        answerC.answerContent = "我是C选项"
        answerC.isHasAnswered = hasAnswer
        answerC.selectedIcon = R.drawable.img_c_selected
        answerC.unSelectedIcon = R.drawable.img_c
        answerC.isCorrectAnswer = true
        answerList.add(answerC)
        val answerD = AnswerOld()
        answerD.answerContent = "我是D选项"
        answerD.answerId = "4"
        answerD.isHasAnswered = hasAnswer
        answerD.selectedIcon = R.drawable.img_d_selected
        answerD.unSelectedIcon = R.drawable.img_d
        answerList.add(answerD)
        val answerE = AnswerOld()
        answerE.answerContent = "我是E选项"
        answerE.answerId = "5"
        answerE.isHasAnswered = hasAnswer
        answerE.selectedIcon = R.drawable.img_d_selected
        answerE.unSelectedIcon = R.drawable.img_d
        answerList.add(answerE)
        val answerF = AnswerOld()
        answerF.answerContent = "我是F选项"
        answerF.answerId = "6"
        answerF.isHasAnswered = hasAnswer
        answerF.selectedIcon = R.drawable.img_d_selected
        answerF.unSelectedIcon = R.drawable.img_d
        answerList.add(answerF)
        val answerG = AnswerOld()
        answerG.answerContent = "我是G选项"
        answerG.answerId = "7"
        answerG.isHasAnswered = hasAnswer
        answerG.selectedIcon = R.drawable.img_d_selected
        answerG.unSelectedIcon = R.drawable.img_d
        answerList.add(answerG)
        return answerList
    }


    private fun getCorrectAnswerList(hasAnswer: Boolean): ArrayList<AnswerOld> {
        val answerList = ArrayList<AnswerOld>()
        val answerC = AnswerOld()
        answerC.answerId = "3"
        answerC.answerContent = "我是C选项"
        answerC.selectedIcon = R.drawable.img_c_selected
        answerC.unSelectedIcon = R.drawable.img_c
        answerC.isCorrectAnswer = true
        answerC.isHasAnswered = hasAnswer
        answerList.add(answerC)
        val answerD = AnswerOld()
        answerD.answerContent = "我是D选项"
        answerD.answerId = "4"
        answerD.isCorrectAnswer = true
        answerD.isHasAnswered = hasAnswer
        answerD.selectedIcon = R.drawable.img_d_selected
        answerD.unSelectedIcon = R.drawable.img_d
        answerList.add(answerD)
        return answerList
    }

    private fun loadItemClick(question: Question) {
        if (adapter == null) {
            return
        }
        adapter!!.setOnItemClickListener { adapter, view, position ->
            if (question.isHasAnswered) {
                return@setOnItemClickListener
            }
            when (question.type) {
                QUESTION_TYPE_SINGLE -> {
                    handleClickSingle(position)
                }
                QUESTION_TYPE_MULTIPLE -> {
                    //多选
                    handleClickMultiple(position)
                }
                //判断
                QUESTION_TYPE_JUDGE -> {
                    handleClickSingle(position)
                }
                else -> {
                }
            }
        }
    }


    private fun assemblyExamEntity(): ExaminationEntityOld {
        val entity = ExaminationEntityOld()
        entity.questionId = "1001"
//        entity.questionType = QUESTION_TYPE_SINGLE
        entity.questionType = QUESTION_TYPE_MULTIPLE
        entity.isHasAnswered = false
        entity.answerOldList = getAllAnswerList(entity.isHasAnswered)
        entity.correctAnswerOldList = getCorrectAnswerList(entity.isHasAnswered)
        entity.questionContent = "安排开发骄傲阿松发哦苏东坡快结束了可根据"
        for (answer in entity.answerOldList) {
            for (correctAnswer in entity.correctAnswerOldList) {
                if (answer.answerId == correctAnswer.answerId) {
                    answer.isCorrectAnswer = true
                }
            }
        }
        return entity
    }

    /* private fun transformQuestion(question :Question ){
         //todo
         if(question.){

         }
     }
 */
    private fun showQuestion(question: Question?) {
        when (question?.type) {
            QUESTION_TYPE_SINGLE -> {
                tvQuestionType?.text = "单选"
            }
            QUESTION_TYPE_MULTIPLE -> {
                tvQuestionType?.text = "多选"
            }
            QUESTION_TYPE_JUDGE -> {
                tvQuestionType?.text = "判断"
            }
            else -> {
            }
        }
        tvCurrentQuestion?.text = question?.question
        tvAnswerParsing?.text = question?.analysis
    }


    private fun handleClickSingle(position: Int) {
        //所有答案选项
        val allAnswerList = question!!.answerItems
        val clickedAnswer = adapter!!.data[position] as Answer
        for (current in allAnswerList) {
            if (current.answerId == clickedAnswer.answerId) {
                clickedAnswer.isSelect = !clickedAnswer.isSelect
            } else {
                current.isSelect = false
            }
        }
        adapter!!.notifyDataSetChanged()
    }

    private fun handleClickMultiple(position: Int) {
        //多选
        val allAnswerList = question!!.answerItems
        val clickedAnswer = adapter!!.data[position] as Answer
        for (current in allAnswerList) {
            if (current.answerId == clickedAnswer.answerId) {
                clickedAnswer.isSelect = !clickedAnswer.isSelect
            }
        }
        adapter!!.notifyDataSetChanged()
    }

    /**
     * 回答问题
     */
    fun answerQuestion(): Boolean {
        if (question == null || question?.answerItems == null) {
            ToastUtil.show("未获取到题目信息")
            return false
        }
        val answerList = question!!.answerItems
        if (!hasSelect()) {
            return false
        }
        question!!.isHasAnswered = true
//        回答问题后每次都创建一个新的用户选择集合
        question!!.answer = ArrayList()
        for (answer in answerList) {
            //已经回答过问题
            answer!!.isHasAnswered = true
            if (answer.isSelect) {
                //关键点 如果改题目被选中 则添加到用户选择集合中 表示用户选择了这些答案
                question!!.answer.add(answer.answerId)
            }
        }
        //
        adapter?.notifyDataSetChanged()
        return true
    }

    /**
     * 判断题目是否选中
     */
    private fun hasSelect(): Boolean {
        return getSelectCount() > 0
    }

    /**
     * 判断是否选中答案
     */
    fun getSelectCount(): Int {
        var count = 0
        for (answer in adapter!!.data) {
            if (answer.isSelect) {
                count++
            } else {
                continue
            }
        }
        return count
    }

    /**
     * 是否是多选题
     */
    fun isMultipleAnswer(): Boolean {
        return QUESTION_TYPE_MULTIPLE == question!!.type
    }


    private fun loadCorrectAdapter(question: Question) {
        if (question.correctAnswer == null) {
            return
        }
        for (answerItem in question.answerItems) {
            for (s in question.correctAnswer) {
                if (answerItem.answerId == s) {
                    answerItem.isCorrectAnswer = true
                }
            }
        }
    }


    fun getQuestion(): Question {
        return question!!
    }

    private fun loadQuestionHistory(question: Question){
       if( question.answer == null || question.answer.isEmpty() || question.answerItems == null  ){
           return
       }
        for (answerId in question.answer) {
            for (answerItem in question.answerItems) {
                if(answerId == answerItem.answerId){
                    //说明该题目已经被回答过
                    setHasAnswer(question.answerItems)
                    answerItem.isSelect = true
                    question.isHasAnswered = true

                }
            }
        }
    }

    private fun setHasAnswer(allAnswer: MutableList<Answer>){
        for (answer in allAnswer) {
            answer.isHasAnswered = true
        }
    }


    private fun testData(question: Question){
        if(question.type == QUESTION_TYPE_SINGLE ){
            question.answer = ArrayList<String>()
            question.answer.add("C")
        }
    }
}
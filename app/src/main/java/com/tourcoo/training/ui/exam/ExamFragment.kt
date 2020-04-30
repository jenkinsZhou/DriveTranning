package com.tourcoo.training.ui.exam

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tourcoo.training.R
import com.tourcoo.training.adapter.exam.QuestionAdapter
import com.tourcoo.training.constant.ExamConstant.*
import com.tourcoo.training.core.base.fragment.BaseFragment
import com.tourcoo.training.core.util.CommonUtil
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
class ExamFragment : BaseFragment(), View.OnClickListener {

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
        //先加载用户的答题状态
        question!!.answerStatus = getQuestionStatus()
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
        fun newInstance(question: Question): ExamFragment {
            val args = Bundle()
            args.putParcelable("question", question)
            val fragment = ExamFragment()
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
        tvAnswerAnalysis?.text = question?.analysis
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
        question!!.answerStatus = getQuestionStatus()
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

    private fun loadQuestionHistory(question: Question) {
        if (question.answer == null || question.answer.isEmpty() || question.answerItems == null) {
            return
        }
        for (answerId in question.answer) {
            for (answerItem in question.answerItems) {
                if (answerId == answerItem.answerId) {
                    //说明该题目已经被回答过
                    setHasAnswer(question.answerItems)
                    answerItem.isSelect = true
                    question.isHasAnswered = true
                }
            }
        }
        showQuestionAnalysis(getQuestionStatus() != STATUS_NO_ANSWER)
    }

    private fun setHasAnswer(allAnswer: MutableList<Answer>) {
        for (answer in allAnswer) {
            answer.isHasAnswered = true
        }
    }


    private fun testData(question: Question) {
        if (question.type == QUESTION_TYPE_SINGLE) {
            question.answer = ArrayList<String>()
            question.answer.add("C")
        }
    }


    fun getQuestionStatus(): Int {
        if (question == null || question!!.answer == null || question!!.answer.isEmpty() || question!!.answerItems == null || question!!.correctAnswer == null) {
            //未回答
            return STATUS_NO_ANSWER
        }
        //然后判断这个回答是否正确
        val same = CommonUtil.checkDifferent(question!!.correctAnswer, question!!.answer)
        if (same) {
            return STATUS_ANSWER_RIGHT
        }
        return STATUS_ANSWER_WRONG
    }


    fun showQuestionAnalysis(visible: Boolean) {
        setViewVisible(llQuestionAnalysis, visible)
    }
}
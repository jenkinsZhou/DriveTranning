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
import com.tourcoo.training.entity.exam.Answer
import com.tourcoo.training.entity.exam.ExaminationEntity
import com.tourcoo.training.entity.exam.ExaminationEntity.*

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
    private var examinationEntity: ExaminationEntity? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_exam_content_online
    }

    override fun initView(savedInstanceState: Bundle?) {
        questionRecyclerView = mContentView.findViewById(R.id.questionRecyclerView)
        tvCurrentQuestion = mContentView.findViewById(R.id.tvCurrentQuestion)
        tvQuestionType = mContentView.findViewById(R.id.tvQuestionType)
        questionRecyclerView?.layoutManager = LinearLayoutManager(mContext)
        adapter = QuestionAdapter()
        adapter?.bindToRecyclerView(questionRecyclerView)
        examinationEntity = assemblyExamEntity()
        showQuestion(examinationEntity!!)
        adapter?.setNewData(examinationEntity!!.answerList)
        loadItemClick(examinationEntity!!)
    }



    companion object {
        fun newInstance(): OnlineExamFragment {
            val args = Bundle()
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


    private fun getAllAnswerList(hasAnswer: Boolean): ArrayList<Answer> {
        val answerList = ArrayList<Answer>()
        val answerA = Answer()
        answerA.answerId = "1"
        answerA.answerContent = "我是A选项"
        answerA.isHasAnswered = hasAnswer
        answerA.selectedIcon = R.drawable.img_a_selected
        answerA.unSelectedIcon = R.drawable.img_a
        answerList.add(answerA)
        val answerB = Answer()
        answerB.answerId = "2"
        answerB.answerContent = "我是B选项"
        answerB.isHasAnswered = hasAnswer
        answerB.selectedIcon = R.drawable.img_b_selected
        answerB.unSelectedIcon = R.drawable.img_b
        answerList.add(answerB)
        val answerC = Answer()
        answerC.answerId = "3"
        answerC.answerContent = "我是C选项"
        answerC.isHasAnswered = hasAnswer
        answerC.selectedIcon = R.drawable.img_c_selected
        answerC.unSelectedIcon = R.drawable.img_c
        answerC.isCorrectAnswer = true
        answerList.add(answerC)
        val answerD = Answer()
        answerD.answerContent = "我是D选项"
        answerD.answerId = "4"
        answerD.isHasAnswered = hasAnswer
        answerD.selectedIcon = R.drawable.img_d_selected
        answerD.unSelectedIcon = R.drawable.img_d
        answerList.add(answerD)
        return answerList
    }


    private fun getCorrectAnswerList(hasAnswer: Boolean): ArrayList<Answer> {
        val answerList = ArrayList<Answer>()
        val answerC = Answer()
        answerC.answerId = "3"
        answerC.answerContent = "我是C选项"
        answerC.selectedIcon = R.drawable.img_c_selected
        answerC.unSelectedIcon = R.drawable.img_c
        answerC.isCorrectAnswer = true
        answerC.isHasAnswered = hasAnswer
        answerList.add(answerC)
        val answerD = Answer()
        answerD.answerContent = "我是D选项"
        answerD.answerId = "4"
        answerD.isCorrectAnswer = true
        answerD.isHasAnswered = hasAnswer
        answerD.selectedIcon = R.drawable.img_d_selected
        answerD.unSelectedIcon = R.drawable.img_d
        answerList.add(answerD)
        return answerList
    }

    private fun loadItemClick(examinationEntity: ExaminationEntity) {
        if (adapter == null) {
            return
        }
        adapter!!.setOnItemClickListener { adapter, view, position ->
            when (examinationEntity.questionType) {
                QUESTION_TYPE_SINGLE -> {
                    handleClickSingle(position)
                }
                QUESTION_TYPE_MULTIPLE -> {
                    //多选
                    handleClickMultiple(position)
                }
                else -> {
                }
            }
        }
    }


    private fun assemblyExamEntity(): ExaminationEntity {
        val entity = ExaminationEntity()
        entity.questionId = "1001"
//        entity.questionType = QUESTION_TYPE_SINGLE
        entity.questionType = QUESTION_TYPE_MULTIPLE
        entity.isHasAnswered = false
        entity.answerList = getAllAnswerList(entity.isHasAnswered)
        entity.correctAnswerList = getCorrectAnswerList(entity.isHasAnswered)
        entity.questionContent = "安排开发骄傲阿松发哦苏东坡快结束了可根据"
        for (answer in entity.answerList) {
            for (correctAnswer in entity.correctAnswerList) {
                if (answer.answerId == correctAnswer.answerId) {
                    answer.isCorrectAnswer = true
                }
            }
        }
        return entity
    }


    private fun showQuestion(examinationEntity: ExaminationEntity) {
        when (examinationEntity.questionType) {
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
        tvCurrentQuestion?.text = examinationEntity.questionContent
    }


    private fun handleClickSingle(position: Int) {
        //单选
        val allAnswerList = examinationEntity!!.answerList
        val clickedAnswer = adapter!!.data[position] as Answer
        for (current in allAnswerList) {
            if (current.answerId == clickedAnswer.answerId) {
                clickedAnswer.isSelected = !clickedAnswer.isSelected
            } else {
                current.isSelected = false
            }
        }
        adapter!!.notifyDataSetChanged()
    }

    private fun handleClickMultiple(position: Int) {
        //多选
        val allAnswerList = examinationEntity!!.answerList
        val clickedAnswer = adapter!!.data[position] as Answer
        for (current in allAnswerList) {
            if (current.answerId == clickedAnswer.answerId) {
                clickedAnswer.isSelected = !clickedAnswer.isSelected
            }
        }
        adapter!!.notifyDataSetChanged()
    }

    /**
     * 回答问题
     */
    fun answerQuestion(): Boolean {
        if (examinationEntity == null || examinationEntity?.answerList == null) {
            ToastUtil.show("未获取到题目信息")
            return false
        }
        val answerList = examinationEntity!!.answerList
        if (!hasSelect()) {
            return false
        }
        examinationEntity!!.isHasAnswered = true
        for (answer in answerList) {
            answer!!.isHasAnswered = true
        }
        adapter?.notifyDataSetChanged()
        return true
    }


    private fun hasSelect(): Boolean {
        return getSelectCount() > 0
    }

    fun getSelectCount(): Int {
        var count = 0
        for (answer in examinationEntity!!.answerList) {
            if (answer.isSelected) {
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
        return QUESTION_TYPE_MULTIPLE == examinationEntity!!.questionType
    }
}
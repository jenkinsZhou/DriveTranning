package com.tourcoo.training.ui.training.safe

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tourcoo.training.R
import com.tourcoo.training.adapter.dialog.CourseSelectAdapter
import com.tourcoo.training.adapter.training.OnLineTrainingCourseAdapter
import com.tourcoo.training.adapter.training.OnLineTrainingCourseAdapter.*
import com.tourcoo.training.adapter.training.ProfessionalTrainingAdapter
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.base.fragment.BaseFragment
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.account.UserInfo
import com.tourcoo.training.entity.course.CourseEntity
import com.tourcoo.training.entity.course.CourseInfo
import com.tourcoo.training.entity.training.ProfessionTrainingEntity
import com.tourcoo.training.ui.exam.OnlineExamActivity
import com.tourcoo.training.ui.exam.OnlineExamActivity.Companion.EXTRA_EXAM_ID
import com.tourcoo.training.ui.exam.OnlineExamActivity.Companion.EXTRA_TRAINING_PLAN_ID
import com.tourcoo.training.widget.dialog.CommonListDialog
import com.trello.rxlifecycle3.android.FragmentEvent
import kotlinx.android.synthetic.main.fragment_mine_tab_new.*

/**
 *@description :线上培训
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月11日10:55
 * @Email: 971613168@qq.com
 */
class OnlineTrainFragment : BaseFragment() {
    private var adapter: OnLineTrainingCourseAdapter? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_training_profressional
    }


    override fun initView(savedInstanceState: Bundle?) {
        refreshLayout = mContentView.findViewById(R.id.smartRefreshLayoutCommon)
        refreshLayout?.setRefreshHeader(ClassicsHeader(mContext))
        recyclerView = mContentView.findViewById(R.id.rvCommon)
        recyclerView?.layoutManager = LinearLayoutManager(mContext)
        adapter = OnLineTrainingCourseAdapter()
        adapter?.bindToRecyclerView(recyclerView)
        initTrainingPlanClick()
        /* testData()
         baseHandler.postDelayed(Runnable {
             showCourseDialog()
         }, 500)*/
    }


    companion object {
        fun newInstance(): OnlineTrainFragment {
            val args = Bundle()
            val fragment = OnlineTrainFragment()
            fragment.arguments = args
            return fragment
        }
    }

    /* private fun testData() {
         adapter?.addData(ProfessionTrainingEntity())
         adapter?.addData(ProfessionTrainingEntity())
         adapter?.addData(ProfessionTrainingEntity())
         adapter?.addData(ProfessionTrainingEntity())
     }*/


    private fun showCourseDialog() {
        val adapter = CourseSelectAdapter()
        adapter.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                for (entity in adapter!!.data) {
                    val item = entity as CourseEntity
                    val clickEntity = adapter.data[position] as CourseEntity
                    if (item.id == clickEntity.id) {
                        clickEntity.isSelect = !clickEntity.isSelect
                    } else {
                        item.isSelect = false
                    }
                }
                adapter.notifyDataSetChanged()
            }
        })
        val list = ArrayList<CourseEntity>()
        for (i in 0 until 3) {
            var item = CourseEntity()
            item.id = "900$i"
            item.courseDurationDesc = "d"
            list.add(item)
        }
        val dialog = CommonListDialog<CourseEntity>(mContext).create().setDataAdapter(adapter)
                .setDataList(list)
        dialog.show()
    }

    private fun requestCourseOnLine() {
        ApiRepository.getInstance().requestOnLineTrainingList().compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<CourseInfo>?>?>() {
            override fun onSuccessNext(entity: BaseResult<MutableList<CourseInfo>?>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    handleOnLineCourseList(entity?.data)
                } else {
                    ToastUtil.show(entity.msg)
                }

            }

            override fun onError(e: Throwable) {
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed(e.toString())
                }
            }

        })

    }

    override fun loadData() {
        super.loadData()
        requestCourseOnLine()
    }

    private fun handleOnLineCourseList(list: MutableList<CourseInfo>?) {
        if (list == null) {
            return
        }
        adapter?.setNewData(list)
    }

    private fun initTrainingPlanClick() {
        if (adapter == null) {
            return
        }
        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            doSkipByStatus(adapter!!.data[position] as CourseInfo)
        }
    }

    private fun doSkipByStatus(courseInfo: CourseInfo?) {
        if (courseInfo == null) {
            return
        }
        when (courseInfo.status) {
            COURSE_STATUS_NEED_PAY -> {
                val intent = Intent(mContext, OnlineExamActivity::class.java)
                //培训计划id
                intent.putExtra(EXTRA_TRAINING_PLAN_ID, courseInfo.trainingPlanID)
                //考试题id
                intent.putExtra(EXTRA_EXAM_ID, "0")
                startActivity(intent)
            }
            else -> {
                ToastUtil.show("未匹配到类型")
            }
        }

    }
}
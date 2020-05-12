package com.tourcoo.training.ui.training.safe.online.fragment

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.didichuxing.doraemonkit.zxing.activity.CaptureActivity
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tourcoo.training.R
import com.tourcoo.training.adapter.training.OffLineTrainingAdapter
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant.*
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.base.fragment.BaseFragment
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.account.UserInfoEvent
import com.tourcoo.training.entity.course.CourseInfo
import com.tourcoo.training.ui.training.safe.online.detail.common.CommonPlanDetailActivity
import com.tourcoo.training.ui.training.safe.online.detail.student.StudentPlanDetailActivity
import com.tourcoo.training.ui.training.safe.online.detail.teacher.TeacherPlanDetailActivity
import com.tourcoo.training.utils.RecycleViewDivider
import com.trello.rxlifecycle3.android.FragmentEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *@description :现场培训
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月11日10:57
 * @Email: 971613168@qq.com
 */
class OfflineTrainFragment : BaseFragment() {

    private var adapter: OffLineTrainingAdapter? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var currentPlanId: String? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_training_profressional
    }


    override fun initView(savedInstanceState: Bundle?) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        refreshLayout = mContentView.findViewById(R.id.smartRefreshLayoutCommon)
        refreshLayout?.setRefreshHeader(ClassicsHeader(mContext))
        refreshLayout?.setOnRefreshListener {
            requestCourseOffLine()
        }

        recyclerView = mContentView.findViewById(R.id.rvCommon)
        recyclerView?.layoutManager = LinearLayoutManager(mContext)
        recyclerView?.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, ConvertUtils.dp2px(10f), resources.getColor(R.color.grayFBF8FB), true))
        adapter = OffLineTrainingAdapter()
        adapter?.bindToRecyclerView(recyclerView)
        adapter?.setEmptyView(R.layout.empty_driver_layout)
        adapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            skipTrainingDetail(position)
        }


    }


    companion object {
        fun newInstance(): OfflineTrainFragment {
            val args = Bundle()
            val fragment = OfflineTrainFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun loadData() {
        super.loadData()
        requestCourseOffLine()
    }

    private fun handleOnLineCourseList(list: MutableList<CourseInfo>?) {
        if (list == null) {
            return
        }
        adapter?.setNewData(list)
    }

    private fun requestCourseOffLine() {
        if (!AccountHelper.getInstance().isLogin) {
            removeData()
            refreshLayout?.finishRefresh()
            return
        }
        ApiRepository.getInstance().requestOffLineTrainingList().compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<CourseInfo>?>?>() {
            override fun onSuccessNext(entity: BaseResult<MutableList<CourseInfo>?>?) {
                refreshLayout?.finishRefresh()
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
                refreshLayout?.finishRefresh(false)
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed(e.toString())
                }
            }

        })

    }

    private fun skipTrainingDetail(position: Int) {
        val courseInfo = (adapter as OffLineTrainingAdapter).data[position]
        when (courseInfo.role) {
            TRAIN_ROLE_STUDENT -> {
                val intent = Intent(mContext, StudentPlanDetailActivity::class.java)
                intent.putExtra(EXTRA_TRAINING_PLAN_ID, if (courseInfo.trainingPlanID.isNullOrEmpty()) "" else courseInfo.trainingPlanID)
                startActivity(intent)
            }
            TRAIN_ROLE_TEACHER -> {
                val intent = Intent(mContext, TeacherPlanDetailActivity::class.java)
                intent.putExtra(EXTRA_TRAINING_PLAN_ID, if (courseInfo.trainingPlanID.isNullOrEmpty()) "" else courseInfo.trainingPlanID)
                startActivity(intent)
            }
            TRAIN_ROLE_TEACHER_AND_STUDENT -> {
                //安全员+学员
                val intent = Intent(mContext, CommonPlanDetailActivity::class.java)
                intent.putExtra(EXTRA_TRAINING_PLAN_ID, if (courseInfo.trainingPlanID.isNullOrEmpty()) "" else courseInfo.trainingPlanID)
                startActivity(intent)
            }
            else -> {
                ToastUtil.show("当前身份未知,Role为" + courseInfo.role)
            }
        }


    }


    private fun removeData() {
        adapter?.data?.clear()
        adapter?.notifyDataSetChanged()
        refreshLayout?.finishRefresh()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


    /**
     * 收到消息
     *
     * @param userInfoEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserInfoRefreshEvent(userInfoEvent: UserInfoEvent?) {
        if (userInfoEvent == null) {
            return
        }
        if (userInfoEvent.userInfo == null) {
            removeData()
        } else {
            requestCourseOffLine()
        }

    }
}
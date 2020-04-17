package com.tourcoo.training.ui.training.safe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tourcoo.training.R
import com.tourcoo.training.adapter.dialog.CourseSelectAdapter
import com.tourcoo.training.adapter.training.OnLineTrainingCourseAdapter
import com.tourcoo.training.adapter.training.OnLineTrainingCourseAdapter.COURSE_STATUS_NEED_PAY
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.base.fragment.BaseFragment
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.entity.course.CourseEntity
import com.tourcoo.training.entity.course.CourseInfo
import com.tourcoo.training.ui.account.LoginActivity
import com.tourcoo.training.ui.account.LoginActivity.Companion.EXTRA_TYPE_RECOGNIZE_COMPARE
import com.tourcoo.training.ui.account.register.RecognizeIdCardActivity
import com.tourcoo.training.ui.exam.OnlineExamActivity
import com.tourcoo.training.ui.exam.OnlineExamActivity.Companion.EXTRA_EXAM_ID
import com.tourcoo.training.ui.face.FaceRecognitionActivity
import com.tourcoo.training.ui.training.online.PlayVideoActivity
import com.tourcoo.training.widget.dialog.CommonListDialog
import com.tourcoo.training.widget.dialog.recognize.RecognizeStepDialog
import com.trello.rxlifecycle3.android.FragmentEvent

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
    private var currentPlanId: String? = null
    private var dialog: RecognizeStepDialog? = null
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

        const val REQUEST_CODE_FACE_RECORD = 201

        const val REQUEST_CODE_FACE_COMPARE = 202
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
            //todo
            if (position == 2) {
                currentPlanId = (adapter!!.data[position] as CourseInfo).trainingPlanID
                showRecognize(currentPlanId)
            } else {
                doSkipByStatus(adapter!!.data[position] as CourseInfo)
            }
//            doSkipByStatus(adapter!!.data[position] as CourseInfo)
        }
    }

    private fun doSkipByStatus(courseInfo: CourseInfo?) {
        if (courseInfo == null) {
            return
        }
        when (courseInfo.status) {
            COURSE_STATUS_NEED_PAY -> {
                skipPlayVideo("")
            }
            else -> {
                val intent = Intent(mContext, OnlineExamActivity::class.java)
                //培训计划id
                intent.putExtra(EXTRA_TRAINING_PLAN_ID, courseInfo.trainingPlanID)
                //考试题id
                //todo 考试id 暂时写死
                intent.putExtra(EXTRA_EXAM_ID, "0")
                startActivity(intent)
            }
        }
    }


    private fun showRecognize(trainingId: String?) {
        if (TextUtils.isEmpty(trainingId)) {
            ToastUtil.show("'未获取到培训信息")
            return
        }
        dialog = RecognizeStepDialog(mContext).create().setPositiveButton {
            skipFaceRecord(trainingId!!)
        }
        dialog?.show()
    }

    private fun skipFaceRecord(trainingId: String) {
        val intent = Intent(mContext, FaceRecognitionActivity::class.java)
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, trainingId)
        startActivityForResult(intent, REQUEST_CODE_FACE_RECORD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_FACE_RECORD -> {
                    if (data != null) {
                        handleStepOneSuccess()
                    }
                }
                REQUEST_CODE_FACE_COMPARE -> {
                    ToastUtil.showSuccess("验证通过")
                    closeFaceDialog()
                }
            }
        } else {
            baseHandler.postDelayed({
                closeFaceDialog()
            }, 500)
        }
    }


    private fun handleStepOneSuccess() {
        dialog?.showStepOneSuccess()
        dialog?.setPositiveButton(View.OnClickListener {
            skipIdCardRecognize(currentPlanId)
        })
    }

    /**
     * 人脸和身份证比对
     */
    private fun skipIdCardRecognize(trainingId: String?) {
        if (TextUtils.isEmpty(trainingId)) {
            ToastUtil.show("'未获取到培训信息")
            return
        }
        val bundle = Bundle()
        bundle.putInt(LoginActivity.EXTRA_KEY_REGISTER_TYPE, LoginActivity.EXTRA_REGISTER_TYPE_DRIVER)
        AccountTempHelper.getInstance().recognizeType = EXTRA_TYPE_RECOGNIZE_COMPARE
        val intent = Intent(mContext, RecognizeIdCardActivity::class.java)
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, trainingId)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_FACE_COMPARE)
    }


    private fun closeFaceDialog() {
        dialog?.dismiss()
    }


    private fun skipPlayVideo(trainingId: String?) {
        val intent = Intent(mContext, PlayVideoActivity::class.java)
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, trainingId)
        startActivity(intent)
    }
}
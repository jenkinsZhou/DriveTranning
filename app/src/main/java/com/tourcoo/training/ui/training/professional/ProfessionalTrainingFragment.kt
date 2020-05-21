package com.tourcoo.training.ui.training.professional

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tourcoo.training.R
import com.tourcoo.training.adapter.training.OnLineTrainingCourseAdapter
import com.tourcoo.training.adapter.training.ProfessionalTrainingAdapter
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.entity.BasePageResult
import com.tourcoo.training.core.base.fragment.BaseTitleRefreshLoadFragment
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.entity.course.CourseInfo
import com.tourcoo.training.entity.training.ProfessionTrainingEntity
import com.tourcoo.training.ui.account.LoginActivity
import com.tourcoo.training.ui.account.register.RecognizeIdCardActivity
import com.tourcoo.training.ui.face.FaceRecognitionActivity
import com.tourcoo.training.ui.pay.BuyNowActivity
import com.tourcoo.training.ui.study.StudyDetailActivity
import com.tourcoo.training.ui.training.safe.online.TencentPlayVideoActivity
import com.tourcoo.training.ui.training.safe.online.aliyun.AliYunPlayVideoActivity
import com.tourcoo.training.ui.training.safe.online.fragment.OnlineTrainFragment
import com.tourcoo.training.ui.training.safe.online.web.HtmlBrowserActivity
import com.tourcoo.training.utils.RecycleViewDivider
import com.tourcoo.training.widget.dialog.CommonBellAlert
import com.tourcoo.training.widget.dialog.recognize.RecognizeStepDialog
import com.tourcoo.training.widget.dialog.training.LocalTrainingConfirmDialog
import com.trello.rxlifecycle3.android.FragmentEvent

/**
 *@description : 专项培训列表
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月10日20:29
 * @Email: 971613168@qq.com
 */
class ProfessionalTrainingFragment : BaseTitleRefreshLoadFragment<ProfessionTrainingEntity>() {

    private var adapter: ProfessionalTrainingAdapter? = null
    private var dialog: RecognizeStepDialog? = null
    private var clickTrainingEntity: ProfessionTrainingEntity? = null
    override fun getAdapter(): BaseQuickAdapter<ProfessionTrainingEntity, BaseViewHolder> {
        adapter = ProfessionalTrainingAdapter()
        return adapter!!
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.visibility = View.GONE
    }

    override fun loadData(page: Int) {
        requestCourseProfessionalTraining(page)
    }


    override fun getContentLayout(): Int {
        return R.layout.fragment_training_profressional_new
    }


    override fun initView(savedInstanceState: Bundle?) {
        mRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, ConvertUtils.dp2px(10f), resources.getColor(R.color.grayFBF8FB), true))
        initTrainingPlanClick()
    }


    companion object {
        fun newInstance(): ProfessionalTrainingFragment {
            val args = Bundle()
            val fragment = ProfessionalTrainingFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private fun requestCourseProfessionalTraining(page: Int) {
        ApiRepository.getInstance().requestCourseProfessionalTraining(page).compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BasePageResult<ProfessionTrainingEntity>?>(iHttpRequestControl) {
            override fun onSuccessNext(entity: BasePageResult<ProfessionTrainingEntity>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
//                    handleOnLineCourseList(entity.data)
                    UiManager.getInstance().httpRequestControl.httpRequestSuccess(iHttpRequestControl, if (entity.data == null) ArrayList<ProfessionTrainingEntity>() else entity.data.rows, null)
                } else {
                    UiManager.getInstance().httpRequestControl.httpRequestSuccess(iHttpRequestControl, ArrayList<ProfessionTrainingEntity>())
                    ToastUtil.show(entity.msg)
                }

            }

        })

    }


    private fun initTrainingPlanClick() {
        if (adapter == null) {
            return
        }
        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            clickTrainingEntity = adapter!!.data[position] as ProfessionTrainingEntity
            if (!AccountHelper.getInstance().isLogin) {
                ToastUtil.show("请先登录")
                return@OnItemClickListener
            }

            //isAuthenticated:0：企业分配  1：自己注册暂未审核 2：已审核  3：驳回
            if (AccountHelper.getInstance().userInfo.isAuthenticated == 1 || AccountHelper.getInstance().userInfo.isAuthenticated == 3) {
                val dialog = CommonBellAlert(mContext)
                dialog.create().setContent("驾驶员自主注册，需等待企业管理员审核。").setPositiveButton("知道了", object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        dialog.dismiss()
                    }
                })
                dialog.show()
                return@OnItemClickListener
            }
            when (AccountHelper.getInstance().userInfo.status) {
                0 -> {  //未认证
                    showRecognize()
                }
                else -> {

                    skipTwoTypeActivity()
                }
            }


        }
    }

    private fun showRecognize() {
        dialog = RecognizeStepDialog(mContext).create().setPositiveButton {
            skipFaceRecord()
        }
        dialog?.show()
    }


    private fun skipFaceRecord() {
        val intent = Intent(mContext, FaceRecognitionActivity::class.java)
        intent.putExtra("OnlyBase64", true)
        //这里因为不需要用到 trainId 但又必须要传 因此传个0
        intent.putExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID, "0")
        startActivityForResult(intent, OnlineTrainFragment.REQUEST_CODE_FACE_RECORD)
    }


    private fun doSkipByStatus(courseInfo: CourseInfo?) {
        if (courseInfo == null) {
            return
        }
        when (courseInfo.status) {


            OnLineTrainingCourseAdapter.COURSE_STATUS_CONTINUE -> {
                skipFaceVefify(courseInfo.trainingPlanID)
            }

            OnLineTrainingCourseAdapter.COURSE_STATUS_WAIT_EXAM -> {
                skipPlayVideoByType(courseInfo.trainingPlanID, courseInfo.type)
            }

            OnLineTrainingCourseAdapter.COURSE_STATUS_FINISHED -> {
                //已完成 跳转到培训记录
                skipStudyDetailActivity(courseInfo.trainingPlanID)
            }

            else -> {
                skipPlayVideoByType(courseInfo.trainingPlanID, courseInfo.type)
            }
        }
    }

    private fun skipFaceVefify(trainingId: String) {
        val intent = Intent(mContext, FaceRecognitionActivity::class.java)
        intent.putExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID, trainingId)
        startActivityForResult(intent, OnlineTrainFragment.REQUEST_CODE_FACE_VERIFY)
    }


    private fun skipPlayVideoByType(trainingId: String?, courseType: Int) {
        var intent: Intent? = null
        when (courseType) {
            TrainingConstant.TYPE_COURSE_HTML -> {
                intent = Intent(mContext, HtmlBrowserActivity::class.java)
            }
            TrainingConstant.TYPE_COURSE_TYPE_DRIVE -> {
                //车学堂 使用腾讯播放器
                intent = Intent(mContext, TencentPlayVideoActivity::class.java)
            }
            TrainingConstant.TYPE_TYPE_COURSE_HLS -> {
                //hls 使用阿里播放器
                intent = Intent(mContext, AliYunPlayVideoActivity::class.java)
            }
            TrainingConstant.TYPE_COURSE_OTHER -> {
                //混合非加密 使用腾讯播放器
                intent = Intent(mContext, TencentPlayVideoActivity::class.java)
                //todo 暂时跳转到阿里播放器
//                intent = Intent(mContext, AliYunPlayVideoActivity::class.java)
            }

        }
        intent?.putExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID, trainingId)
        startActivityForResult(intent, OnlineTrainFragment.REQUEST_CODE_REFRESH_ALL)
    }


    private fun skipStudyDetailActivity(trainingId: String?) {
        val intent = Intent(mContext, StudyDetailActivity::class.java)
        intent.putExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID, trainingId)
        startActivityForResult(intent, OnlineTrainFragment.REQUEST_CODE_REFRESH_ALL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OnlineTrainFragment.REQUEST_CODE_FACE_RECORD -> {
                    if (data != null) {
                        handleStepOneSuccess()
                    }
                }
                OnlineTrainFragment.REQUEST_CODE_FACE_COMPARE -> {
                    AccountHelper.getInstance().userInfo.status = 1
                    ToastUtil.showSuccess("验证通过")
                    closeFaceDialog()
                    //todo
                    if (clickTrainingEntity == null) {
                        ToastUtil.show("未获取到计划详情")
                        return
                    }
                    skipTwoTypeActivity()

                }

            }
        } else {
            baseHandler.postDelayed({
                closeFaceDialog()
            }, 500)
        }
        //以下是刷新列表逻辑
        if (requestCode == OnlineTrainFragment.REQUEST_CODE_REFRESH_ALL || resultCode == Activity.RESULT_OK) {
            mRefreshLayout.autoRefresh()
        }
    }

    private fun closeFaceDialog() {
        dialog?.dismiss()
    }


    private fun handleStepOneSuccess() {
        dialog?.showStepOneSuccess()
        dialog?.setPositiveButton(View.OnClickListener {
            skipIdCardRecognize()
        })
    }

    /**
     * 人脸和身份证比对
     */
    private fun skipIdCardRecognize() {
        val bundle = Bundle()
        bundle.putInt(LoginActivity.EXTRA_KEY_REGISTER_TYPE, LoginActivity.EXTRA_REGISTER_TYPE_DRIVER)
        AccountTempHelper.getInstance().recognizeType = LoginActivity.EXTRA_TYPE_RECOGNIZE_COMPARE
        val intent = Intent(mContext, RecognizeIdCardActivity::class.java)
        intent.putExtras(bundle)
        //身份证识别比对
        startActivityForResult(intent, OnlineTrainFragment.REQUEST_CODE_FACE_COMPARE)
    }


    /*  private fun test(){
          if (clickTrainingEntity == null) {
              ToastUtil.show("未获取到相关数据")
              return@OnItemClickListener
          }
          if (!AccountHelper.getInstance().isLogin) {
              ToastUtil.show("请先登录")
              return@OnItemClickListener
          }


      }*/

    /**
     * 跳转到两类人员专项列表
     */
    private fun skipTwoTypeActivity(){
        val intent = Intent(context, ProfessionalTrainTwoTypeActivity::class.java)
        intent.putExtra("id", clickTrainingEntity!!.id)
        intent.putExtra("title", clickTrainingEntity!!.name)
        startActivity(intent)
    }
}
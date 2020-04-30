package com.tourcoo.training.ui.training.safe.online.web

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dyhdyh.support.countdowntimer.CountDownTimerSupport
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant
import com.tourcoo.training.constant.TrainingConstant.EXTRA_COURSE_INFO
import com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.*
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.training.Catalog
import com.tourcoo.training.entity.training.Course
import com.tourcoo.training.entity.training.TrainingPlanDetail
import com.tourcoo.training.ui.face.OnLineFaceRecognitionActivity
import com.tourcoo.training.ui.training.safe.online.PlayVideoActivity
import com.tourcoo.training.ui.training.safe.online.TencentPlayVideoActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_play_video_tencent.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月27日17:30
 * @Email: 971613168@qq.com
 */
class HtmlBrowserActivity : BaseTitleActivity(), View.OnClickListener {
    private val mTag = "HtmlBrowserActivity"

    private var mCourseList: MutableList<Course>? = null

    private var mCourseHashMap: HashMap<Course, View>? = null

    private var trainingPlanID = ""
    private var trainingPlanDetail: TrainingPlanDetail? = null

    //人脸验证间隔时间
    private var faceVerifyInterval = Int.MAX_VALUE

    /**
     * 判断考试是否完成
     */
    private var hasRequireExam = true
    private var mTimerTask: CountDownTimerSupport? = null

    /**
     * 章的数量
     */
    private var countCatalog = 0

    /**
     * 节的数量
     */
    private var countNode = 0

    override fun getContentLayout(): Int {
        return R.layout.activity_browser_html
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("在线学习")

    }

    override fun initView(savedInstanceState: Bundle?) {
        trainingPlanID = intent.getStringExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID)
        if (TextUtils.isEmpty(trainingPlanID)) {
            ToastUtil.show("未获取到计划信息")
            finish()
            return
        }
        tvExam.isEnabled = false
        TourCooLogUtil.i(mTag, "trainingPlanID=$trainingPlanID")
        tvExam.setOnClickListener(this)
        mCourseList = ArrayList()
        mCourseHashMap = HashMap()
        //增加封面
//        val imageView = ImageView(this)
//        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//        imageView.setImageResource(R.drawable.img_training_free_video)

        /*tvTest.setOnClickListener {
            if (mTitleBar.visibility != View.GONE) {
                mTitleBar.visibility = View.GONE
            } else {
                mTitleBar.visibility = View.VISIBLE
            }
        }*/

        requestPlanDetail()
    }

    override fun onClick(v: View?) {
    }


    private fun requestPlanDetail() {
        ApiRepository.getInstance().trainingPlanID(trainingPlanID).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<TrainingPlanDetail>>() {
            override fun onSuccessNext(entity: BaseResult<TrainingPlanDetail>?) {
                if (entity!!.code == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    trainingPlanDetail = entity.data
                    hasRequireExam = entity.data.finishedCourses == 1
                    handleTrainingPlanDetail(entity.data)
                }
            }
        })
    }


    private fun handleTrainingPlanDetail(detail: TrainingPlanDetail?) {
        if (detail == null || detail.subjects == null) {
            return
        }
        //拿到后台配置的间隔时间
        faceVerifyInterval = detail.faceVerifyInterval
        //不需要初始化计时器 因此屏蔽
//        initTimerAndStart()
        if(detail.requireExam == 1){
            tvExam.visibility = View.VISIBLE
        }

        if (detail.finishedCourses == 1 && detail.finishedExam == 0) {
            tvExam.setBackgroundColor(ResourceUtil.getColor(R.color.blue5087FF))
            tvExam.isEnabled = true
        } else {
            tvExam.setBackgroundColor(ResourceUtil.getColor(R.color.grayCCCCCC))
            tvExam.isEnabled = false
        }
        mCourseHashMap!!.clear()
        mCourseList!!.clear()
        llPlanContentView.removeAllViews()
        tvTitle.text = getNotNullValue(detail.title)
        val subjects = detail.subjects
        for (subject in subjects) {
            //如果标题名称不为空则添加标题
            if (!TextUtils.isEmpty(subject.name)) {
                val subjectView = LayoutInflater.from(mContext).inflate(R.layout.item_training_plan_detail_header, null)
                val tvSubjectTitle = subjectView.findViewById<TextView>(R.id.tvSubjectTitle)
                tvSubjectTitle.text = getNotNullValue(subject.name)
                //一级标题
                llPlanContentView.addView(subjectView)
            }
            loadCatalogs(subject.catalogs)
        }
        parseTrainingStatus(mCourseList)
        for (entry in mCourseHashMap!!.entries) {
            loadCourseStatusAndClick(entry.value, entry.key)
        }
        tvCourseCountInfo.text = "共" + countCatalog + "章" + countNode + "小节"
        tvCourseTime.text = "课时：" + detail.courseTime.toString()
        tvSubjectDesc.text = getNotNullValue(detail.description)
    }


    /**
     * 初始化计时器 并开始计时
     */
    private fun initTimerAndStart() {
        //总时长 间隔时间
        if (faceVerifyInterval <= 0) {
            //取消计时器
            cancelTimer()
            return
        }
        //初始化计时器
//        faceVerifyInterval = 12
        mTimerTask = CountDownTimerSupport(faceVerifyInterval.toLong() * 1000, 1000L)
        mTimerTask!!.setOnCountDownTimerListener(object : OnCountDownTimerListener {
            override fun onFinish() {
                ToastUtil.show("时间到")
                //时间到 开始下一个计时
                startTimer()
                //todo 处理认证逻辑
                skipRecognize()
            }

            override fun onTick(millisUntilFinished: Long) {

            }

        })
        startTimer()
    }

    private fun cancelTimer() {
        if (mTimerTask != null) {
            mTimerTask!!.stop()
        }
    }


    private fun startTimer() {
        if (mTimerTask != null) {
            //先重置 在启动
            mTimerTask!!.reset()
            mTimerTask!!.start()
        }
    }

    private fun timerPause() {
        if (mTimerTask != null) {
            //暂停
            mTimerTask!!.pause()
        }
    }

    private fun timerResume() {
        if (mTimerTask != null) {
            //恢复
            mTimerTask!!.resume()
        }
    }


    private fun getNotNullValue(value: String?): String {
        if (TextUtils.isEmpty(value)) {
            return "暂无"
        }
        return value!!
    }


    private fun loadCatalogs(catalogs: MutableList<Catalog>?) {
        if (catalogs.isNullOrEmpty()) {
            return
        }
        val newCatalogs: MutableList<Catalog> = reverseList(catalogs)
        if (newCatalogs.isEmpty()) {
            return
        }
        for (index in newCatalogs.size - 1 downTo 0) {
            val catalog = newCatalogs[index]
            if (!TextUtils.isEmpty(catalog.name)) {
                if (catalog.level == 1) {
                    countCatalog++
                }
                if (catalog.level == 2) {
                    countNode++
                }
                //标题不为空时添加TextView
                val contentView = LayoutInflater.from(mContext).inflate(R.layout.item_training_plan_detail_content, null)
                val tvPlanTitle = contentView.findViewById<TextView>(R.id.tvPlanTitle)
                tvPlanTitle.text = getNotNullValue(catalog.name)
                llPlanContentView.addView(contentView)
                contentView.setPadding(SizeUtil.dp2px(catalog.level * 10f), 0, 0, 0)
            }
            if (catalog.catalogs != null) {
                newCatalogs.remove(catalog)
                catalogs.remove(catalog)
                loadCatalogs(catalog.catalogs)
            } else {
                addCourse(catalog.courses)
            }
        }

    }


    private fun reverseList(list: MutableList<Catalog>?): MutableList<Catalog> {
        val result: MutableList<Catalog> = ArrayList()
        if (list == null) {
            return result
        }
        for (index in list.size - 1 downTo 0) {
            result.add(list[index])
        }
        return result
    }


    private fun parseTrainingStatus(list: MutableList<Course>?) {
        if (list == null) {
            return
        }
        var index = -1
        for (i in 0 until list.size) {
            val course = list[i]
            if (course.completed <= 0 && index == -1) {
                //该课件没有播放过,当前正在播放的视频
                course.currentPlayStatus = TencentPlayVideoActivity.COURSE_STATUS_PLAYING
                index = i
            } else if (course.completed <= 0 && index != -1) {
                course.currentPlayStatus = TencentPlayVideoActivity.COURSE_STATUS_NO_COMPLETE
            } else {
                course.currentPlayStatus = TencentPlayVideoActivity.COURSE_STATUS_COMPLETE
            }
        }
    }


    /**
     * 定位上次播放位置 并加载点击事件
     */
    private fun loadCourseStatusAndClick(view: View, course: Course?) {
        if (course == null) {
            return
        }
        //先禁止点击
        view.isEnabled = false
        val tvPlanDesc = view.findViewById<TextView>(R.id.tvPlanDesc)
        val imageView = view.findViewById<ImageView>(R.id.ivCourseStatus)
        val tvPlanTitle = view.findViewById<TextView>(R.id.tvPlanTitle)
        when (course.currentPlayStatus) {
            TencentPlayVideoActivity.COURSE_STATUS_NO_COMPLETE -> {
                imageView.setImageResource(R.mipmap.ic_play_no_complete)
                setViewGone(tvPlanDesc, false)
            }
            TencentPlayVideoActivity.COURSE_STATUS_COMPLETE -> {
                imageView.setImageResource(R.mipmap.ic_play_finish)
                setViewGone(tvPlanDesc, false)
            }
            TencentPlayVideoActivity.COURSE_STATUS_PLAYING -> {
                imageView.setImageResource(R.mipmap.ic_eyes)
                tvPlanTitle.setTextColor(ResourceUtil.getColor(R.color.blue5087FF))
                setViewGone(tvPlanDesc, true)
                view.setBackgroundColor(ResourceUtil.getColor(R.color.blueEFF3FF))
                //只有当前正在浏览的课件才允许点击
                setCourseInfoClick(view, course)
            }
            else -> {
            }
        }

    }


    /**
     * 跳转浏览网页课件
     */
    private fun loadWebAndSkipBrowser(course: Course?) {
        if (course == null || course.streams == null && course.streams.size == 0) {
            return
        }
        when (course.mediaType) {
            //视频
            0 -> {
                //todo 模拟跳转
                skipWebView(course)
            }
            else -> {
                //外链URL
                //跳转web链接
                skipWebView(course)
            }
        }
    }


    private fun skipRecognize() {
        //暂停视频
        smartVideoPlayer.onPause()
        //暂停计时器
        timerPause()
        //跳转到人脸认证
        skipFace()
    }

    private fun skipFace() {
        //人脸认证
        val intent = Intent(this, OnLineFaceRecognitionActivity::class.java)
        intent.putExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID, CommonUtil.getNotNullValue(trainingPlanID))
        startActivityForResult(intent, PlayVideoActivity.REQUEST_CODE_FACE)
    }


    private fun addCourse(courses: MutableList<Course>?) {
        if (courses == null) {
            return
        }
        for (course in courses) {
            TourCooLogUtil.i(mTag, "已执行")
            val contentView = LayoutInflater.from(mContext).inflate(R.layout.item_training_plan_detail_content, null)
            val tvPlanTitle = contentView.findViewById<TextView>(R.id.tvPlanTitle)
            tvPlanTitle.text = getNotNullValue(course.name)
            llPlanContentView.addView(contentView)
            contentView.setPadding(SizeUtil.dp2px(course.level * 10f), 0, 0, 0)
            if (course.streams != null) {
                val tvPlanDesc = contentView.findViewById<TextView>(R.id.tvPlanDesc)
                //播放状态
                val ivCourseStatus = contentView.findViewById<ImageView>(R.id.ivCourseStatus)
                val endTime = TimeUtil.secondToDate(course.progress.toLong(), "mm:ss")
                tvPlanDesc.text = getNotNullValue("00:00学习到$endTime")
                tvPlanDesc.textSize = 12f
                setViewGone(tvPlanDesc, true)
                setViewGone(ivCourseStatus, true)
                //关键
                mCourseHashMap!!.put(course, contentView)
                mCourseList!!.add(course)
            }

        }
    }

    /**
     * 设置课程
     */
    private fun setCourseInfoClick(view: View, course: Course) {
        //先解禁点击事件
        view.isEnabled = true
        view.setOnClickListener {
            loadWebAndSkipBrowser(course)
        }
    }

    private fun skipWebView(course: Course) {
        //缓存当前课件
        WebCourseTempHelper.getInstance().course = course
        val intent = Intent(this, TrainingWebViewActivity::class.java)
        val bundle = Bundle()
        var url = course.mediaUrl
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
//        if (isSingle) CommonUtil.ACTIVITY_SINGLE_FLAG else
        //
        url = "https://www.baidu.com"
        intent.putExtra("url", CommonUtil.getNotNullValue(url))
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, CommonUtil.getNotNullValue(trainingPlanID))
//        bundle.putParcelable(EXTRA_COURSE_INFO, course)
        startActivityForResult(intent, 1001, bundle)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //刷新课件
            requestPlanDetail()
        }
    }
}
package com.tourcoo.training.ui.training.safe.online

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener
import com.tencent.liteav.demo.play.SuperPlayerConst
import com.tencent.liteav.demo.play.SuperPlayerGlobalConfig
import com.tencent.liteav.demo.play.SuperPlayerModel
import com.tencent.liteav.demo.play.SuperPlayerView
import com.tencent.liteav.demo.play.v3.SuperPlayerVideoId
import com.tencent.rtmp.TXLiveConstants
import com.tourcoo.training.R
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.ExamConstant
import com.tourcoo.training.constant.ExamConstant.RESULT_CODE_REFRESH_EXAM_ID
import com.tourcoo.training.constant.FaceConstant.FACE_CERTIFY_FAILED
import com.tourcoo.training.constant.FaceConstant.FACE_CERTIFY_SUCCESS
import com.tourcoo.training.constant.TrainingConstant.*
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.manager.GlideManager
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.*
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.entity.account.UserInfo
import com.tourcoo.training.entity.medal.MedalDictionary
import com.tourcoo.training.entity.training.Catalog
import com.tourcoo.training.entity.training.Course
import com.tourcoo.training.entity.training.DRMParams
import com.tourcoo.training.entity.training.TrainingPlanDetail
import com.tourcoo.training.event.WorkProRefreshEvent
import com.tourcoo.training.ui.exam.ExamActivity
import com.tourcoo.training.ui.exam.ExamActivity.Companion.EXTRA_EXAM_ID
import com.tourcoo.training.ui.face.OnLineFaceRecognitionActivity
import com.tourcoo.training.ui.training.StudyMedalRecordActivity
import com.tourcoo.training.ui.training.safe.online.web.PlayHtmlWebActivity
import com.tourcoo.training.ui.training.safe.online.web.WebCourseTempHelper
import com.tourcoo.training.utils.CustomCountDownTimer
import com.tourcoo.training.utils.TourCooUtil
import com.tourcoo.training.widget.aliplayer.utils.ScreenUtils
import com.tourcoo.training.widget.dialog.IosAlertDialog
import com.tourcoo.training.widget.dialog.exam.ExamCommonDialog
import com.tourcoo.training.widget.dialog.medal.MedalDialog
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_news_detail_video.*
import kotlinx.android.synthetic.main.activity_play_video_tencent.*
import org.greenrobot.eventbus.EventBus
import java.net.URLEncoder

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月19日23:27
 * @Email: 971613168@qq.com
 */
class TencentPlayVideoActivity : BaseTitleActivity(), View.OnClickListener {
    private val mTag = "腾讯播放器"
    private var isTransition = false

    private var currentCourseId: String = ""

    private var mCourseList: MutableList<Course>? = null

    private var mCourseHashMap: HashMap<Course, View>? = null

    private var trainingPlanID = ""
    private var trainingPlanDetail: TrainingPlanDetail? = null

    //人脸验证间隔时间
    private var faceVerifyInterval = Int.MAX_VALUE

    private var mRemainTime = Int.MAX_VALUE

    private var mCurrentCourse: Course? = null

    /**
     * 判断考试是否完成
     */
    private var hasRequireExam = true
    private var mTimerTask: CustomCountDownTimer? = null

    /**
     * 章的数量
     */
    private var countCatalog = 0

    /**
     * 节的数量
     */
    private var countNode = 0

    /**
     * 是否是考试状态
     */
    private var mExamEnable = false


    private var mExamId = ""

    /**
     * 是否是第一次显示考试弹窗
     */
    private var isFirstShow = true


    companion object {
        const val TRANSITION = "TRANSITION"
        const val REQUEST_CODE_FACE = 1006
        const val REQUEST_CODE_WEB = 1001

    }


    override fun getContentLayout(): Int {
        return R.layout.activity_play_video_tencent
    }


    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("线上学习")
        titleBar?.setOnLeftTextClickListener {
            onBackPressed()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        //保持常亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isTransition = intent.getBooleanExtra(TRANSITION, false)
        trainingPlanID = intent.getStringExtra(EXTRA_TRAINING_PLAN_ID)
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
        requestPlanDetail()
    }

    private fun loadPlayerSetting(currentProgress: Int) {
        //进度条拖动开关
        //调试模式下允许拖动进度条
        TourCooLogUtil.d(mTag, "loadPlayerSetting()：设置的进度" + currentProgress)
        smartVideoPlayer.setSeekEnable(AppConfig.DEBUG_MODE)
//        smartVideoPlayer.seekTo(currentProgress)
        smartVideoPlayer.setOnPlayStatusListener(object : SuperPlayerView.OnPlayStatusListener {
            override fun resumeVideo() {
                timerResume()
            }

            override fun pauseVideo() {
                timerPause()
            }

            override fun onAutoPlayStart() {
                ivCoverView.visibility = View.GONE
                smartVideoPlayer.mVodControllerSmall.enableClick(true)

            }

            override fun enableSeek() {
                TourCooLogUtil.d(mTag, "设置了进度：" + currentProgress)
                smartVideoPlayer.seekTo(currentProgress)
                //只有播放状态下才初始化计时器
                initTimerAndStart(mRemainTime)
            }

            override fun onAutoPlayComplete() {
                //通知后台当前课程播放结束
                if (smartVideoPlayer.playMode != SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                    ivCoverView.visibility = View.VISIBLE
                } else {
                    ivCoverView.visibility = View.GONE
                }
                requestCompleteCurrentCourse(currentCourseId)
            }

        })

        // 播放器配置
        val prefs = SuperPlayerGlobalConfig.getInstance()
        // 开启悬浮窗播放
        prefs.enableFloatWindow = false
        // 设置悬浮窗的初始位置和宽高
        // 播放器默认缓存个数
        prefs.maxCacheItem = 5
        // 设置播放器渲染模式
        prefs.enableHWAcceleration = true;
        prefs.renderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;

        smartVideoPlayer.setPlayerViewCallback(object : SuperPlayerView.OnSuperPlayerViewCallback {
            override fun onStartFullScreenPlay() {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

                if (!isStrangePhone) {
                    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    smartVideoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                }
                //设置view的布局，宽高
                val aliVcVideoViewLayoutParams = smartVideoPlayer
                        .getLayoutParams() as FrameLayout.LayoutParams
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT


                if (mTitleBar.visibility != View.GONE) {
                    mTitleBar.visibility = View.GONE
                }
            }

            override fun onStopFullScreenPlay() {
                baseHandler.postDelayed({
                    window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                    smartVideoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE)
                    //设置view的布局，宽高之类
                    val aliVcVideoViewLayoutParams = smartVideoPlayer
                            .getLayoutParams() as FrameLayout.LayoutParams
                    aliVcVideoViewLayoutParams.height = (ScreenUtils.getWidth(this@TencentPlayVideoActivity) * 9.0f / 16).toInt()
                    aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    if (mTitleBar.visibility != View.VISIBLE) {
                        mTitleBar.visibility = View.VISIBLE
                    }
                }, 200)

                smartVideoPlayer.seekTo(smartVideoPlayer.currentProgress)


            }

            override fun onClickFloatCloseBtn() {
            }

            override fun onClickSmallReturnBtn() {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                setStatusBarDarkMode(mContext, isStatusBarDarkMode)
                //设置返回按键功能
                onBackPressed()
            }

            override fun onStartFloatWindowPlay() {
            }

        })

    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (smartVideoPlayer.playMode != SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            setStatusBarDarkMode(mContext, isStatusBarDarkMode)
        } else {
            mTitleBar.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        if (smartVideoPlayer != null) {
            // 停止播放
            if (smartVideoPlayer.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
                smartVideoPlayer.onPause()
            }
        }
        timerPause()
    }


    override fun onResume() {
        super.onResume()
        if (smartVideoPlayer != null) {
            // 重新开始播放
            if (smartVideoPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAY) {
                smartVideoPlayer.onResume()
                timerResume()
                if (smartVideoPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FLOAT) {
                    smartVideoPlayer.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW)
                }
            }
        }
    }


    override fun onDestroy() {
        cancelTimer()
        //清除常亮设置
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (smartVideoPlayer != null) {
            if (smartVideoPlayer.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
                smartVideoPlayer.resetPlayer()
            }
            // 释放
            smartVideoPlayer.release()
        }
        super.onDestroy()

    }


    override fun isStatusBarDarkMode(): Boolean {
        return true
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
        //请求数据后计时器计时器要先停止
        cancelTimer()

        if (hasRequireExam) {
            cancelTimer()
        }
        if (detail == null || detail.subjects == null) {
            return
        }
        mExamId = detail.latestExamID.toString()
        clearCount()
        //拿到后台配置的间隔时间
        faceVerifyInterval = if (AppConfig.DEBUG_MODE) {
            //todo 人脸验证间隔时间 调试模式下 固定成120秒 方便测试
            120
        } else {
            detail.faceVerifyInterval
        }
        if (detail.requireExam == 1) {
            tvExam.visibility = View.VISIBLE
        }
        mExamEnable = detail.finishedCourses == 1 && detail.finishedExam == 0
        if (mExamEnable) {
            //如果允许考试则将考试按钮置为蓝色 并允许点击 否则置灰
            tvExam.setBackgroundColor(ResourceUtil.getColor(R.color.blue5087FF))
            tvExam.isEnabled = true

            //先判断是否需要显示勋章
            requestMedalDictionary()

        } else {
            //置灰
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
        if (mRemainTime == Int.MAX_VALUE) {
            mRemainTime = faceVerifyInterval
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
            if (catalog.level == 1) {
                countCatalog++
            }
            if (catalog.level == 2) {
                countNode++
            }
            if (!TextUtils.isEmpty(catalog.name)) {
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

    private fun addCourse(courses: MutableList<Course>?) {
        if (courses == null) {
            return
        }
        for (course in courses) {
            val contentView = LayoutInflater.from(mContext).inflate(R.layout.item_training_plan_detail_content, null)
            val tvPlanTitle = contentView.findViewById<TextView>(R.id.tvPlanTitle)
            tvPlanTitle.text = getNotNullValue(course.name)
            llPlanContentView.addView(contentView)
            contentView.setPadding(SizeUtil.dp2px(course.level * 10f), 0, 0, 0)
            //不需要判断course.streams是否为空了
//            if (course.streams != null) {
            val tvPlanDesc = contentView.findViewById<TextView>(R.id.tvPlanDesc)
            //播放状态
            val ivCourseStatus = contentView.findViewById<ImageView>(R.id.ivCourseStatus)
            val endTime = TimeUtil.getTime(course.progress.toLong())
            val tips = TimeUtil.getTime(course.duration) + "  学习到 " + endTime
            tvPlanDesc.text = tips
            tvPlanDesc.textSize = 12f
            setViewGone(tvPlanDesc, true)
            setViewGone(ivCourseStatus, true)
            //关键
            mCourseHashMap!!.put(course, contentView)
            mCourseList!!.add(course)
//            }

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

    /**
     * 定位上次播放位置并设置点击事件
     */
    private fun loadCourseStatusAndClick(view: View, course: Course?) {
        if (course == null) {
            return
        }
        val tvPlanDesc = view.findViewById<TextView>(R.id.tvPlanDesc)
        val imageView = view.findViewById<ImageView>(R.id.ivCourseStatus)
        val tvPlanTitle = view.findViewById<TextView>(R.id.tvPlanTitle)

        GlideManager.loadGrayImg(CommonUtil.getNotNullValue(course.coverURL), ivCoverView, R.drawable.ic_rect_default)

        when (course.currentPlayStatus) {
            COURSE_PLAY_STATUS_NO_COMPLETE -> {
                //锁定状态 下的提示文字需要修改成 时长+未解锁
                imageView.setImageResource(R.mipmap.ic_play_no_complete)
                //显示未解锁提示
                showUnlockTips(course, tvPlanDesc, tvPlanTitle)
            }
            COURSE_PLAY_STATUS_COMPLETE -> {
                //已完成 下的提示文字需要修改成 时长+已听完
                imageView.setImageResource(R.mipmap.ic_play_no_complete)
                imageView.setImageResource(R.mipmap.ic_play_finish)
                //显示已听完
                showPlayFinishTips(course, tvPlanDesc, tvPlanTitle)
            }
            COURSE_PLAY_STATUS_PLAYING -> {
                if (course.mediaType == MEDIA_TYPE_HTML) {
                    imageView.setImageResource(R.mipmap.ic_eyes)
                } else {
                    imageView.setImageResource(R.mipmap.ic_playing)
                }

                tvPlanTitle.setTextColor(ResourceUtil.getColor(R.color.blue5087FF))
                setViewGone(tvPlanDesc, true)
                view.setBackgroundColor(ResourceUtil.getColor(R.color.blueEFF3FF))

                smartVideoPlayer.mVodControllerSmall.enableClick(false)

                //只有当前正在浏览的课件 并且是html课件才允许点击
                if (course.mediaType == MEDIA_TYPE_HTML) {
                    ToastUtil.show("当前是网页课件,需要手动点击学习")
                    setCourseInfoClick(view, course)
                    TourCooLogUtil.i(mTag, "执行了MEDIA_TYPE_HTML")
                } else {
                    TourCooLogUtil.d(mTag, "执行了MEDIA_TYPE_VIDEO")
                    playStreamUrlOrHtml(course)
                }
            }
            else -> {
            }
        }

    }

    private fun parseTrainingStatus(list: MutableList<Course>?) {
        if (list == null) {
            return
        }
        var index = -1
        for (i in 0 until list.size) {
            val course = list[i]
            if (course.completed <= 0 && index == -1) {
                //该视频没有播放过,当前正在播放的视频
                course.currentPlayStatus = COURSE_PLAY_STATUS_PLAYING
                index = i
            } else if (course.completed <= 0 && index != -1) {
                course.currentPlayStatus = COURSE_PLAY_STATUS_NO_COMPLETE
            } else {
                course.currentPlayStatus = COURSE_PLAY_STATUS_COMPLETE
            }
        }
    }


    private fun playStreamUrlOrHtml(course: Course?) {
        if (course == null) {
            ToastUtil.show("未获取到课件")
            return
        }
        when (course.mediaType) {
            //视频
            MEDIA_TYPE_VIDEO -> {
                if (course.streams == null || course.streams.size == 0) {
                    ToastUtil.show("当前课件不是视频课件")
                    return
                }
                if (mExamEnable) {
                    //如果是考试状态 就不用播放视频了 因此直接拦截播放操作
                    return
                }
                //从上次播放进度开始播放
                currentCourseId = "" + course.id
                loadPlayerSetting(course.progress)
                if (course.streams[0].encryptType != null && course.streams[0].encryptType == "DriveDu-DRM") {
                    requestVideoEncryptParamsCurrentCourse(course)
                } else {
                    val superPlayerModel = SuperPlayerModel()
                    superPlayerModel.multiURLs = ArrayList()
                    superPlayerModel.title = course.name
                    course.streams.forEach {
                        superPlayerModel.multiURLs.add(SuperPlayerModel.SuperPlayerURL(it.url, TourCooUtil.getDefinitionName(it.definition)))
                    }
                    superPlayerModel.playDefaultIndex = 0

                    smartVideoPlayer.playWithModel(superPlayerModel)
                }

            }
            MEDIA_TYPE_HTML -> {
                //因为网页课件需要主动点击触发 因此这里不做任何处理了
            }
        }


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvExam -> {
                skipExamActivity(trainingPlanDetail)
            }
            else -> {
            }
        }
    }


    private fun skipExamActivity(trainingPlanDetail: TrainingPlanDetail?) {
        if (trainingPlanDetail == null) {
            ToastUtil.show("未获取到考试信息")
            return
        }
        val intent = Intent(mContext, ExamActivity::class.java)
        //培训计划id
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, trainingPlanID)
        //考试题id
        intent.putExtra(EXTRA_EXAM_ID, trainingPlanDetail.latestExamID.toString())
        startActivityForResult(intent, ExamConstant.EXTRA_CODE_REQUEST_EXAM)
    }

    /**
     * 当前课程播放完毕
     */
    private fun requestCompleteCurrentCourse(courseId: String) {
        ApiRepository.getInstance().requestSaveProgress(trainingPlanID, courseId, "-1").compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>?>() {
            override fun onSuccessNext(entity: BaseResult<Any>?) {
                if (entity?.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    requestPlanDetail()
                }
            }
        })
    }

    /**
     * 获取腾讯drm解密参数
     */
    private fun requestVideoEncryptParamsCurrentCourse(course: Course) {
        ApiRepository.getInstance().requestVideoEncryptParamsCurrentCourse(trainingPlanID, course.videoID).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<DRMParams>?>() {
            override fun onSuccessNext(entity: BaseResult<DRMParams>?) {
                if (entity?.code == RequestConfig.CODE_REQUEST_SUCCESS) {

                    val model = SuperPlayerModel()
                    model.appId = entity.data.appid.toInt()
                    model.title = course.name
                    model.token = URLEncoder.encode(replaceBlank(entity.data.token), "UTF-8")

                    model.videoId = SuperPlayerVideoId()
                    model.videoId.fileId = course.videoID
                    model.videoId.version = SuperPlayerVideoId.FILE_ID_V3
                    model.videoId.playDefinition = entity.data.playdefinition
                    model.videoId.sign = entity.data.sign
                    model.videoId.us = entity.data.us
                    model.videoId.timeout = entity.data.t
                    smartVideoPlayer.playWithModel(model)
                } else {
                    if (entity != null)
                        ToastUtil.showFailed(entity.msg)
                }
            }

        })
    }

    private fun replaceBlank(str: String): String {
        return str.replace("\\\\".toRegex(), "")
    }

    /**
     * 保存当前观看进度
     */
    private fun requestSaveProgress(courseId: String, second: String) {
        ApiRepository.getInstance().requestSaveProgress(trainingPlanID, courseId, second).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>?>() {
            override fun onSuccessNext(entity: BaseResult<Any>?) {
                if (entity?.code == RequestConfig.CODE_REQUEST_SUCCESS) {

                    if (AppConfig.DEBUG_MODE) {
                        ToastUtil.showSuccess("进度保存成功")
                    }

                } else {
                    ToastUtil.show(entity?.msg)
                }
                finish()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                finish()
            }
        })
    }

    private fun showExit() {
        if (smartVideoPlayer.currentProgress <= 0) {
            finish()
            return
        }
        IosAlertDialog(mContext)
                .init()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setTitle("确认退出学习")
                .setMsg("退出前会保存当前学习进度")
                .setPositiveButton("确认退出", View.OnClickListener {
                    doSaveProgressAndFinish()
                })
                .setNegativeButton("取消", View.OnClickListener {
                    window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                    setStatusBarDarkMode(mContext, isStatusBarDarkMode)
                }).show()
    }


    override fun onBackPressed() {
        if (smartVideoPlayer.playMode == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            smartVideoPlayer.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW)
            return
        }

        if (hasRequireExam) {
            finish()
        } else {
            showExit()
        }

    }


    override fun finish() {
        super.finish()
        val offLineRefreshEvent = WorkProRefreshEvent()
        offLineRefreshEvent.userInfo = UserInfo()
        EventBus.getDefault().post(offLineRefreshEvent)
    }


    private fun doSkipRecognize() {
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
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, CommonUtil.getNotNullValue(trainingPlanID))
        startActivityForResult(intent, REQUEST_CODE_FACE)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_FACE -> {
                when (resultCode) {
                    FACE_CERTIFY_SUCCESS -> {
                        //本次人脸验证通过 需要继续播放课件
                        //处理继续播放课件逻辑
                        doPlayVideoContinue()
                    }


                    FACE_CERTIFY_FAILED -> {
                        //本次人脸验证失败 需要提示用户后 关闭页面并保存进度 不让用户学习了
                        handleRecognizeFailedCallback()
                    }
                    else -> {
                        //用户压根就没验证人脸 直接返回的 肯定关闭页面并保存进度 更不让用户学习了
                        handleRecognizeCancelCallback()
                    }
                }

            }
            REQUEST_CODE_WEB -> {
                if (resultCode == Activity.RESULT_OK) {
                    //刷新课件
                    requestPlanDetail()
                }
            }
            ExamConstant.EXTRA_CODE_REQUEST_EXAM -> {
                if (resultCode == Activity.RESULT_OK) {
                    finish()
                } else if (resultCode == RESULT_CODE_REFRESH_EXAM_ID) {
                    requestPlanDetail()
                }
            }
        }
    }

    /**
     * 初始化计时器 并开始计时
     */
    private fun initTimerAndStart(faceVerifySecond: Int) {

        cancelTimer()
        if (faceVerifySecond <= 0) {
            return
        }
        //初始化计时器
//        faceVerifyInterval = 12
        val faceVerifyMillisecond = faceVerifySecond * 1000.toLong()
        mTimerTask = CustomCountDownTimer(faceVerifyMillisecond, 1000L)

        mTimerTask!!.setOnCountDownTimerListener(object : OnCountDownTimerListener {
            override fun onFinish() {
                //时间到 进行人脸验证
                //todo 处理认证逻辑
                TourCooLogUtil.i("计时完成")

                doSkipRecognize()
            }

            override fun onTick(millisUntilFinished: Long) {
                TourCooLogUtil.i("人脸计时器计时中：还剩：" + mRemainTime + "秒")
                mRemainTime--
            }

        })
        startTimer()
    }


    private fun cancelTimer() {
        if (mTimerTask != null) {
            mTimerTask!!.stop()
            mTimerTask = null
            TourCooLogUtil.w(mTag, "计时器销毁")
        }
    }


    private fun startTimer() {
        if (mTimerTask != null) {
            //先重置 在启动
            mTimerTask!!.reset()
            TourCooLogUtil.i(mTag, "计时器启动")
            mTimerTask!!.start()
        }
    }

    private fun timerPause() {
        if (mTimerTask != null) {
            mTimerTask!!.pause()
            //暂停
            TourCooLogUtil.d(mTag, "计时器暂停")
        }
    }

    private fun timerResume() {
        if (mTimerTask != null) {
            //恢复
            TourCooLogUtil.d(mTag, "计时器恢复")
            mTimerTask!!.resume()
        }
    }

    /**
     * 保存进度并关闭当前页面
     */
    private fun doSaveProgressAndFinish() {
        val progress = smartVideoPlayer.currentProgress
        if (progress == 0) {
            //说明没有播放 直接finish 不保存进度
            finish()
            return
        }
        requestSaveProgress(currentCourseId, progress.toString())
    }


    /**
     * 人脸验证用户未通过的回调
     */
    private fun handleRecognizeFailedCallback() {
        smartVideoPlayer?.onPause()
        ToastUtil.show("您当前人脸验证未通过 本次学习结束")
        baseHandler.postDelayed(Runnable {
            //保存进度并关闭
            doSaveProgressAndFinish()
        }, 1000)
    }


    /**
     * 人脸验证用户未点击拍照的回调
     */
    private fun handleRecognizeCancelCallback() {
        smartVideoPlayer?.onPause()
        ToastUtil.show("您当前没有验证人脸 本次学习结束")
        baseHandler.postDelayed(Runnable {
            //保存进度并关闭
            doSaveProgressAndFinish()
        }, 1000)
    }

    /**
     * 设置课程点击事件
     */
    private fun setCourseInfoClick(view: View, course: Course) {
        //先解禁点击事件
        view.isEnabled = true
        view.setOnClickListener {
            loadWebAndSkipBrowser(course)
        }
    }


    /**
     * 显示参加考试对话框
     */
    private fun showAcceptExamDialog() {
        if (!isFirstShow) {
            //如果之前显示过则 不在弹出
            return
        }
        baseHandler.postDelayed({
            val dialog = ExamCommonDialog(mContext)
            dialog.create().setContent("学习完成是否参加考试？").setPositiveButtonListener(View.OnClickListener {
                dialog.dismiss()
                //跳转考试
                skipExamActivity(trainingPlanDetail)
            }).show()
            isFirstShow = false
        }, 500)
    }

    /**
     * 跳转浏览网页课件
     */
    private fun loadWebAndSkipBrowser(course: Course?) {
        if (course == null) {
            return
        }
        when (course.mediaType) {
            //视频
            MEDIA_TYPE_VIDEO -> {
                ToastUtil.show("当前课程不是网页类型")
            }
            MEDIA_TYPE_HTML -> {
                //外链URL
                //跳转web链接
                skipWebView(course)
            }
            else -> {
                ToastUtil.show("未找到对应课程类型")
            }
        }
    }

    private fun skipWebView(course: Course) {
        //缓存当前课件
        WebCourseTempHelper.getInstance().course = course
        val intent = Intent(this, PlayHtmlWebActivity::class.java)
        val bundle = Bundle()
        if (course.html != null) {
            intent.putExtra(EXTRA_COURSE_INFO, course)
            intent.putExtra("url", CommonUtil.getNotNullValue(course.html.url))
        }
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, trainingPlanID)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, CommonUtil.getNotNullValue(trainingPlanID))
        startActivityForResult(intent, REQUEST_CODE_WEB, bundle)
    }

    private fun clearCount() {
        countNode = 0
        countCatalog = 0
    }

    /**
     * 获取勋章领取条件接口
     */
    private fun requestMedalDictionary() {
        ApiRepository.getInstance().requestMedalDictionary().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MedalDictionary>>() {
            override fun onSuccessNext(entity: BaseResult<MedalDictionary>?) {
                if (entity == null) {
                    return
                }
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    if (TextUtils.isEmpty(entity.data.hour)) {
                        entity.data.hour = "0"
                    }
                    //显示勋章
                    showMedalDialog(entity.data.hour.toInt())
                }
            }
        })
    }

    private fun showMedalDialog(number: Int) {
        val dialog = MedalDialog(mContext)
        val isShow = dialog.create()
                .setMedal(1, number, AccountTempHelper.getInstance().studyMedalEntity)
                .setPositiveButtonListener {
                    dialog.dismiss()
                    val intent = Intent(this, StudyMedalRecordActivity::class.java)
                    startActivityForResult(intent, 2017)
                }.show()

        if (!isShow) {
            //延时弹出是否考试弹窗
            showAcceptExamDialog()
        }

    }


    private fun showUnlockTips(course: Course, tvPlanDesc: TextView, tvPlanTitle: TextView) {
        val tips = TimeUtil.getTime(course.duration) + " 未解锁"
        tvPlanDesc.text = tips
        tvPlanTitle.setTextColor(CommonUtil.getColor(R.color.black333333))
        tvPlanDesc.setTextColor(CommonUtil.getColor(R.color.gray999999))
        tvPlanDesc.textSize = 12f
        setViewGone(tvPlanDesc, true)
    }

    private fun showPlayFinishTips(course: Course, tvPlanDesc: TextView, tvPlanTitle: TextView) {
        val tips = TimeUtil.getTime(course.duration) + " 已听完"
        tvPlanDesc.text = tips
        tvPlanDesc.textSize = 12f
        tvPlanDesc.setTextColor(CommonUtil.getColor(R.color.grayAAAAAA))
        tvPlanTitle.setTextColor(CommonUtil.getColor(R.color.gray999999))
        setViewGone(tvPlanDesc, true)
    }

    /**
     * 人脸验证通过后 需要继续播放课件 计时又得开始了
     */
    private fun doPlayVideoContinue() {
        superPlayerView?.onResume()
        //开始新一轮计时
        mRemainTime = faceVerifyInterval
        TourCooLogUtil.i("开始新一轮计时")
        initTimerAndStart(mRemainTime)
        TourCooLogUtil.d("人脸验证通过 继续播放视频")
    }

}
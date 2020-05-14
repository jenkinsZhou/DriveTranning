package com.tourcoo.training.ui.training.safe.online

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.dyhdyh.support.countdowntimer.CountDownTimerSupport
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
import com.tourcoo.training.constant.TrainingConstant.*
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.*
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.medal.MedalDictionary
import com.tourcoo.training.entity.training.Catalog
import com.tourcoo.training.entity.training.Course
import com.tourcoo.training.entity.training.DRMParams
import com.tourcoo.training.entity.training.TrainingPlanDetail
import com.tourcoo.training.ui.exam.ExamActivity
import com.tourcoo.training.ui.exam.ExamActivity.Companion.EXTRA_EXAM_ID
import com.tourcoo.training.ui.face.OnLineFaceRecognitionActivity
import com.tourcoo.training.ui.training.StudyMedalRecordActivity
import com.tourcoo.training.ui.training.safe.online.web.PlayHtmlWebActivity
import com.tourcoo.training.ui.training.safe.online.web.WebCourseTempHelper
import com.tourcoo.training.utils.TourCooUtil
import com.tourcoo.training.widget.dialog.IosAlertDialog
import com.tourcoo.training.widget.dialog.exam.ExamCommonDialog
import com.tourcoo.training.widget.dialog.medal.MedalDialog
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_play_video_tencent.*

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年04月19日23:27
 * @Email: 971613168@qq.com
 */
class TencentPlayVideoActivity : BaseTitleActivity(), View.OnClickListener {
    private val mTag = "PlayVideoActivity"
    private var isTransition = false

    private var currentCourseId: String = ""

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

    /**
     * 是否是考试状态
     */
    private var mExamEnable = false


    companion object {
        const val TRANSITION = "TRANSITION"
        const val REQUEST_CODE_FACE = 1006
        const val REQUEST_CODE_WEB = 1001

    }

    override fun beforeSetContentView() {
        super.beforeSetContentView()

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
        //增加封面
//        val imageView = ImageView(this)
//        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//        imageView.setImageResource(R.drawable.img_training_free_video)

        /*   tvTest.setOnClickListener {
               if (mTitleBar.visibility != View.GONE) {
                   mTitleBar.visibility = View.GONE
               } else {
                   mTitleBar.visibility = View.VISIBLE
               }
           }*/
        requestPlanDetail()
    }

    private fun loadPlayerSetting(currentProgress: Int) {
        //进度条拖动开关
        smartVideoPlayer.setSeekEnable(true)
        smartVideoPlayer.setOnPlayStatusListener(object : SuperPlayerView.OnPlayStatusListener {

            override fun enableSeek() {
                smartVideoPlayer.seekTo(currentProgress)
            }

            override fun onAutoPlayComplete() {
                //通知后台当前课程播放结束
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
                if (mTitleBar.visibility != View.GONE) {
                    mTitleBar.visibility = View.GONE
                }
            }

            override fun onStopFullScreenPlay() {
                window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                if (mTitleBar.visibility != View.VISIBLE) {
                    mTitleBar.visibility = View.VISIBLE
                }
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
                if (smartVideoPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FLOAT) {
                    smartVideoPlayer.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW)
                }
            }
        }
        timerResume()
    }


    override fun onDestroy() {
        cancelTimer()
        //清除常亮设置
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy()
        if (smartVideoPlayer != null) {
            // 释放
            smartVideoPlayer.release()
            if (smartVideoPlayer.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
                smartVideoPlayer.resetPlayer()
            }
        }

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
        if (hasRequireExam) {
            cancelTimer()
        }
        if (detail == null || detail.subjects == null) {
            return
        }
        clearCount()
        //拿到后台配置的间隔时间
        faceVerifyInterval = detail.faceVerifyInterval
        //初始化计时器
        initTimerAndStart()

        if (detail.requireExam == 1) {
            tvExam.visibility = View.VISIBLE
        }
        mExamEnable = detail.finishedCourses == 1 && detail.finishedExam == 0
        if (mExamEnable) {
            //如果允许考试则将考试按钮置为蓝色 并允许点击 否则置灰
            tvExam.setBackgroundColor(ResourceUtil.getColor(R.color.blue5087FF))
            tvExam.isEnabled = true
            //延时弹出是否考试弹窗
            showAcceptExamDialog()
            //todo 由于没有勋章等级 无法展示各种勋章
//            requestMedalDictionary()

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
            TourCooLogUtil.i(mTag, "已执行")
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
                //只有当前正在浏览的课件 并且是html课件才允许点击
                if (course.mediaType == MEDIA_TYPE_HTML) {
                    ToastUtil.show("当前是网页课件,需要手动点击学习")
                    setCourseInfoClick(view, course)
                }
                playStreamUrlOrHtml(course)
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
        startActivity(intent)
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
                    model.videoId = SuperPlayerVideoId()
                    model.videoId.fileId = course.videoID
                    model.videoId.version = SuperPlayerVideoId.FILE_ID_V3
                    model.title = course.name

                    model.videoId.playDefinition = entity.data.playdefinition
                    model.videoId.sign = entity.data.sign
                    model.token = entity.data.token
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

    /**
     * 保存当前观看进度
     */
    private fun requestSaveProgress(courseId: String, second: String) {
        ApiRepository.getInstance().requestSaveProgress(trainingPlanID, courseId, second).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>?>() {
            override fun onSuccessNext(entity: BaseResult<Any>?) {
                if (entity?.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    //todo
                    ToastUtil.showSuccess("进度保存成功")
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
        intent.putExtra(EXTRA_TRAINING_PLAN_ID, CommonUtil.getNotNullValue(trainingPlanID))
        startActivityForResult(intent, REQUEST_CODE_FACE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_FACE -> {
                if (resultCode == Activity.RESULT_OK) {
                    //人脸认证成功 不做任何处理
                } else {
                    //人脸识别失败 处理人脸识别逻辑
                    if (!AppConfig.DEBUG_MODE) {
                        //如果是正式包 则必须执行认证失败的处理
                        handleRecognizeFailedCallback()
                    }

                }
            }
            REQUEST_CODE_WEB -> {
                if (resultCode == Activity.RESULT_OK) {
                    //刷新课件
                    requestPlanDetail()
                }
            }

        }
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

    /**
     * 保存进度并关闭当前页面
     */
    private fun doSaveProgressAndFinish() {
        val progress = smartVideoPlayer.currentProgress
        requestSaveProgress(currentCourseId, progress.toString())
    }


    private fun handleRecognizeFailedCallback() {
        //将视频置为不可点击
//        smartVideoPlayer.startButton.isEnabled = false
        //暂停视频
        baseHandler.postDelayed(Runnable {
            smartVideoPlayer?.onPause()
        }, 1000)

        ToastUtil.show("人脸识别失败")
        baseHandler.postDelayed(Runnable {
            doSaveProgressAndFinish()
        }, 1500)
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
        baseHandler.postDelayed({
            val dialog = ExamCommonDialog(mContext)
            dialog.create().setContent("学习完成是否参加考试？").setPositiveButtonListener(View.OnClickListener {
                dialog.dismiss()
                //跳转考试
                skipExamActivity(trainingPlanDetail)
            }).show()
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

    private fun requestMedalDictionary() {
        ApiRepository.getInstance().requestMedalDictionary().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MedalDictionary>>() {
            override fun onSuccessNext(entity: BaseResult<MedalDictionary>?) {
                if (entity == null) {
                    return
                }
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    //显示勋章
                    showMedalDialog()
                }
            }
        })
    }


    private fun showMedalDialog() {
        val dialog = MedalDialog(mContext)
        dialog.create().setPositiveButtonListener {
            dialog.dismiss()
            val intent = Intent(this, StudyMedalRecordActivity::class.java)
            startActivityForResult(intent, 2017)
        }.show()
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
}
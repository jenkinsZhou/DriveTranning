package com.tourcoo.training.ui.training.safe.online

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dyhdyh.support.countdowntimer.CountDownTimerSupport
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant.*
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
import com.tourcoo.training.ui.exam.ExamActivity
import com.tourcoo.training.ui.exam.ExamActivity.Companion.EXTRA_EXAM_ID
import com.tourcoo.training.ui.face.OnLineFaceRecognitionActivity
import com.tourcoo.training.ui.training.safe.online.TencentPlayVideoActivity.Companion.REQUEST_CODE_FACE
import com.tourcoo.training.widget.dialog.IosAlertDialog
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_play_video.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月19日23:27
 * @Email: 971613168@qq.com
 */
class PlayVideoActivity : BaseTitleActivity(), View.OnClickListener {
    private val mTag = "PlayVideoActivity"
    //    private var orientationUtils: OrientationUtils? = null
    private var isTransition = false

    private var transition: Transition? = null

    private var mCourseList: MutableList<Course>? = null

    private var mCourseHashMap: HashMap<Course, View>? = null

    private var trainingPlanID = ""
    private var trainingPlanDetail: TrainingPlanDetail? = null

    //人脸验证间隔时间
    private var faceVerifyInterval = Int.MAX_VALUE


    private var mTimerTask: CountDownTimerSupport? = null

    companion object {
        const val IMG_TRANSITION = "IMG_TRANSITION"
        const val TRANSITION = "TRANSITION"

    }


    override fun getContentLayout(): Int {
        return R.layout.activity_play_video
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("线上学习")
    }

    override fun initView(savedInstanceState: Bundle?) {
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
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.drawable.img_training_free_video)
//        smartVideoPlayer!!.thumbImageView = imageView
        requestPlanDetail()
    }

/*    private fun loadPlayerSetting() {
        PlayerFactory.setPlayManager(IjkPlayerManager::class.java)
        //增加title
        smartVideoPlayer!!.titleTextView.visibility = View.VISIBLE
        //设置返回键
        smartVideoPlayer!!.backButton.visibility = View.VISIBLE
        //设置旋转
        orientationUtils = OrientationUtils(this, smartVideoPlayer)
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        smartVideoPlayer!!.fullscreenButton.setOnClickListener { orientationUtils!!.resolveByClick() }
        //是否可以滑动调整
        smartVideoPlayer!!.setIsTouchWiget(true)

        //设置ijk内核
        //设置返回按键功能
        smartVideoPlayer!!.backButton.setOnClickListener {
            onBackPressed()
        }
        smartVideoPlayer!!.onPlayStatusListener = this
        smartVideoPlayer!!.setVideoAllCallBack(object : VideoAllCallBack {
            override fun onClickResumeFullscreen(url: String?, vararg objects: Any?) {
                ToastUtil.show("onClickResumeFullscreen")
                //全屏模式下的恢复播放
                timerResume()
            }

            override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onClickResume(url: String?, vararg objects: Any?) {
                ToastUtil.show("onClickResume")
                //正常模式下的恢复播放
                timerResume()
            }

            override fun onClickSeekbarFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onStartPrepared(url: String?, vararg objects: Any?) {
            }

            override fun onClickStartIcon(url: String?, vararg objects: Any?) {
            }

            override fun onTouchScreenSeekLight(url: String?, vararg objects: Any?) {
            }

            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onClickStartThumb(url: String?, vararg objects: Any?) {
            }

            override fun onEnterSmallWidget(url: String?, vararg objects: Any?) {
            }

            override fun onClickStartError(url: String?, vararg objects: Any?) {
                ToastUtil.show("onClickStartError")
            }

            override fun onClickBlankFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onPrepared(url: String?, vararg objects: Any?) {
            }

            override fun onAutoComplete(url: String?, vararg objects: Any?) {
                ToastUtil.show("onAutoComplete")
            }

            override fun onQuitSmallWidget(url: String?, vararg objects: Any?) {
            }

            override fun onTouchScreenSeekVolume(url: String?, vararg objects: Any?) {
                ToastUtil.show("onClickBlank")
            }

            override fun onClickBlank(url: String?, vararg objects: Any?) {
            }

            override fun onClickStop(url: String?, vararg objects: Any?) {
                //正常模式下的暂停播放
                timerPause()
                ToastUtil.show("onClickStop")
            }

            override fun onClickSeekbar(url: String?, vararg objects: Any?) {
                ToastUtil.show("onClickSeekbar")
            }

            override fun onPlayError(url: String?, vararg objects: Any?) {
                ToastUtil.show("onPlayError")
                timerPause()
            }

            override fun onClickStopFullscreen(url: String?, vararg objects: Any?) {
                //全屏模式下的暂停播放
                timerPause()
            }

            override fun onTouchScreenSeekPosition(url: String?, vararg objects: Any?) {
            }

        })
        //过渡动画
        loadTransition()
    }*/

//    private fun loadTransition() {
//        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            postponeEnterTransition()
//            ViewCompat.setTransitionName(smartVideoPlayer!!, IMG_TRANSITION)
//            addTransitionListener()
//            startPostponedEnterTransition()
//        } else {
//            smartVideoPlayer!!.startPlayLogic()
//        }
//    }

    override fun onPause() {
        super.onPause()
        /*  if (smartVideoPlayer != null) {
              smartVideoPlayer!!.onVideoPause()
          }*/
        timerPause()
    }

    override fun onResume() {
        super.onResume()
        /*if (smartVideoPlayer != null) {
            smartVideoPlayer!!.onVideoResume()
        }*/
        timerResume()
    }


    override fun onDestroy() {
        cancelTimer()
        super.onDestroy()
        if (smartVideoPlayer != null) {
            smartVideoPlayer!!.release()
        }
//        if (orientationUtils != null) orientationUtils!!.releaseListener()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
//        smartVideoPlayer!!.startWindowFullscreen(this, true, true)
        setStatusBarDarkMode(mContext, isStatusBarDarkMode)
    }


    /* private fun addTransitionListener(): Boolean {
         transition = window.sharedElementEnterTransition
         if (transition != null) {
             transition!!.addListener(object : OnTransitionListener() {
                 override fun onTransitionEnd(transition: Transition?) {
                     super.onTransitionEnd(transition)
 //                    smartVideoPlayer!!.startPlayLogic();
                     transition!!.removeListener(this);
                 }
             })
             return true
         }
         return false
     }*/

    override fun isStatusBarDarkMode(): Boolean {
        return true
    }

    private fun requestPlanDetail() {
        ApiRepository.getInstance().trainingPlanID(trainingPlanID).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<TrainingPlanDetail>>() {
            override fun onSuccessNext(entity: BaseResult<TrainingPlanDetail>?) {
                if (entity!!.code == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    trainingPlanDetail = entity.data
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
        //初始化计时器
        initTimerAndStart()
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
            loadCourseStatus(entry.value, entry.key)
        }


    }


    private fun getNotNullValue(value: String?): String {
        if (TextUtils.isEmpty(value)) {
            return "未知"
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
     * 定位上次播放位置
     */
    private fun loadCourseStatus(view: View, course: Course) {
        val tvPlanDesc = view.findViewById<TextView>(R.id.tvPlanDesc)
        val imageView = view.findViewById<ImageView>(R.id.ivCourseStatus)
        val tvPlanTitle = view.findViewById<TextView>(R.id.tvPlanTitle)
        when (course.currentPlayStatus) {
            COURSE_PLAY_STATUS_NO_COMPLETE -> {
                imageView.setImageResource(R.mipmap.ic_play_no_complete)
                setViewGone(tvPlanDesc, false)
            }
            COURSE_PLAY_STATUS_COMPLETE -> {
                imageView.setImageResource(R.mipmap.ic_play_finish)
                setViewGone(tvPlanDesc, false)
            }
            COURSE_PLAY_STATUS_PLAYING -> {
                imageView.setImageResource(R.mipmap.ic_playing)
                tvPlanTitle.setTextColor(ResourceUtil.getColor(R.color.blue5087FF))
                setViewGone(tvPlanDesc, true)
                view.setBackgroundColor(ResourceUtil.getColor(R.color.blueEFF3FF))
                playStreamUrl(course)
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


    private fun playStreamUrl(course: Course?) {
        if (course == null || course.streams == null) {
            return
        }
        when (course.mediaType) {
            //视频
            0 -> {
                //从上次播放进度开始播放
                /*    //todo
                    smartVideoPlayer.seekOnStart = course.progress * 1000L
                    smartVideoPlayer.currentCourseId = course.id
                    smartVideoPlayer!!.setUp(course.streams, false, getNotNullValue(course.name))
                    loadPlayerSetting()*/


            }
            else -> {
                //外链URL
                ToastUtil.show("跳转外链逻辑")
            }
        }


    }

    /* override fun onPlayComplete(courseId: Int) {
         //todo
     }

     override fun onAutoPlayComplete(courseId: Int) {
         //todo
         //通知后台当前课程播放结束
         requestCompleteCurrentCourse(courseId.toString())
     }
 */

    /* private fun handlePlayComplete(courseId: Int) {
         playNext(courseId)
     }*/


    /**
     * 播放下一集
     *
     * @return true表示还有下一集
     */
    /*  private fun playNext(courseId: Int) {
          val course = getNextCourse(courseId)
          if (course != null) {
              requestPlanDetail()
          } else {
              ToastUtil.show("沒有下一集了")
          }

      }*/


    private fun getNextCourse(courseId: Int): Course? {
        for (entry in mCourseHashMap!!.entries) {
            if (entry.key.id == courseId) {
                var index = mCourseList!!.indexOf(entry.key)
                if (index < 0) {
                    //说明没有找到
                    return null
                }
                if (index == mCourseList!!.size - 1) {
                    //说明已经是最后一个视频了
                    return null
                }
                if (index + 1 < mCourseList!!.size) {
                    return mCourseList!![index + 1]
                }
                return null
            }
        }
        return null
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
        })
    }

    private fun showExit() {
        IosAlertDialog(mContext)
                .init()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setTitle("确认退出学习")
                .setMsg("退出前会保存当前学习进度")
                .setPositiveButton("确认退出", View.OnClickListener {
                    //todo 保存观看进度
                    doSaveProgressAndFinish()
                })
                .setNegativeButton("取消", View.OnClickListener {
                    finish()
                }).show()
    }


    override fun onBackPressed() {
        /*  if (GSYVideoManager.backFromWindowFull(this)) {
              return
          }*/
        showExit()
    }


//    private fun isFullScreen(): Boolean {
//        if (orientationUtils == null) {
//            return false
//        }
//        if (orientationUtils!!.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            return true
//        }
//        return false
//    }


    private fun skipRecognize() {
        //暂停视频
//        smartVideoPlayer.onVideoPause()
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
//                    handleRecognizeFailedCallback()
                }
            }
            else -> {
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

    /**
     * 保存进度并关闭当前页面
     */
    private fun doSaveProgressAndFinish() {
        /*  val progress = smartVideoPlayer!!.currentPositionWhenPlaying / 1000
          requestSaveProgress(smartVideoPlayer!!.currentCourseId.toString(), progress.toString())*/
    }


    /*  private fun handleRecognizeFailedCallback() {
          //将视频置为不可点击
          smartVideoPlayer.startButton.isEnabled = false
          //暂停视频
          baseHandler.postDelayed(Runnable {
              smartVideoPlayer?.onVideoPause()
          }, 1000)

          ToastUtil.show("人脸识别失败")
          baseHandler.postDelayed(Runnable {
              doSaveProgressAndFinish()
          }, 1500)
      }*/
}
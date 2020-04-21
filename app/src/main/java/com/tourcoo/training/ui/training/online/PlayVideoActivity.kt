package com.tourcoo.training.ui.training.online

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.training.Catalog
import com.tourcoo.training.entity.training.Course
import com.tourcoo.training.entity.training.TrainingPlanDetail
import com.tourcoo.training.ui.exam.OnlineExamActivity
import com.tourcoo.training.ui.exam.OnlineExamActivity.Companion.EXTRA_EXAM_ID
import com.tourcoo.training.widget.dialog.IosAlertDialog
import com.tourcoo.training.widget.oldplayer.OnTransitionListener
import com.tourcoo.training.widget.player.OnPlayStatusListener
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_play_video.*

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年04月19日23:27
 * @Email: 971613168@qq.com
 */
class PlayVideoActivity : BaseTitleActivity(), OnPlayStatusListener, View.OnClickListener {
    private val mTag = "PlayVideoActivity"
    private var orientationUtils: OrientationUtils? = null

    private var isTransition = false

    private var transition: Transition? = null

    private var mCourseList: MutableList<Course>? = null

    private var mCourseHashMap: HashMap<Course, View>? = null

    private var trainingPlanID = ""
    private var trainingPlanDetail: TrainingPlanDetail? = null

    companion object {
        const val IMG_TRANSITION = "IMG_TRANSITION"
        const val TRANSITION = "TRANSITION"
        const val COURSE_STATUS_NO_COMPLETE = 0
        const val COURSE_STATUS_COMPLETE = 2
        const val COURSE_STATUS_PLAYING = 1
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_play_video
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("线上学习")
    }

    override fun initView(savedInstanceState: Bundle?) {
        isTransition = intent.getBooleanExtra(PlayVideoActivityOld.TRANSITION, false)
        trainingPlanID = intent.getStringExtra(EXTRA_TRAINING_PLAN_ID)
        if (TextUtils.isEmpty(trainingPlanID)) {
            ToastUtil.show("未获取到计划信息")
            finish()
            return
        }
        TourCooLogUtil.i(mTag, "trainingPlanID=$trainingPlanID")
        tvExam.setOnClickListener(this)
        mCourseList = ArrayList()
        mCourseHashMap = HashMap()
        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.drawable.img_training_free_video)
        smartVideoPlayer!!.thumbImageView = imageView
        requestPlanDetail()
    }

    private fun loadPlayer() {
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
        //过渡动画
        loadTransition()
    }

    private fun loadTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
            ViewCompat.setTransitionName(smartVideoPlayer!!, PlayVideoActivityOld.IMG_TRANSITION)
            addTransitionListener()
            startPostponedEnterTransition()
        } else {
            smartVideoPlayer!!.startPlayLogic()
        }
    }

    override fun onPause() {
        super.onPause()
        if (smartVideoPlayer != null) {
            smartVideoPlayer!!.onVideoPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (smartVideoPlayer != null) {
            smartVideoPlayer!!.onVideoResume()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (smartVideoPlayer != null) {
            smartVideoPlayer!!.release()
        }
        if (orientationUtils != null) orientationUtils!!.releaseListener()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        smartVideoPlayer!!.startWindowFullscreen(this, true, true)
        setStatusBarDarkMode(mContext, isStatusBarDarkMode)
    }


    private fun addTransitionListener(): Boolean {
        transition = window.sharedElementEnterTransition
        if (transition != null) {
            transition!!.addListener(object : OnTransitionListener() {
                override fun onTransitionEnd(transition: Transition?) {
                    super.onTransitionEnd(transition)
                    super.onTransitionEnd(transition);
                    smartVideoPlayer!!.startPlayLogic();
                    transition!!.removeListener(this);
                }
            })
            return true
        }
        return false
    }

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
        mCourseHashMap!!.clear()
        mCourseList!!.clear()
        llPlanContentView.removeAllViews()
        tvTitle.text = getNotNullValue(detail.description)
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
                tvPlanDesc.text = getNotNullValue("00:00学习到01:12")
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
            COURSE_STATUS_NO_COMPLETE -> {
                imageView.setImageResource(R.mipmap.ic_play_no_complete)
                setViewGone(tvPlanDesc, false)
            }
            COURSE_STATUS_COMPLETE -> {
                imageView.setImageResource(R.mipmap.ic_play_finish)
                setViewGone(tvPlanDesc, false)
            }
            COURSE_STATUS_PLAYING -> {
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
                course.currentPlayStatus = COURSE_STATUS_PLAYING
                index = i
            } else if (course.completed <= 0 && index != -1) {
                course.currentPlayStatus = COURSE_STATUS_NO_COMPLETE
            } else {
                course.currentPlayStatus = COURSE_STATUS_COMPLETE
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
                //todo
                    smartVideoPlayer.seekOnStart = course.progress * 1000L
                    smartVideoPlayer.currentCourseId = course.id
                    smartVideoPlayer!!.setUp(course.streams, false, getNotNullValue(course.name))
                    loadPlayer()


            }
            else -> {
                //外链URL
                ToastUtil.show("跳转外链逻辑")
            }
        }


    }

    override fun onPlayComplete(courseId: Int) {
        //todo
    }

    override fun onAutoPlayComplete(courseId: Int) {
        //todo
        //通知后台当前课程播放结束
        requestCompleteCurrentCourse(courseId.toString())
    }


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
        val intent = Intent(mContext, OnlineExamActivity::class.java)
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

    private fun showExit(courseId: String) {
        IosAlertDialog(mContext)
                .init()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setTitle("确认退出学习")
                .setMsg("退出前会保存当前学习进度")
                .setPositiveButton("确认退出", View.OnClickListener {
                    //todo 保存观看进度
                    val progress = smartVideoPlayer!!.currentPositionWhenPlaying / 1000
                    requestSaveProgress(courseId, progress.toString())
                })
                .setNegativeButton("取消", View.OnClickListener {
                }).show()
    }


    override fun onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        showExit(smartVideoPlayer.currentCourseId.toString())
    }


    private fun isFullScreen(): Boolean{
        if(orientationUtils == null){
            return false
        }
        if(orientationUtils!!.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            return true
        }
        return false
    }
}
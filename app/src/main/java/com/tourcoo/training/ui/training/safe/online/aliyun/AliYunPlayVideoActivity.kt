package com.tourcoo.training.ui.training.safe.online.aliyun

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import com.aliyun.player.IPlayer
import com.aliyun.player.IPlayer.OnRenderingStartListener
import com.aliyun.player.IPlayer.OnSeiDataListener
import com.aliyun.player.bean.ErrorCode
import com.aliyun.player.nativeclass.PlayerConfig
import com.aliyun.player.source.UrlSource
import com.aliyun.player.source.VidSts
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.dyhdyh.support.countdowntimer.CountDownTimerSupport
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant
import com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.*
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.training.*
import com.tourcoo.training.ui.exam.ExamActivity
import com.tourcoo.training.ui.face.OnLineFaceRecognitionActivity
import com.tourcoo.training.ui.training.safe.online.TencentPlayVideoActivity
import com.tourcoo.training.widget.AliYunVodPlayerView
import com.tourcoo.training.widget.AliYunVodPlayerView.*
import com.tourcoo.training.widget.aliplayer.activity.AliyunPlayerSkinActivity.DEFAULT_URL
import com.tourcoo.training.widget.aliplayer.activity.AliyunPlayerSkinActivity.DEFAULT_VID
import com.tourcoo.training.widget.aliplayer.constants.PlayParameter
import com.tourcoo.training.widget.aliplayer.listener.OnChangeQualityListener
import com.tourcoo.training.widget.aliplayer.listener.OnStoppedListener
import com.tourcoo.training.widget.aliplayer.listener.QualityValue
import com.tourcoo.training.widget.aliplayer.mode.AliyunScreenMode
import com.tourcoo.training.widget.aliplayer.playlist.AlivcPlayListAdapter
import com.tourcoo.training.widget.aliplayer.playlist.AlivcVideoInfo.DataBean.VideoListBean
import com.tourcoo.training.widget.aliplayer.utils.FixedToastUtils
import com.tourcoo.training.widget.aliplayer.utils.ScreenUtils
import com.tourcoo.training.widget.aliplayer.utils.VidStsUtil
import com.tourcoo.training.widget.aliplayer.utils.VidStsUtil.OnStsResultListener
import com.tourcoo.training.widget.aliplayer.utils.download.AliyunDownloadMediaInfo
import com.tourcoo.training.widget.aliplayer.view.choice.AlivcShowMoreDialog
import com.tourcoo.training.widget.aliplayer.view.control.ControlView
import com.tourcoo.training.widget.aliplayer.view.gesturedialog.BrightnessDialog
import com.tourcoo.training.widget.aliplayer.view.more.AliyunShowMoreValue
import com.tourcoo.training.widget.aliplayer.view.more.SpeedValue
import com.tourcoo.training.widget.aliplayer.view.tipsview.ErrorInfo
import com.tourcoo.training.widget.dialog.IosAlertDialog
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_play_video_ali.*
import kotlinx.android.synthetic.main.activity_play_video_ali.llPlanContentView
import kotlinx.android.synthetic.main.activity_play_video_ali.tvExam
import kotlinx.android.synthetic.main.activity_play_video_ali.tvSubjectDesc
import kotlinx.android.synthetic.main.activity_play_video_ali.tvTitle
import kotlinx.android.synthetic.main.activity_play_video_tencent.*
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 *@description :阿里云播放器播放页面
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月25日14:30
 * @Email: 971613168@qq.com
 */
class AliYunPlayVideoActivity : BaseTitleActivity(), View.OnClickListener {

    val mTag = "PlayVideoActivity"
    private var isTransition = false
    //判断是否在后台
    private val mIsInBackground = false
    private var currentCourseId: String = ""
    private var mCurrentCourse: Course? = null
    private var alivcPlayListAdapter: AlivcPlayListAdapter? = null

    private var mCourseList: MutableList<Course>? = null

    private var mCourseHashMap: HashMap<Course, View>? = null

    private var trainingPlanID = ""
    private var trainingPlanDetail: TrainingPlanDetail? = null

    //人脸验证间隔时间
    private var faceVerifyInterval = Int.MAX_VALUE


    private var mTimerTask: CountDownTimerSupport? = null

    private var currentError = ErrorInfo.Normal

    private var currentVideoPosition = 0

    var showMoreDialog: AlivcShowMoreDialog? = null


    var aliyunDownloadMediaInfoList: List<AliyunDownloadMediaInfo> = java.util.ArrayList()
    private val currentPreparedMediaInfo: List<AliyunDownloadMediaInfo>? = null

    var oldTime: Long = 0
    var downloadOldTime: Long = 0
    val preparedVid: String? = null
    private var mCurrentDownloadScreenMode: AliyunScreenMode? = null

    private var alivcVideoInfos: java.util.ArrayList<VideoListBean>? = null
    /**
     * 是否需要展示下载界面,如果是恢复数据,则不用展示下载界面
     */
    private var showAddDownloadView = false

    /**
     * 是否鉴权过期
     */
    private var mIsTimeExpired = false
    /**
     * 判断是否在下载中
     */
    private var mDownloadInPrepare = false

    /**
     * get StsToken stats
     */
    private var inRequest = false

    /**
     * 判断考试是否完成
     */
    private var hasRequireExam = true

    private val PERMISSIONS_STORAGE = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    )

    private val REQUEST_EXTERNAL_STORAGE = 1

    private val TAB_VIDEO_LIST = 1


    private var currentScreenMode = AliyunScreenMode.Small

    private val downloadDialog: Dialog? = null

    /**
     * 章的数量
     */
    private var countCatalog = 0

    /**
     * 节的数量
     */
    private var countNode = 0

    companion object {
        const val IMG_TRANSITION = "IMG_TRANSITION"
        const val TRANSITION = "TRANSITION"
        //阿里hls标准加密类型
        const val ENCRYPTION_TYPE_HLS = "HLSEncryption"
        //腾讯加密类型
        const val ENCRYPTION_TYPE_DRM = "DriveDu-DRM"
        const val REQUEST_CODE_FACE = 1006
        const val COURSE_STATUS_NO_COMPLETE = 0
        const val COURSE_STATUS_COMPLETE = 2
        const val COURSE_STATUS_PLAYING = 1
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_play_video_ali
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("在线学习")
    }


    override fun initView(savedInstanceState: Bundle?) {
        isTransition = intent.getBooleanExtra(TencentPlayVideoActivity.TRANSITION, false)
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
        //todo 增加封面
        requestPlanDetail()
    }

    private fun loadPlayerSetting(url: String) {
        initAliYunPlayerView(url)
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updatePlayerViewMode()
    }

    override fun onPause() {
        super.onPause()
        aliYunPlayer?.onStop()
        timerPause()
    }

    override fun onResume() {
        super.onResume()
        aliYunPlayer?.onResume()
        timerResume()
    }


    override fun onDestroy() {
        cancelTimer()
        aliYunPlayer?.onDestroy()
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
                    //如果已经学习完成 则不保存进度
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
        //初始化计时器
        initTimerAndStart()

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

        LogUtils.e(countCatalog, countNode)

        tvCourseCountInfoAli.text = "共" + countCatalog + "章" + countNode + "小节"
        tvCourseTimeAli.text = "课时：" + detail.courseTime.toString()
        tvTitle.text = getNotNullValue(detail.title)
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
            TencentPlayVideoActivity.COURSE_STATUS_NO_COMPLETE -> {
                imageView.setImageResource(R.mipmap.ic_play_no_complete)
                setViewGone(tvPlanDesc, false)
            }
            TencentPlayVideoActivity.COURSE_STATUS_COMPLETE -> {
                imageView.setImageResource(R.mipmap.ic_play_finish)
                setViewGone(tvPlanDesc, false)
            }
            TencentPlayVideoActivity.COURSE_STATUS_PLAYING -> {
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
                course.currentPlayStatus = TencentPlayVideoActivity.COURSE_STATUS_PLAYING
                index = i
            } else if (course.completed <= 0 && index != -1) {
                course.currentPlayStatus = TencentPlayVideoActivity.COURSE_STATUS_NO_COMPLETE
            } else {
                course.currentPlayStatus = TencentPlayVideoActivity.COURSE_STATUS_COMPLETE
            }
        }
    }


    private fun playStreamUrl(course: Course?) {
        if (course == null || course.streams == null && course.streams.size == 0) {
            return
        }
        course.streams = sortQuality(course.streams)
        when (course.mediaType) {
            //视频
            0 -> {
                //从上次播放进度开始播放
                currentCourseId = "" + course.id
                mCurrentCourse = course
                //关键点：需要判断当前视频是否是加密视频
                if (course.streams == null || course.streams.isEmpty()) {
                    ToastUtil.show("当前课程为非视频")
                    return
                }
                when (CommonUtil.getNotNullValue(course.streams[0].encryptType)) {
                    ENCRYPTION_TYPE_HLS -> {
                        //阿里加密模式
                        requestHlsEncryptParamsAndPlay(course)
                    }
                    ENCRYPTION_TYPE_DRM -> {
                        //不支持腾讯加密模式
                        ToastUtil.show("播放器不支持此加密类型")
                    }
                    "" -> {
                        //标准播放模式（不加密）
                        //关键点 将当前视频清晰度对象赋值给播放器
                        aliYunPlayer.currentSteam = course.streams[0]
                        loadPlayerSetting(CommonUtil.getUrl(course.streams[0].url))
                    }
                }


/*
                if (course.streams[0].encryptType != null && course.streams[0].encryptType == "DriveDu-DRM") {
//                    requestVideoEncryptParamsCurrentCourse(course)
                }
                else {
              *//*      val superPlayerModel = SuperPlayerModel()
                    superPlayerModel.multiURLs = ArrayList()
                    superPlayerModel.title = course.name*//*
              *//*      course.streams.forEach {
                        superPlayerModel.multiURLs.add(SuperPlayerModel.SuperPlayerURL(it.url, it.definition))
                    }
                    superPlayerModel.playDefaultIndex = 0

                    smartVideoPlayer.playWithModel(superPlayerModel)*//*
                }*/

            }
            else -> {
                //外链URL
                ToastUtil.show("跳转外链逻辑")
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
        intent.putExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID, trainingPlanID)
        //考试题id
        intent.putExtra(ExamActivity.EXTRA_EXAM_ID, trainingPlanDetail.latestExamID.toString())
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

            override fun onError(e: Throwable) {
                super.onError(e)
                finish()
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
                    finish()
                } else {
                    ToastUtil.show(entity?.msg)
                }

            }

            override fun onError(e: Throwable) {
                ToastUtil.show("学习进度未保存")
                finish()
            }
        })
    }

    private fun showExit() {
        val progress = aliYunPlayer.currentSecond
        if (progress <= 0) {
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
                    //todo 保存观看进度
                    //先暂停播放器
                    aliYunPlayer.onStop()
                    baseHandler.postDelayed(Runnable {
                        //延时保存进度
                        doSaveProgressAndFinish()
                    }, 500)
                })
                .setNegativeButton("取消", View.OnClickListener {
                    finish()
                }).show()
    }


    override fun onBackPressed() {
        //先判断当前是否处于横屏状态 如果是则先恢复小屏
        if (aliYunPlayer.screenMode == AliyunScreenMode.Full) {
            aliYunPlayer.changeScreenMode(AliyunScreenMode.Small, false)
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
        aliYunPlayer.onStop()
        //暂停计时器
        timerPause()
        //跳转到人脸认证
        skipFace()
    }


    private fun skipFace() {
        //人脸认证
        val intent = Intent(this, OnLineFaceRecognitionActivity::class.java)
        intent.putExtra(TrainingConstant.EXTRA_TRAINING_PLAN_ID, CommonUtil.getNotNullValue(trainingPlanID))
        startActivityForResult(intent, TencentPlayVideoActivity.REQUEST_CODE_FACE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TencentPlayVideoActivity.REQUEST_CODE_FACE -> {
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
        //todo
        val progress = aliYunPlayer.currentSecond
        requestSaveProgress(currentCourseId, progress.toString())
    }


    private fun initAliYunPlayerView(videoUrl: String) {
        if (mCurrentCourse == null || mCurrentCourse!!.streams == null) {
            //需要判断课程视频流是否正常
            ToastUtil.show("课程视频有误")
            return
        }
        //很关键
        aliYunPlayer.courseInfo = mCurrentCourse
        //设置封面图
        if (!TextUtils.isEmpty(CommonUtil.getUrl(mCurrentCourse!!.coverURL))) {
            aliYunPlayer.setCoverUri(mCurrentCourse!!.coverURL)
        } else {
            aliYunPlayer.setCoverResource(R.drawable.img_training_free_video)
        }
        //定位到上次播放进度
        TourCooLogUtil.i(mTag, "上次播放进度:" + mCurrentCourse!!.progress * 1000)
        aliYunPlayer.seekTo(aliYunPlayer.courseInfo!!.progress * 1000)
        alivcVideoInfos = ArrayList()
        alivcPlayListAdapter = AlivcPlayListAdapter(this, alivcVideoInfos)
        //保持屏幕敞亮
        aliYunPlayer.setKeepScreenOn(true)
        PlayParameter.PLAY_PARAM_URL = DEFAULT_URL
        val sdDir = Environment.getExternalStorageDirectory().absolutePath + "/test_save_cache"
        aliYunPlayer.setPlayingCache(false, sdDir, 60 * 60 /*时长, s */, 300 /*大小，MB*/)
        aliYunPlayer.setTheme(AliYunVodPlayerView.Theme.Blue)
        //aliYunPlayer.setCirclePlay(true);
        aliYunPlayer.setAutoPlay(true)
        aliYunPlayer.setOnPreparedListener(MyPrepareListener(this))
        aliYunPlayer.setNetConnectedListener(MyNetConnectedListener(this))
        aliYunPlayer.setOnCompletionListener(MyCompletionListener(this))
        aliYunPlayer.setOnFirstFrameStartListener(MyFrameInfoListener(this))
        aliYunPlayer.setOnChangeQualityListener(MyChangeQualityListener(this))
        //TODO
        aliYunPlayer.setOnStoppedListener(MyStoppedListener(this))
        aliYunPlayer.setmOnPlayerViewClickListener(MyPlayViewClickListener(this))
        aliYunPlayer.setOrientationChangeListener(MyOrientationChangeListener(this))
        //        aliYunPlayer.setOnUrlTimeExpiredListener(new MyOnUrlTimeExpiredListener(this));
        aliYunPlayer.setOnTimeExpiredErrorListener(MyOnTimeExpiredErrorListener(this))
        aliYunPlayer.setOnShowMoreClickListener(MyShowMoreClickLisener(this))
        aliYunPlayer.setOnPlayStateBtnClickListener(MyPlayStateBtnClickListener(this))
        aliYunPlayer.setOnSeekCompleteListener(MySeekCompleteListener(this))
        aliYunPlayer.setOnSeekStartListener(MySeekStartListener(this))
        aliYunPlayer.setOnScreenBrightness(MyOnScreenBrightnessListener(this))
        aliYunPlayer.setOnErrorListener(MyOnErrorListener(this))
        aliYunPlayer.setScreenBrightness(BrightnessDialog.getActivityBrightness(this@AliYunPlayVideoActivity))
        aliYunPlayer.setSeiDataListener(MyOnSeiDataListener(this))
        aliYunPlayer.setOnChangeDefinitionListener(MyChangeDefinitionListener(this))
        aliYunPlayer.enableNativeLog()
        setPlaySource(videoUrl)
    }


    private class MyPrepareListener(skinActivity: AliYunPlayVideoActivity) : IPlayer.OnPreparedListener {
        private val activityWeakReference: WeakReference<AliYunPlayVideoActivity>
        override fun onPrepared() {
            val activity = activityWeakReference.get()
            activity?.onPrepared()
        }

        init {
            activityWeakReference = WeakReference(skinActivity)
        }
    }

    private fun onPrepared() {
        closeLoading()
    }

    private class MyCompletionListener(skinActivity: AliYunPlayVideoActivity) : IPlayer.OnCompletionListener {
        private val activityWeakReference: WeakReference<AliYunPlayVideoActivity> = WeakReference(skinActivity)
        override fun onCompletion() {
            val activity = activityWeakReference.get()
            activity?.onCompletion()
        }

    }

    private fun onCompletion() {
        //播放完成 调用保存进度接口
        requestCompleteCurrentCourse(currentCourseId)
    }


    private class MyFrameInfoListener(skinActivity: AliYunPlayVideoActivity) : OnRenderingStartListener {
        private val activityWeakReference: WeakReference<AliYunPlayVideoActivity> = WeakReference(skinActivity)
        override fun onRenderingStart() {
            val activity = activityWeakReference.get()
            activity?.onFirstFrameStart()
        }

    }

    private fun onFirstFrameStart() {
        if (aliYunPlayer != null) {
            val debugInfo: Map<String, String?> = aliYunPlayer.getAllDebugInfo() ?: return
            var createPts: Long = 0
            if (debugInfo["create_player"] != null) {
                val time = debugInfo["create_player"]
                createPts = time!!.toDouble().toLong()
            }
            if (debugInfo["open-url"] != null) {
                val time = debugInfo["open-url"]
                val openPts = time!!.toDouble().toLong() + createPts
            }
            if (debugInfo["find-stream"] != null) {
                val time = debugInfo["find-stream"]
                val findPts = time!!.toDouble().toLong() + createPts
            }
            if (debugInfo["open-stream"] != null) {
                val time = debugInfo["open-stream"]
                val openPts = time!!.toDouble().toLong() + createPts
            }

        }
    }


    /**
     * 判断是否有网络的监听
     */
    private class MyNetConnectedListener(activity: AliYunPlayVideoActivity) : AliYunVodPlayerView.NetConnectedListener {
        var weakReference: WeakReference<AliYunPlayVideoActivity>
        override fun onReNetConnected(isReconnect: Boolean) {
            val activity = weakReference.get()
            activity?.onReNetConnected(isReconnect)
        }

        override fun onNetUnConnected() {
            val activity = weakReference.get()
            activity?.onNetUnConnected()
        }

        init {
            weakReference = WeakReference(activity)
        }
    }

    private fun onNetUnConnected() {
        currentError = ErrorInfo.UnConnectInternet
        if (aliyunDownloadMediaInfoList != null && aliyunDownloadMediaInfoList.size > 0) {
            ToastUtils.showShort("onNetUnConnected")
        }
    }

    private fun onReNetConnected(isReconnect: Boolean) {
        currentError = ErrorInfo.Normal
        if (isReconnect) {
            if (aliyunDownloadMediaInfoList != null && aliyunDownloadMediaInfoList.size > 0) {
                var unCompleteDownload = 0
                for (info in aliyunDownloadMediaInfoList) {
                    if (info.status == AliyunDownloadMediaInfo.Status.Stop) {
                        unCompleteDownload++
                    }
                }
                if (unCompleteDownload > 0) {
                    FixedToastUtils.show(this, "网络恢复, 请手动开启下载任务...")
                }
            }
            // 如果当前播放列表为空, 网络重连后需要重新请求sts和播放列表, 其他情况不需要
            if (alivcVideoInfos != null && alivcVideoInfos!!.size == 0) {
                VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, MyStsListener(this))
            }
        }
    }


    private class MyChangeQualityListener(skinActivity: AliYunPlayVideoActivity) : OnChangeQualityListener {
        private val activityWeakReference: WeakReference<AliYunPlayVideoActivity>
        override fun onChangeQualitySuccess(finalQuality: String) {
            val activity = activityWeakReference.get()
            activity?.onChangeQualitySuccess(finalQuality)
        }

        override fun onChangeQualityFail(code: Int, msg: String) {
            val activity = activityWeakReference.get()
            activity?.onChangeQualityFail(code, msg)
        }

        init {
            activityWeakReference = WeakReference(skinActivity)
        }
    }

    private fun onChangeQualitySuccess(finalQuality: String) {
        ToastUtils.showShort("onChangeQualitySuccess=" + finalQuality)
    }

    fun onChangeQualityFail(code: Int, msg: String) {
        ToastUtils.showShort("onChangeQualitySuccess=" + "code=" + code)
    }

    private class MyStoppedListener(skinActivity: AliYunPlayVideoActivity) : OnStoppedListener {
        private val activityWeakReference: WeakReference<AliYunPlayVideoActivity>
        override fun onStop() {
            val activity = activityWeakReference.get()
            activity?.onStopped()
        }

        init {
            activityWeakReference = WeakReference(skinActivity)
        }
    }


    private fun onStopped() {
    }

    private fun setPlaySource(url: String) {
        showLoading("正在加载视频...")
        PlayParameter.PLAY_PARAM_TYPE = "localSource"
        if ("localSource" == PlayParameter.PLAY_PARAM_TYPE) {
            TourCooLogUtil.i(mTag, "执行了1")
            val urlSource = UrlSource()
            urlSource.uri = url
            /*     urlSource.uri = "http://cdn.course.ggjtaq.com/720/renmenjiaotongchubansheshuzikejian/weixianhuowudaoluyunshujiashiyuananquanjiaoyupeixunshuzikechengbanben/diyizhangjiashiyuandeshehuizerenyuzhiyedaode/diyijiedaoluyunshujiashiyuandezhiyetedian/jiaotongshigudeweihai/jiaotongshigudeweihai.mp4"
                 val testUrl = "https://course.vod.ggjtaq.com/20d650dd19664ab9947b93cbe4f1e397/481ae69632e2f8e66eea8ee4846e5cc7-fd-encrypt-stream.m3u8"
                 val token = "lLR1wqWwlfx63beDUHzHr474BmzYKSis8IacgggLKZUYmNOAL9O1s7RVa%2F%2FBWCo9PDOFB9FNNnXDlHOsr1uZhm52GjzZoBt6%2F%2FUuTf9Ov%2BrS01r%2BfJdB1swWZWLhs5Sq%2BFMocedI4oXYJJo8ih8OioHLg9vvAsiupvOcRKpbZz9YoNKnJ3pXX0d2nuxJkrVy0gpkqJKwKZzdgElPYe3HAs%2FM9dex9BCrVmDk8rI4%2FN45fxTfMBz4jd6%2Fd9hDOSylLri2QDZKrxtOgR8Q9yz5o%2Bz2NG3AJ5Pt2YM%2BYKM7sVZqNqYmDUNIzNNSYn7rDtn1o%2BUEOCoAz4zSwgr5lorvi%2F3sTFEPst%2FkY66lEYvg8Nzj0mWuvgliKjPK2ho%2FLx6tGEw5UT5ZM3m%2B1G0Xcc0jnA%3D%3D"
                 urlSource.uri = testUrl + "?MtsHlsUriToken=" + token*/
            //默认是5000
            var maxDelayTime = 5000
            if (PlayParameter.PLAY_PARAM_URL.startsWith("artp")) { //如果url的开头是artp，将直播延迟设置成100，
                maxDelayTime = 100
            }
            if (aliYunPlayer != null) {
                TourCooLogUtil.i(mTag, "执行了2")
                val playerConfig: PlayerConfig = aliYunPlayer.getPlayerConfig()
                playerConfig.mMaxDelayTime = maxDelayTime
                //开启SEI事件通知
                playerConfig.mEnableSEI = true
                aliYunPlayer.setPlayerConfig(playerConfig)
                aliYunPlayer.setLocalSource(urlSource)
            }
        } else if ("vidsts" == PlayParameter.PLAY_PARAM_TYPE) {
            if (!inRequest) {
                TourCooLogUtil.i(mTag, "执行了3")
                val vidSts = VidSts()
                vidSts.vid = PlayParameter.PLAY_PARAM_VID
                vidSts.region = PlayParameter.PLAY_PARAM_REGION
                vidSts.accessKeyId = PlayParameter.PLAY_PARAM_AK_ID
                vidSts.accessKeySecret = PlayParameter.PLAY_PARAM_AK_SECRE
                vidSts.securityToken = PlayParameter.PLAY_PARAM_SCU_TOKEN
                if (aliYunPlayer != null) {
                    aliYunPlayer.setVidSts(vidSts)
                }
            }
        }
    }

    /**
     * 切换播放资源
     *
     * @param position 需要播放的数据在集合中的下标
     */
    private fun changePlaySource(position: Int) {
        currentVideoPosition = position
        val video: VideoListBean = alivcVideoInfos!!.get(position)
        changePlayVidSource(video)
    }

    /**
     * 播放本地资源
     */
    private fun changePlayLocalSource(url: String, title: String) {
        val urlSource = UrlSource()
        urlSource.uri = url
        urlSource.title = title
        aliYunPlayer.setLocalSource(urlSource)
    }

    /**
     * 切换播放vid资源
     *
     * @param video 要切换的资源
     */
    private fun changePlayVidSource(video: VideoListBean) {
        mDownloadInPrepare = true
        val vidSts = VidSts()
        PlayParameter.PLAY_PARAM_VID = video.videoId
        aliYunPlayer.setAutoPlay(!mIsInBackground)
        //切换资源重置下载flag
        mDownloadInPrepare = false
        /**
         * 如果是鉴权过期
         */
        if (mIsTimeExpired) {
            onTimExpiredError()
        } else {
            vidSts.vid = PlayParameter.PLAY_PARAM_VID
            vidSts.region = PlayParameter.PLAY_PARAM_REGION
            vidSts.accessKeyId = PlayParameter.PLAY_PARAM_AK_ID
            vidSts.accessKeySecret = PlayParameter.PLAY_PARAM_AK_SECRE
            vidSts.securityToken = PlayParameter.PLAY_PARAM_SCU_TOKEN
            vidSts.title = video.title
            aliYunPlayer.setVidSts(vidSts)
        }
    }


    private class MyPlayViewClickListener(activity: AliYunPlayVideoActivity) : OnPlayerViewClickListener {
        private val weakReference: WeakReference<AliYunPlayVideoActivity>
        override fun onClick(screenMode: AliyunScreenMode, viewType: PlayViewType) {
            val currentClickTime = System.currentTimeMillis()
            val activity = weakReference.get()
            if (activity == null) {
                return
            }
            // 防止快速点击
            if (currentClickTime - activity!!.oldTime <= 1000) {
                return
            }
            activity.oldTime = currentClickTime
            // 如果当前的Type是Download, 就显示Download对话框
            if (viewType == PlayViewType.Download) {
                ToastUtils.showShort("下载模式")
            }
        }

        init {
            weakReference = WeakReference(activity)
        }
    }


    private class MyOrientationChangeListener(activity: AliYunPlayVideoActivity) : OnOrientationChangeListener {
        private val weakReference: WeakReference<AliYunPlayVideoActivity>
        override fun orientationChange(from: Boolean, currentMode: AliyunScreenMode) {
            val activity = weakReference.get()
            if (activity != null) {
                activity.hideDownloadDialog(from, currentMode)
                activity.hideShowMoreDialog(from, currentMode)
            }
        }

        init {
            weakReference = WeakReference(activity)
        }
    }

    private fun hideShowMoreDialog(from: Boolean, currentMode: AliyunScreenMode) {
        if (showMoreDialog != null) {
            if (currentMode == AliyunScreenMode.Small) {
                showMoreDialog!!.dismiss()
                currentScreenMode = currentMode
            }
        }
    }

    private fun hideDownloadDialog(from: Boolean, currentMode: AliyunScreenMode) {
        if (downloadDialog != null) {
            if (currentScreenMode != currentMode) {
                downloadDialog.dismiss()
                currentScreenMode = currentMode
            }
        }
    }

    class MyOnTimeExpiredErrorListener(activity: AliYunPlayVideoActivity) : OnTimeExpiredErrorListener {
        var weakReference: WeakReference<AliYunPlayVideoActivity>
        override fun onTimeExpiredError() {
            val activity = weakReference.get()
            activity?.onTimExpiredError()
        }

        init {
            weakReference = WeakReference(activity)
        }
    }

    fun onStsRetrySuccess(mVid: String?, akid: String?, akSecret: String?, token: String?) {
        PlayParameter.PLAY_PARAM_VID = mVid
        PlayParameter.PLAY_PARAM_AK_ID = akid
        PlayParameter.PLAY_PARAM_AK_SECRE = akSecret
        PlayParameter.PLAY_PARAM_SCU_TOKEN = token
        inRequest = false
        mIsTimeExpired = false
        val vidSts = VidSts()
        vidSts.vid = PlayParameter.PLAY_PARAM_VID
        vidSts.region = PlayParameter.PLAY_PARAM_REGION
        vidSts.accessKeyId = PlayParameter.PLAY_PARAM_AK_ID
        vidSts.accessKeySecret = PlayParameter.PLAY_PARAM_AK_SECRE
        vidSts.securityToken = PlayParameter.PLAY_PARAM_SCU_TOKEN
        aliYunPlayer.setVidSts(vidSts)
    }

    /**
     * 因为鉴权过期,而去重新鉴权
     */
    private class RetryExpiredSts(activity: AliYunPlayVideoActivity) : OnStsResultListener {
        private val weakReference: WeakReference<AliYunPlayVideoActivity>
        override fun onSuccess(vid: String, akid: String, akSecret: String, token: String) {
            val activity = weakReference.get()
            activity?.onStsRetrySuccess(vid, akid, akSecret, token)
        }

        override fun onFail() {}

        init {
            weakReference = WeakReference(activity)
        }
    }

    /**
     * 鉴权过期
     */
    private fun onTimExpiredError() {
        VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, RetryExpiredSts(this))
    }

    private class MyShowMoreClickLisener internal constructor(activity: AliYunPlayVideoActivity) : ControlView.OnShowMoreClickListener {
        var weakReference: WeakReference<AliYunPlayVideoActivity>
        override fun showMore() {
            val activity = weakReference.get()
            if (activity != null) {
                val currentClickTime = System.currentTimeMillis()
                // 防止快速点击
                if (currentClickTime - activity.oldTime <= 1000) {
                    return
                }
                activity.oldTime = currentClickTime
                activity.showMore(activity)
                activity.requestVidSts()
            }
        }

        init {
            weakReference = WeakReference(activity)
        }
    }

    private fun showMore(activity: AliYunPlayVideoActivity) {
        showMoreDialog = AlivcShowMoreDialog(activity)
        val moreValue = AliyunShowMoreValue()
        moreValue.speed = aliYunPlayer.getCurrentSpeed()
        moreValue.volume = (aliYunPlayer.getCurrentVolume()).toInt()
        val showMoreView = MoreViewLayout(activity, moreValue)
        showMoreDialog!!.setContentView(showMoreView)
        showMoreDialog!!.show()
        showMoreView.setOnDownloadButtonClickListener(object : MoreViewLayout.OnDownloadButtonClickListener {
            override fun onDownloadClick() {
                Toast.makeText(activity, "点击了下载......", Toast.LENGTH_SHORT).show()
            }

        })
        showMoreView.setOnScreenCastButtonClickListener {
            Toast.makeText(activity, "功能正在开发中......", Toast.LENGTH_SHORT).show()
            //                TODO 2019年04月18日16:43:29  先屏蔽投屏功能
//                showMoreDialog.dismiss();
//                showScreenCastView();
        }
        showMoreView.setOnBarrageButtonClickListener {
            Toast.makeText(activity, "功能正在开发中......", Toast.LENGTH_SHORT).show()
            //                if (showMoreDialog != null && showMoreDialog.isShowing()) {
//                    showMoreDialog.dismiss();
//                }
        }
        showMoreView.setOnSpeedCheckedChangedListener { group, checkedId ->
            // 点击速度切换
            if (checkedId == R.id.rb_speed_normal) {
                aliYunPlayer.changeSpeed(SpeedValue.One)
            } else if (checkedId == R.id.rb_speed_onequartern) {
                aliYunPlayer.changeSpeed(SpeedValue.OneQuartern)
            } else if (checkedId == R.id.rb_speed_onehalf) {
                aliYunPlayer.changeSpeed(SpeedValue.OneHalf)
            } else if (checkedId == R.id.rb_speed_twice) {
                aliYunPlayer.changeSpeed(SpeedValue.Twice)
            }
        }
        /**
         * 初始化亮度
         */
        if (aliYunPlayer != null) {
            showMoreView.setBrightness(aliYunPlayer.getScreenBrightness())
        }
        // 亮度seek
        showMoreView.setOnLightSeekChangeListener(object : MoreViewLayout.OnLightSeekChangeListener {
            override fun onStart(seekBar: SeekBar) {}
            override fun onProgress(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                setWindowBrightness(progress)
                if (aliYunPlayer != null) {
                    aliYunPlayer.setScreenBrightness(progress)
                }
            }

            override fun onStop(seekBar: SeekBar) {}
        })
        /**
         * 初始化音量
         */
        if (aliYunPlayer != null) {
            showMoreView.setVoiceVolume(aliYunPlayer.getCurrentVolume())
        }
        showMoreView.setOnVoiceSeekChangeListener(object : MoreViewLayout.OnVoiceSeekChangeListener {
            override fun onStart(seekBar: SeekBar) {}
            override fun onProgress(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                aliYunPlayer.setCurrentVolume(progress / 100.00f)
            }

            override fun onStop(seekBar: SeekBar) {}
        })
    }

    private class MyPlayStateBtnClickListener internal constructor(activity: AliYunPlayVideoActivity) : OnPlayStateBtnClickListener {
        var weakReference: WeakReference<AliYunPlayVideoActivity>
        override fun onPlayBtnClick(playerState: Int) {
            val activity = weakReference.get()
            activity?.onPlayStateSwitch(playerState)
        }

        init {
            weakReference = WeakReference(activity)
        }
    }

    /**
     * 播放状态切换
     */
    private fun onPlayStateSwitch(playerState: Int) {
//        if (playerState == IPlayer.started) {
//            ToastUtils.showShort("IPlayer.started")
//        } else if (playerState == IPlayer.paused) {
//            ToastUtils.showShort("IPlayer.paused")
//        }
    }

    private class MySeekCompleteListener internal constructor(activity: AliYunPlayVideoActivity) : IPlayer.OnSeekCompleteListener {
        var weakReference: WeakReference<AliYunPlayVideoActivity>
        override fun onSeekComplete() {
            val activity = weakReference.get()
            activity?.onSeekComplete()
        }

        init {
            weakReference = WeakReference(activity)
        }
    }

    private fun onSeekComplete() {

    }

    private class MySeekStartListener internal constructor(activity: AliYunPlayVideoActivity) : OnSeekStartListener {
        var weakReference: WeakReference<AliYunPlayVideoActivity> = WeakReference(activity)
        override fun onSeekStart(position: Int) {
            val activity = weakReference.get()
            activity?.onSeekStart(position)
        }

    }

    private class MyOnFinishListener(activity: AliYunPlayVideoActivity) : OnFinishListener {
        var weakReference: WeakReference<AliYunPlayVideoActivity>
        override fun onFinishClick() {
            val AliYunPlayVideoActivity = weakReference.get()
            AliYunPlayVideoActivity?.finish()
        }

        init {
            weakReference = WeakReference(activity)
        }
    }

    /**
     * 设置屏幕亮度
     */
    fun setWindowBrightness(brightness: Int) {
        val window = window
        val lp = window.attributes
        lp.screenBrightness = brightness / 255.0f
        window.attributes = lp
    }

    private class MyOnScreenBrightnessListener(activity: AliYunPlayVideoActivity) : OnScreenBrightnessListener {
        private val weakReference: WeakReference<AliYunPlayVideoActivity>
        override fun onScreenBrightness(brightness: Int) {
            val aliyunPlayerSkinActivity = weakReference.get()
            if (aliyunPlayerSkinActivity != null) {
                aliyunPlayerSkinActivity.setWindowBrightness(brightness)
                if (aliyunPlayerSkinActivity.aliYunPlayer != null) {
                    aliyunPlayerSkinActivity.aliYunPlayer.screenBrightness = brightness
                }
            }
        }

        init {
            weakReference = WeakReference(activity)
        }
    }

    /**
     * 播放器出错监听
     */
    private class MyOnErrorListener(activity: AliYunPlayVideoActivity) : IPlayer.OnErrorListener {
        private val weakReference: WeakReference<AliYunPlayVideoActivity> = WeakReference(activity)
        override fun onError(errorInfo: com.aliyun.player.bean.ErrorInfo) {
            val aliyunPlayerSkinActivity = weakReference.get()
            aliyunPlayerSkinActivity?.onError(errorInfo)
        }

    }

    private class MyOnSeiDataListener(activity: AliYunPlayVideoActivity) : OnSeiDataListener {
        private val weakReference: WeakReference<AliYunPlayVideoActivity>
        override fun onSeiData(i: Int, bytes: ByteArray) {
            val activity = weakReference.get()
            val seiMessage = String(bytes)
            if (activity != null) {
                val log = SimpleDateFormat("HH:mm:ss.SS").format(Date()) + "SEI:type:" + i + ",content:" + seiMessage + "\n"
                TourCooLogUtil.i(activity.mTag, log)
            }
            Log.e("SEI:", "type:$i,content:$seiMessage")
        }

        init {
            weakReference = WeakReference(activity)
        }
    }

    private fun onError(errorInfo: com.aliyun.player.bean.ErrorInfo) { //鉴权过期
        if (errorInfo.code.value == ErrorCode.ERROR_SERVER_POP_UNKNOWN.value) {
            mIsTimeExpired = true
        }
    }

    private fun onSeekStart(position: Int) {
        TourCooLogUtil.i(mTag, "onSeekStart" + position)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event != null && event.keyCode == 67) {
            if (aliYunPlayer != null) {
                //删除按键监听,部分手机在EditText没有内容时,点击删除按钮会隐藏软键盘
                return false
            }
        }
        return super.dispatchKeyEvent(event)
    }


    private class MyStsListener internal constructor(act: AliYunPlayVideoActivity) : OnStsResultListener {
        private val weakActivity: WeakReference<AliYunPlayVideoActivity>
        override fun onSuccess(vid: String, akid: String, akSecret: String, token: String) {
            val activity = weakActivity.get()
            activity?.onStsSuccess(vid, akid, akSecret, token)
        }

        override fun onFail() {
            val activity = weakActivity.get()
            activity?.onStsFail()
        }

        init {
            weakActivity = WeakReference(act)
        }
    }


    private fun onStsSuccess(mVid: String, akid: String, akSecret: String, token: String) {
        PlayParameter.PLAY_PARAM_VID = mVid
        PlayParameter.PLAY_PARAM_AK_ID = akid
        PlayParameter.PLAY_PARAM_AK_SECRE = akSecret
        PlayParameter.PLAY_PARAM_SCU_TOKEN = token
        mIsTimeExpired = false
        inRequest = false
        // 视频列表数据为0时, 加载列表
        if (alivcVideoInfos != null && alivcVideoInfos!!.size == 0) {
            alivcVideoInfos!!.clear()
//            loadPlayList()
            ToastUtils.showShort("加载播放列表")
        }
    }


    private fun onStsFail() {
        FixedToastUtils.show(applicationContext, R.string.request_vidsts_fail)
        inRequest = false
        //finish();
    }


    /**
     * 请求sts
     */
    private fun requestVidSts() {
        Log.e("scar", "requestVidSts: ")
        if (inRequest) {
            return
        }
        inRequest = true
        if (TextUtils.isEmpty(PlayParameter.PLAY_PARAM_VID)) {
            PlayParameter.PLAY_PARAM_VID = DEFAULT_VID
        }
        Log.e("scar", "requestVidSts:xx ")
        VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, MyStsListener(this))
    }


    private fun updatePlayerViewMode() {
        if (aliYunPlayer != null) {
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                //转为竖屏了。
                setViewGone(mTitleBar, true)
//显示状态栏
//                if (!isStrangePhone()) {
//                    getSupportActionBar().show();
//                }
                this.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                aliYunPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE)
                //设置view的布局，宽高之类
                val aliVcVideoViewLayoutParams = aliYunPlayer
                        .getLayoutParams() as LinearLayout.LayoutParams
                aliVcVideoViewLayoutParams.height = (ScreenUtils.getWidth(this) * 9.0f / 16).toInt()
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //转到横屏了。
                //隐藏标题栏
                setViewGone(mTitleBar, false)
                //隐藏状态栏
                if (!isStrangePhone) {
                    this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    aliYunPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                }
                //设置view的布局，宽高
                val aliVcVideoViewLayoutParams = aliYunPlayer
                        .getLayoutParams() as LinearLayout.LayoutParams
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
    }


    /**
     * 获取阿里HLS解密参数并且播放视频
     */
    private fun requestHlsEncryptParamsAndPlay(course: Course) {
        ApiRepository.getInstance().requestHlsEncryptParams(trainingPlanID, course.videoID).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<HlsParams>?>() {
            override fun onSuccessNext(entity: BaseResult<HlsParams>?) {
                if (entity?.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    //播放视频 注意：这里不做非空 和加密判断了
                    //取出当前课程所有的视频流并拼接token解密
                    for (stream in course.streams) {
                        stream.url = CommonUtil.getNotNullValue(stream.url) + "?MtsHlsUriToken=" + CommonUtil.getNotNullValue(entity.data.token)
                    }
                    //设置当前播放的清晰度对象
                    course.currentVideoStream = course.streams[0]
                    val finalUrl = course.streams[0].url
                    //关键点 将当前视频清晰度对象赋值给播放器
                    aliYunPlayer.currentSteam = course.streams[0]
                    //播放hls加密视频
                    loadPlayerSetting(finalUrl)
                } else {
                    if (entity != null)
                        ToastUtil.showFailed(entity.msg)
                }
            }

        })
    }

    /**
     * 视频清晰度切换
     */
    private class MyChangeDefinitionListener internal constructor(activity: AliYunPlayVideoActivity) : OnChangeDefinitionListener {
        private val weakReference: WeakReference<AliYunPlayVideoActivity> = WeakReference(activity)
        override fun onChangeDefinition(finalSteam: VideoStream?, lastSeek: Long) {
            val weakAct = weakReference.get()
            weakAct?.onChangeDefinition(finalSteam, lastSeek)
        }

    }

    private fun onChangeDefinition(finalSteam: VideoStream?, lastSeek: Long) {
        //清晰度切换逻辑
        //关键点 拿到当前清晰度对象赋值给播放器
        aliYunPlayer.currentSteam = finalSteam
        loadPlayerSettingDefinitionAndSeek(finalSteam!!.url, lastSeek)
    }

    /**
     * 带定位
     */
    private fun loadPlayerSettingDefinitionAndSeek(url: String, seek: Long) {
        initAliYunPlayerView(url)
        showLoading("清晰度切换中...")
        aliYunPlayer.seekTo(seek.toInt())
    }


    /**
     * SQ，HQ，FD，LD，SD，HD，2K，4K，OD
     */
    private fun sortQuality(qualities: List<VideoStream>): List<VideoStream>? {
        var ld: VideoStream? = null
        var sd: VideoStream? = null
        var hd: VideoStream? = null
        var fd: VideoStream? = null
        var k2: VideoStream? = null
        var k4: VideoStream? = null
        var od: VideoStream? = null
        var sq: VideoStream? = null
        var hq: VideoStream? = null
        for (quality in qualities) {
            if (QualityValue.QUALITY_FLUENT == quality.definition) { //                fd = QualityValue.QUALITY_FLUENT;
                fd = quality
                fd.definition = "流畅"
            } else if (QualityValue.QUALITY_LOW == quality.definition) { //                ld = QualityValue.QUALITY_LOW;
                ld = quality
                ld.definition = "标清"
            } else if (QualityValue.QUALITY_STAND == quality.definition) { //                sd = QualityValue.QUALITY_STAND;
                sd = quality
                sd.definition = "高清"
            } else if (QualityValue.QUALITY_HIGH == quality.definition) { //                hd = QualityValue.QUALITY_HIGH;
                hd = quality
                hd.definition = "超清"
            } else if (QualityValue.QUALITY_2K == quality.definition) { //                k2 = QualityValue.QUALITY_2K;
                k2 = quality
            } else if (QualityValue.QUALITY_4K == quality.definition) { //                k4 = QualityValue.QUALITY_4K;
                k4 = quality
            } else if (QualityValue.QUALITY_ORIGINAL == quality.definition) { //                od = QualityValue.QUALITY_ORIGINAL;
                od = quality
            } else if (QualityValue.QUALITY_SQ == quality.definition) {
                sq = quality
            } else if (QualityValue.QUALITY_HQ == quality.definition) {
                hq = quality
            }
        }
        //清晰度按照fd,ld,sd,hd,2k,4k,od排序
        val sortedQuality: MutableList<VideoStream> = LinkedList()
        //        if (!TextUtils.isEmpty(fd)) {
//            sortedQuality.add(fd);
//        }
//
//        if (!TextUtils.isEmpty(ld)) {
//            sortedQuality.add(ld);
//        }
//        if (!TextUtils.isEmpty(sd)) {
//            sortedQuality.add(sd);
//        }
//        if (!TextUtils.isEmpty(hd)) {
//            sortedQuality.add(hd);
//        }
//
//        if (!TextUtils.isEmpty(k2)) {
//            sortedQuality.add(k2);
//        }
//        if (!TextUtils.isEmpty(k4)) {
//            sortedQuality.add(k4);
//        }
//        if (!TextUtils.isEmpty(od)) {
//            sortedQuality.add(od);
//        }
        if (sq != null) {
            sortedQuality.add(sq)
        }
        if (hq != null) {
            sortedQuality.add(hq)
        }
        if (fd != null) {
            sortedQuality.add(fd)
        }
        if (ld != null) {
            sortedQuality.add(ld)
        }
        if (sd != null) {
            sortedQuality.add(sd)
        }
        if (hd != null) {
            sortedQuality.add(hd)
        }
        if (k2 != null) {
            sortedQuality.add(k2)
        }
        if (k4 != null) {
            sortedQuality.add(k4)
        }
        if (od != null) {
            sortedQuality.add(od)
        }
        return sortedQuality
    }
}


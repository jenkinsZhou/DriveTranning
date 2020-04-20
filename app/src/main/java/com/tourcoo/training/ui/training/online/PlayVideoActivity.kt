package com.tourcoo.training.ui.training.online

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
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
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.training.Catalog
import com.tourcoo.training.entity.training.Course
import com.tourcoo.training.entity.training.VideoStream
import com.tourcoo.training.entity.training.TrainingPlanDetail
import com.tourcoo.training.widget.oldplayer.OnTransitionListener
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_play_video.*
import kotlinx.android.synthetic.main.item_training_plan_detail_content.*
import kotlin.collections.ArrayList

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年04月19日23:27
 * @Email: 971613168@qq.com
 */
class PlayVideoActivity : BaseTitleActivity() {
    private val mTag = "PlayVideoActivity"
    private var orientationUtils: OrientationUtils? = null

    private var isTransition = false

    private var transition: Transition? = null

    private var mCourseList: MutableList<Course>? = null

    private var mCourseHashMap : HashMap<Course,View>? = null

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
        init()
        mCourseList = ArrayList()
        mCourseHashMap = HashMap()
        requestPlanDetail()
    }

    private fun init() {
        //借用了jjdxm_ijkplayer的URL
        //借用了jjdxm_ijkplayer的URL
        val source1 = "http://cdn.course.ggjtaq.com/360/renmenjiaotongchubansheshuzikejian/weixianhuowudaoluyunshujiashiyuananquanjiaoyupeixunshuzikechengbanben/diyizhangjiashiyuandeshehuizerenyuzhiyedaode/diyijiedaoluyunshujiashiyuandezhiyetedian/jiaotongshigudeweihai/jiaotongshigudeweihai.mp4"
        val switchVideoModel = VideoStream()
        switchVideoModel.definitionDesc = "普通"
        switchVideoModel.url = source1
        //        String source2 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
        val source2 = "http://cdn.course.ggjtaq.com/1080/renmenjiaotongchubansheshuzikejian/weixianhuowudaoluyunshujiashiyuananquanjiaoyupeixunshuzikechengbanben/diyizhangjiashiyuandeshehuizerenyuzhiyedaode/diyijiedaoluyunshujiashiyuandezhiyetedian/jiaotongshigudeweihai/jiaotongshigudeweihai.mp4"
        val name2 = "清晰"
        val switchVideoModel2 = VideoStream()
        switchVideoModel2.url = source2
        switchVideoModel2.definitionDesc = name2
        val list: MutableList<VideoStream> = ArrayList()
        list.add(switchVideoModel)
        list.add(switchVideoModel2)
        smartVideoPlayer!!.setUp(list, false, "测试视频")
        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.drawable.img_training_free_video)
        smartVideoPlayer!!.thumbImageView = imageView
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
        PlayerFactory.setPlayManager(IjkPlayerManager::class.java)
        //设置返回按键功能
        smartVideoPlayer!!.backButton.setOnClickListener { onBackPressed() }
        //过渡动画
        initTransition()
    }

    private fun initTransition() {
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

    override fun onBackPressed() { //先返回正常状态
        if (orientationUtils!!.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            smartVideoPlayer!!.fullscreenButton.performClick()
            return
        }
        //释放所有
        smartVideoPlayer!!.setVideoAllCallBack(null)
        GSYVideoManager.releaseAllVideos()
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed()
        } else {
            Handler().postDelayed({
                finish()
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }, 500)
        }
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
        ApiRepository.getInstance().trainingPlanID("9044").compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<TrainingPlanDetail>>() {
            override fun onSuccessNext(entity: BaseResult<TrainingPlanDetail>?) {
                if (entity!!.code == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    parseTrainingPlanDetail(entity.data)
                }
            }
        })
    }


    private fun parseTrainingPlanDetail(detail: TrainingPlanDetail?) {
        if (detail == null || detail.subjects == null) {
            return
        }
        val subjects = detail.subjects
        for (subject in subjects) {
            val subjectView = LayoutInflater.from(mContext).inflate(R.layout.item_training_plan_detail_header, null)
            val tvSubjectTitle = subjectView.findViewById<TextView>(R.id.tvSubjectTitle)
            tvSubjectTitle.text = getNotNullValue(subject.name)
            //一级标题
            llPlanContentView.addView(subjectView)
            loadCatalogs(subject.catalogs)
        }
        parseTrainingStatus(mCourseList)

        for (index in 0 until mCourseList!!.size ){
            TourCooLogUtil.i("mTag", "item的状态："+mCourseList!![index].currentPlayStatus)
        }
        TourCooLogUtil.i("mTag", "数量："+mCourseList!!.size)
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
            TourCooLogUtil.i(mTag, "catalog = " + newCatalogs[index])
            val catalog = newCatalogs[index]
            val contentView = LayoutInflater.from(mContext).inflate(R.layout.item_training_plan_detail_content, null)
            val tvPlanTitle = contentView.findViewById<TextView>(R.id.tvPlanTitle)
            tvPlanTitle.text = getNotNullValue(catalog.name)
            llPlanContentView.addView(contentView)
            val layoutParams = contentView.layoutParams as LinearLayout.LayoutParams
            layoutParams.setMargins(SizeUtil.dp2px(catalog.level * 10f), 0, 0, 0)
            contentView.layoutParams = layoutParams
            if (catalog.catalogs != null) {
                newCatalogs.remove(catalog)
                catalogs.remove(catalog)
                loadCatalogs(catalog.catalogs)
            } else {
                addCourse(catalog.courses)
            }
            /*  if (catalog.courses != null) {
                  loadCourse(catalog.courses)
              } else {
                  loadCatalogs(catalogs)
              }*/

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
            val layoutParams = contentView.layoutParams as LinearLayout.LayoutParams
            layoutParams.setMargins(SizeUtil.dp2px(course.level * 10f), 0, 0, 0)
            contentView.layoutParams = layoutParams
            if (course.streams != null) {
                val tvPlanDesc = contentView.findViewById<TextView>(R.id.tvPlanDesc)
                //播放状态
                val ivCourseStatus = contentView.findViewById<ImageView>(R.id.ivCourseStatus)
                tvPlanDesc.text = getNotNullValue("00:00学习到01:12")
                tvPlanDesc.textSize = 12f
                setViewGone(tvPlanDesc, true)
                setViewGone(ivCourseStatus, true)
                showCourseStatus(ivCourseStatus, course.completed)
                //关键
                mCourseHashMap!!.put(course,contentView)
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


    private fun showCourseStatus(imageView: ImageView, status: Int) {
        when (status) {
            COURSE_STATUS_NO_COMPLETE -> {
                imageView.setImageResource(R.mipmap.ic_play_no_complete)
            }
            COURSE_STATUS_COMPLETE -> {
                imageView.setImageResource(R.mipmap.ic_play_finish)
            }
            COURSE_STATUS_PLAYING -> {
                imageView.setImageResource(R.mipmap.ic_playing)
            }
            else -> {
            }
        }

    }

    private fun parseTrainingStatus(list: MutableList<Course>?) {
        if (list == null) {
            return
        }
        for (index in 0 until list.size) {
            val course = list[index]
            if (course.completed <= 0) {
                course.currentPlayStatus = COURSE_STATUS_NO_COMPLETE
            } else {
                course.currentPlayStatus = COURSE_STATUS_PLAYING
                break
            }
        }
        for (index in 0 until list.size) {
            val course = list[index]
            if (course.currentPlayStatus != COURSE_STATUS_NO_COMPLETE && course.currentPlayStatus != COURSE_STATUS_PLAYING) {
                course.currentPlayStatus = COURSE_STATUS_COMPLETE
            }
        }
    }
}
package com.tourcoo.training.ui.training.online

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.Transition
import android.view.View
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.widget.player.OnTransitionListener
import com.tourcoo.training.widget.player.SmartPickVideo
import com.tourcoo.training.widget.player.SwitchVideoModel
import kotlinx.android.synthetic.main.activity_play_video.*
import java.util.*

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年03月11日23:27
 * @Email: 971613168@qq.com
 */
class PlayVideoActivity : BaseTitleActivity() {

    private var isPlay = false
    private var isPause = false

    companion object {
        const val IMG_TRANSITION = "IMG_TRANSITION"
        const val TRANSITION = "TRANSITION"
    }

    var videoPlayer: SmartPickVideo? = null

    var orientationUtils: OrientationUtils? = null

    private var isTransition = false

    private var transition: Transition? = null

    override fun getContentLayout(): Int {
        return R.layout.activity_play_video
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("线上学习")
    }

    override fun initView(savedInstanceState: Bundle?) {
        isTransition = intent.getBooleanExtra(TRANSITION, false)
        videoPlayer = findViewById(R.id.videoPlayer)
        init()
    }


    private fun init() {
        //借用了jjdxm_ijkplayer的URL
        val source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"
        val name = "普通"
        val switchVideoModel = SwitchVideoModel(name, source1)
        val source2 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4"
        val name2 = "清晰"
        val switchVideoModel2 = SwitchVideoModel(name2, source2)
        val list: MutableList<SwitchVideoModel> = ArrayList<SwitchVideoModel>()
        list.add(switchVideoModel)
        list.add(switchVideoModel2)
        videoPlayer!!.setUp(list, false, "测试视频")
        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.drawable.img_training_free_video)
        videoPlayer!!.thumbImageView = imageView
        //增加title
        videoPlayer!!.titleTextView.visibility = View.VISIBLE
        //设置返回键
        videoPlayer!!.backButton.visibility = View.VISIBLE
        //设置旋转
        orientationUtils = OrientationUtils(this, videoPlayer)
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer!!.fullscreenButton.setOnClickListener { orientationUtils!!.resolveByClick() }
        //是否可以滑动调整
        videoPlayer!!.setIsTouchWiget(true)
        //设置返回按键功能
        videoPlayer!!.backButton.setOnClickListener { onBackPressed() }
        //过渡动画
        initTransition()
    }


    private fun initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
            ViewCompat.setTransitionName(videoPlayer!!, IMG_TRANSITION)
            addTransitionListener()
            startPostponedEnterTransition()
        } else {
            videoPlayer!!.startPlayLogic()
        }
    }


    private fun addTransitionListener(): Boolean {
        transition = window.sharedElementEnterTransition
        if (transition != null) {
            transition!!.addListener(object : OnTransitionListener() {
                override fun onTransitionEnd(transition: Transition?) {
                    super.onTransitionEnd(transition)
                    super.onTransitionEnd(transition);
                    videoPlayer!!.startPlayLogic();
                    transition!!.removeListener(this);
                }
            })
            return true
        }
        return false
    }


    override fun onDestroy() {
        super.onDestroy()
        if (videoPlayer != null) {
            videoPlayer!!.release()
        }
        if (orientationUtils != null) orientationUtils!!.releaseListener()
    }

    override fun onBackPressed() { //先返回正常状态
        if (orientationUtils!!.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer!!.fullscreenButton.performClick()
            return
        }
        //释放所有
        videoPlayer!!.setVideoAllCallBack(null)
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
        videoPlayer!!.startWindowFullscreen(this, true, true)
    }
}
package com.tourcoo.training.ui.training.online

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.widget.neplayer.*

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年04月18日22:53
 * @Email: 971613168@qq.com
 */
class PlayVideoActivityNewOld1 : BaseTitleActivity()  , PlayerContract.PlayerUi{

    val EXTRA_URL = "extra_url"

    /**
     * 视频展示SurfaceView
     */
    private  var mVideoView: NEVideoView? = null

    private var mUrl // 拉流地址
            : String? = null
    /**
     * 播放控制器
     */
 private   var mediaPlayController: VideoPlayerController? = null

    /**
     * 直播状态控制View
     */
    private  var controlLayout: NEVideoControlLayout? = null

    override fun getContentLayout(): Int {
        return R.layout.activity_play_video_new
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        mVideoView = findViewById(R.id.mVideoView)
      /*  controlLayout = NEVideoControlLayout(this)
        initAudienceParam()
        intClickView()*/
        val controller = TestPlayControl (mVideoView)
        controller.reOpenVideo()
    }

    override fun onSeekComplete() {
    }

    override fun showLoading(show: Boolean) {
    }

    override fun setFileName(name: String?) {
    }

    override fun onBufferingUpdate() {
        ToastUtil.show("onBufferingUpdate")
    }

    override fun onCompletion(): Boolean {
        try {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.tip)
            builder.setMessage(R.string.video_finished)
            builder.setCancelable(false)
            builder.setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, whichButton -> finish() })
            builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onError(errorInfo: String?): Boolean {
        ToastUtil.showFailed(errorInfo)
        try {
            runOnUiThread {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@PlayVideoActivityNewOld1)
                builder.setTitle(R.string.tip)
                builder.setMessage(errorInfo)
                builder.setPositiveButton(R.string.ok
                ) { dialog, whichButton -> finish() }
                builder.setCancelable(false)
                builder.show()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

        }
        return true

    }

    override fun showAudioAnimate(b: Boolean) {
    }


    override fun onResume() {
        // 恢复播放
        if (mediaPlayController != null) {
            mediaPlayController!!.onActivityResume()
        }
        super.onResume()
    }

    override fun onPause() {
        //暂停播放
        if (mediaPlayController != null) {
            mediaPlayController!!.onActivityPause()
        }
        super.onPause()
    }

    override fun onDestroy() {
        // 释放资源
        if (mediaPlayController != null) {
            mediaPlayController!!.onActivityDestroy()
        }
        super.onDestroy()
    }


    private fun initAudienceParam() {
        val intent = intent
//        mUrl = intent.getStringExtra(EXTRA_URL)
        mUrl =    "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4"
        mediaPlayController = VideoPlayerController(this@PlayVideoActivityNewOld1, this, mVideoView, controlLayout, mUrl, VideoConstant.VIDEO_SCALING_MODE_NONE, false, true)
        mediaPlayController!!.initVideo()
    }


    private fun intClickView() {
//        mBackView.setOnClickListener(View.OnClickListener { finish() })
        controlLayout!!.setChangeVisibleListener(object : NEVideoControlLayout.ChangeVisibleListener {
            override fun onShown() {
                mVideoView!!.invalidate()
            }

            override fun onHidden() {
            }
        })
    }
}
package com.tourcoo.training.ui.face

import android.graphics.Point
import android.hardware.camera2.CameraDevice
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.ViewGroup
import android.view.WindowManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.widget.camera2.Camera2Helper
import com.tourcoo.training.widget.camera2.Camera2Listener
import kotlinx.android.synthetic.main.activity_face_recognition.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月03日14:52
 * @Email: 971613168@qq.com
 */
class FaceRecognitionActivity : BaseTitleActivity(), Camera2Listener {
    companion object {
        const val TAG = "FaceRecognitionActivity"
    }

    private var camera2Helper: Camera2Helper? = null
    //默认打开的CAMERA
    private val cameraId = Camera2Helper.CAMERA_ID_FRONT

    override fun getContentLayout(): Int {
        return R.layout.activity_face_recognition
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar!!.setTitleMainText("人脸识别")
    }

    override fun initView(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        ivFaceBackground.post {
            LogUtils.iTag("FaceRecognitionActivity", "ivFaceBackground:参数" + ivFaceBackground.width)
            LogUtils.iTag("FaceRecognitionActivity", "ivFaceBackground:参数" + ivFaceBackground.height)
            initCamera()
        }
    }

    override fun isStatusBarDarkMode(): Boolean {
        return true
    }


    private fun initCamera() {
        camera2Helper = Camera2Helper.Builder()
                .cameraListener(this)
                .specificCameraId(cameraId)
                .context(applicationContext)
                .previewOn(roundSurfaceView)
                .previewViewSize(Point(ivFaceBackground.layoutParams.width - SizeUtil.dp2px(2f), ivFaceBackground.layoutParams.height - SizeUtil.dp2px(2f)))
                .rotation(windowManager.defaultDisplay.rotation)
                .build()
        camera2Helper!!.start()
    }

    override fun onCameraError(e: Exception?) {
    }

    override fun onPreview(y: ByteArray?, u: ByteArray?, v: ByteArray?, previewSize: Size?, yRowStride: Int, uRowStride: Int, vRowStride: Int) {
    }

    override fun onCameraOpened(cameraDevice: CameraDevice?, cameraId: String?, previewSize: Size?, displayOrientation: Int, isMirror: Boolean) {
        ToastUtils.showShort("执行了")
        Log.i(TAG, "onCameraOpened:  previewSize = " + previewSize!!.width + "x" + previewSize.height)
        //在相机打开时，添加右上角的view用于显示原始数据和预览数据
        //在相机打开时，添加右上角的view用于显示原始数据和预览数据
        runOnUiThread(object : Runnable {
            override fun run() { //将预览控件和预览尺寸比例保持一致，避免拉伸
                run {
                    val layoutParams: ViewGroup.LayoutParams = roundSurfaceView!!.layoutParams
                    //横屏
                    if (displayOrientation % 180 == 0) {
                        layoutParams.height = layoutParams.width * previewSize.height / previewSize.width
                    } else {
                        layoutParams.height = layoutParams.width * previewSize.width / previewSize.height
                    }
                    roundSurfaceView!!.layoutParams = layoutParams
                }



            }
        })


    }

    override fun onCameraClosed() {
    }
}
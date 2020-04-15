package com.tourcoo.training.ui.face

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.hardware.Camera
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.tourcoo.training.R
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.widget.camera.CameraHelper
import com.tourcoo.training.widget.camera.CameraListener
import kotlinx.android.synthetic.main.activity_face_recognition.*
import java.io.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月04日15:08
 * @Email: 971613168@qq.com
 */
class DialogFaceRecognitionActivity : AppCompatActivity() , CameraListener, View.OnClickListener {
    companion object {
        const val tag = "FaceRecognitionActivity"
    }

    private val photoName = "test.jpg"
    private var cameraHelper: CameraHelper? = null
    private val cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_face_recognition)
        /*   val win: Window = window
           val lp = win.attributes
           lp.width = WindowManager.LayoutParams.WRAP_CONTENT
           lp.height = WindowManager.LayoutParams.WRAP_CONTENT
           lp.dimAmount = 0.2f
           win.attributes = lp*/
        window.setGravity(Gravity.CENTER)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        llTakePhoto.setOnClickListener(this)
        ivFaceBackground.post {
            LogUtils.iTag("FaceRecognitionActivity", "ivFaceBackground:参数" + ivFaceBackground.width)
            LogUtils.iTag("FaceRecognitionActivity", "ivFaceBackground:参数" + ivFaceBackground.height)
            resetTextureViewSize()
            initCamera()
        }
    }


    override fun onCameraError(e: Exception?) {
    }

    override fun onPreview(data: ByteArray?, camera: Camera?) {

    }

    override fun onCameraConfigurationChanged(cameraID: Int, displayOrientation: Int) {
    }

    override fun onCameraOpened(camera: Camera?, cameraId: Int, displayOrientation: Int, isMirror: Boolean) {
        val previewSize = camera!!.parameters.previewSize
        LogUtils.iTag(tag, "onCameraOpened:  previewSize = " + previewSize.width + "x" + previewSize.height)
    }

    override fun onCameraClosed() {

    }


    private fun initCamera() {
        cameraHelper = CameraHelper.Builder()
                .cameraListener(this)
                .specificCameraId(cameraId)
                .previewOn(roundSurfaceView)
                .previewViewSize(Point(ivFaceBackground.width, ivFaceBackground.width))
                .rotation(windowManager.defaultDisplay.rotation)
                .build()
        Log.i(tag, "预览宽高:" + roundSurfaceView.layoutParams.width)
        cameraHelper!!.start()
    }


    private fun resetTextureViewSize() {
        val layoutParams = roundSurfaceView.layoutParams
//        val proportion = ivFaceBackground.width.toFloat() / ivFaceBackground.height.toFloat()
        layoutParams.height = ivFaceBackground.height - SizeUtil.dp2px(15f)
        layoutParams.width = layoutParams.height
        roundSurfaceView.layoutParams = layoutParams
        roundSurfaceView.radius = SizeUtil.dp2px(25f)
        roundSurfaceView.invalidateRound()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.llTakePhoto -> {
                takePhoto()
            }
            else -> {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraHelper!!.release()
    }


    private fun takePhoto() {
        cameraHelper!!.takePhoto(object : Camera.PictureCallback {
            override fun onPictureTaken(data: ByteArray?, camera: Camera?) {
                if(!sdCardIsAvailable()){
                    ToastUtil.show("存储空间不足或异常")
                    return
                }
                val resource = BitmapFactory.decodeByteArray(data, 0, data!!.size)
                if (resource == null) {
                    ToastUtil.show("拍照失败")
                    return
                }
                val photoPath =com.tourcoo.training.core.util.FileUtil.getExternalStorageDirectory()+ File.separator+photoName
                saveImage(photoPath, toTurn(resource)!!)
                notifyMedia(photoPath)
            }
        })
    }

    private fun notifyMedia(filePath: String) {
        val localUri = Uri.fromFile(File(filePath))
        val localIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri)
        sendBroadcast(localIntent)
    }

    /**
     * 将Bitmap转成本地图片
     * @param path 保存为本地图片的地址
     * @param bitmap 要转化的Bitmap
     */
    private fun saveImage(path: String?, bitmap: Bitmap) {
        try {
            val bos = BufferedOutputStream(FileOutputStream(path))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun sdCardIsAvailable(): Boolean { //首先判断外部存储是否可用
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val sd = File(getExternalFilesDir(null)!!.path)
            sd.canWrite()
        } else {
            false
        }
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    fun readPictureDegree(path: String?): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }
    private  fun toTurn(img: Bitmap): Bitmap? {
        var img = img
        val matrix = Matrix()
        matrix.postRotate(270f) /*翻转90度*/
        val width = img.width
        val height = img.height
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true)
        return img
    }
}
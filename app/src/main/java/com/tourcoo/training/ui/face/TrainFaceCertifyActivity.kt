package com.tourcoo.training.ui.face

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.hardware.Camera
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.tourcoo.training.R
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant.*
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.Base64Util
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.entity.recognize.FaceRecognizeResult
import com.tourcoo.training.entity.training.QrScanResult
import com.tourcoo.training.ui.account.register.RecognizeIdCardActivity
import com.tourcoo.training.utils.threadpool.ThreadPoolManager
import com.tourcoo.training.widget.camera.CameraHelper
import com.tourcoo.training.widget.camera.CameraListener
import com.tourcoo.training.widget.dialog.IosAlertDialog
import com.tourcoo.training.widget.locate.LocateHelper
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_face_recognition.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.*
import java.util.*


/**
 *@description :培训相关人脸识别认证
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月03日14:52
 * @Email: 971613168@qq.com
 */
class TrainFaceCertifyActivity : BaseTitleActivity(), CameraListener, View.OnClickListener, EasyPermissions.PermissionCallbacks {
    companion object {
        const val tag = "FaceRecognitionActivity"
        const val EXTRA_FACE_IMAGE_PATH = "EXTRA_FACE_IMAGE_PATH"
    }

    private var mapLocation: AMapLocation? = null
    private var scanResult: QrScanResult? = null
    private val photoName = "face.jpeg"
    private var trainId = ""
    private var cameraHelper: CameraHelper? = null
    private val cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT
    override fun getContentLayout(): Int {
        return R.layout.activity_face_recognition
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar!!.setTitleMainText("人脸识别")
    }

    override fun initView(savedInstanceState: Bundle?) {
        trainId = intent.getStringExtra(EXTRA_TRAINING_PLAN_ID) as String
        scanResult = intent.getParcelableExtra(EXTRA_KEY_QR_SCAN_RESULT)
        if (scanResult == null) {
            ToastUtil.show("未获取对应信息")
            finish()
            return
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        llTakePhoto.setOnClickListener(this)
        loadCamera()
    }

    override fun isStatusBarDarkMode(): Boolean {
        return true
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
        LocateHelper.getInstance().destroyLocationInstance()
        if (cameraHelper != null) {
            cameraHelper!!.release()
        }

    }


    private fun takePhoto() {
        if (cameraHelper == null) {
            return
        }
        cameraHelper!!.takePhoto(object : Camera.PictureCallback {
            override fun onPictureTaken(data: ByteArray?, camera: Camera?) {
                if (!sdCardIsAvailable()) {
                    ToastUtil.show("存储空间不足或异常")
                    return
                }
                try {
                    baseHandler.postDelayed(Runnable {
                        showLoading("图片处理中...")
                        ThreadPoolManager.getThreadPoolProxyNormal().execute(Runnable {
                            val resource = BitmapFactory.decodeByteArray(data, 0, data!!.size)
                            if (resource == null) {
                                baseHandler.post {
                                    ToastUtil.show("图片处理失败")
                                    closeLoading()
                                }
                            } else {
                                val faceBitmap = toTurn(resource)!!
                                val compressBitmap = ImageUtils.compressBySampleSize(faceBitmap, SizeUtil.dp2px(235f), SizeUtil.dp2px(235f))
                                TourCooLogUtil.i(tag, "长宽信息:" + compressBitmap.width + "---" + compressBitmap.height)
                                val faceBase64Data = Base64Util.bitmapToBase64(compressBitmap)
                                //缓存人脸数据
                                AccountTempHelper.getInstance().tempBase64FaceData = faceBase64Data
                                handleTakePhotoCallback(faceBase64Data)
                                faceBitmap.recycle()
                                baseHandler.post(Runnable {
                                    closeLoading()
                                    getLocateAndCertify(faceBase64Data)
                                })
                            }
                        })


                    }, 200)
                } catch (e: Exception) {
                    e.printStackTrace()
                    closeLoading()
                    ToastUtil.show("拍照失败 请稍后再试")
                }


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
        if (TextUtils.isEmpty(path)) {
            return
        }
        try {
            val bos = BufferedOutputStream(FileOutputStream(path))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos)
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

    private fun toTurn(img: Bitmap): Bitmap? {
        var img = img
        val matrix = Matrix()
        matrix.postRotate(270f) /*翻转90度*/
        val width = img.width
        val height = img.height
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true)
        return img
    }

    /**
     * 检查权限
     */
    @AfterPermissionGranted(RecognizeIdCardActivity.REQUEST_PERMISSION)
    private fun loadCamera() {
        baseHandler.postDelayed({
            if (!EasyPermissions.hasPermissions(this@TrainFaceCertifyActivity, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                EasyPermissions.requestPermissions(this@TrainFaceCertifyActivity, "请授予拍照相关权限", RecognizeIdCardActivity.REQUEST_PERMISSION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                //拥有权限
                ivFaceBackground.post {
                    LogUtils.iTag("FaceRecognitionActivity", "ivFaceBackground:参数" + ivFaceBackground.width)
                    LogUtils.iTag("FaceRecognitionActivity", "ivFaceBackground:参数" + ivFaceBackground.height)
                    resetTextureViewSize()
                    initCamera()
                }
            }
        }, 500)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //将结果传入EasyPermissions中
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) { //这个方法有个前提是，用户点击了“不再询问”后，才判断权限没有被获取到
            baseHandler.postDelayed({ showSetting() }, 10)
        } else if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //这里响应的是除了AppSettingsDialog这个弹出框，剩下的两个弹出框被拒绝或者取消的效果
            ToastUtil.show("您未授予相关权限")
            finish()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        initCamera()
    }

    private fun showSetting() {
        IosAlertDialog(mContext)
                .init()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setTitle("权限申请")
                .setMsg("应用需要您授予相关权限 请前往授权管理页面授权")
                .setPositiveButton("前往授权", View.OnClickListener {
                    skipDetailSettingIntent()
                })
                .setNegativeButton("取消", View.OnClickListener {
                    //                    exitApp()

                }).show()
    }


    private fun handleRecognizeSuccessCallback() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun handleRecognizeFailCallback() {
        finish()
    }


    private fun uploadFaceImage(base64Data: String?, location: AMapLocation?) {
        if (location == null || TextUtils.isEmpty(base64Data)) {
            ToastUtil.show("未获取到有效数据")
            return
        }

        val params: MutableMap<String, Any> = HashMap(6)
        val base64Image = "data:image/jpeg;base64,$base64Data"
        params["photo"] = base64Image
        params["trainingPlanID"] = scanResult!!.trainingPlanID
        params["eventId"] = scanResult!!.eventId
        params["scene"] = scanResult!!.scene
        params["lng"] = location.longitude
        params["lat"] = location.latitude
//        TourCooLogUtil.i("上传的参数", params)
        ApiRepository.getInstance().requestTrainFaceVerify(params).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<FaceRecognizeResult>?>("比对中,请稍后...") {
            override fun onSuccessNext(entity: BaseResult<FaceRecognizeResult>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    handleRecognizeSuccessCallback()
                } else {
                    ToastUtil.show(entity.msg)
                    handleRecognizeFailCallback()
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                ToastUtil.show("验证超时")
                baseHandler.postDelayed(Runnable {
                    finish()
                }, 500)
            }
        })
    }


    private fun skipDetailSettingIntent() {
        val intent = Intent()
        //        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.action = Intent.ACTION_VIEW
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            intent.putExtra("com.android.settings.ApplicationPkgName", packageName)
        }
        try {
            startActivityForResult(intent, RecognizeIdCardActivity.REQUEST_PERMISSION)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getLocateAndCertify(base64Data: String?) {
        if (!EasyPermissions.hasPermissions(this@TrainFaceCertifyActivity, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ToastUtil.show("请先授予定位权限")
            return
        }
        baseHandler.post {
            showLoading("定位中...")
        }
        LocateHelper.getInstance().startLocation(AMapLocationListener { aMapLocation ->
            if (aMapLocation != null) {
                mapLocation = aMapLocation
                if (aMapLocation.errorCode == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    //定位成功 上传人脸数据到服务器
                    baseHandler.post {
                        uploadFaceImage(base64Data, aMapLocation)
                    }
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    TourCooLogUtil.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                    handleLocateFailed()
                    if(AppConfig.DEBUG_MODE){
                        val string = "定位失败：原因--->location Error, ErrCode:"+ aMapLocation.getErrorCode() + ", errInfo:"+ aMapLocation.getErrorInfo()
                        ToastUtil.show(string)
                    }
                }
            } else {
                //aMapLocation ==null 处理定位失败
                TourCooLogUtil.e("AmapError", "aMapLocation=null")
                handleLocateFailed()
                if(AppConfig.DEBUG_MODE){
                    val string = "定位失败：原因--->aMapLocation=null"
                    ToastUtil.show(string)
                }
            }
            LocateHelper.getInstance().stopLocation()
            baseHandler.post {
             closeLoading()
            }
        })
    }


    private fun handleTakePhotoCallback(base64Data: String?) {
        //获取经纬度 然后上传图片
        baseHandler.post {
            getLocateAndCertify(base64Data)
        }

    }

    /**
     * 处理定位失败逻辑
     */
    private fun handleLocateFailed() {
        baseHandler.post {
            ToastUtil.show("获取位置信息失败 请重试")
            finish()
        }
    }
}

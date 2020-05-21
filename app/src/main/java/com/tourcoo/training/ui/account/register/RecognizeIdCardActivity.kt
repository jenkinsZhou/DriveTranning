package com.tourcoo.training.ui.account.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.ImageUtils
import com.kaopiz.kprogresshud.KProgressHUD
import com.tourcoo.training.R
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.manager.GlideManager
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.UploadProgressBody
import com.tourcoo.training.core.retrofit.UploadRequestListener
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.Base64Util
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.IdCardInfo
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.entity.recognize.FaceRecognizeResult
import com.tourcoo.training.ui.account.LoginActivity.Companion.EXTRA_KEY_REGISTER_TYPE
import com.tourcoo.training.ui.account.LoginActivity.Companion.EXTRA_TYPE_RECOGNIZE_COMPARE
import com.tourcoo.training.widget.dialog.IosAlertDialog
import com.tourcoo.training.widget.idcardcamera.camera.IDCardCamera
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_upload_id_card.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.ref.WeakReference

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月13日10:05
 * @Email: 971613168@qq.com
 */
class RecognizeIdCardActivity : BaseTitleActivity(), View.OnClickListener, PermissionCallbacks {
    private var photoPath = ""
    private var type: Int = -1
//    private var trainId:String? = null
    private val mTag = "RecognizeIdCardActivity"
    private var hud: KProgressHUD? = null
    private val mHandler: MyHandler = MyHandler(this)
    private var message: Message? = null

    /**
     * 随便赋值的一个唯一标识码
     */
    companion object {
        const val REQUEST_PERMISSION = 100
        const val EXTRA_PHOTO_PATH = "EXTRA_PHOTO_PATH"
        const val MSG_CODE_PROGRESS = 1
        const val MSG_CODE_CLOSE_PROGRESS = 201
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_upload_id_card
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("身份证上传")
    }

    override fun initView(savedInstanceState: Bundle?) {
        type = intent.getIntExtra(EXTRA_KEY_REGISTER_TYPE, -1)
        TourCooLogUtil.i(mTag, "跳转类型=" + type)
        tvNextStep.setOnClickListener(this)
        llTakePhoto.setOnClickListener(this)
        ivSelectedImage.setOnClickListener(this)
        GlideManager.loadImageAuto(R.drawable.img_front, ivUploadBackground)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvNextStep -> {
                handleCallback(photoPath)
            }
            R.id.ivSelectedImage -> {
                doTakePhoto()
            }
            R.id.llTakePhoto -> {
                doTakePhoto()
            }
            else -> {
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == IDCardCamera.RESULT_CODE) {
            //获取图片路径，显示图片
            val path = IDCardCamera.getImagePath(data)
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) {
                    //身份证正面
                    setViewGone(ivSelectedImage, true)
                    photoPath = path
                    ivSelectedImage.setImageBitmap(BitmapFactory.decodeFile(path))
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {
                    //身份证反面
                    photoPath = path
                    setViewGone(ivSelectedImage, true)
                    ivSelectedImage.setImageBitmap(BitmapFactory.decodeFile(path))
                }
            }
        }
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
            startActivityForResult(intent, REQUEST_PERMISSION)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 检查权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSION)
    private fun doTakePhoto() {
        baseHandler.postDelayed({
            if (!EasyPermissions.hasPermissions(this@RecognizeIdCardActivity, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                EasyPermissions.requestPermissions(this@RecognizeIdCardActivity, "请授予拍照相关权限", REQUEST_PERMISSION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                //拥有权限
                skipCamera()
            }
        }, 10)
    }

    private fun skipCamera() {
        IDCardCamera.create(this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT)
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
            finish()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        skipCamera()
    }

    private fun initProgressDialog() {
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setCancellable(false)
                .setAutoDismiss(false)
                .setMaxProgress(100)
    }

    private fun handleCallback(imagePath: String) {
        if (TextUtils.isEmpty(imagePath)) {
            ToastUtil.show("请先上传身份证照片")
            return
        }
        if (AccountTempHelper.getInstance().recognizeType == EXTRA_TYPE_RECOGNIZE_COMPARE) { //身份证比对
           val idCardBitmap =  BitmapFactory.decodeFile(imagePath)
            val  compressBitmap  =      ImageUtils.compressBySampleSize(idCardBitmap, SizeUtil.dp2px(235f), SizeUtil.dp2px(235f))
           val idCardBase64 =  Base64Util.bitmapToBase64(compressBitmap)
            uploadFaceImage(idCardBase64, AccountTempHelper.getInstance().tempBase64FaceData)
        } else {
            uploadImage(imagePath)
        }
    }

    private fun showHudProgressDialog() {
        if (hud != null) {
            hud!!.setProgress(0)
        } else {
            initProgressDialog()
        }
        hud!!.show()
    }

    /**
     * 上传图片
     *
     * @param path
     */
    private fun uploadImage(path: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
        //注意，file是后台约定的参数，如果是多图，files，如果是单张图片，file就行
        //这里上传的是多图
        var file: File = File(path)
        builder.addFormDataPart("file", file.name, RequestBody.create(MediaType.parse("image/*"), file))
        val requestBody: RequestBody = builder.build()
        val uploadProgressBody = UploadProgressBody(requestBody, object : UploadRequestListener {
            @Override
            override fun onProgress(progress: Float, current: Long, total: Long) {
                message = mHandler.obtainMessage()
                message!!.arg1 = (progress * 100).toInt()
                message!!.what = MSG_CODE_PROGRESS
                mHandler.sendMessage(message!!)
                if (message!!.arg1 == 100) {
                    mHandler.sendEmptyMessage(MSG_CODE_CLOSE_PROGRESS)
                }
            }

            override fun onFail(e: Throwable) {
                TourCooLogUtil.e("异常：$e")
                closeHudProgressDialog()
            }
        })
        showHudProgressDialog()
        ApiRepository.getInstance().apiService.requestIdCardRecognition(uploadProgressBody).enqueue(object : Callback<BaseResult<IdCardInfo>?> {
            override fun onResponse(call: Call<BaseResult<IdCardInfo>?>, response: Response<BaseResult<IdCardInfo>?>) {
                val resp: BaseResult<IdCardInfo>? = response.body()
                if (resp != null && resp.data != null) {
                    ToastUtil.showSuccess("识别成功")
                    handleRecognizeSuccess(resp.data)
                } else {
                    ToastUtil.showFailed("未能识别")
                }
                closeHudProgressDialog()
                closeLoading()
            }

            override fun onFailure(call: Call<BaseResult<IdCardInfo>?>, t: Throwable) {
                if (AppConfig.DEBUG_MODE) {
                    ToastUtil.showFailed("回调失败:$t")
                } else {
                    ToastUtil.show("未能识别")
                }
                closeHudProgressDialog()
                closeLoading()
            }
        })
    }

    private fun updateProgress(progress: Int) {
        if (hud != null) {
            hud!!.setProgress(progress)
        }
    }

    private fun closeHudProgressDialog() {
        if (hud != null && hud!!.isShowing) {
            hud!!.setProgress(0)
            hud!!.dismiss()
        }
        hud = null
    }


    private class MyHandler internal constructor(dataActivity: RecognizeIdCardActivity) : Handler() {
        var personalDataActivity: WeakReference<RecognizeIdCardActivity> = WeakReference<RecognizeIdCardActivity>(dataActivity)
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_CODE_PROGRESS -> personalDataActivity.get()!!.updateProgress(msg.arg1)
                MSG_CODE_CLOSE_PROGRESS -> {
                    personalDataActivity.get()!!.closeHudProgressDialog()
                    personalDataActivity.get()!!.showLoading("识别中,请稍后...")
                }
                else -> {
                }
            }
        }
    }

    /**
     * 服务器识别成功回调
     */
    private fun handleRecognizeSuccess(idCardInfo: IdCardInfo?) {
        if (idCardInfo == null) {
            return
        }
        AccountTempHelper.getInstance().idCardInfo = idCardInfo
        skipResultInfo()
    }


    private fun skipResultInfo() {
        if (TextUtils.isEmpty(photoPath)) {
            ToastUtil.show("请先上传身份证正面照")
            return
        }
        AccountTempHelper.getInstance().isRecognizeIdCard = true
        val intent = Intent(this, RecognizeResultActivity::class.java)
        intent.putExtra(EXTRA_PHOTO_PATH, photoPath)
        startActivity(intent)
    }

    /**
     * 人脸和身份证比对
     */
    private fun uploadFaceImage(base64DataIdCard: String?, facePhotoBase64: String) {
//        || TextUtils.isEmpty(trainId)
        if (TextUtils.isEmpty(base64DataIdCard) || TextUtils.isEmpty(facePhotoBase64) ) {
            ToastUtil.show("未获取到图像信息")
//            TourCooLogUtil.d("未获取到图像信息="+trainId)
            return
        }
        //身份证base64数据
        val base64Image = "data:image/jpeg;base64,$base64DataIdCard"
        //人脸base64数据
        val finalFaceBase64 = "data:image/jpeg;base64,$facePhotoBase64"
//        val base64FaceImage = "data:image/jpeg;base64," + Base64Util.bitmapToBase64(BitmapFactory.decodeFile(facePhotoPath))
        ApiRepository.getInstance().requestIdCardVerify( base64Image, finalFaceBase64).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<FaceRecognizeResult>?>("比对中,请稍后...") {
            override fun onSuccessNext(entity: BaseResult<FaceRecognizeResult>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    handleRecognizeSuccessCallback()
                } else {
                    ToastUtil.show(entity.msg)
                    finish()
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                ToastUtil.show("验证超时")
                baseHandler.postDelayed(Runnable {
                    finish()
                },500)
            }
        })
    }

    private fun handleRecognizeSuccessCallback() {
        val intent = Intent()
//        intent.putExtra(EXTRA_FACE_IMAGE_PATH,faceImagePath)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


}
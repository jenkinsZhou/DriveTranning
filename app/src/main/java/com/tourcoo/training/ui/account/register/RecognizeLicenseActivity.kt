package com.tourcoo.training.ui.account.register

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import com.kaopiz.kprogresshud.KProgressHUD
import com.tourcoo.training.R
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.UploadProgressBody
import com.tourcoo.training.core.retrofit.UploadRequestListener
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.entity.account.register.BusinessLicenseInfo
import com.tourcoo.training.widget.dialog.IosAlertDialog
import com.tourcoo.training.widget.idcardcamera.camera.LicenseCameraActivity
import kotlinx.android.synthetic.main.activity_upload_business_license.*
import kotlinx.android.synthetic.main.activity_upload_business_license.llTakePhoto
import kotlinx.android.synthetic.main.activity_upload_business_license.tvNextStep
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.ref.WeakReference

/**
 *@description :营业执照识别
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月08日16:14
 * @Email: 971613168@qq.com
 */
class RecognizeLicenseActivity : BaseTitleActivity(), View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private var photoPath = ""
    private var type: Int = -1
    private val mTag = "RecognizeIdCardActivity"
    private var hud: KProgressHUD? = null
    private val mHandler: MyHandler = MyHandler(this)
    private var message: Message? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_upload_business_license
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("营业执照上传")
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvNextStep.setOnClickListener(this)
        llTakePhoto.setOnClickListener(this)
        ivSelectedLicense.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivSelectedLicense -> {
                doTakePhoto()
            }
            R.id.ivSelectedImage -> {
                doTakePhoto()
            }
            R.id.tvNextStep -> {
                handleCallback(photoPath)
            }
            R.id.llTakePhoto -> {
                doTakePhoto()
            }
        }
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
//        skipCamera()
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
            startActivityForResult(intent, RecognizeIdCardActivity.REQUEST_PERMISSION)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 拍摄证件照片
     *
     * @param type 拍摄证件类型
     */
    private fun takePhoto(type: Int) {
        LicenseCameraActivity.openCertificateCamera(this, type)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //将结果传入EasyPermissions中
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 检查权限
     */
    @AfterPermissionGranted(RecognizeIdCardActivity.REQUEST_PERMISSION)
    private fun doTakePhoto() {
        baseHandler.postDelayed({
            if (!EasyPermissions.hasPermissions(this@RecognizeLicenseActivity, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                EasyPermissions.requestPermissions(this@RecognizeLicenseActivity, "请授予拍照相关权限", RecognizeIdCardActivity.REQUEST_PERMISSION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                //拥有权限
                skipCamera()
            }
        }, 10)
    }

    private fun skipCamera() {
        takePhoto(LicenseCameraActivity.TYPE_COMPANY_LANDSCAPE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == LicenseCameraActivity.REQUEST_CODE && resultCode == LicenseCameraActivity.RESULT_CODE) {
        //获取图片路径，显示图片
        val path = LicenseCameraActivity.getResult(data)
        if (!TextUtils.isEmpty(path)) {
            //身份证正面
            setViewGone(ivSelectedLicense, true)
            photoPath = path
            ivSelectedLicense.setImageBitmap(BitmapFactory.decodeFile(path))
        }
//        }
    }

    private class MyHandler internal constructor(dataActivity: RecognizeLicenseActivity) : Handler() {
        var personalDataActivity: WeakReference<RecognizeLicenseActivity> = WeakReference<RecognizeLicenseActivity>(dataActivity)
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                RecognizeIdCardActivity.MSG_CODE_PROGRESS -> personalDataActivity.get()!!.updateProgress(msg.arg1)
                RecognizeIdCardActivity.MSG_CODE_CLOSE_PROGRESS -> {
                    personalDataActivity.get()!!.closeHudProgressDialog()
                    personalDataActivity.get()!!.showLoading("识别中,请稍后...")
                }
                else -> {
                }
            }
        }
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


    private fun initProgressDialog() {
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setCancellable(false)
                .setAutoDismiss(false)
                .setMaxProgress(100)
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
                message!!.what = RecognizeIdCardActivity.MSG_CODE_PROGRESS
                mHandler.sendMessage(message!!)
                if (message!!.arg1 == 100) {
                    mHandler.sendEmptyMessage(RecognizeIdCardActivity.MSG_CODE_CLOSE_PROGRESS)
                }
            }

            override fun onFail(e: Throwable) {
                TourCooLogUtil.e("异常：$e")
                closeHudProgressDialog()
            }
        })
        showHudProgressDialog()
        ApiRepository.getInstance().apiService.requestBusinessRecognition(uploadProgressBody).enqueue(object : Callback<BaseResult<BusinessLicenseInfo>?> {
            override fun onResponse(call: Call<BaseResult<BusinessLicenseInfo>?>, response: Response<BaseResult<BusinessLicenseInfo>?>) {
                val resp: BaseResult<BusinessLicenseInfo>? = response.body()
                if (resp != null && resp.data != null) {
                    ToastUtil.showSuccess("识别成功")
                    handleRecognizeSuccess(resp.data)
                } else {
                    ToastUtil.showFailed("未能识别")
                }
                closeHudProgressDialog()
                closeLoading()
            }

            override fun onFailure(call: Call<BaseResult<BusinessLicenseInfo>?>, t: Throwable) {
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

    private fun showHudProgressDialog() {
        if (hud != null) {
            hud!!.setProgress(0)
        } else {
            initProgressDialog()
        }
        hud!!.show()
    }


    /**
     * 服务器识别成功回调
     */
    private fun handleRecognizeSuccess(info: BusinessLicenseInfo?) {
        if (info == null) {
            return
        }
        AccountTempHelper.getInstance().businessLicenseInfo = info
        ToastUtil.showSuccess("识别成功")
        skipResultInfo()
    }


    private fun handleCallback(imagePath: String) {
        if (TextUtils.isEmpty(imagePath)) {
            ToastUtil.show("请先上传营业执照")
            return
        }
        uploadImage(imagePath)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun skipResultInfo(){
        if(TextUtils.isEmpty(photoPath)){
            ToastUtil.show("请先上传身份证正面照")
            return
        }
        AccountTempHelper.getInstance().isRecognizeIdCard = false
        val intent = Intent(this, RecognizeResultActivity::class.java)
        intent.putExtra(RecognizeIdCardActivity.EXTRA_PHOTO_PATH, photoPath)
        startActivity(intent)
    }
}


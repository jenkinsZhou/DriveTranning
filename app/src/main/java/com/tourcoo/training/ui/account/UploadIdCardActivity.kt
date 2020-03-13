package com.tourcoo.training.ui.account

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.manager.GlideManager
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.widget.dialog.IosAlertDialog
import com.tourcoo.training.widget.idcardcamera.camera.IDCardCamera
import kotlinx.android.synthetic.main.activity_upload_id_card.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月13日10:05
 * @Email: 971613168@qq.com
 */
class UploadIdCardActivity : BaseTitleActivity(), View.OnClickListener, PermissionCallbacks {
    private var photoPath = ""

    /**
     * 随便赋值的一个唯一标识码
     */
    companion object {
        const val REQUEST_PERMISSION = 100
        const val EXTRA_PHOTO_PATH = "EXTRA_PHOTO_PATH"
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_upload_id_card
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("身份证上传")
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvNextStep.setOnClickListener(this)
        llTakePhoto.setOnClickListener(this)
        ivSelectedImage.setOnClickListener(this)
        GlideManager.loadImageAuto(R.drawable.img_front, ivUploadBackground)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvNextStep -> {
                val intent = Intent(this, IdCardInfoActivity::class.java)
                intent.putExtra(EXTRA_PHOTO_PATH, photoPath)
                startActivity(intent)
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
            if (!EasyPermissions.hasPermissions(this@UploadIdCardActivity, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                EasyPermissions.requestPermissions(this@UploadIdCardActivity, "请授予拍照相关权限", REQUEST_PERMISSION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
}
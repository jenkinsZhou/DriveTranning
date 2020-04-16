package com.tourcoo.training.ui.account.register

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.Base64Util
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.entity.recognize.FaceRecognizeResult
import com.tourcoo.training.ui.account.LoginActivity
import com.tourcoo.training.ui.account.LoginActivity.Companion.EXTRA_REGISTER_TYPE_DRIVER
import com.tourcoo.training.ui.account.LoginActivity.Companion.EXTRA_REGISTER_TYPE_INDUSTRY
import com.tourcoo.training.ui.account.LoginActivity.Companion.EXTRA_TYPE_RECOGNIZE_COMPARE
import com.tourcoo.training.ui.account.register.RecognizeIdCardActivity.Companion.EXTRA_PHOTO_PATH
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_recognize_result.*
import kotlinx.android.synthetic.main.activity_upload_id_card.ivSelectedImage
import kotlinx.android.synthetic.main.activity_upload_id_card.tvNextStep

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月13日16:18
 * @Email: 971613168@qq.com
 */
class RecognizeResultActivity : BaseTitleActivity(), View.OnClickListener {
    private var photoPath: String? = null
    private var mBitmap: Bitmap? = null
    private val isRecognizeId = AccountTempHelper.getInstance().isRecognizeIdCard
    override fun getContentLayout(): Int {
        return R.layout.activity_recognize_result
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        if (isRecognizeId) {
            titleBar?.setTitleMainText("身份证上传")
        } else {
            titleBar?.setTitleMainText("营业执照上传")
        }

    }

    override fun initView(savedInstanceState: Bundle?) {
        photoPath = intent.getStringExtra(EXTRA_PHOTO_PATH)
        tvNextStep.setOnClickListener(this)
        if (TextUtils.isEmpty(photoPath)) {
            ToastUtil.show("未获取到身份证图片")
            finish()
            return
        }
        mBitmap = BitmapFactory.decodeFile(photoPath)
        ivSelectedImage.setImageBitmap(mBitmap)
        showResultInfo()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvNextStep -> {
                handleRecognizeResultByType()
            }
            else -> {
            }
        }
    }

    private fun showResultInfo() {
        if (isRecognizeId) {
            if (AccountTempHelper.getInstance().idCardInfo != null) {
                tvName.text = AccountTempHelper.getInstance().idCardInfo.name
                tvIdCard.text = AccountTempHelper.getInstance().idCardInfo.idCard
            }
        } else {
            if (AccountTempHelper.getInstance().businessLicenseInfo != null) {
                tvName.text = AccountTempHelper.getInstance().businessLicenseInfo.name
                tvIdCard.text = AccountTempHelper.getInstance().businessLicenseInfo.creditCode
            }
        }

    }

    private fun handleRecognizeResultByType() {
        /*  val intent = if (AccountTempHelper.getInstance().recognizeType == LoginActivity.EXTRA_REGISTER_TYPE_DRIVER) {
              Intent(this, DriverRegisterActivity::class.java)
          } else if(AccountTempHelper.getInstance().recognizeType == EXTRA_REGISTER_TYPE_INDUSTRY ) {
              Intent(this, IndustryRegisterActivity::class.java)
          }
          startActivity(intent)*/
        when (AccountTempHelper.getInstance().recognizeType) {
            EXTRA_REGISTER_TYPE_DRIVER -> {
                Intent(this, DriverRegisterActivity::class.java)
                startActivity(intent)
            }
            EXTRA_REGISTER_TYPE_INDUSTRY -> {
                Intent(this, IndustryRegisterActivity::class.java)
                startActivity(intent)
            }
            else -> {
            }
        }
    }



}
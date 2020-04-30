package com.tourcoo.training.ui.account.register

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity

import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountTempHelper

import com.tourcoo.training.ui.account.LoginActivity.Companion.EXTRA_REGISTER_TYPE_DRIVER
import com.tourcoo.training.ui.account.LoginActivity.Companion.EXTRA_REGISTER_TYPE_INDUSTRY
import com.tourcoo.training.ui.account.register.RecognizeIdCardActivity.Companion.EXTRA_PHOTO_PATH
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
            tvLabel1.text  = "姓名"
            tvLabel2.text  = "身份证号码"
            if (AccountTempHelper.getInstance().idCardInfo != null) {
                etName.setText(AccountTempHelper.getInstance().idCardInfo.name)
                etIdCard.setText(AccountTempHelper.getInstance().idCardInfo.idCard)
            }
        } else {
            tvLabel1.text  = "名称"
            tvLabel2.text  = "统一社会信用代码"
            if (AccountTempHelper.getInstance().businessLicenseInfo != null) {
                etName.setText(AccountTempHelper.getInstance().businessLicenseInfo.name)
                etIdCard.setText(AccountTempHelper.getInstance().businessLicenseInfo.creditCode)
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
                val intent = Intent(this, DriverRegisterActivity::class.java)
                startActivity(intent)
            }
            EXTRA_REGISTER_TYPE_INDUSTRY -> {
                val intent = Intent(this, IndustryRegisterActivity::class.java)
                startActivity(intent)
            }
            else -> {
            }
        }
    }


}
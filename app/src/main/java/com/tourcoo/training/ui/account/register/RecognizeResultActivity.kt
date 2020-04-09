package com.tourcoo.training.ui.account.register

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.RegisterTempHelper
import com.tourcoo.training.ui.account.LoginActivity
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
    private val isRecognizeId = RegisterTempHelper.getInstance().isRecognizeIdCard
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
        ivSelectedImage.setImageBitmap(BitmapFactory.decodeFile(photoPath))
        showResultInfo()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvNextStep -> {
                skipRegisterByType()
            }
            else -> {
            }
        }
    }

    private fun showResultInfo() {
        if (isRecognizeId) {
            if (RegisterTempHelper.getInstance().idCardInfo != null) {
                tvName.text = RegisterTempHelper.getInstance().idCardInfo.name
                tvIdCard.text = RegisterTempHelper.getInstance().idCardInfo.idCard
            }
        } else {
            if (RegisterTempHelper.getInstance().businessLicenseInfo != null) {
                tvName.text = RegisterTempHelper.getInstance().businessLicenseInfo.name
                tvIdCard.text = RegisterTempHelper.getInstance().businessLicenseInfo.creditCode
            }
        }

    }

    private fun skipRegisterByType() {
        val intent = if (RegisterTempHelper.getInstance().registerType == LoginActivity.EXTRA_REGISTER_TYPE_DRIVER) {
            Intent(this, DriverRegisterActivity::class.java)
        } else {
            Intent(this, IndustryRegisterActivity::class.java)
        }
        startActivity(intent)
    }
}
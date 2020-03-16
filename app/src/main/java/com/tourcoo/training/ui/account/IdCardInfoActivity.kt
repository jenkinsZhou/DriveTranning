package com.tourcoo.training.ui.account

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
import com.tourcoo.training.ui.account.UploadIdCardActivity.Companion.EXTRA_PHOTO_PATH
import kotlinx.android.synthetic.main.activity_id_card_info.*
import kotlinx.android.synthetic.main.activity_upload_id_card.*
import kotlinx.android.synthetic.main.activity_upload_id_card.ivSelectedImage
import kotlinx.android.synthetic.main.activity_upload_id_card.tvNextStep

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月13日16:18
 * @Email: 971613168@qq.com
 */
class IdCardInfoActivity : BaseTitleActivity(), View.OnClickListener {
    private var photoPath: String? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_id_card_info
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("身份证上传")
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
        showIdInfo()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvNextStep -> {
                val intent = Intent(this, IndustrialRegisterActivity::class.java)
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    private fun showIdInfo(){
        tvName.text = RegisterTempHelper.getInstance().registerName
        tvIdCard.text = RegisterTempHelper.getInstance().registerIdCard
        RegisterTempHelper.getInstance().businessLicensePath ="awdad"
//        tvName.text = RegisterTempHelper.getInstance().registerPhone
    }
}
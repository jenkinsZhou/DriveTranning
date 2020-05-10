package com.tourcoo.training.ui.certificate

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blankj.utilcode.util.ImageUtils
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BasePageResult
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.manager.GlideManager
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.certificate.CertifyDetail
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_certification_details.*
import com.tencent.liteav.muxer.R.id.image
import androidx.core.app.ComponentActivity.ExtraData
import com.trello.rxlifecycle3.RxLifecycle.bindUntilEvent
import androidx.core.content.ContextCompat.getSystemService
import com.tourcoo.training.utils.TourImageUtils


class CertificationDetailsActivity : BaseTitleActivity() {

    private var mCertifyId: String = ""

    override fun getContentLayout(): Int {
        return R.layout.activity_certification_details
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("学习证书")
    }

    override fun initView(savedInstanceState: Bundle?) {
        mCertifyId = intent.getStringExtra("id")
        TourCooLogUtil.d("证书id=" + mCertifyId)
        requestCertificate()
    }


    private fun requestCertificate() {
        ApiRepository.getInstance().requestCertificateDetail(mCertifyId).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<CertifyDetail>>() {
            override fun onSuccessNext(entity: BaseResult<CertifyDetail>?) {
                if (entity == null) {
                    return
                }
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    showDetail(entity.data)
                } else {
                    ToastUtil.show(entity.getMsg())
                }
            }
        })
    }


    private fun showDetail(detail: CertifyDetail?) {
        if (detail == null) {
            ToastUtil.show("证书信息获取失败")
            return
        }
        GlideManager.loadImg(CommonUtil.getUrl(detail.avatar), ivAvatar)
        GlideManager.loadImg(CommonUtil.getUrl(detail.certificateImage), ivImage)


        btnSave.setOnClickListener {
            val bitmap = (ivImage.drawable as BitmapDrawable).bitmap
            TourImageUtils.saveBitmap2Gallery(this,bitmap)
        }

        btnShare.setOnClickListener {
            //todo: 微信仅分享图片
            val bitmap = ImageUtils.view2Bitmap(llShare)
            TourImageUtils.saveBitmap2Gallery(this,bitmap)
        }

    }

}

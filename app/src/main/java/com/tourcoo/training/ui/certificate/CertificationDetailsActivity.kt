package com.tourcoo.training.ui.certificate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tourcoo.training.R
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BasePageResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.trello.rxlifecycle3.android.ActivityEvent

class CertificationDetailsActivity : BaseTitleActivity() {

    override fun getContentLayout(): Int {
        return R.layout.activity_certification_details
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("学习证书")
    }

    override fun initView(savedInstanceState: Bundle?) {
        val id = intent.getStringExtra("id")
        println(id)
    }


    private fun requestCertificate(id: String) {
//        ApiRepository.getInstance().requestCertificate(page).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BasePageResult<CertificateInfo>>() {
//            override fun onSuccessNext(entity: BasePageResult<CertificateInfo>?) {
//                val total = entity?.data?.total ?: 0
//                tvTotalCertificate.text = "已获得" + total + "张"
//                UiManager.getInstance().httpRequestControl.httpRequestSuccess(iHttpRequestControl, if (entity!!.data == null) ArrayList<CertificateInfo>() else transformData(entity.data.rows), null)
//            }
//        })
    }


}

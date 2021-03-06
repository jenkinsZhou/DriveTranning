package com.tourcoo.training.ui.certificate

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tourcoo.training.R
import com.tourcoo.training.adapter.certificate.CertificateInfoAdapter
import com.tourcoo.training.adapter.certificate.CertificateInfoAdapter.ITEM_TYPE_CONTENT
import com.tourcoo.training.adapter.certificate.CertificateInfoAdapter.ITEM_TYPE_HEADER
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.activity.BaseTitleRefreshLoadActivity
import com.tourcoo.training.core.base.entity.BasePageResult
import com.tourcoo.training.core.interfaces.OnHttpRequestListener
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.certificate.CertificateInfo
import com.tourcoo.training.entity.certificate.CertifyDetail
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_study_record_certificate.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 *@description :我的证书列表
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月15日9:26
 * @Email: 971613168@qq.com
 */
class MyCertificationActivity : BaseTitleRefreshLoadActivity<CertificateInfo>() {
    private var adapter: CertificateInfoAdapter? = null

    override fun getContentLayout(): Int {
        return R.layout.activity_study_record_certificate
    }

    override fun getAdapter(): BaseQuickAdapter<CertificateInfo, BaseViewHolder> {
        val list: MutableList<CertificateInfo>? = ArrayList()
        adapter = CertificateInfoAdapter(list)
        return adapter!!
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("学习证书")
    }

    override fun initView(savedInstanceState: Bundle?) {

        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val info = adapter.data[position] as CertificateInfo
            if (!TextUtils.isEmpty(CommonUtil.getNotNullValue(info.id))) {
                //非标题item才响应点击事件
                val intent = Intent(this, CertificationDetailsActivity::class.java)
                intent.putExtra("id", CommonUtil.getNotNullValue(info.id))
                startActivity(intent)
            }

        }

    }

    override fun loadData(page: Int) {
        requestCertificate(page)
    }


    private var resultList: MutableList<CertificateInfo>? = ArrayList()

    private fun requestCertificate(page: Int) {
        ApiRepository.getInstance().requestCertificate(page).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BasePageResult<CertificateInfo>>(iHttpRequestControl) {
            override fun onSuccessNext(entity: BasePageResult<CertificateInfo>?) {
                if (entity == null) {
                    UiManager.getInstance().httpRequestControl.httpRequestError(iHttpRequestControl, NullPointerException("data==null"))
                    return
                }
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {

                    if (page == 1) {
                        resultList = ArrayList()
                    }

                    if (entity.data == null) {
                        resultList!!.addAll(ArrayList())
                    } else {
                        resultList!!.addAll(entity.data.rows)
                    }

                    val total = entity.data?.total ?: 0
                    tvTotalCertificate.text = "已获得" + total + "张"
                    UiManager.getInstance().httpRequestControl.httpRequestSuccess(iHttpRequestControl, transformData(resultList), null)

                    if (adapter == null) {
                        return
                    }
                    adapter!!.setNewData(transformData(resultList))
                    if(entity.data.rows.size < 10){
                        adapter!!.loadMoreEnd()
                    }

                } else {
                    UiManager.getInstance().httpRequestControl.httpRequestSuccess(iHttpRequestControl, ArrayList<CertificateInfo>())
                }
            }
        })
    }


    private fun transformData(list: MutableList<CertificateInfo>?): MutableList<CertificateInfo> {
        val result = ArrayList<CertificateInfo>()
        if (list == null || list.isEmpty()) {
            return result
        }

        try {
            for (certificate in list) {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                if (!TextUtils.isEmpty(certificate.certificateTime)) {
                    val date = sdf.parse(certificate.certificateTime)
                    if (date != null) {
                        certificate.date = date
                    }
                }
            }
        } catch (e: Exception) {
            TourCooLogUtil.e("MyCertificationActivity", "e:" + e.toString())
        }

        val maps = CommonUtil.sort(list)
        TourCooLogUtil.e("MyCertificationActivity  1`  ", maps)
        for (map in maps) {
            val values = map.value
            val certificateInfo = CertificateInfo()
            certificateInfo.itemType = ITEM_TYPE_HEADER
            certificateInfo.headerContent = map.key
            values.add(0, certificateInfo)
            result.addAll(values)
        }
        TourCooLogUtil.e("MyCertificationActivity", maps)
        return result
    }


}
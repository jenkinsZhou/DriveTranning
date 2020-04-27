package com.tourcoo.training.ui.training.professional

import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tourcoo.training.R
import com.tourcoo.training.adapter.certificate.CertificateInfoAdapter
import com.tourcoo.training.core.base.activity.BaseTitleRefreshLoadActivity
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.certificate.CertificateInfo

/**
 *@description :两类人员专项
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月27日16:32
 * @Email: 971613168@qq.com
 */
class  ProfessionalTrainTwoTypeActivity : BaseTitleRefreshLoadActivity<CertificateInfo>() {
    override fun getContentLayout(): Int {
        return R.layout.frame_layout_title_refresh_recycler
    }

    override fun getAdapter(): BaseQuickAdapter<CertificateInfo, BaseViewHolder> {
        val list: MutableList<CertificateInfo>? = ArrayList()
        return CertificateInfoAdapter(list)
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("两类人员专项")
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun loadData(page: Int) {
    }
}
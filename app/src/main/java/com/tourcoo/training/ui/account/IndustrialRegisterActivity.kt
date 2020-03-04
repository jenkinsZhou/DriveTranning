package com.tourcoo.training.ui.account

import android.os.Bundle
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import kotlinx.android.synthetic.main.activity_register_industrial.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月04日17:38
 * @Email: 971613168@qq.com
 */
class IndustrialRegisterActivity : BaseTitleActivity(),View.OnClickListener {
    override fun getContentLayout(): Int {
        return R.layout.activity_register_industrial
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar!!.setTitleMainText("驾驶员注册")
        tvRegisterIndustrial.setOnClickListener(this)
        tvGoLogin.setOnClickListener(this)

    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvRegisterIndustrial -> {
                CommonUtil.startActivity(mContext, DriverRegisterActivity::class.java)
            }
            R.id.tvGoLogin->{
                CommonUtil.startActivity(mContext, LoginActivity::class.java)
            }
            else -> {
            }
        }
    }
}
package com.tourcoo.training.ui.account

import android.os.Bundle
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.RegisterTempHelper
import kotlinx.android.synthetic.main.activity_login.*

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年03月04日23:50
 * @Email: 971613168@qq.com
 */
class LoginActivity : BaseTitleActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_KEY_REGISTER_TYPE = "EXTRA_KEY_REGISTER_TYPE"
        const val EXTRA_REGISTER_TYPE_INDUSTRY = 1
        const val EXTRA_REGISTER_TYPE_DRIVER = 2
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_login
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar!!.visibility = View.GONE
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvRegisterDriver.setOnClickListener(this)
        tvRegisterIndustrial.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvRegisterDriver -> {
                RegisterTempHelper.getInstance().registerType = EXTRA_REGISTER_TYPE_DRIVER
                skipRegister(EXTRA_REGISTER_TYPE_DRIVER)
            }
            R.id.tvRegisterIndustrial -> {
                RegisterTempHelper.getInstance().registerType = EXTRA_REGISTER_TYPE_INDUSTRY
                skipRegister(EXTRA_REGISTER_TYPE_INDUSTRY)
            }
        }
    }

    override fun isStatusBarDarkMode(): Boolean {
        return false
    }

    private fun skipRegister(type: Int) {
        val bundle = Bundle()
        bundle.putInt(EXTRA_KEY_REGISTER_TYPE, type)
        CommonUtil.startActivity(mContext, RecognizeIdCardActivity::class.java, bundle)
    }
}
package com.tourcoo.training.ui.account

import android.os.Bundle
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import kotlinx.android.synthetic.main.activity_register_driver.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月04日17:37
 * @Email: 971613168@qq.com
 */
class DriverRegisterActivity  : BaseTitleActivity() , View.OnClickListener {

    override fun getContentLayout(): Int {
        return R.layout.activity_register_driver
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar!!.setTitleMainText("个体工商户注册")
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvGoLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvGoLogin->{
                CommonUtil.startActivity(mContext, LoginActivity::class.java)
            }
            else -> {
            }
        }
    }
}
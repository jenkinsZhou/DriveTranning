package com.tourcoo.training.ui.account

import android.os.Bundle
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年03月04日23:50
 * @Email: 971613168@qq.com
 */
class LoginActivity : BaseTitleActivity(),View.OnClickListener {
    override fun getContentLayout(): Int {
        return R.layout.activity_login
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar!!.visibility = View.GONE
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun onClick(v: View?) {
        when (v!!.id) {


        }
    }

    override fun isStatusBarDarkMode(): Boolean {
        return false
    }



}
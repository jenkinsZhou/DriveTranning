package com.tourcoo.training.ui.account

import android.os.Bundle
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.widget.view.bar.TitleBarView

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月04日17:38
 * @Email: 971613168@qq.com
 */
class IndustrialRegisterActivity : BaseTitleActivity() {
    override fun getContentLayout(): Int {
        return R.layout.activity_register_industrial
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar!!.setTitleMainText("个体工商户注册")
    }

    override fun initView(savedInstanceState: Bundle?) {
    }
}
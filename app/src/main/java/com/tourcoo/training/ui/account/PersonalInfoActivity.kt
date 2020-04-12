package com.tourcoo.training.ui.account

import android.os.Bundle
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.widget.view.bar.TitleBarView

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年04月11日23:09
 * @Email: 971613168@qq.com
 */
class PersonalInfoActivity :BaseTitleActivity() {
    override fun getContentLayout(): Int {
        return R.layout.activity_personal_info
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
    }

    override fun initView(savedInstanceState: Bundle?) {
    }
}
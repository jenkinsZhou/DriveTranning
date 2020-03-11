package com.tourcoo.training.ui.training.online

import android.os.Bundle
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.widget.view.bar.TitleBarView

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年03月11日23:27
 * @Email: 971613168@qq.com
 */
class StudyOnlineActivity : BaseTitleActivity() {
    override fun getContentLayout(): Int {
        return R.layout.activity_study_online
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("线上学习")
    }

    override fun initView(savedInstanceState: Bundle?) {
    }
}
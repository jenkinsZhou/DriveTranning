package com.tourcoo.training.ui.home.news

import android.os.Bundle
import com.just.agentweb.AgentWeb
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.widget.view.bar.TitleBarView


/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年05月06日13:50
 * @Email: 971613168@qq.com
 */
class NewsDetailActivity : BaseTitleActivity() {
    private var mAgentWeb: AgentWeb? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_news_detail_old
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("资讯")
    }

    override fun initView(savedInstanceState: Bundle?) {

        initWeb()
    }

    private fun initWeb(){
        //传入Activity
     /*   mAgentWeb = AgentWeb.with(this) .setAgentWebParent(llWebContainer,LinearLayout.LayoutParams(-1,-2))
                .useDefaultIndicator()

                // 使用默认进度条
//                .defaultProgressBarColor() // 使用默认进度条颜色
//                .setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb() //
                .ready()
                .go("http://www.baidu.com")*/
    }
}
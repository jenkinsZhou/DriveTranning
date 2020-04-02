package com.tourcoo.training.ui.pay

import android.os.Bundle
import com.tourcoo.training.R
import com.tourcoo.training.core.base.mvp.BaseMvpTitleActivity
import com.tourcoo.training.core.base.mvp.NullPresenter
import com.tourcoo.training.core.widget.view.bar.TitleBarView

/**
 *@description :立即购买（支付）
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月02日15:11
 * @Email: 971613168@qq.com
 */
class BuyNowActivity : BaseMvpTitleActivity<NullPresenter>() {
    override fun loadPresenter() {
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_pay_buy_now
    }


    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("立即购买")
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun createPresenter(): NullPresenter {
        return NullPresenter()
    }
}
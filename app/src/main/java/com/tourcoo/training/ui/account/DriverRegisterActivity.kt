package com.tourcoo.training.ui.account

import android.os.Bundle
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseMovieEntity
import com.tourcoo.training.core.base.mvp.BaseMvpActivity
import com.tourcoo.training.core.base.mvp.BaseMvpTitleActivity
import com.tourcoo.training.core.base.mvp.NullPresenter
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.ui.account.register.DriverRegisterContract
import com.tourcoo.training.ui.account.register.DriverRegisterPresenter
import com.tourcoo.training.ui.training.StudyMedalRecordActivity
import com.tourcoo.training.widget.keyboard.KingKeyboard
import kotlinx.android.synthetic.main.activity_register_driver.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月04日17:37
 * @Email: 971613168@qq.com
 */
class DriverRegisterActivity : BaseMvpTitleActivity<NullPresenter>(), View.OnClickListener {

    private lateinit var kingKeyboard: KingKeyboard
    override fun getContentLayout(): Int {
        return R.layout.activity_register_driver
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar!!.setTitleMainText("驾驶员注册")
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvGoLogin.setOnClickListener(this)
        initPlantKeyBoard()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvGoLogin -> {
//                CommonUtil.startActivity(mContext, MyAccountActivity::class.java)
                CommonUtil.startActivity(mContext, StudyMedalRecordActivity::class.java)

            }
            else -> {
            }
        }
    }

    private fun initPlantKeyBoard() {
        kingKeyboard = KingKeyboard(this, keyboardParent)
        kingKeyboard.register(etDriverPlantNum, KingKeyboard.KeyboardType.LICENSE_PLATE)
        kingKeyboard.setKeyboardCustom(R.xml.keyboard_custom)
        kingKeyboard.setVibrationEffectEnabled(true)
    }

    override fun loadPresenter() {
    }

    override fun createPresenter(): NullPresenter {
        return NullPresenter()
    }

    private fun selectImage() {


    }

}
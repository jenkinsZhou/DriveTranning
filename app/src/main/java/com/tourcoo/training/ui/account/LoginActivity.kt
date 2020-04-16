package com.tourcoo.training.ui.account

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.entity.account.UserInfo
import com.tourcoo.training.ui.MainTabActivity
import com.tourcoo.training.ui.account.register.RecognizeIdCardActivity
import com.tourcoo.training.ui.account.register.RecognizeLicenseActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_login.*

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年03月04日23:50
 * @Email: 971613168@qq.com
 */
class LoginActivity : BaseTitleActivity(), View.OnClickListener {
    private val mTag = "LoginActivity"

    companion object {
        const val EXTRA_KEY_REGISTER_TYPE = "EXTRA_KEY_REGISTER_TYPE"
        const val EXTRA_REGISTER_TYPE_INDUSTRY = 1
        const val EXTRA_REGISTER_TYPE_DRIVER = 2
        //身份证比对比对
        const val EXTRA_TYPE_RECOGNIZE_COMPARE = 3
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
        tvLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvRegisterDriver -> {
                AccountTempHelper.getInstance().recognizeType = EXTRA_REGISTER_TYPE_DRIVER
                skipRegisterDriver()
            }
            R.id.tvRegisterIndustrial -> {
                AccountTempHelper.getInstance().recognizeType = EXTRA_REGISTER_TYPE_INDUSTRY
                skipRegisterIndustrial()
            }
            R.id.tvLogin -> {
                doLogin()
            }
        }
    }

    override fun isStatusBarDarkMode(): Boolean {
        return false
    }

    /**
     * 驾驶员注册
     */
    private fun skipRegisterDriver() {
        val bundle = Bundle()
        bundle.putInt(EXTRA_KEY_REGISTER_TYPE, EXTRA_REGISTER_TYPE_DRIVER)
        CommonUtil.startActivity(mContext, RecognizeIdCardActivity::class.java, bundle)
    }

    /**
     * 个体公司户注册
     */
    private fun skipRegisterIndustrial() {
        val bundle = Bundle()
        bundle.putInt(EXTRA_KEY_REGISTER_TYPE, EXTRA_REGISTER_TYPE_INDUSTRY)
        CommonUtil.startActivity(mContext, RecognizeLicenseActivity::class.java, bundle)
    }

    private fun doLogin() {
        if (TextUtils.isEmpty(getTextValue(etIdCard))) {
            ToastUtil.show("请输入身份证号")
            return
        }
        if (TextUtils.isEmpty(getTextValue(etPass))) {
            ToastUtil.show("请输入密码")
            return
        }
        ApiRepository.getInstance().requestLoginByIdCard(getTextValue(etIdCard), getTextValue(etPass)).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<UserInfo>>("正在登录...") {
            override fun onSuccessNext(entity: BaseResult<UserInfo>?) {
                if (entity != null) {
                    if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                        TourCooLogUtil.i(mTag, "--->" + "回调成功")
                        handleLoginCallback(entity.data)
                    } else {
                        ToastUtil.show(entity.msg)
                    }
                }
            }

            override fun onError(e: Throwable) {
                if(AppConfig.DEBUG_MODE){
                    ToastUtil.showFailed(e.toString())
                }
            }

        })
    }


    private fun handleLoginCallback(userInfo: UserInfo?) {
        if (userInfo == null) {
            ToastUtil.show("登录失败")
            return
        }
        AccountHelper.getInstance().userInfo = userInfo
        val intent = Intent(this, MainTabActivity::class.java)
        startActivity(intent)
        finish()
    }
}
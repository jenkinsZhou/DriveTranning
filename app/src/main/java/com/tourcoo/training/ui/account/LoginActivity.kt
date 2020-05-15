package com.tourcoo.training.ui.account

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.tourcoo.training.R
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.*
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.*
import com.tourcoo.training.entity.setting.SettingEntity
import com.tourcoo.training.ui.MainTabActivity
import com.tourcoo.training.ui.account.register.RecognizeIdCardActivity
import com.tourcoo.training.ui.account.register.RecognizeLicenseActivity
import com.tourcoo.training.ui.training.RichWebViewActivity
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月04日23:50
 * @Email: 971613168@qq.com
 */
class LoginActivity : BaseTitleActivity(), View.OnClickListener {
    private val mTag = "LoginActivity"
    private var checkInputList: MutableList<InputStatus>? = null

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
        checkInputList = ArrayList()
        tvRegisterDriver.setOnClickListener(this)
        tvRegisterIndustrial.setOnClickListener(this)
        tvLogin.setOnClickListener(this)
        btnForgetPassword.setOnClickListener(this)
        btnContent.setOnClickListener(this)
        listenInputClear(etPass, ivClearInputPass)
        listenInputLegal(etIdCard, ivAccountCheck)
        listenInputLegalPass(etPass, ivPassCheck, 6)
        showButtonByInput(hasInputAll())
        listenInputFocus(etIdCard, viewLineAccount, OnFocusListener {
            if (it) {
                ivUser.setImageResource(R.drawable.icon_identity_card_selected)
            } else {
                ivUser.setImageResource(R.drawable.icon_identity_card_normal)
            }
        })
        listenInputFocus(etPass, viewLinePass, OnFocusListener {
            if (it) {
                ivPass.setImageResource(R.drawable.icon_password_selected)
            } else {
                ivPass.setImageResource(R.drawable.icon_password_normal)
            }
        })
        listenInputClear(etIdCard, ivClearInputId)
        listenInputClear(etPass, ivClearInputPass)

        initTestInput()
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
            R.id.btnContent -> {
                requestSystemConfigAndSkip()
            }

            R.id.btnForgetPassword -> {
                CommonUtil.startActivity(this, FindPassActivity::class.java)
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

        val isRead = cbRead.isChecked
        if (!isRead) {
            ToastUtil.show("请勾选已同意《交通安培》用户服务协议")
            return
        }


        ApiRepository.getInstance().requestLoginByIdCard(getTextValue(etIdCard), getTextValue(etPass)).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<UserInfo>>("正在登录...") {
            override fun onSuccessNext(entity: BaseResult<UserInfo>?) {
                if (entity != null) {
                    if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                        SPUtils.getInstance().put("TraineeID", entity.data.traineeID)
                        handleLoginCallback(entity.data)
                    } else {
                        ToastUtil.show(entity.msg)
                    }
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
        EventBus.getDefault().post(UserInfoEvent(userInfo))
        val intent = Intent(this, MainTabActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun requestSystemConfigAndSkip() {
        ApiRepository.getInstance().requestSystemConfig().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<SettingEntity>>() {
            override fun onSuccessNext(entity: BaseResult<SettingEntity>?) {
                if (entity != null) {
                    if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                        if (entity.data != null) {
                            val intent = Intent(mContext, RichWebViewActivity::class.java)
                            intent.putExtra(RichWebViewActivity.EXTRA_RICH_TEXT, CommonUtil.getNotNullValue(entity.data.agreement))
                            startActivity(intent)
                        }

                    } else {
                        ToastUtil.show(entity.msg)
                    }
                }
            }
        })
    }


    private fun showButtonByInput(canInput: Boolean) {
        tvLogin.isClickable = canInput
        if (canInput) {
            tvLogin.background = ResourceUtil.getDrawable(R.drawable.selector_gradient_radius_25_blue)
        } else {
            tvLogin.background = ResourceUtil.getDrawable(R.drawable.bg_radius_25_gray_d2d2d2)
        }
    }

    private fun listenInput(editText: EditText, imageView: ImageView?) {
        var inputStatus = InputStatus()
        checkInputList?.add(inputStatus)
        imageView?.setOnClickListener { editText.setText("") }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                inputStatus.isHasInput = s.isNotEmpty()
                setViewVisible(imageView, s.isNotEmpty())
                LogUtils.iTag(mTag, "hasInputAll=" + hasInputAll())
                showButtonByInput(hasInputAll())
            }
        })

    }


    private fun listenInputLegal(editText: EditText, imageView: ImageView?) {
        var inputStatus = InputStatus()
        checkInputList?.add(inputStatus)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                inputStatus.isHasInput = s.isNotEmpty()
                setViewVisible(imageView, s.toString().length == 18 || s.toString().length == 11)
                LogUtils.iTag(mTag, "hasInputAll=" + hasInputAll())
                showButtonByInput(hasInputAll())
            }
        })
    }

    private fun listenInputLegalPass(editText: EditText, imageView: ImageView?, correctLength: Int) {
        var inputStatus = InputStatus()
        checkInputList?.add(inputStatus)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                inputStatus.isHasInput = s.isNotEmpty()
                setViewVisible(imageView, s.toString().length >= correctLength)
                LogUtils.iTag(mTag, "hasInputAll=" + hasInputAll())
                showButtonByInput(hasInputAll())
            }
        })
    }

    private fun listenInputClear(editText: EditText, imageView: ImageView?) {
        imageView?.setOnClickListener { editText.setText("") }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                setViewVisible(imageView, !TextUtils.isEmpty(editText.text.toString()))
            }
        })
    }


    private fun hasInputAll(): Boolean {
        for (hasInput in checkInputList!!) {
            if (!hasInput.isHasInput) {
                return false
            } else {
                continue
            }
        }
        return true
    }


    private fun listenInputFocus(editText: EditText, lineView: View, focusListener: OnFocusListener) {
        editText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            focusListener.onFocus(hasFocus)
            if (hasFocus) {
                //获得焦点
                showSelectLine(lineView)
                editText.setHintTextColor(ResourceUtil.getColor(R.color.blue004D8E))
                editText.setTextColor(ResourceUtil.getColor(R.color.blue004D8E))
            } else {
                //失去焦点
                showUnSelectLine(lineView)
                editText.setHintTextColor(ResourceUtil.getColor(R.color.gray999999))
                editText.setTextColor(ResourceUtil.getColor(R.color.gray999999))
            }
        }
    }

    private fun showSelectLine(view: View?) {
        if (view != null) {
            view.background = ResourceUtil.getDrawable(R.color.colorPrimaryDark)
        }
    }

    private fun showUnSelectLine(view: View?) {
        if (view != null) {
            view.background = ResourceUtil.getDrawable(R.color.grayA0A0A0)
        }
    }

    override fun onBackPressed() {
        if (StackUtil.getInstance().previous == null) {
            quitApp()
        }
    }

    private fun initTestInput() {
        if (AppConfig.DEBUG_MODE) {
            etIdCard.setText("340811199311135335")
            etPass.setText("135335")
        } else {
            etIdCard.setText("")
            etPass.setText("")
        }

    }
}


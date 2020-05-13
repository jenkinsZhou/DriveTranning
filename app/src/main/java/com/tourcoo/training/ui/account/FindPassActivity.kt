package com.tourcoo.training.ui.account

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.ImageView
import com.blankj.utilcode.util.LogUtils
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.manager.RxJavaManager
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.account.InputStatus
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.ui.account.register.IndustryRegisterActivity
import com.tourcoo.training.utils.TourCooUtil
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_find_password.*

/**
 *@description :找回密码
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月17日11:12
 * @Email: 971613168@qq.com
 */
class FindPassActivity : BaseTitleActivity(), View.OnClickListener {
    private val mTag = "FindPassActivity"
    private val disposableList = ArrayList<Disposable>()
    private val mHandler = Handler()
    private var timeCount = COUNT

    companion object {
        const val COUNT = 60L
        const val ONE_SECOND = 1000L
    }


    private var checkInputList: MutableList<InputStatus>? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_find_password
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("找回密码")
    }

    override fun initView(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val isLogin = bundle?.getBoolean("isLogin")

        if (isLogin == null || isLogin == false) {
            etPhone.isEnabled = true
        } else {
            etPhone.isEnabled = false
            etPhone.setText(AccountHelper.getInstance().userInfo.phone)
            etPhone.setTextColor(resources.getColor(R.color.colorAccent))
        }


        checkInputList = ArrayList()
        listenInputFocus(etPhone, linePass)
        listenInputFocus(etVCode, lineVCode)
        listenInputFocus(etNewPass, lineNewPass)
        listenInput(etNewPass, ivNewPassClose)
        listenInput(etVCode, ivVCodeClose)
        listenInput(etPhone, null)
        tvConfirmPass.setOnClickListener(this)
        tvGetVCode.setOnClickListener(this)
        showButtonByInput(false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvNextStep -> {
                val intent = Intent(this, IndustryRegisterActivity::class.java)
                startActivity(intent)
            }
            R.id.tvConfirmPass -> {
                val phone = etPhone.text.toString().trim()
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.show("请输入手机号")
                    return
                }

                if (!phone.startsWith("1") || phone.length != 11) {
                    ToastUtil.show("手机号格式有误")
                    return
                }
                doResetPass(phone)
            }
            R.id.tvGetVCode -> {
                val phone = etPhone.text.toString().trim()
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.show("请输入手机号")
                    return
                }
                if (!phone.startsWith("1") || phone.length != 11) {
                    ToastUtil.show("手机号格式有误")
                    return
                }
                sendVCodeAndCountDownTime(phone)
            }

            else -> {
            }
        }
    }

    private fun listenInputFocus(editText: EditText, lineView: View) {
        editText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                //获得焦点
                showSelectLine(lineView)
            } else {
                //失去焦点
                showUnSelectLine(lineView)
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

    private fun listenInput(editText: EditText, imageView: ImageView?) {
        var inputStatus = InputStatus()
        checkInputList?.add(inputStatus)
        imageView?.setOnClickListener { editText.setText("") }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                inputStatus.isHasInput = s.isNotEmpty()
                setViewGone(imageView, s.isNotEmpty())
                LogUtils.iTag(mTag, "hasInputAll=" + hasInputAll())
                showButtonByInput(hasInputAll())
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

    private fun showButtonByInput(canInput: Boolean) {
        tvConfirmPass.isClickable = canInput
        if (canInput) {
            tvConfirmPass.background = ResourceUtil.getDrawable(R.drawable.selector_gradient_radius_25_blue)
        } else {
            tvConfirmPass.background = ResourceUtil.getDrawable(R.drawable.bg_radius_25_gray_d2d2d2)
        }
    }


    /**
     * 验证码发送接口并倒计时
     *
     * @param phone
     */
    private fun sendVCodeAndCountDownTime(phone: String) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("请输入手机号")
            return
        }
        if (!TourCooUtil.isMobileNumber(phone)) {
            ToastUtil.show("请输入正确的手机号")
            return
        }
        ApiRepository.getInstance().requestVCode(1, phone).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<*>>() {
            override fun onSuccessNext(entity: BaseResult<*>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    //验证码发送成功开始，倒计时
                    ToastUtil.showSuccess("发送成功")
                    countDownTime()
                } else {
                    ToastUtil.showFailed(entity.msg)
                }
            }

        })
    }


    /**
     * 倒计时
     */
    private fun countDownTime() {
        reset()
        setClickEnable(false)
        mHandler.postDelayed(Runnable {
            tvGetVCode.setTextColor(ResourceUtil.getColor(R.color.grayA2A2A2))
        }, ONE_SECOND)
        RxJavaManager.getInstance().doEventByInterval(ONE_SECOND, object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {
                disposableList.add(d)
            }

            override fun onNext(aLong: Long) {
                --timeCount
                setText("还有" + timeCount + "秒")
                if (aLong >= COUNT - 1) {
                    onComplete()
                }
            }

            override fun onError(e: Throwable) {
                cancelTime()
            }

            override fun onComplete() {
                reset()
                cancelTime()
            }
        })
    }

    private fun cancelTime() {
        if (disposableList.isNotEmpty()) {
            var disposable: Disposable
            for (i in disposableList.indices) {
                disposable = disposableList.get(i)
                if (!disposable.isDisposed) {
                    disposable.dispose()
                    disposableList.remove(disposable)
                }
            }
        }
    }

    override fun onDestroy() {
        cancelTime()
        super.onDestroy()
    }


    private fun reset() {
        setClickEnable(true)
        timeCount = COUNT
        setText("发送验证码")
        tvGetVCode.setTextColor(ResourceUtil.getColor(R.color.white))
        tvGetVCode.background = ResourceUtil.getDrawable(R.drawable.selector_gradient_radius_25_blue)

    }


    private fun setClickEnable(clickEnable: Boolean) {
        tvGetVCode.isEnabled = clickEnable
    }

    private fun setText(text: String) {
        tvGetVCode.text = text
        tvGetVCode.background = ResourceUtil.getDrawable(R.drawable.bg_radius_25_gray_d2d2d2)
        tvGetVCode.setTextColor(ResourceUtil.getColor(R.color.gray888888))
    }


    private fun doResetPass(phone: String) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("未获取到手机号")
            return
        }
        if (!TourCooUtil.isMobileNumber(phone)) {
            ToastUtil.show("未获取到正确的手机号")
            return
        }
        ApiRepository.getInstance().requestResetPass(phone, getTextValue(etNewPass), getTextValue(etVCode)).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<*>>() {
            override fun onSuccessNext(entity: BaseResult<*>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.showSuccess("修改密码成功")
                    finish()
                } else {
                    ToastUtil.showFailed(entity.msg)
                }
            }
        })

    }
}
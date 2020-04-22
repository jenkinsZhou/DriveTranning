package com.tourcoo.training.ui.account

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.manager.RxJavaManager
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.utils.TourCooUtil
import com.tourcoo.training.widget.dialog.BottomSheetDialog
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_my_phone.*

class ChangePhoneActivity  : BaseTitleActivity(), View.OnClickListener {

    private val disposableList = ArrayList<Disposable>()
    private val mHandler = Handler()
    private var timeCount = COUNT

    companion object {
        const val COUNT = 60L
        const val ONE_SECOND = 1000L
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_my_phone
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("手机号码")
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvGetVCode.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvGetVCode -> {
                val phone = etPhone.text.toString().trim()
                if(!phone.startsWith("1") || phone.length != 11){
                    ToastUtil.show("手机号格式有误")
                    return
                }
                sendVCodeAndCountDownTime(phone)
            }

            R.id.btnSubmit -> {

            }

            else -> {
            }
        }
    }

    private fun requestLogout() {
        ApiRepository.getInstance().requestLogout().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>>() {
            override fun onSuccessNext(entity: BaseResult<Any>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    ToastUtil.show("账号已退出")
                    AccountHelper.getInstance().logout()
                    finish()
                }

            }

        })
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
        ApiRepository.getInstance().requestVCode(0, phone).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<*>>() {
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
        }, FindPassActivity.ONE_SECOND)
        RxJavaManager.getInstance().doEventByInterval(FindPassActivity.ONE_SECOND, object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {
                disposableList.add(d)
            }

            override fun onNext(aLong: Long) {
                --timeCount
                setText("还有" + timeCount + "秒")
                if (aLong >= FindPassActivity.COUNT - 1) {
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
        timeCount = FindPassActivity.COUNT
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


}


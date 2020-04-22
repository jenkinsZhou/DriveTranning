package com.tourcoo.training.ui.setting

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.account.UserInfo
import com.tourcoo.training.entity.account.UserInfoEvent
import com.tourcoo.training.entity.account.register.CompanyInfo
import com.tourcoo.training.ui.account.FindPassActivity
import com.tourcoo.training.widget.dialog.BottomSheetDialog
import com.tourcoo.training.widget.dialog.share.BottomShareDialog
import com.trello.rxlifecycle3.RxLifecycle.bindUntilEvent
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_setting_system.*
import org.greenrobot.eventbus.EventBus

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月17日11:30
 * @Email: 971613168@qq.com
 */
class SettingActivity : BaseTitleActivity(), View.OnClickListener {
    override fun getContentLayout(): Int {
        return R.layout.activity_setting_system
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("系统设置")
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvLogout.setOnClickListener(this)
        llForgetPassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvLogout -> {
                doLogout()
            }

            R.id.llForgetPassword -> {
                val bundle = Bundle()
                bundle.putBoolean("isLogin", AccountHelper.getInstance().isLogin)
                CommonUtil.startActivity(mContext, FindPassActivity::class.java, bundle)
            }

            else -> {
            }
        }
    }


    private fun doLogout() {
        if (!AccountHelper.getInstance().isLogin) {
            ToastUtil.show("您还未登录")
            return
        }
        val dialog = BottomSheetDialog(mContext)
        val style = BottomSheetDialog.SheetItemTextStyle()
        style.textColor = ResourceUtil.getColor(R.color.redFF4A5C)
        style.typeface = Typeface.DEFAULT
        val item = BottomSheetDialog.SheetItem("退出登录", style, BottomSheetDialog.OnSheetItemClickListener {
            requestLogout()
        })
        dialog.addSheetItem(item)
        dialog.create().setTitle("退出登录").show()
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


}





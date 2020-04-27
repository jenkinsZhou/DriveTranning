package com.tourcoo.training.ui.setting

import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ResourceUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.ui.account.FindPassActivity
import com.tourcoo.training.utils.DataCleanUtil
import com.tourcoo.training.utils.DataCleanUtil.EMPTY_CACHE
import com.tourcoo.training.utils.DataCleanUtil.clearAllCache
import com.tourcoo.training.widget.dialog.BottomSheetDialog
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_setting_system.*

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
        llClearCache.setOnClickListener(this)
        showCache()
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
            R.id.llClearCache->{
                doClearCache()
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

    /**
     * 获取缓存大小
     *
     * @return
     */
    private fun getCacheSize(): String? {
        var str = ""
        str = try {
            DataCleanUtil.getTotalCacheSize(mContext)
        } catch (e: Exception) {
            TourCooLogUtil.e("错误信息", e.toString())
            e.printStackTrace()
            return str
        }
        return str
    }

    private fun doClearCache(){
        if (getCacheSize().equals(EMPTY_CACHE, ignoreCase = true)) {
            ToastUtil.show("暂无缓存")
            return
        }
        cleanCache()
        showCache()
        ToastUtil.showSuccess("清除成功")
    }

    /**
     * 清空缓存
     */
    private fun cleanCache() {
        clearAllCache(mContext)
    }


    private fun showCache() {
        TourCooLogUtil.i("缓存大小：" + getCacheSize())
        if (EMPTY_CACHE==getCacheSize()) {
            tvCacheSize.setText("")
        } else {
            tvCacheSize.setText(getCacheSize())
        }
    }



}





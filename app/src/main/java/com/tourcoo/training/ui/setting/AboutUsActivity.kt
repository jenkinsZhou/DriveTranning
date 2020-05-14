package com.tourcoo.training.ui.setting

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.text.Html
import com.blankj.utilcode.util.AppUtils
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.setting.SettingEntity
import com.tourcoo.training.ui.update.AppUpdateInfo
import com.trello.rxlifecycle3.android.ActivityEvent
import com.vector.update_app.UpdateAppManager
import com.vector.update_app_kotlin.check
import com.vector.update_app_kotlin.updateApp
import kotlinx.android.synthetic.main.activity_about_us.*
import org.json.JSONObject
import java.util.HashMap

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年05月06日11:42
 * @Email: 971613168@qq.com
 */
class AboutUsActivity : BaseTitleActivity() {

    private val mTag = "AboutUsActivity"

    override fun getContentLayout(): Int {
        return R.layout.activity_about_us
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("关于我们")
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvAppVersion.text = "V " + CommonUtil.getVersionName(mContext)
        requestSystemConfig()
        requestAppVersionInfo()

        llUpdate.setOnClickListener {
            requestCheckUpdate()
        }

    }

    private fun requestSystemConfig() {
        ApiRepository.getInstance().requestSystemConfig().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<SettingEntity>>() {
            override fun onSuccessNext(entity: BaseResult<SettingEntity>?) {
                if (entity != null) {
                    if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                        showConfig(entity.data)
                    } else {
                        ToastUtil.show(entity.msg)
                    }
                }
            }
        })
    }

    private fun showConfig(settingEntity: SettingEntity?) {
        if (settingEntity == null) {
            return
        }
        tvTelephone.text = CommonUtil.getNotNullValue(settingEntity.telephone)
//        tvVersionInfo.text = CommonUtil.getNotNullValue(settingEntity.v)
        TourCooLogUtil.i(mTag, "settingEntity=" + settingEntity.version)
    }


    private fun requestAppVersionInfo() {
        ApiRepository.getInstance().requestAppVersionInfo().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<AppUpdateInfo>>() {
            override fun onSuccessNext(entity: BaseResult<AppUpdateInfo>?) {
                if (entity != null) {
                    if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                        showAppInfo(entity.data)
                    } else {
                        ToastUtil.show(entity.msg)
                    }
                }
            }
        })
    }


    private fun showAppInfo(appInfo: AppUpdateInfo?) {
        if (appInfo == null) {
            return
        }
        if (appInfo.isUpdate != 1) {
            tvVersionInfo.text = "好赞，当前已是最新版本"
            setViewVisible(ivRedDot, false)
        } else {
            //需要更新
            tvVersionInfo.text = "最新版本号：" + appInfo.versionName
            setViewVisible(ivRedDot, true)
        }

    }


    private fun requestCheckUpdate() {
        ApiRepository.getInstance().requestAppVersionInfo().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<AppUpdateInfo>>() {
            override fun onSuccessNext(entity: BaseResult<AppUpdateInfo>?) {
                if (entity != null) {
                    if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                        handleCheckUpdateCallback(entity.data)
                    } else {
                        ToastUtil.show(entity.msg)
                    }
                }
            }
        })
    }

    private fun handleCheckUpdateCallback(appInfo: AppUpdateInfo?) {
        if (appInfo == null) {
            return
        }
        if (appInfo.isUpdate != 1) {
            tvVersionInfo.text = "好赞，当前已是最新版本"
            setViewVisible(ivRedDot, false)
            ToastUtil.show("当前已是最新版本")
        } else {
            //需要更新
            tvVersionInfo.text = "最新版本号：" + appInfo.versionName
            setViewVisible(ivRedDot, true)
        }
        updateApp {
            topPic = R.mipmap.app_update_top_bg
            themeColor = Color.parseColor("#3CC2E9")
        }.update(appInfo.isUpdate == 1, appInfo.versionName, appInfo.link, appInfo.content, appInfo.isMandatoryUpdate == 1)


    }
}
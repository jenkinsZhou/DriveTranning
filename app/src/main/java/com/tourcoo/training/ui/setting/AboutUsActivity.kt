package com.tourcoo.training.ui.setting

import android.os.Bundle
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
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import kotlinx.android.synthetic.main.activity_about_us.*

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
}
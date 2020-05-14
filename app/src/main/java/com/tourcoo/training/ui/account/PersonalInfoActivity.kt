package com.tourcoo.training.ui.account

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tourcoo.training.R
import com.tourcoo.training.config.AppConfig
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.manager.GlideManager
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.account.UserInfo
import com.tourcoo.training.entity.account.register.IndustryCategory
import com.tourcoo.training.widget.citypicker.OnCityItemClickListener
import com.tourcoo.training.widget.citypicker.bean.CityBean
import com.tourcoo.training.widget.citypicker.bean.DistrictBean
import com.tourcoo.training.widget.citypicker.bean.ProvinceBean
import com.tourcoo.training.widget.citypicker.cityjd.JDCityConfig
import com.tourcoo.training.widget.citypicker.cityjd.JDCityPicker
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_personal_info.*

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年04月11日23:09
 * @Email: 971613168@qq.com
 */
class PersonalInfoActivity : BaseTitleActivity() {
    override fun getContentLayout(): Int {
        return R.layout.activity_personal_info
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("个人信息")
    }

    override fun initView(savedInstanceState: Bundle?) {
        requestUserInfo()
    }


    private fun requestUserInfo() {
        ApiRepository.getInstance().requestUserInfo().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<UserInfo?>?>() {
            override fun onError(e: Throwable) {
                super.onError(e)

            }

            override fun onSuccessNext(entity: BaseResult<UserInfo?>?) {
                if (entity == null || entity.data == null) {
                    return
                }
                when {
                    entity.code == RequestConfig.CODE_REQUEST_SUCCESS -> {
                        AccountHelper.getInstance().userInfo = entity.data
                        showUserInfo(entity.data)
                    }
                    RequestConfig.CODE_REQUEST_TOKEN_INVALID == entity.code -> {
                        ToastUtil.show("登录已过期")
                    }
                    else -> {
                        ToastUtil.show(entity.msg)
                    }
                }
            }
        })
    }

    private fun showUserInfo(userInfo: UserInfo?) {
        if (userInfo == null) {
            return
        }

        GlideManager.loadCircleImg(userInfo.avatar, civAvatar, R.mipmap.ic_avatar_default)

        GlideManager.loadImageByXml(userInfo.idCardUrl, ivIdCardUrl, R.drawable.img_front)

        tvName.text = userInfo.name
        tvPhone.text = userInfo.phone
        tvIdCard.text = userInfo.idCard
        tvCompanyName.text = userInfo.companyName
        tvPlateNumber.text = if (userInfo.vehicles == null || userInfo.vehicles.size == 0) "" else userInfo.vehicles[0].plateNumber

        tvIndustryCategoryNames.text = userInfo.industryCategoryNames

        llTradeType.setOnClickListener {
            requestTradeTypeList()
        }

        llPhone.setOnClickListener {
            val intent = Intent(this, ChangePhoneActivity::class.java)
            startActivityForResult(intent, 2015)
        }

        llContentIdCard.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                    setClipboard(getTextValue(tvIdCard))
                    ToastUtil.show("复制成功")
                return false
            }

        })

    }


    private fun requestTradeTypeList() {
        ApiRepository.getInstance().requestCategory().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<IndustryCategory>>>("加载中...") {
            override fun onSuccessNext(entity: BaseResult<MutableList<IndustryCategory>>?) {
                initCityPicker(entity!!.data)
                showJD()
            }
        })
    }

    private var industryId = ""
    private var cityPicker: JDCityPicker? = null
    private val jdCityConfig: JDCityConfig = JDCityConfig.Builder().build()
    private var mWheelType: JDCityConfig.ShowType = JDCityConfig.ShowType.PRO_CITY
    private fun initCityPicker(list: MutableList<IndustryCategory>?) {
        cityPicker = JDCityPicker()
        //初始化数据
        //初始化数据
        cityPicker!!.init(this, list)

        mWheelType = JDCityConfig.ShowType.PRO_CITY
        jdCityConfig.showType = mWheelType

        //设置JD选择器样式位只显示省份和城市两级
        //设置JD选择器样式位只显示省份和城市两级
        cityPicker!!.setConfig(jdCityConfig)
        cityPicker!!.setOnCityItemClickListener(object : OnCityItemClickListener() {
            override fun onSelected(province: ProvinceBean?, city: CityBean?, district: DistrictBean?) {
                var proData: String? = null
                if (province != null) {
                    proData = "name:  " + province.getName().toString() + "   id:  " + province.getId()
                }
                var cituData: String? = null
                if (city != null) {
                    cituData = "name:  " + city.getName().toString() + "   id:  " + city.getId()
                    industryId = city.id
                    setIndustryCategory(industryId, city.name)
                }

            }

            override fun onCancel() {}
        })
    }


    private fun setIndustryCategory(industryId: String, name: String) {
        ApiRepository.getInstance().setIndustryCategory(industryId).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>>("加载中...") {
            override fun onSuccessNext(entity: BaseResult<Any>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == 200) {
                    showSelectTradeType(name)
                } else {
                    ToastUtil.showFailed(entity.msg)
                }
            }
        })
    }

    private fun showJD() {
        if (cityPicker == null) {
            ToastUtil.show("未获取到行业类型")
            return
        }
        cityPicker!!.showCityPicker()
    }

    private fun showSelectTradeType(tradeTypeName: String) {
        tvIndustryCategoryNames.text = tradeTypeName
        ToastUtil.show("设置行业类型成功")
            }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            requestUserInfo()
        }
    }


    fun setClipboard(text: String) {
        //获取剪贴板管理器：
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", text)
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData)
    }

}
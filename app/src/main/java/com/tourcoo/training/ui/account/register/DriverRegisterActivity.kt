package com.tourcoo.training.ui.account.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.alibaba.fastjson.JSON
import com.tourcoo.training.R
import com.tourcoo.training.core.base.mvp.BaseMvpTitleActivity
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.RegisterTempHelper
import com.tourcoo.training.entity.account.UserInfo
import com.tourcoo.training.entity.account.register.CompanyInfo
import com.tourcoo.training.ui.account.register.SelectCompanyActivity.Companion.EXTRA_KEY_COMPANY
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
class DriverRegisterActivity : BaseMvpTitleActivity<DriverRegisterPresenter>(), View.OnClickListener, DriverRegisterContract.RegisterView {
    private var mCompanyInfo: CompanyInfo? = null

    companion object {
        const val REQUEST_CODE_COMPANY_SELECT = 1001
    }

    private lateinit var kingKeyboard: KingKeyboard
    override fun getContentLayout(): Int {
        return R.layout.activity_register_driver
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar!!.setTitleMainText("驾驶员注册")
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvGoLogin.setOnClickListener(this)
        tvCompany.setOnClickListener(this)
        tvRegisterDriver.setOnClickListener(this)
        initPlantKeyBoard()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvGoLogin -> {
//                CommonUtil.startActivity(mContext, MyAccountActivity::class.java)
                CommonUtil.startActivity(mContext, StudyMedalRecordActivity::class.java)
            }
            R.id.tvCompany -> {
//                CommonUtil.starAc(mContext, SelectCompanyActivity::class.java)
                val intent = Intent(this, SelectCompanyActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_COMPANY_SELECT)
            }
            R.id.tvRegisterDriver -> {
                doRegister()
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
        presenter.start()
    }

    override fun createPresenter(): DriverRegisterPresenter {
        return DriverRegisterPresenter()
    }


    /* override fun showIdCardInfo(idCardInfo: IdCardInfo?) {
         if (idCardInfo == null) {
             ToastUtil.show("未获取到身份证信息")
             finish()
             return
         }
         etIdCard.setText(idCardInfo.idCard)
         etName.setText(idCardInfo.name)
     }*/


    override fun showCompanyByKeyword(keyWord: String?): String {
        return ""
    }

    override fun registerSuccess(userInfo: UserInfo?) {
        ToastUtil.showSuccess(JSON.toJSONString(userInfo))
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_COMPANY_SELECT -> {
                if (resultCode == RESULT_OK && data != null) {
                    handleCompanyCallback(data.getParcelableExtra<CompanyInfo>(EXTRA_KEY_COMPANY))
                }
            }
            else -> {
            }
        }
    }


    private fun handleCompanyCallback(companyInfo: CompanyInfo?) {
        if (companyInfo != null) {
            mCompanyInfo = companyInfo
            tvCompany.text = mCompanyInfo!!.name
        }
    }


    private fun doRegister() {
        if (RegisterTempHelper.getInstance().idCardInfo == null) {
            ToastUtil.show("未获取到身份证信息")
            return
        }
        if (TextUtils.isEmpty(getTextValue(etPhone))) {
            ToastUtil.show("请填写驾驶员联系电话")
            return
        }
        if (TextUtils.isEmpty(getTextValue(etDriverPlantNum))) {
            ToastUtil.show("请填写驾驶员车牌号")
            return
        }
        if (mCompanyInfo == null) {
            ToastUtil.show("请选择所属公司")
            return
        }
        val map = HashMap<String, Any>()
        map["name"] = RegisterTempHelper.getInstance().idCardInfo.name
        map["idCard"] = RegisterTempHelper.getInstance().idCardInfo.idCard
        map["plateNumber"] = getTextValue(etDriverPlantNum)
        map["phone"] = getTextValue(etPhone)
        map["companyId"] = mCompanyInfo!!.id
        presenter.doRegister(map)
    }
}
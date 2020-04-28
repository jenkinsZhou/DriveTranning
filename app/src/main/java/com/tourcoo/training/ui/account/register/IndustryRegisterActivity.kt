package com.tourcoo.training.ui.account.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.tourcoo.training.R
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.base.mvp.BaseMvpTitleActivity
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.manager.GlideManager
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.StackUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.*
import com.tourcoo.training.entity.account.register.BusinessLicenseInfo
import com.tourcoo.training.entity.account.register.IndustryCategory
import com.tourcoo.training.entity.account.register.Supervisors
import com.tourcoo.training.ui.MainTabActivity
import com.tourcoo.training.widget.citypicker.OnCityItemClickListener
import com.tourcoo.training.widget.citypicker.bean.CityBean
import com.tourcoo.training.widget.citypicker.bean.DistrictBean
import com.tourcoo.training.widget.citypicker.bean.ProvinceBean
import com.tourcoo.training.widget.citypicker.cityjd.JDCityConfig
import com.tourcoo.training.widget.citypicker.cityjd.JDCityPicker
import com.tourcoo.training.widget.dialog.BottomSheetDialog
import com.tourcoo.training.widget.keyboard.KingKeyboard
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_register_industrial.*
import org.apache.commons.lang.StringUtils
import org.greenrobot.eventbus.EventBus

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月04日17:38
 * @Email: 971613168@qq.com
 */
class IndustryRegisterActivity : BaseMvpTitleActivity<IndustryRegisterPresenter>(), View.OnClickListener, IndustryRegisterContract.RegisterView {
    private val mTag = "IndustrialRegisterActivity"
    private var selectTradeId = ""
    private var selectFileList: MutableList<LocalMedia>? = ArrayList()
    private var imageDiskPathList: MutableList<String>? = ArrayList()
    private lateinit var kingKeyboard: KingKeyboard
    private var businessLicenseInfo: BusinessLicenseInfo? = null
    private var industryId = ""
    private var cityPicker: JDCityPicker? = null
    private var mWheelType: JDCityConfig.ShowType = JDCityConfig.ShowType.PRO_CITY

    private val jdCityConfig: JDCityConfig = JDCityConfig.Builder().build()

    private var supervisor: Supervisors? = null

    private var pvCustomOptions: OptionsPickerView<String>? = null
    private val options1Items: ArrayList<String> = ArrayList()

    override fun getContentLayout(): Int {
        return R.layout.activity_register_industrial
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar!!.setTitleMainText("个体工商户注册")
    }

    override fun initView(savedInstanceState: Bundle?) {
        businessLicenseInfo = AccountTempHelper.getInstance().businessLicenseInfo
        if (businessLicenseInfo == null || businessLicenseInfo!!.supervisors == null) {
            ToastUtil.show("未识别出营业执照信息")
            finish()
            return
        }
        tvRegisterIndustrial.setOnClickListener(this)
        tvGoLogin.setOnClickListener(this)
        ivSelectIndustry.setOnClickListener(this)
        rlPickImage.setOnClickListener(this)
        tvAreaSelect.setOnClickListener(this)
        ivSelectedImage.setOnClickListener(this)
        tvTradeType.setOnClickListener(this)
        initPicker(businessLicenseInfo!!.supervisors)
        initPlantKeyBoard()
        initCityPickerConfig()

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvRegisterIndustrial -> {
                doRegister()
            }
            R.id.tvTradeType -> {
                showJD()
            }
            R.id.tvGoLogin -> {
                CommonUtil.startActivity(mContext, RecognizeIdCardActivity::class.java)
            }


            R.id.ivSelectedImage -> {
                selectPic()
            }
            R.id.rlPickImage -> {
                selectPic()
            }
            R.id.ivSelectIndustry -> {
                requestTradeTypeList()
            }
            R.id.tvAreaSelect -> {
                pvCustomOptions!!.show()
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


    private fun selectPic() {
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1) // 最小选择数量
                .theme(R.style.picture_default_style)
                .minSelectNum(1) // 每行显示个数
                .imageSpanCount(4) // 多选 or 单选
                .selectionMode(PictureConfig.SINGLE) // 是否可预览图片
                .previewImage(true) // 是否可播放音频
                .enablePreviewAudio(false) // 是否显示拍照按钮
                .isCamera(true) // 图片列表点击 缩放效果 默认true
                .isZoomAnim(true) //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
//.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
// 是否裁剪
                .enableCrop(true)
                // 是否压缩
                .compress(true)
                //同步true或异步false 压缩 默认同步
                .synOrAsy(true)
                //.compressSavePath(getPath())//压缩图片保存地址
//.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .glideOverride(160, 160)
                // 是否显示uCrop工具栏，默认不显示
                .hideBottomControls(false)
                // 是否显示gif图片
                .isGif(false)
                // 裁剪框是否可拖拽
                .freeStyleCropEnabled(false)
                // 是否传入已选图片
                .selectionMedia(selectFileList)
                // 小于100kb的图片不压缩
                .minimumCompressSize(100)
                //结果回调onActivityResult code
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    selectFileList = PictureSelector.obtainMultipleResult(data)
                    handlePictureSelectCallback()
                }
                else -> {
                }
            }
        }
    }

    private fun handlePictureSelectCallback() {
        for (localMedia in selectFileList!!) {
            imageDiskPathList?.add(localMedia.compressPath)
        }
//        val path = parsePath(imageDiskPathList)
        GlideManager.loadImg(selectFileList!![0].compressPath, ivSelectedImage)
        val par = ivSelectedImage.layoutParams
        ivSelectedImage.layoutParams.height = rlPickImage.height
        ToastUtil.show("--->" + rlPickImage.height)
        ivSelectedImage.layoutParams = par
//        ToastUtil.show("---"+ivSelectedImage.height)
        setViewGone(ivSelectedImage, true)
        selectFileList?.clear()
        TourCooLogUtil.i("高度:" + ivSelectedImage.layoutParams.height)
    }

    private fun parsePath(imageList: List<String>?): String? {
        if (imageList == null || imageList.isEmpty()) {
            return ""
        }
        return if (imageList.size == 1) {
            CommonUtil.getNotNullValue(imageList[0])
        } else StringUtils.join(imageList, ",")
    }


    private fun requestTradeTypeList() {
        ApiRepository.getInstance().requestTradeType().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<TradeType>>>("加载中...") {
            override fun onSuccessNext(entity: BaseResult<MutableList<TradeType>>?) {
//                ToastUtil.show("en"+entity?.data)
//                TourCooLogUtil.i(mTag, entity?.data)
                showIndustryList(entity!!.data)
            }
        })
    }


    private fun showIndustryList(list: MutableList<TradeType>?) {
        if (list == null) {
            ToastUtil.show("未获取到行业类型")
            return
        }
        val dialog = BottomSheetDialog(mContext)
        for (tradeType in list) {
            val item = BottomSheetDialog.SheetItem(tradeType.name, BottomSheetDialog.OnSheetItemClickListener {
                ToastUtil.show("点击了：行业id" + tradeType.id)
                selectTradeId = tradeType.id
                showSelectTradeType(tradeType.name)
            })
            dialog.addSheetItem(item)
        }
        dialog.create().show()
    }

    private fun showSelectTradeType(tradeTypeName: String) {
        tvTradeType.text = tradeTypeName
    }

    override fun loadPresenter() {
        presenter.start()
    }

    override fun createPresenter(): IndustryRegisterPresenter {
        return IndustryRegisterPresenter()
    }

    override fun loginSuccess(userInfo: UserInfo?) {
        handleLoginCallback(userInfo)
    }

    override fun registerSuccess(userInfo: Any?) {
        if (userInfo != null) {
            //保存用户信息
//            AccountHelper.getInstance().userInfo = userInfo
            ToastUtil.show("注册成功")
            finish()
            StackUtil.getInstance().previous?.finish()
            StackUtil.getInstance().getActivity(RecognizeLicenseActivity::class.java)?.finish()
        }

    }

    override fun initIndustry(list: MutableList<IndustryCategory>?) {
        initCityPicker(list)
    }


    private fun doRegister() {
        if (businessLicenseInfo == null) {
            ToastUtil.show("请选择所属公司")
            return
        }
        if (TextUtils.isEmpty(getTextValue(etName))) {
            ToastUtil.show("请填写姓名")
            return
        }
        if (TextUtils.isEmpty(getTextValue(etPhone))) {
            ToastUtil.show("请填写电话")
            return
        }
        if (TextUtils.isEmpty(getTextValue(etIdCard))) {
            ToastUtil.show("请填写身份证号")
            return
        }
        if (TextUtils.isEmpty(getTextValue(etDriverPlantNum))) {
            ToastUtil.show("请填写车牌号")
            return
        }
        if (supervisor == null) {
            ToastUtil.show("请选择所属区域")
            return
        }
        if (TextUtils.isEmpty(industryId)) {
            ToastUtil.show("请输入行业类型")
            return
        }
        val map = HashMap<String, Any>()
        map["name"] = getTextValue(etName)
        map["idCard"] = getTextValue(etIdCard)
        map["plateNumber"] = getTextValue(etDriverPlantNum)
        map["phone"] = getTextValue(etPhone)
        //todo
        //行业类型
        map["industryCategoryId"] = industryId
        //区域选择
        map["supervisorId"] = supervisor!!.id
        map["creditCode"] = businessLicenseInfo!!.creditCode
        presenter.doRegister(map)
    }

    private fun initCityPickerConfig() {
        mWheelType = JDCityConfig.ShowType.PRO_CITY
        setWheelType(mWheelType)
        jdCityConfig.showType = mWheelType
    }

    /**
     * @param wheelType
     */
    private fun setWheelType(wheelType: JDCityConfig.ShowType) {
        if (wheelType === JDCityConfig.ShowType.PRO_CITY) {
            /*  mTwoTv.setBackgroundResource(R.drawable.city_wheeltype_selected)
              mThreeTv.setBackgroundResource(R.drawable.city_wheeltype_normal)
              mTwoTv.setTextColor(Color.parseColor("#ffffff"))
              mThreeTv.setTextColor(Color.parseColor("#333333"))*/
        } else {
            /* mTwoTv.setBackgroundResource(R.drawable.city_wheeltype_normal)
             mThreeTv.setBackgroundResource(R.drawable.city_wheeltype_selected)
             mTwoTv.setTextColor(Color.parseColor("#333333"))
             mThreeTv.setTextColor(Color.parseColor("#ffffff"))*/
        }
    }


    private fun initCityPicker(list: MutableList<IndustryCategory>?) {
        cityPicker = JDCityPicker()
        //初始化数据
        //初始化数据
        cityPicker!!.init(this, list)
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
                    showSelectTradeType(city.name)

                }

            }

            override fun onCancel() {}
        })
    }


    private fun showJD() {
        if (cityPicker == null) {
            ToastUtil.show("未获取到行业类型")
            return
        }
        cityPicker!!.showCityPicker()
    }


    /**
     * @description 注意事项：
     * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
     * 具体可参考demo 里面的两个自定义layout布局。
     */
    private fun initPicker(list: MutableList<Supervisors>?) {
        for (sup in list!!) {
            options1Items.add(sup.name)
        }
        pvCustomOptions = OptionsPickerBuilder(context, OnOptionsSelectListener { options1, options2, options3, v ->
            if( businessLicenseInfo!!.supervisors == null || businessLicenseInfo!!.supervisors.isEmpty()){
                return@OnOptionsSelectListener
            }
            supervisor = businessLicenseInfo!!.supervisors[options1]
            tvAreaSelect.text = supervisor?.name
        })
                .setCyclic(false, false, false)
                .isDialog(false)
                .setContentTextSize(18)
                .setLineSpacingMultiplier(2.0f)
                .setDividerColor(CommonUtil.getColor(R.color.transparent))
                .setTextColorOut(CommonUtil.getColor(R.color.gray999999))
                .setTextColorCenter(CommonUtil.getColor(R.color.black333333))
                .build()
        pvCustomOptions!!.setPicker(options1Items)
        //添加数据
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
}
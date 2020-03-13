package com.tourcoo.training.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.manager.GlideManager
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.widget.keyboard.KingKeyboard
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_register_industrial.*
import org.apache.commons.lang.StringUtils

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月04日17:38
 * @Email: 971613168@qq.com
 */
class IndustrialRegisterActivity : BaseTitleActivity(), View.OnClickListener {
    private val mTag = "IndustrialRegisterActivity"
    private var selectFileList: MutableList<LocalMedia>? = ArrayList()
    private var imageDiskPathList: MutableList<String>? = ArrayList()
    private lateinit var kingKeyboard: KingKeyboard
    override fun getContentLayout(): Int {
        return R.layout.activity_register_industrial
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar!!.setTitleMainText("个体工商户注册")
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvRegisterIndustrial.setOnClickListener(this)
        tvGoLogin.setOnClickListener(this)
        ivSelectIndustry.setOnClickListener(this)
        rlPickImage.setOnClickListener(this)
        ivSelectedImage.setOnClickListener(this)
        initPlantKeyBoard()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvRegisterIndustrial -> {
                CommonUtil.startActivity(mContext, DriverRegisterActivity::class.java)
//                CommonUtil.startActivity(mContext, UploadIdCardActivity::class.java)
            }
            R.id.tvGoLogin -> {
//                CommonUtil.startActivity(mContext, LoginActivity::class.java)
                CommonUtil.startActivity(mContext, UploadIdCardActivity::class.java)
            }
            R.id.ivSelectedImage->{
                selectPic()
            }
            R.id.rlPickImage -> {
                selectPic()
            }
            R.id.ivSelectIndustry->{
                requestTradeTypeList()
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
        ivSelectedImage.layoutParams.height =  rlPickImage.height
        ToastUtil.show("--->"+rlPickImage.height)
        ivSelectedImage.layoutParams = par
//        ToastUtil.show("---"+ivSelectedImage.height)
        setViewGone(ivSelectedImage,true)
        selectFileList?.clear()
       TourCooLogUtil.i("高度:"+ivSelectedImage.layoutParams.height)
    }

    private fun parsePath(imageList: List<String>?): String? {
        if (imageList == null || imageList.isEmpty()) {
            return ""
        }
        return if (imageList.size == 1) {
            CommonUtil.getNotNullValue(imageList[0])
        } else StringUtils.join(imageList, ",")
    }



    private fun requestTradeTypeList(){
        ApiRepository.getInstance().requestTradeType().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object :BaseLoadingObserver<BaseResult<MutableList<*>>>("加载中..."){
            override fun onSuccessNext(entity: BaseResult<MutableList<*>>?) {
//                ToastUtil.show("en"+entity?.data)
                TourCooLogUtil.i(mTag, entity?.data)
            }

        })
    }
}
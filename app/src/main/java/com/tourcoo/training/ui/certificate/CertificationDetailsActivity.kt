package com.tourcoo.training.ui.certificate

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blankj.utilcode.util.ImageUtils
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.UiManager
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BasePageResult
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.manager.GlideManager
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.certificate.CertifyDetail
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_certification_details.*
import com.tencent.liteav.muxer.R.id.image
import androidx.core.app.ComponentActivity.ExtraData
import com.trello.rxlifecycle3.RxLifecycle.bindUntilEvent
import androidx.core.content.ContextCompat.getSystemService
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SizeUtils
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tourcoo.training.constant.TrainingConstant
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.entity.medal.MedalDictionary
import com.tourcoo.training.ui.training.StudyMedalRecordActivity
import com.tourcoo.training.utils.TourImageUtils
import com.tourcoo.training.widget.dialog.medal.MedalDialog
import com.tourcoo.training.widget.dialog.share.BottomShareDialog
import com.tourcoo.training.widget.dialog.share.ShareEntity
import com.yzq.zxinglibrary.encode.CodeCreator


class CertificationDetailsActivity : BaseTitleActivity() {

    private var mCertifyId: String = ""
    private var api: IWXAPI? = null

    override fun getContentLayout(): Int {
        return R.layout.activity_certification_details
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("学习证书")
    }

    override fun initView(savedInstanceState: Bundle?) {
        mCertifyId = intent.getStringExtra("id")
        TourCooLogUtil.d("证书id=" + mCertifyId)
        api = WXAPIFactory.createWXAPI(mContext, TrainingConstant.APP_ID)

        requestCertificate()
        requestMedalDictionary()
    }


    private fun requestCertificate() {
        ApiRepository.getInstance().requestCertificateDetail(mCertifyId).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<CertifyDetail>>() {
            override fun onSuccessNext(entity: BaseResult<CertifyDetail>?) {
                if (entity == null) {
                    return
                }
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    showDetail(entity.data)
                } else {
                    ToastUtil.show(entity.getMsg())
                }
            }
        })
    }


    private fun showDetail(detail: CertifyDetail?) {
        if (detail == null) {
            ToastUtil.show("证书信息获取失败")
            return
        }
        GlideManager.loadCircleImg(CommonUtil.getUrl(detail.avatar), ivAvatar)
        GlideManager.loadImg(CommonUtil.getUrl(detail.certificateImage), ivImage)

        val contentEtString = TrainingConstant.STUDY_SHARE_URL + "?TraineeID=${SPUtils.getInstance().getString("TraineeID")}&trainingPlanID=${detail.trainingPlanID}"
        val bitmap = CodeCreator.createQRCode(contentEtString, 500, 500, null)
        ivCode.setImageBitmap(bitmap)
        ivCode.isEnabled = false
        btnSave.setOnClickListener {
            val bitmap = (ivImage.drawable as BitmapDrawable).bitmap
            TourImageUtils.saveBitmap2Gallery(this, bitmap)
        }

        btnShare.setOnClickListener {
            val bitmap = ImageUtils.view2Bitmap(llShare)
            share(bitmap)
        }

    }


    private fun share(bmp: Bitmap) {
        if (!api!!.isWXAppInstalled) {
            ToastUtil.show("您尚未安装微信客户端")
            return
        }

        val wx = ShareEntity("微信", R.mipmap.ic_share_type_wx)
        val pyq = ShareEntity("朋友圈", R.mipmap.ic_share_type_friend)
        val dialog = BottomShareDialog(mContext).create().addData(wx).addData(pyq)
        dialog.setItemClickListener { adapter, view, position ->
            dialog.dismiss()
            when (position) {
                0 -> shareImageToWx(api!!, bmp, true)
                1 ->  //朋友圈
                    shareImageToWx(api!!, bmp, false)
            }
        }
        dialog.show()


    }

    /**
     * 分享图片到微信
     */

    private fun shareImageToWx(api: IWXAPI, bmp: Bitmap, isSession: Boolean) {
        //初始化 WXImageObject 和 WXMediaMessage 对象
        val imgObj = WXImageObject(bmp)
        val msg = WXMediaMessage()
        msg.mediaObject = imgObj

        //设置缩略图
        val thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 350, true)
        bmp.recycle()
        msg.thumbData = ImageUtils.bitmap2Bytes(thumbBmp, Bitmap.CompressFormat.JPEG)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        //表示发送给朋友圈  WXSceneTimeline  表示发送给朋友  WXSceneSession
        req.scene = if (isSession) SendMessageToWX.Req.WXSceneSession else SendMessageToWX.Req.WXSceneTimeline
        api.sendReq(req)
    }

    private fun buildTransaction(type: String?): String {
        return if (type == null) System.currentTimeMillis().toString() else type + System.currentTimeMillis()
    }


    /**
     * 获取勋章领取条件接口
     */
    private fun requestMedalDictionary() {
        ApiRepository.getInstance().requestMedalDictionary().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MedalDictionary>>() {
            override fun onSuccessNext(entity: BaseResult<MedalDictionary>?) {
                if (entity == null) {
                    return
                }
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS && entity.data != null) {
                    //显示勋章
                    showMedalDialog(entity.data.certificate.toInt())
                }
            }
        })
    }

    private fun showMedalDialog(number: Int) {
        val dialog = MedalDialog(mContext)
        dialog.create()
                .setMedal(2, number, AccountTempHelper.getInstance().studyMedalEntity)
                .setPositiveButtonListener {
                    dialog.dismiss()
                    val intent = Intent(this, StudyMedalRecordActivity::class.java)
                    startActivityForResult(intent, 2017)
                }.show()
    }


}

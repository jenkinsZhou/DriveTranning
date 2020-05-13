package com.tourcoo.training.ui.home.news

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import com.blankj.utilcode.util.SPUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tourcoo.training.R
import com.tourcoo.training.adapter.news.NewsMultipleAdapter
import com.tourcoo.training.constant.CommonConstant
import com.tourcoo.training.constant.CommonConstant.*
import com.tourcoo.training.constant.NewsConstant.REQUEST_CODE_NEWS_DETAIL
import com.tourcoo.training.constant.TrainingConstant
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.base.fragment.BaseTitleMvpRefreshLoadFragment
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.SizeUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.news.NewsEntity
import com.tourcoo.training.entity.pay.WxShareEvent
import com.tourcoo.training.widget.dialog.share.BottomShareDialog
import com.tourcoo.training.widget.dialog.share.ShareEntity
import com.trello.rxlifecycle3.android.FragmentEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayOutputStream

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月28日10:02
 * @Email: 971613168@qq.com
 */
class NewsTabFragment : BaseTitleMvpRefreshLoadFragment<NewsListPresenter, NewsEntity>(), NewsListContract.NewsListView {
    private var adapter: NewsMultipleAdapter? = null
    private var api: IWXAPI? = null
    override fun getAdapter(): BaseQuickAdapter<NewsEntity, BaseViewHolder> {
        val list: MutableList<NewsEntity>? = ArrayList()
        adapter = NewsMultipleAdapter(list)
        return adapter!!
    }

    private var mNewsEntity: NewsEntity? = null
    override fun loadPresenter() {

    }


    override fun setTitleBar(titleBar: TitleBarView?) {
        if (titleBar != null) {
            titleBar.setBgResource(R.drawable.bg_gradient_blue_53c2ff_4e52ff)
//            titleBar.setLeftTextDrawable(R.drawable.ic_back_white)
            titleBar.setTitleMainTextColor(CommonUtil.getColor(R.color.white))
            titleBar.height = SizeUtil.dp2px(45f)
            titleBar.setTitleMainText("资讯")
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        api = WXAPIFactory.createWXAPI(mContext, TrainingConstant.APP_ID)
        adapter!!.setOnItemClickListener { adapter, view, position ->
            val news = adapter.data[position] as NewsEntity
            //必须赋值
            mNewsEntity = news
            val bundle = Bundle()
            bundle.putSerializable(EXTRA_NEWS_BEAN, news)
            if (!TextUtils.isEmpty(CommonUtil.getNotNullValue(news.videoUrl))) {
                val intent = Intent(mContext, NewsDetailVideoActivity::class.java)
                intent.putExtras(bundle)
                intent.putExtra(CommonConstant.EXTRA_KEY_POSITION, position)
                startActivityForResult(intent, REQUEST_CODE_NEWS_DETAIL)
            } else {
                //网页
                val intent = Intent(mContext, NewsDetailHtmlActivity::class.java)
                intent.putExtras(bundle)
                intent.putExtra(CommonConstant.EXTRA_KEY_POSITION, position)
                startActivityForResult(intent, REQUEST_CODE_NEWS_DETAIL)
            }
        }
        adapter!!.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.llShare -> {
                    val entity = adapter.data[position] as NewsEntity
                    showShareDialog(entity)
                }
                else -> {
                }
            }
            val news = adapter.data[position] as NewsEntity
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.frame_layout_title_refresh_recycler
    }

    override fun loadData(page: Int) {
        presenter.getNewsList(page)
    }


    override fun createPresenter(): NewsListPresenter {
        return NewsListPresenter()
    }


    companion object {
        fun newInstance(): NewsTabFragment {
            val args = Bundle()
            val fragment = NewsTabFragment()
            fragment.arguments = args
            return fragment
        }

        const val EXTRA_NEWS_BEAN = "EXTRA_NEWS_BEAN"

    }


    private fun wxSharePic(isSession: Boolean, mNewsEntity: NewsEntity) { //初始化WXImageObject和WXMediaMessage对象
        val webPage = WXWebpageObject()
        webPage.webpageUrl = TrainingConstant.NEWS_SHARE_URL + "?id=" + mNewsEntity.id +
                "&TraineeID=" + SPUtils.getInstance().getString("TraineeID")
        val msg = WXMediaMessage(webPage)
        msg.title = mNewsEntity.getTitle()
        msg.description = mNewsEntity.getTitle()
        val bmp = BitmapFactory.decodeResource(resources, R.mipmap.ic_news_share_icon)
        val thumbBmp = Bitmap.createScaledBitmap(bmp, SizeUtil.dp2px(41f), SizeUtil.dp2px(40f), true)
        bmp.recycle()
        msg.thumbData = bmpToByteArray(thumbBmp, true)
        val req = SendMessageToWX.Req()
        req.transaction = "webPage" + System.currentTimeMillis()
        req.message = msg

//表示发送给朋友圈  WXSceneTimeline  表示发送给朋友  WXSceneSession
        req.scene = if (isSession) SendMessageToWX.Req.WXSceneSession else SendMessageToWX.Req.WXSceneTimeline
        //调用api接口发送数据到微信
        api!!.sendReq(req)
        /* bitmap.recycle();
        scaledBitmap.recycle();*/
    }


    fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray? {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)
        if (needRecycle) {
            bmp.recycle()
        }
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun showShareDialog(mNewsEntity: NewsEntity) {
        val wx = ShareEntity("微信", R.mipmap.ic_share_type_wx)
        val pyq = ShareEntity("朋友圈", R.mipmap.ic_share_type_friend)
        val dialog = BottomShareDialog(mContext).create().addData(wx).addData(pyq)
        dialog.setItemClickListener { adapter, view, position ->
            dialog.dismiss()
            when (position) {
                0 -> wxSharePic(true, mNewsEntity)
                1 ->  //朋友圈
                    wxSharePic(false, mNewsEntity)
            }
        }
        dialog.show()
    }

    override fun onDestroy() {
        if (api != null) {
            api!!.detach()
        }
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


    /**
     * 收到消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWxPayCallbackEvent(event: WxShareEvent?) {
        if (event == null) {
            return
        }
        TourCooLogUtil.i(TAG, "执行了" + mNewsEntity)
        if (event.isShareSuccess && mNewsEntity != null) {
            requestShareSuccess(mNewsEntity!!.getID())
            TourCooLogUtil.i(TAG, "执行了1")
        }
    }

    private fun requestShareSuccess(newsId: String) {
        ApiRepository.getInstance().requestShareSuccess(newsId + "").compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<Any>?>() {
            override fun onSuccessNext(entity: BaseResult<Any>?) {
                baseHandler.postDelayed(Runnable {
                    mRefreshLayout.autoRefresh()
                }, 200)
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_NEWS_DETAIL -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val position = data.getIntExtra(EXTRA_KEY_POSITION, -1)
                        if (position < 0) {
                            TourCooLogUtil.e("未找到对应位置")
                            return
                        }
                        val readNumber = data.getIntExtra(EXTRA_KEY_READ_NUMBER, -1)
                        val shareNumber = data.getIntExtra(EXTRA_KEY_SHARE_NUMBER, -1)

                        val news = adapter?.getItem(position)
                        news?.readTotal = readNumber
                        news?.sharedNum = shareNumber
                        baseHandler.postDelayed(Runnable {
                            adapter?.notifyItemChanged(position)
                        }, 500)
                    }
                } else {

                }
            }
            else -> {
            }
        }


    }
}




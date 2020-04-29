package com.tourcoo.training.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.tourcoo.training.R
import com.tourcoo.training.adapter.mine.MineItemAdapter
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.base.fragment.BaseTitleFragment
import com.tourcoo.training.core.manager.GlideManager
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.StatusBarUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.account.UserInfo
import com.tourcoo.training.entity.account.UserInfoEvent
import com.tourcoo.training.entity.account.VehicleInfo
import com.tourcoo.training.entity.mine.MineItem
import com.tourcoo.training.ui.account.FindPassActivity
import com.tourcoo.training.ui.account.LoginActivity
import com.tourcoo.training.ui.account.MyAccountActivity
import com.tourcoo.training.ui.account.PersonalInfoActivity
import com.tourcoo.training.ui.certificate.MyCertificationActivity
import com.tourcoo.training.ui.exam.OnlineExamActivity
import com.tourcoo.training.ui.guide.GuideActivity
import com.tourcoo.training.ui.setting.SettingActivity
import com.tourcoo.training.ui.training.professional.ProfessionalExamSelectActivity
import com.tourcoo.training.widget.aliplayer.activity.AliyunPlayerSkinActivity
import com.tourcoo.training.widget.dialog.exam.CommitAnswerDialog
import com.tourcoo.training.widget.dialog.exam.ExamNotPassDialog
import com.tourcoo.training.widget.dialog.exam.ExamPassDialog
import com.tourcoo.training.widget.dialog.medal.MedalDialog
import com.tourcoo.training.widget.dialog.training.LocalTrainingAlert
import com.tourcoo.training.widget.dialog.training.LocalTrainingConfirmDialog
import com.trello.rxlifecycle3.android.FragmentEvent
import kotlinx.android.synthetic.main.fragment_mine_tab_new.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月13日10:20
 * @Email: 971613168@qq.com
 */
class MineTabFragmentNew : BaseTitleFragment(), View.OnClickListener, OnRefreshListener {
    private var accountAdapter: MineItemAdapter? = null
    private var achievementAdapter: MineItemAdapter? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_mine_tab_new
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.visibility = View.GONE
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivSetting -> {
                CommonUtil.startActivity(mContext, SettingActivity::class.java)
            }

            R.id.llAvatar -> {
                skipPersonalInfo()
            }
            R.id.ivAddCar -> {
                CommonUtil.startActivity(mContext, OnlineExamActivity::class.java)
            }
            R.id.llGoldLevel -> {
                CommonUtil.startActivity(mContext, FindPassActivity::class.java)
            }
            R.id.llUserInfo -> {
                skipPersonalInfo()
            }
            else -> {
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshUserInfo()
    }


    override fun loadData() {
        super.loadData()
        ivSetting.setOnClickListener(this)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        setStatusBarModeWhite(this)
        setMarginTop()
        smartRefreshLayoutCommon.setRefreshHeader(ClassicsHeader(mContext))
        smartRefreshLayoutCommon.setOnRefreshListener(this)
        accountAdapter = MineItemAdapter()
        achievementAdapter = MineItemAdapter()
        accountAdapter!!.bindToRecyclerView(rvMyAccount)
        achievementAdapter!!.bindToRecyclerView(rvStudyAchievement)
        rvMyAccount.layoutManager = GridLayoutManager(mContext, 3)
        rvStudyAchievement.layoutManager = GridLayoutManager(mContext, 4)
        llAvatar.setOnClickListener(this)
        llUserInfo.setOnClickListener(this)
        initItemClick()
        loadMineAccount()
        loadAchievement()
        if (AccountHelper.getInstance().isLogin) {
            showUserInfo(AccountHelper.getInstance().userInfo)
        } else {
            refreshUserInfo()
        }

    }

    private fun setMarginTop() {
        val layoutParams: ViewGroup.LayoutParams = rlTitleBarContent.layoutParams
        if (layoutParams is LinearLayout.LayoutParams) {
            layoutParams.setMargins(0, marginTop, 0, 0)
        } else if (layoutParams is RelativeLayout.LayoutParams) {
            layoutParams.setMargins(0, marginTop, 0, 0)
        } else if (layoutParams is FrameLayout.LayoutParams) {
            layoutParams.setMargins(0, marginTop, 0, 0)
        }
    }

    private fun initItemClick() {
        accountAdapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            when (position) {
                0, 1 -> {
                    CommonUtil.startActivity(mContext, MyAccountActivity::class.java)
                }
                2 ->
                    CommonUtil.startActivity(mContext, GuideActivity::class.java)
//                    showDialog3()
                else -> {

                }
            }
        }
        achievementAdapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            when (position) {
                0 -> {

                }

                1 -> {

                }

                2 -> {

                }

                3 -> {
                    CommonUtil.startActivity(mContext, MyCertificationActivity::class.java)
                }

                else -> {
                }
            }
        }
    }

    //本次现场培训已结束
    private fun showDialog() {
        val alert = LocalTrainingAlert(mContext)
        alert.create().show()
    }

    private fun showDialog1() {
        val dialog = LocalTrainingConfirmDialog(mContext)
        dialog.create().show()
    }

    private fun showDialog2() {
        val dialog = MedalDialog(mContext)
        dialog.create().show()
    }

    private fun showDialog3() {
        val dialog = CommitAnswerDialog(mContext)
        dialog.create().show()
    }


    private fun requestUserInfo() {
        ApiRepository.getInstance().requestUserInfo().compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<UserInfo?>?>() {
            override fun onError(e: Throwable) {
                super.onError(e)
                smartRefreshLayoutCommon.finishRefresh()

            }

            override fun onSuccessNext(entity: BaseResult<UserInfo?>?) {
                smartRefreshLayoutCommon.finishRefresh(true)
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
            showUnLogin()
            return
        }
        if (userInfo.vehicles != null && userInfo.vehicles.isNotEmpty()) {
            //有车辆
            setViewGone(rlMyCarInfo, true)
            setViewGone(llNoCar, false)
            showCarInfo(userInfo.vehicles[0])
        } else {
            //没有车辆
            setViewGone(llNoCar, true)
            setViewGone(rlMyCarInfo, false)
        }
        setViewVisible(llGoldLevel, true)
        setViewVisible(monthRate, true)

        tvMonthRanking.text = userInfo.monthRanking

        tvUserNickName.text = userInfo.name
        progressBarOnLine.progress = userInfo.onlineLearnProgress.toInt()
        tvOnlineProgress.text = "" + progressBarOnLine.progress + "%"
        progressBarLocal.progress = userInfo.onsiteLearnProgress.toInt()
        tvOfflineProgress.text = "" + progressBarLocal.progress + "%"
        GlideManager.loadImg(userInfo.avatar, civAvatar, R.mipmap.ic_avatar_default)
        tvCoinsRemain.text = "剩余学币： " + CommonUtil.doubleTransStringZhen(userInfo.coinsRemain)
    }


    private fun refreshUserInfo() {
        if (!AccountHelper.getInstance().isLogin) {
            showUnLogin()
            smartRefreshLayoutCommon.finishRefresh()
            return
        }
        requestUserInfo()
    }

    private fun showUnLogin() {
        showNoCar()
        tvCoinsRemain.text = "剩余学币：0"
        tvUserNickName.text = "登录/注册"
        GlideManager.loadImg(R.mipmap.ic_avatar_default, civAvatar)

    }

    companion object {
        fun newInstance(): MineTabFragmentNew {
            val args = Bundle()
            val fragment = MineTabFragmentNew()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getMarginTop(): Int {
        return StatusBarUtil.getStatusBarHeight()
    }

    private fun loadMineAccount() {
        val accountItem = MineItem()
        accountItem.iconId = R.drawable.icon_account
        accountItem.itemName = "账户"
        val chargeItem = MineItem()
        chargeItem.iconId = R.drawable.icon_recharge
        chargeItem.itemName = "充值"
        val orderItem = MineItem()
        orderItem.iconId = R.drawable.icon_indent
        orderItem.itemName = "订单"
        val accountList: MutableList<MineItem> = ArrayList<MineItem>()
        accountList.add(accountItem)
        accountList.add(chargeItem)
        accountList.add(orderItem)
        accountAdapter!!.setNewData(accountList)
    }

    private fun loadAchievement() {
        val studyItem = MineItem()
        studyItem.iconId = R.drawable.icon_record
        studyItem.itemName = "学习记录"
        val dataItem = MineItem()
        dataItem.iconId = R.drawable.icon_data
        dataItem.itemName = "学习数据"
        val medalItem = MineItem()
        medalItem.iconId = R.drawable.icon_medal
        medalItem.itemName = "学习勋章"
        val studyCertifyItem = MineItem()
        studyCertifyItem.iconId = R.drawable.icon_credential
        studyCertifyItem.itemName = "学习证书"
        val accountList: MutableList<MineItem> = ArrayList()
        accountList.add(studyItem)
        accountList.add(dataItem)
        accountList.add(medalItem)
        accountList.add(studyCertifyItem)
        achievementAdapter!!.setNewData(accountList)
    }


    private fun skipPersonalInfo() {
        if (!AccountHelper.getInstance().isLogin) {
            skipLogin()
            return
        }
        val intent = Intent(mContext, PersonalInfoActivity::class.java)
        startActivity(intent)

    }

    private fun skipLogin() {
        val intent = Intent()
        intent.setClass(mContext, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showNoCar() {
        setViewGone(llNoCar, true)
        setViewGone(rlMyCarInfo, false)
    }

    private fun showCarInfo(vehicleInfo: VehicleInfo?) {
        if (vehicleInfo == null) {
            showNoCar()
            return
        }
        tvCarPlantNum.text = "车牌号：" + CommonUtil.getNotNullValue(vehicleInfo.plateNumber)
        tvCarModule.text = " 车型：" + CommonUtil.getNotNullValue(vehicleInfo.model)
        tvCarBrand.text = "品牌：" + CommonUtil.getNotNullValue(vehicleInfo.brand)
        tvCarExpire.text = "年审到期 " + CommonUtil.getNotNullValue(vehicleInfo.expiredTime)
    }


    /**
     * 收到消息
     *
     * @param userInfoEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserInfoRefreshEvent(userInfoEvent: UserInfoEvent?) {
        if (userInfoEvent == null) {
            return
        }
        if (userInfoEvent.userInfo == null) {
            showUnLogin()
        } else {
            showUserInfo(userInfoEvent.userInfo)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
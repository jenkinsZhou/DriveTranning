package com.tourcoo.training.ui.training

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tourcoo.training.R
import com.tourcoo.training.adapter.study.StudyMedalAdapter
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.AccountTempHelper
import com.tourcoo.training.entity.medal.MedalInfo
import com.tourcoo.training.entity.medal.StudyMedalEntity
import com.tourcoo.training.entity.study.StudyMedal
import com.tourcoo.training.entity.study.StudyMedalGroup
import com.tourcoo.training.entity.study.StudyMedalGroup.ITEM_TYPE_CONTENT
import com.tourcoo.training.entity.study.StudyMedalGroup.ITEM_TYPE_HEADER
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_study_record_medal.*
import kotlinx.android.synthetic.main.frame_layout_recycler.*
import kotlinx.android.synthetic.main.frame_layout_refresh_recycler.*

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年03月05日23:47
 * @Email: 971613168@qq.com
 */
class StudyMedalRecordActivity : BaseTitleActivity() {

    companion object {
        const val MEDAL_STATUS_UN_LOCKED = 0
        const val MEDAL_STATUS_LOCKED = 1

    }

    private var adapter: StudyMedalAdapter? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_study_record_medal
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("学习勋章")
    }

    override fun initView(savedInstanceState: Bundle?) {
        smartRefreshLayoutCommon.setRefreshHeader(ClassicsHeader(mContext))
        adapter = StudyMedalAdapter(ArrayList())
        val manager = GridLayoutManager(mContext, 3)
        rvCommon.layoutManager = manager
        adapter?.bindToRecyclerView(rvCommon)
//        adapter?.setNewData(parseDataList(getGroup()))
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (adapter?.getItemViewType(position)) {
                    ITEM_TYPE_HEADER -> {
                        return 3
                    }
                    ITEM_TYPE_CONTENT -> {
                        return 1
                    }

                }
                return 1
            }
        }
    }

    override fun loadData() {
        super.loadData()
        requestMedalRecord()
    }

    private fun getGroup(): ArrayList<StudyMedalGroup> {
        val groupList = ArrayList<StudyMedalGroup>()
        for (index in 0 until 3) {
            val group = StudyMedalGroup()
            val data = ArrayList<StudyMedal>()
            group.groupName = "学习勋章" + index
            for (i in 0 until index + 3) {
                val bean = StudyMedal()
                bean.medalDesc = "安培新星" + index + "" + i
                bean.isHeader = false
                data.add(bean)
            }
            group.medalList = data
            groupList.add(group)
        }
        return groupList
    }


    private fun parseDataList(netData: ArrayList<StudyMedalGroup>): ArrayList<StudyMedal> {
        val resultList = ArrayList<StudyMedal>()
        for (group in netData) {
            val groupItem = StudyMedal()
            groupItem.isHeader = true
            groupItem.groupName = group.groupName
            resultList.add(groupItem)
            resultList.addAll(group.medalList)
        }
        return resultList
    }

    //
    private fun transitionEntity(medal: StudyMedalEntity?): MutableList<MedalInfo> {
        val medalList = ArrayList<MedalInfo>()
        if (medal == null) {
            return medalList
        }
        medalList.addAll(getStudyList(medal))
        medalList.addAll(getCertifyList(medal))
        medalList.addAll(getConsumeList(medal))
        return medalList
    }

    private fun getStudyList(entity: StudyMedalEntity): MutableList<MedalInfo> {
        val resultList = ArrayList<MedalInfo>()
        val medalList = entity.studyMedals
        var lockCount = 0
        for (index in 0 until medalList.size) {
            if (medalList[index].status == MEDAL_STATUS_LOCKED) {
                lockCount++
            }
        }
        //添加证书勋章头布局
        val certifyMedalHeader = MedalInfo()
        certifyMedalHeader.isHeader = true
        certifyMedalHeader.title = "学习勋章（" + lockCount + "/" + medalList.size + ")"
        certifyMedalHeader.headerIcon = R.mipmap.icon_star_blue
        resultList.add(certifyMedalHeader)
        for (index in 0 until medalList.size) {
            val current = medalList[index]
            resultList.add(current)
            when (index) {
                0 -> {
                    setItemLockInfo(current, "安培新星", R.drawable.img_apxx_unlock, R.mipmap.img_apxx_locked)

                }
                1 -> {
                    setItemLockInfo(current, "贵在坚持", R.drawable.img_gzjc_unlock, R.mipmap.img_gzjc)

                }
                2 -> {
                    setItemLockInfo(current, "学习之星", R.drawable.img_xxzx_unlock, R.mipmap.img_xxzx)
                }
                3 -> {
                    setItemLockInfo(current, "持之以恒", R.mipmap.img_czyh_unlock, R.drawable.img_czyh)
                }
                4 -> {
                    setItemLockInfo(current, "安培代言人", R.mipmap.img_apdyr_unlock, R.drawable.img_apdyr)
                }
            }
        }
        return resultList
    }


    private fun getCertifyList(entity: StudyMedalEntity): MutableList<MedalInfo> {
        val resultList = ArrayList<MedalInfo>()
        val certifyMedalList = entity.certificateMedals
        var lockCount = 0
        for (index in 0 until certifyMedalList.size) {
            if (certifyMedalList[index].status == MEDAL_STATUS_LOCKED) {
                lockCount++
            }
        }
        //添加证书勋章头布局
        val certifyMedalHeader = MedalInfo()
        certifyMedalHeader.isHeader = true
        certifyMedalHeader.title = "证书勋章（" + lockCount + "/" + certifyMedalList.size + ")"
        certifyMedalHeader.headerIcon = R.mipmap.ic_medal_header_certify
        resultList.add(certifyMedalHeader)
        for (index in 0 until certifyMedalList.size) {
            val current = certifyMedalList[index]
            resultList.add(current)
            when (index) {
                0 -> {
                    setItemLockInfo(current, "牛刀小试", R.drawable.img_ndxs_unlock, R.mipmap.img_ndxs)

                }
                1 -> {
                    setItemLockInfo(current, "小有成就", R.drawable.img_xycj_unlock, R.mipmap.img_xycj)

                }
                2 -> {
                    setItemLockInfo(current, "登堂入室", R.drawable.img_dtrs_unlock, R.mipmap.img_dtrs)

                }
                3 -> {
                    setItemLockInfo(current, "安全达人", R.drawable.img_aqdr_unlock, R.drawable.img_aqdr)
                }
                4 -> {
                    setItemLockInfo(current, "证书终结者", R.drawable.img_zszjz_unlock, R.drawable.img_zszjz)
                }
            }
        }
        return resultList
    }

    /**
     * 消费勋章
     */
    private fun getConsumeList(entity: StudyMedalEntity): MutableList<MedalInfo> {
        val resultList = ArrayList<MedalInfo>()
        val medalList = entity.consumptionMedals
        var lockCount = 0
        for (index in 0 until medalList.size) {
            if (medalList[index].status == MEDAL_STATUS_LOCKED) {
                lockCount++
            }
        }
        //添加证书勋章头布局
        val certifyMedalHeader = MedalInfo()
        certifyMedalHeader.isHeader = true
        certifyMedalHeader.title = "消费勋章（" + lockCount + "/" + medalList.size + ")"
        certifyMedalHeader.headerIcon = R.mipmap.ic_medal_header_consume
        resultList.add(certifyMedalHeader)
        for (index in 0 until medalList.size) {
            val current = medalList[index]
            when (index) {
                0 -> {
                    setItemLockInfo(current, "安培萌新",R.drawable.img_apmx_unlock,  R.mipmap.img_apmx)
                }
                1 -> {
                    setItemLockInfo(current, "安培新贵", R.drawable.img_apxg_unlock, R.drawable.img_apxg)
                }
                2 -> {
                    setItemLockInfo(current, "安培豪杰", R.drawable.img_aphj_unlock, R.drawable.img_aphj)
                }
                3 -> {
                    setItemLockInfo(current, "安培大师", R.drawable.img_apds_unlock, R.drawable.img_apds)
                }
                4 -> {
                    setItemLockInfo(current, "土豪不差钱", R.drawable.img_thbcq_unlock, R.drawable.img_thbcq)
                }
            }
            resultList.add(current)
        }
        return resultList
    }

    private fun requestMedalRecord() {
        ApiRepository.getInstance().requestStudyMedalList().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<StudyMedalEntity>>() {
            override fun onSuccessNext(entity: BaseResult<StudyMedalEntity>?) {
                if (entity != null) {
                    if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                        AccountTempHelper.getInstance().studyMedalEntity = entity.data
                        showMedalRecord(entity.data)
                    } else {
                        ToastUtil.show(entity.msg)
                    }
                }
            }
        })
    }


    private fun showMedalRecord(medal: StudyMedalEntity?) {
        if (medal == null) {
            return
        }
        tvMedalCount.text = "已获得" + medal!!.currentNum + "枚"
        adapter!!.setNewData(transitionEntity(medal))
    }


    private fun setItemLockInfo(medal: MedalInfo, lockedDesc: String, unlockIcon: Int, lockedIcon: Int) {
        if (medal.status == MEDAL_STATUS_LOCKED) {
            medal.iconId = lockedIcon
            medal.desc = lockedDesc
        } else {
            medal.iconId = unlockIcon
            medal.desc = "暂未解锁"
        }
    }
}

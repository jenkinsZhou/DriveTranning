package com.tourcoo.training.ui.study

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.tourcoo.training.R
import com.tourcoo.training.adapter.study.StudyRecordAdapter
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.constant.TrainingConstant.EXTRA_TRAINING_PLAN_ID
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.log.TourCooLogUtil
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.study.StudyRecord
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.frame_layout_recycler.*
import kotlinx.android.synthetic.main.frame_layout_refresh_recycler.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 *@description :学习记录列表
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年05月07日14:20
 * @Email: 971613168@qq.com
 */
class StudyRecordActivity : BaseTitleActivity(), OnRefreshListener, View.OnClickListener {

    private var adapter: StudyRecordAdapter? = null
    private var pvCustomOptions: OptionsPickerView<Int>? = null
    private val options1Items: ArrayList<Int> = ArrayList()
    private var selectYear = 0
    /**
     * 右侧标题栏
     */
    private var rightTitleBarLayout: LinearLayout? = null
    private var tvSelect: TextView? = null

    override fun getContentLayout(): Int {
        return R.layout.frame_layout_title_refresh_recycler
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("学习记录")
    }

    override fun initView(savedInstanceState: Bundle?) {
        initPicker()
        rightTitleBarLayout = mTitleBar?.getLinearLayout(Gravity.END)
        val rootView = LayoutInflater.from(mContext).inflate(R.layout.titlebar_right_view_select_layout, rightTitleBarLayout, false)
        tvSelect = rootView.findViewById(R.id.tvSelect) as TextView
        //默认取第一个年份
        selectYear = options1Items[0]
        showSelectYear(selectYear)
        adapter = StudyRecordAdapter(ArrayList())
//        val emptyView = LayoutInflater.from(mContext).inflate(R.layout.empty_view_layout, null)
        rootView!!.setOnClickListener(this)
        rightTitleBarLayout!!.addView(rootView)
        smartRefreshLayoutCommon.setRefreshHeader(ClassicsHeader(mContext))
        smartRefreshLayoutCommon.setOnRefreshListener(this)
        rvCommon.layoutManager = LinearLayoutManager(mContext)
        adapter!!.bindToRecyclerView(rvCommon)
        initItemAndChildClick()
        requestStudyRecordList(selectYear.toString())
    }


    private fun requestStudyRecordList(year: String) {
        ApiRepository.getInstance().requestStudyRecordList(year).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<StudyRecord>>>() {
            override fun onSuccessNext(entity: BaseResult<MutableList<StudyRecord>>?) {
                smartRefreshLayoutCommon?.finishRefresh()
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    showRecord(entity.data)
                } else {
                    ToastUtil.show(entity.msg)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                smartRefreshLayoutCommon?.finishRefresh(false)
            }
        })
    }

    /**
     * 重组数据
     */
    private fun assemblyData(list: MutableList<StudyRecord>?): MutableList<StudyRecord> {
        val result = ArrayList<StudyRecord>()
        if (list == null || list.isEmpty()) {
            return result
        }
        try {
            for (record in list) {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
//                sdf.timeZone = TimeZone.getTimeZone("GMT+00:00")
                if (!TextUtils.isEmpty(record.trainingPlanStartTime)) {
                    val date = sdf.parse(record.trainingPlanStartTime)
                    if (date != null) {
                        record.trainDate = date
                    }
                }
            }

            CommonUtil.listSortByDate(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val maps = CommonUtil.sortStudyRecord(list)
        TourCooLogUtil.e(maps)
        var count = 0
        for (map in maps) {
            val values = map.value
            val record = StudyRecord()
            record.title = map.key
            record.isHeader = true
            values.add(0, record)
            result.addAll(values)
            if (count == 0) {
                for (value in values) {
                    value.isFolding = false
                }
            } else {
                for (value in values) {
                    value.isFolding = true
                }
            }
            count++
        }

        return result
    }


    private fun showRecord(list: MutableList<StudyRecord>?) {
        adapter!!.setNewData(assemblyData(list))
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        requestStudyRecordList(selectYear.toString())
    }

    private fun initItemAndChildClick() {
        adapter!!.setOnItemClickListener { adapter, view, position ->

            val current = adapter.data[position] as StudyRecord
            if(!current.isHeader){
                //只影响非标题点击事件
                val intent = Intent(this, StudyDetailActivity::class.java)
                intent.putExtra(EXTRA_TRAINING_PLAN_ID, current.trainingPlanID.toString())
                startActivity(intent)
            }


        }
        adapter!!.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.llLookMore -> {
                    val record = adapter.data[position] as StudyRecord
                    //取消折叠
                    record.isFolding = false
                    adapter.notifyDataSetChanged()
                }
                else -> {
                }
            }
        }
    }


    private fun initPicker() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar[Calendar.YEAR]
        for (year in currentYear downTo currentYear - 2) {
            options1Items.add(year)
        }
        pvCustomOptions = OptionsPickerBuilder(mContext, OnOptionsSelectListener { options1, options2, options3, v ->
            selectYear = options1Items[options1]
            showSelectYear(selectYear)
            //关键点 选择年份后重新请求数据
            requestStudyRecordList(selectYear.toString())
        })
                .setCyclic(false, false, false)
                .isDialog(false)
                .setContentTextSize(18)
                .setLineSpacingMultiplier(2.0f)
                .setDividerColor(CommonUtil.getColor(R.color.transparent))
                .setTextColorOut(CommonUtil.getColor(R.color.gray999999))
                .setTextColorCenter(CommonUtil.getColor(R.color.black333333))
                .build()
        pvCustomOptions?.setPicker(options1Items)
        //添加数据
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llRightContainer -> {
                pvCustomOptions?.show()
            }
            else -> {
            }
        }
    }

    /**
     * 显示选择年份
     */
    private fun showSelectYear(year: Int) {
        tvSelect!!.text = year.toString() + "年度"
    }


}
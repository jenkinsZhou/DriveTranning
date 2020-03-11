package com.tourcoo.training.ui.training.safe

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tourcoo.training.R
import com.tourcoo.training.adapter.training.ProfessionalTrainingAdapter
import com.tourcoo.training.core.base.fragment.BaseFragment
import com.tourcoo.training.entity.training.ProfessionTrainingEntity

/**
 *@description :现场培训
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月11日10:57
 * @Email: 971613168@qq.com
 */
class OfflineTrainFragment  : BaseFragment(){

    private var adapter: ProfessionalTrainingAdapter? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    override fun getContentLayout(): Int {
        return R.layout.fragment_training_profressional
    }


    override fun initView(savedInstanceState: Bundle?) {
        refreshLayout = mContentView.findViewById(R.id.smartRefreshLayoutCommon)
        refreshLayout?.setRefreshHeader(ClassicsHeader(mContext))
        recyclerView = mContentView.findViewById(R.id.rvCommon)
        recyclerView?.layoutManager = LinearLayoutManager(mContext)
        adapter = ProfessionalTrainingAdapter()
        adapter?.bindToRecyclerView(recyclerView)
        testData()
    }


    companion object {
        fun newInstance(): OfflineTrainFragment {
            val args = Bundle()
            val fragment = OfflineTrainFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun testData(){
        adapter?.addData(ProfessionTrainingEntity())
        adapter?.addData(ProfessionTrainingEntity())
        adapter?.addData(ProfessionTrainingEntity())
        adapter?.addData(ProfessionTrainingEntity())
    }
}
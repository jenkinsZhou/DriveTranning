package com.tourcoo.training.ui.training.professional

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tourcoo.training.R
import com.tourcoo.training.adapter.training.ProfessionalTrainingAdapter
import com.tourcoo.training.core.base.fragment.BaseFragment

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年03月10日20:29
 * @Email: 971613168@qq.com
 */
class ProfessionalTrainingFragment : BaseFragment() {

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

    }


    companion object {
        fun newInstance(): ProfessionalTrainingFragment {
            val args = Bundle()
            val fragment = ProfessionalTrainingFragment()
            fragment.arguments = args
            return fragment
        }
    }


}
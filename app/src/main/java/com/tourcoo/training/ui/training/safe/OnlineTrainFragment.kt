package com.tourcoo.training.ui.training.safe

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tourcoo.training.R
import com.tourcoo.training.adapter.dialog.CourseSelectAdapter
import com.tourcoo.training.adapter.training.ProfessionalTrainingAdapter
import com.tourcoo.training.core.base.fragment.BaseFragment
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.entity.course.CourseEntity
import com.tourcoo.training.entity.training.ProfessionTrainingEntity
import com.tourcoo.training.widget.dialog.CommonListDialog

/**
 *@description :线上培训
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月11日10:55
 * @Email: 971613168@qq.com
 */
class OnlineTrainFragment :BaseFragment(){
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
        baseHandler.postDelayed(Runnable {
            showCourseDialog()
        },500)
    }


    companion object {
        fun newInstance(): OnlineTrainFragment {
            val args = Bundle()
            val fragment = OnlineTrainFragment()
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


    private fun showCourseDialog(){
        val adapter = CourseSelectAdapter()
        adapter.setOnItemClickListener( object : BaseQuickAdapter.OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            ToastUtil.show("点击了")
            }
        })
        val list = ArrayList<CourseEntity>()
        for (i in 0 until 3){
            var item = CourseEntity()
            item.id = "900$i"
            item.courseDurationDesc = "d"
            list.add(item)
        }
       val dialog =  CommonListDialog<CourseEntity>(mContext).create().setDataAdapter(adapter)
               .setDataList(list)
        dialog.show()
    }
}
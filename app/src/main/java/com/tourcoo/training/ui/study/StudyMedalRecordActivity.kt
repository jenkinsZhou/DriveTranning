package com.tourcoo.training.ui.study

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tourcoo.training.R
import com.tourcoo.training.adapter.study.StudyMedalAdapter
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.study.StudyMedal
import com.tourcoo.training.entity.study.StudyMedalGroup.ITEM_TYPE_CONTENT
import com.tourcoo.training.entity.study.StudyMedalGroup.ITEM_TYPE_TITLE
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
    private var adapter : StudyMedalAdapter ?= null
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
        adapter?.setNewData(testData())
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
    }

    //计算在哪个position时要显示1列数据，即columnCount / 1列 = 4格，即1列数据占满4格
    /* ImageView ivModuleStatus = helper.getView(R.id.ivModuleStatus);
                helper.addOnClickListener(R.id.ivModuleStatus);
                ImageView ivModuleIcon = helper.getView(R.id.ivModuleIcon);
                GlideManager.loadRoundImg(item.getIcon(), ivModuleIcon, 5);
                HomeChildItem childItem = item.getChildList().get(helper.getLayoutPosition());*/
/* if (childItem.isParentGroup()) {
                    //全部应用
                    if (childItem.isSelect()) {
                        ivModuleStatus.setImageResource(R.mipmap.icon_app_select);
                    } else {
                        ivModuleStatus.setImageResource(R.mipmap.icon_app_add);
                    }
                } else {
                    ivModuleStatus.setImageResource(R.mipmap.icon_app_remove);
                }*/

    private fun testData() : ArrayList<StudyMedal>{
        val data  =   ArrayList<StudyMedal>()
        var  bean  = StudyMedal()
        for(index in 0 until   12  ){
            bean  = StudyMedal()
            bean.medalDesc = "安培新星"+index
            bean.groupName = " 学习勋章  （ 3 / 5 ）"
            if(index % 2 ==0){
                bean.itemType = ITEM_TYPE_CONTENT
            }else{
                bean.itemType = ITEM_TYPE_TITLE
            }
            data.add(bean)
        }
        return data
    }

}
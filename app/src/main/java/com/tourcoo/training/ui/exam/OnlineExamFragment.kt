package com.tourcoo.training.ui.exam

import android.os.Bundle
import com.tourcoo.training.R
import com.tourcoo.training.core.base.fragment.BaseFragment

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年03月09日17:31
 * @Email: 971613168@qq.com
 */
class OnlineExamFragment : BaseFragment(){
    override fun getContentLayout(): Int {
        return  R.layout.fragment_exam_content_online
    }

    override fun initView(savedInstanceState: Bundle?) {
    }


    companion object {
        fun newInstance(): OnlineExamFragment {
            val args = Bundle()
            val fragment = OnlineExamFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
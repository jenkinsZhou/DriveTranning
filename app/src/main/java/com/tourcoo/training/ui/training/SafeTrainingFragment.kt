package com.tourcoo.training.ui.training

import android.os.Bundle
import com.tourcoo.training.R
import com.tourcoo.training.core.base.fragment.BaseFragment

/**
 *@description :安全培训
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年03月10日20:37
 * @Email: 971613168@qq.com
 */
class SafeTrainingFragment : BaseFragment() {

    override fun getContentLayout(): Int {
        return R.layout.fragment_training_safe
    }



    override fun initView(savedInstanceState: Bundle?) {
    }


    companion object {
        fun newInstance(): SafeTrainingFragment {
            val args = Bundle()
            val fragment = SafeTrainingFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
package com.tourcoo.training.ui.training

import android.os.Bundle
import com.tourcoo.training.R
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.fragment.BaseFragment

/**
 *@description :
 *@company :翼迈科技股份有限公司
 * @author :JenkinsZhou
 * @date 2020年03月10日21:19
 * @Email: 971613168@qq.com
 */
class WorkProTrainingFragment  : BaseFragment()  {

    override fun getContentLayout(): Int {
        return R.layout.fragment_training_pre_work
    }



    override fun initView(savedInstanceState: Bundle?) {
    }


    companion object {
        fun newInstance(): WorkProTrainingFragment {
            val args = Bundle()
            val fragment = WorkProTrainingFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
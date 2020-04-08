package com.tourcoo.training.ui.account.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.tourcoo.training.R
import com.tourcoo.training.config.RequestConfig
import com.tourcoo.training.core.base.activity.BaseTitleActivity
import com.tourcoo.training.core.base.entity.BaseResult
import com.tourcoo.training.core.retrofit.BaseLoadingObserver
import com.tourcoo.training.core.retrofit.repository.ApiRepository
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.core.widget.view.bar.TitleBarView
import com.tourcoo.training.entity.account.register.CompanyInfo
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_select_company.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2020年04月08日11:21
 * @Email: 971613168@qq.com
 */
class SelectCompanyActivity : BaseTitleActivity() {
    private var companyAdapter: CompanyAdapter? = null
    override fun getContentLayout(): Int {
        return R.layout.activity_select_company
    }

    companion object {
        const val EXTRA_KEY_COMPANY = "EXTRA_KEY_COMPANY"
    }

    override fun setTitleBar(titleBar: TitleBarView?) {
        titleBar?.setTitleMainText("搜索公司")
    }

    override fun initView(savedInstanceState: Bundle?) {
        companyAdapter = CompanyAdapter()
        rvCompany.layoutManager = LinearLayoutManager(mContext)
        companyAdapter!!.bindToRecyclerView(rvCompany)
        etCompany.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    companyAdapter!!.data.clear()
                    companyAdapter!!.notifyDataSetChanged()
                    return
                }
                requestCompanyByKeyword(s.toString())
            }
        })

        companyAdapter!!.setOnItemClickListener { adapter, view, position ->
            handleItemClick(position)
        }
    }


    private fun requestCompanyByKeyword(keyword: String?) {
        ApiRepository.getInstance().requestCompanyByKeyword(keyword).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(object : BaseLoadingObserver<BaseResult<MutableList<CompanyInfo>>>() {
            override fun onSuccessNext(entity: BaseResult<MutableList<CompanyInfo>>?) {
                if (entity == null) {
                    return
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    val lis = entity.data
                    showCompanyInfo(lis)
                } else {
                    ToastUtil.showFailed(entity.msg)
                }
            }
        })
    }


    private fun showCompanyInfo(list: MutableList<CompanyInfo>?) {
        if (list != null) {
            companyAdapter!!.setNewData(list)
        }
    }

    private fun handleItemClick(position: Int) {
        val intent = Intent()
        intent.putExtra(EXTRA_KEY_COMPANY, companyAdapter!!.data[position])
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
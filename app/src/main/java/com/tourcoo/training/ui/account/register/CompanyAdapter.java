package com.tourcoo.training.ui.account.register;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.entity.account.register.CompanyInfo;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月08日11:28
 * @Email: 971613168@qq.com
 */
public class CompanyAdapter extends BaseQuickAdapter<CompanyInfo, BaseViewHolder> {

    public CompanyAdapter() {
        super(R.layout.item_company_name);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CompanyInfo item) {
        helper.setText(R.id.tvCompanyName, item.getName());
    }
}

package com.tourcoo.training.adapter.account;

import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.util.ResourceUtil;
import com.tourcoo.training.entity.account.RechargeEntity;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :充值金额适配器
 * @company :途酷科技
 * @date 2019年03月28日14:28
 * @Email: 971613168@qq.com
 */
public class RechargeAmountAdapter extends BaseQuickAdapter<RechargeEntity, BaseViewHolder> {
     public static final String TAG = "RechargeAmountAdapter";
    private int itemViewWidth;
    public RechargeAmountAdapter(@Nullable List<RechargeEntity> data) {
        super(R.layout.item_recharge_amount, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeEntity item) {
        helper.setText(R.id.tvRechargeMoney,  item.rechargeMoney + "元");
        if (item.selected) {
            helper.setBackgroundRes(R.id.llRechargeMoney, R.drawable.selector_bg_radius_7_blue_hollow);
            helper.setTextColor(R.id.tvRechargeMoney, ResourceUtil.getColor(R.color.blue5087FF));
            helper.setTextColor(R.id.tvRechargeDesc, ResourceUtil.getColor(R.color.blue5087FF));
            helper.setText(R.id.tvRechargeDesc,item.rechargeDesc);
        } else {
            helper.setBackgroundRes(R.id.llRechargeMoney, R.drawable.bg_radius_7_white_fffeff);
            helper.setTextColor(R.id.tvRechargeMoney, ResourceUtil.getColor(R.color.black525252));
            helper.setTextColor(R.id.tvRechargeDesc, ResourceUtil.getColor(R.color.gray666666));
            helper.setText(R.id.tvRechargeDesc,item.rechargeDesc);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}

package com.tourcoo.training.adapter.order;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.entity.order.OrderEntity;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月08日14:19
 * @Email: 971613168@qq.com
 */
public class OrderAdapter extends BaseQuickAdapter<OrderEntity, BaseViewHolder> {
    //全部
    public static final int ORDER_TYPE_ALL = 0;
    //充值
    public static final int ORDER_TYPE_RECHARGE = 1;
    //花费
    public static final int ORDER_TYPE_COST = 2;


    public OrderAdapter() {
        super(R.layout.item_order_record);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OrderEntity item) {
        TextView tvOrderStatus = helper.getView(R.id.tvOrderStatus);
        double yuanPrice = (item.getUnitPrice() / 100);
        helper.setText(R.id.tvPrice, CommonUtil.doubleTransStringZhen(yuanPrice));
        helper.setText(R.id.tvTotalMoney, "支付金额：" + CommonUtil.doubleTransStringZhen(item.getAmount()/100));
        helper.setText(R.id.tvCount, "x" + item.getNumber());
        helper.setText(R.id.tvTotalDesc, "共" + item.getNumber() + "件商品 合计：¥" + CommonUtil.doubleTransStringZhen((item.getAmount() / 100)));
        helper.addOnClickListener(R.id.btnOne);
        switch (item.getOrderType()) {
            case ORDER_TYPE_RECHARGE:
                //充值
                if (item.getStatus() == 0) {
                    tvOrderStatus.setText("充值成功");
                } else {
                    tvOrderStatus.setText("充值失败");
                }
                helper.setGone(R.id.btnOne, true);
                break;
            case ORDER_TYPE_COST:
                //花费
                if (item.getStatus() == 1) {
                    tvOrderStatus.setText("支付成功");
                } else {
                    tvOrderStatus.setText("支付失败");
                }
                helper.setGone(R.id.btnOne, false);
                break;
            default:
                break;
        }
    }

}

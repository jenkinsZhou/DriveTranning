package com.tourcoo.training.adapter.order;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.entity.order.OrderEntity;
import com.tourcoo.training.entity.training.Course;
import com.tourcoo.training.widget.aliplayer.utils.Common;

import org.jsoup.helper.StringUtil;

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
        String singlePriceStr = CommonUtil.getNotNullValue(item.getUnitPrice());
        String regex = ",";
        singlePriceStr = singlePriceStr.replaceAll(regex, "");
        double singlePrice = 0;
        try {
            singlePrice = Double.parseDouble(singlePriceStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //单价
        helper.setText(R.id.tvPrice,  CommonUtil.doubleTransStringZhen(singlePrice/100));
        helper.setText(R.id.tvTotalMoney, "支付金额：" + CommonUtil.doubleTransStringZhen(item.getAmount()/100) + "元");
        helper.setText(R.id.tvCount, "x" + item.getNumber());
        helper.setText(R.id.tvTotalDesc, "共" + item.getNumber() + "件商品 合计：¥" + CommonUtil.doubleTransStringZhen(item.getAmount()/100));
        helper.addOnClickListener(R.id.btnOne);
        helper.setText(R.id.tvOrderTypeLabel, CommonUtil.getNotNullValue(item.getTitle()));
        switch (item.getOrderType()) {
            case ORDER_TYPE_RECHARGE:
                //充值
                if (item.getStatus() == 1) {
                    tvOrderStatus.setText("充值成功");
                } else {
                    tvOrderStatus.setText("充值失败");
                }
                helper.setGone(R.id.llBtn, true);

                break;
            case ORDER_TYPE_COST:
                //花费
                if (item.getStatus() == 1) {
                    tvOrderStatus.setText("支付成功");
                } else {
                    tvOrderStatus.setText("支付失败");
                }
                helper.setGone(R.id.llBtn, false);
                break;
            default:
                break;
        }
    }

}

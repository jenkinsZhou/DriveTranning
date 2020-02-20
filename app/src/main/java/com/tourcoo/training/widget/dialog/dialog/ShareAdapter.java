package com.tourcoo.training.widget.dialog.dialog;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月20日19:32
 * @Email: 971613168@qq.com
 */
public class ShareAdapter extends BaseQuickAdapter<ShareEntity, BaseViewHolder> {
    public ShareAdapter() {
        super(R.layout.item_dialog_bottom_share);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShareEntity item) {
        if (item == null) {
            return;
        }
        ImageView ivShareIcon = helper.getView(R.id.ivShareIcon);
        ivShareIcon.setImageResource(item.getShareIcon());
        helper.setText(R.id.tvShareName, item.getShareItemName());


    }
}

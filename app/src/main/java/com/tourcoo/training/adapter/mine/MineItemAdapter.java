package com.tourcoo.training.adapter.mine;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;
import com.tourcoo.training.entity.mine.MineItem;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月20日12:05
 * @Email: 971613168@qq.com
 */
public class MineItemAdapter extends BaseQuickAdapter<MineItem, BaseViewHolder> {
    public MineItemAdapter() {
        super(R.layout.item_mine_menu);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MineItem item) {
        if(item == null){
            return;
        }
        ImageView ivMineMenuIcon  = helper.getView(R.id.ivMineMenuIcon);
        if(TextUtils.isEmpty(item.getIconUrl())){
            ivMineMenuIcon.setImageResource(item.getIconId());
        }else {
            GlideManager.loadImg(item.getIconUrl(),ivMineMenuIcon);
        }
        helper.setText(R.id.tvMineMenuName,item.getItemName());
    }


}

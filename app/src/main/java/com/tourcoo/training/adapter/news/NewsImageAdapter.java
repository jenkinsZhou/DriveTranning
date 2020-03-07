package com.tourcoo.training.adapter.news;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年03月06日20:39
 * @Email: 971613168@qq.com
 */
public class NewsImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public NewsImageAdapter() {
        super(R.layout.item_news_image);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String url) {
       ImageView ivNewsImage =  helper.getView(R.id.ivNewsImage);
        GlideManager.loadImageAuto(url,ivNewsImage);
    }
}

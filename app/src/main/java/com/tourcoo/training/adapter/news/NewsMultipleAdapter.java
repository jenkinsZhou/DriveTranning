package com.tourcoo.training.adapter.news;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;
import com.tourcoo.training.entity.news.NewsEntity;

import java.util.List;

import static com.tourcoo.training.entity.news.NewsEntity.NEWS_TYPE_IMAGE_HORIZONTAL;
import static com.tourcoo.training.entity.news.NewsEntity.NEWS_TYPE_IMAGE_VERTICAL;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月06日16:19
 * @Email: 971613168@qq.com
 */
public class NewsMultipleAdapter extends BaseMultiItemQuickAdapter<NewsEntity, BaseViewHolder> {

    public NewsMultipleAdapter(List<NewsEntity> data) {
        super(data);
        addItemType(NEWS_TYPE_IMAGE_HORIZONTAL, R.layout.item_news_type_image_horizontal_layout);
        addItemType(NEWS_TYPE_IMAGE_VERTICAL, R.layout.item_news_type_image_verticle_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewsEntity item) {
        switch (helper.getItemViewType()) {
            case NEWS_TYPE_IMAGE_HORIZONTAL:
                helper.setText(R.id.tvNewsTitle, item.newsTitle);
                ImageView ivImageNew = helper.getView(R.id.ivImageNew);
             if(item.imageId > 0){
                 GlideManager.loadImageAuto(item.imageId,ivImageNew,null);
             }
                break;
            case NEWS_TYPE_IMAGE_VERTICAL:
                helper.setText(R.id.tvNewsTitle, item.newsTitle);
                helper.setText(R.id.tvNewsTitle, item.newsTitle);
                break;
        }
    }
}

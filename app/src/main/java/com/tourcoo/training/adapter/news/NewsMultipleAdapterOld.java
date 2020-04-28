package com.tourcoo.training.adapter.news;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;
import com.tourcoo.training.entity.news.NewsEntityOld;

import java.util.List;

import static com.tourcoo.training.entity.news.NewsEntityOld.NEWS_TYPE_IMAGE_HORIZONTAL;
import static com.tourcoo.training.entity.news.NewsEntityOld.NEWS_TYPE_IMAGE_VERTICAL;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月06日16:19
 * @Email: 971613168@qq.com
 */
public class NewsMultipleAdapterOld extends BaseMultiItemQuickAdapter<NewsEntityOld, BaseViewHolder> {

    public NewsMultipleAdapterOld(List<NewsEntityOld> data) {
        super(data);
        addItemType(NEWS_TYPE_IMAGE_HORIZONTAL, R.layout.item_news_type_one_image_layout);
        addItemType(NEWS_TYPE_IMAGE_VERTICAL, R.layout.item_news_type_multi_images_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewsEntityOld item) {
        switch (helper.getItemViewType()) {
            case NEWS_TYPE_IMAGE_HORIZONTAL:
                helper.setText(R.id.tvNewsTitle, item.newsTitle);
                ImageView ivImageNew = helper.getView(R.id.ivImageNew);
                if (item.imageId > 0) {
                    GlideManager.loadImageAuto(item.imageId, ivImageNew);
                }
                break;
            case NEWS_TYPE_IMAGE_VERTICAL:
                helper.setText(R.id.tvNewsTitle, item.newsTitle);
                helper.setText(R.id.tvNewsTitle, item.newsTitle);
                if (item.imagesList != null && !item.imagesList.isEmpty()) {
                    helper.setGone(R.id.rvNewsImage, true);
                    RecyclerView rvNewsImage = helper.getView(R.id.rvNewsImage);
                    rvNewsImage.setLayoutManager(new GridLayoutManager(mContext,3));
                    NewsImageAdapter adapter = new NewsImageAdapter();
                    adapter.bindToRecyclerView(rvNewsImage);
                    adapter.setNewData(item.imagesList);

                } else {
                    helper.setGone(R.id.rvNewsImage, false);
                }
                break;
        }
    }
}

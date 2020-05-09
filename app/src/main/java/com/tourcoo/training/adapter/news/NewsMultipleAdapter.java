package com.tourcoo.training.adapter.news;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.entity.news.NewsEntity;
import com.tourcoo.training.entity.news.NewsImage;

import java.util.ArrayList;
import java.util.List;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月28日9:26
 * @Email: 971613168@qq.com
 */
public class NewsMultipleAdapter extends BaseMultiItemQuickAdapter<NewsEntity, BaseViewHolder> {
    public static final int NEWS_TYPE_IMAGE_ONE = 1;
    public static final int NEWS_TYPE_IMAGE_MULTI = 2;
    public static final int NEWS_TYPE_VIDEO = 3;

    public NewsMultipleAdapter(List<NewsEntity> data) {
        super(data);
        addItemType(NEWS_TYPE_IMAGE_MULTI, R.layout.item_news_type_multi_images_layout);
        addItemType(NEWS_TYPE_IMAGE_ONE, R.layout.item_news_type_one_image_layout);
        addItemType(NEWS_TYPE_VIDEO, R.layout.item_news_type_video_layout);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewsEntity item) {
        helper.addOnClickListener(R.id.llShare);
        switch (helper.getItemViewType()) {
            case NEWS_TYPE_IMAGE_ONE:
                helper.setText(R.id.tvTime,CommonUtil.getNotNullValue(item.getPublishTime()));
                helper.setGone(R.id.ivFlagTop,item.getMountainTop() == 1);
                helper.setText(R.id.tvShareCount,item.getSharedNum()+"");
                helper.setText(R.id.tvCommentCount,item.getRecommendationTotal()+"");
                helper.setText(R.id.tvNewsTitle, CommonUtil.getNotNullValue(item.getTitle()));
                ImageView ivImageNew = helper.getView(R.id.ivImageNew);
                List<NewsImage> imageList = item.getImages();
                if (imageList != null && !imageList.isEmpty()) {
                    GlideManager.loadImg(CommonUtil.getUrl(imageList.get(0).getImageUrl()), ivImageNew);
                }
                break;
            case NEWS_TYPE_IMAGE_MULTI:
                helper.setText(R.id.tvTime,CommonUtil.getNotNullValue(item.getPublishTime()));
                helper.setGone(R.id.ivFlagTop,item.getMountainTop() == 1);
                helper.setText(R.id.tvShareCount,item.getSharedNum()+"");
                helper.setText(R.id.tvCommentCount,item.getRecommendationTotal()+"");
                helper.setText(R.id.tvNewsTitle, CommonUtil.getNotNullValue(item.getTitle()));
                if (item.getImages() != null && !item.getImages().isEmpty()) {
                    helper.setGone(R.id.rvNewsImage, true);
                    RecyclerView rvNewsImage = helper.getView(R.id.rvNewsImage);
                    rvNewsImage.setLayoutManager(new GridLayoutManager(mContext, 3));
                    NewsImageAdapter adapter = new NewsImageAdapter();
                    adapter.bindToRecyclerView(rvNewsImage);
                    List<String> urlList = new ArrayList<>();
                    for (NewsImage image : item.getImages()) {
                        urlList.add(CommonUtil.getUrl(image.getImageUrl()));
                    }
                    adapter.setNewData(urlList);
                } else {
                    helper.setGone(R.id.rvNewsImage, false);
                }
                break;
            case NEWS_TYPE_VIDEO:
                helper.setText(R.id.tvTime,CommonUtil.getNotNullValue(item.getPublishTime()));
                helper.setGone(R.id.ivFlagTop,item.getMountainTop() == 1);
                helper.setText(R.id.tvNewsTitle, CommonUtil.getNotNullValue(item.getTitle()));
                helper.setText(R.id.tvShareCount,item.getSharedNum()+"");
                helper.setText(R.id.tvCommentCount,item.getRecommendationTotal()+"");
                ImageView ivVideoCover = helper.getView(R.id.ivVideoCover);
                GlideManager.loadImg(CommonUtil.getUrl(item.getCoverUrl()), ivVideoCover);
                break;
        }
    }
}

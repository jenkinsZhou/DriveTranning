package com.tourcoo.training.entity.news;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月06日16:09
 * @Email: 971613168@qq.com
 */
public class NewsEntityOld implements MultiItemEntity {
    public static final int NEWS_TYPE_IMAGE_VERTICAL = 0;
    public static final int NEWS_TYPE_IMAGE_HORIZONTAL = 1;
    public String publishTime;
    public String newsTitle;
    public String detailUrl;
    public  int newsType;
    public List<String> imagesList;
    public int imageId;

    @Override
    public int getItemType() {
        return newsType;
    }
}

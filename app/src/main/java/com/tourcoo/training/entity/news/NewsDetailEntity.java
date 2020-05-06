package com.tourcoo.training.entity.news;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年05月06日23:20
 * @Email: 971613168@qq.com
 */
public class NewsDetailEntity {
    private List<NewsEntity> RecommendList;

    public List<NewsEntity> getRecommendList() {
        return RecommendList;
    }

    public void setRecommendList(List<NewsEntity> recommendList) {
        RecommendList = recommendList;
    }
}

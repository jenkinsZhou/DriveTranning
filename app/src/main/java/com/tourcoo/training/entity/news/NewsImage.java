package com.tourcoo.training.entity.news;

import java.io.Serializable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月28日9:07
 * @Email: 971613168@qq.com
 */
public class NewsImage implements Serializable {

    /**
     * ImageUrl : http://cdn-runtime.ggjtaq.com/upload/news/20200421/b52b6bf844454d9d4825a5f45a03b150752f4cb4.jpg
     */

    private String ImageUrl;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }
}

package com.tourcoo.training.entity.news;

import android.text.TextUtils;
import android.util.Log;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import static com.tourcoo.training.adapter.news.NewsMultipleAdapter.NEWS_TYPE_IMAGE_MULTI;
import static com.tourcoo.training.adapter.news.NewsMultipleAdapter.NEWS_TYPE_IMAGE_ONE;
import static com.tourcoo.training.adapter.news.NewsMultipleAdapter.NEWS_TYPE_VIDEO;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月28日9:04
 * @Email: 971613168@qq.com
 */
public class NewsEntity implements MultiItemEntity , Serializable {

    /**
     * ID : a247a3f79c5b4ca8936d813c97d04a60bcab3e38
     * Title : 资讯0421
     * Url : http://cdn-runtime.ggjtaq.com/upload/news/20200421/b52b6bf844454d9d4825a5f45a03b150752f4cb4.jpg
     * VideoCoverUrl :
     * VideoUrl :
     * Audited : 1
     * Auditor : 0000000000002512
     * AuditingTime : 2020-04-21 18:16:40
     * IsRelease : 1
     * MountainTop : 1
     * RecommendationTotal : 1
     * LikeTotal : 2
     * ReadTotal : 7
     * CreateTime : 2020-04-21 18:16:40
     * SystemTime : 2020-04-28 09:03:36
     * time : 6天前
     * Tag : 1
     * SharedNum : 1
     * Time : 2020-04-21 18:16:40
     * CoverUrl :
     * Images : [{"ImageUrl":"http://cdn-runtime.ggjtaq.com/upload/news/20200421/b52b6bf844454d9d4825a5f45a03b150752f4cb4.jpg"}]
     */

    private String ID;
    private String Title;
    private String TiTle;
    private String Url;
    private String VideoCoverUrl;
    private String VideoUrl;
    private int Audited;
    private String Auditor;
    private String AuditingTime;
    private int IsRelease;
    private int MountainTop;
    private int RecommendationTotal;
    private int LikeTotal;
    private int ReadTotal;
    private String CreateTime;
    private String SystemTime;
    private String Content;


    @SerializedName("time")
    private String publishTime;

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    private int Tag;
    private int SharedNum;
    private String Time;
    private String CoverUrl;
    private List<NewsImage> Images;


    public String getTiTle() {
        return TiTle;
    }

    public void setTiTle(String tiTle) {
        TiTle = tiTle;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public String getVideoCoverUrl() {
        return VideoCoverUrl;
    }

    public void setVideoCoverUrl(String VideoCoverUrl) {
        this.VideoCoverUrl = VideoCoverUrl;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String VideoUrl) {
        this.VideoUrl = VideoUrl;
    }

    public int getAudited() {
        return Audited;
    }

    public void setAudited(int Audited) {
        this.Audited = Audited;
    }

    public String getAuditor() {
        return Auditor;
    }

    public void setAuditor(String Auditor) {
        this.Auditor = Auditor;
    }

    public String getAuditingTime() {
        return AuditingTime;
    }

    public void setAuditingTime(String AuditingTime) {
        this.AuditingTime = AuditingTime;
    }

    public int getIsRelease() {
        return IsRelease;
    }

    public void setIsRelease(int IsRelease) {
        this.IsRelease = IsRelease;
    }

    public int getMountainTop() {
        return MountainTop;
    }

    public void setMountainTop(int MountainTop) {
        this.MountainTop = MountainTop;
    }

    public int getRecommendationTotal() {
        return RecommendationTotal;
    }

    public void setRecommendationTotal(int RecommendationTotal) {
        this.RecommendationTotal = RecommendationTotal;
    }

    public int getLikeTotal() {
        return LikeTotal;
    }

    public void setLikeTotal(int LikeTotal) {
        this.LikeTotal = LikeTotal;
    }

    public int getReadTotal() {
        return ReadTotal;
    }

    public void setReadTotal(int ReadTotal) {
        this.ReadTotal = ReadTotal;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getSystemTime() {
        return SystemTime;
    }

    public void setSystemTime(String SystemTime) {
        this.SystemTime = SystemTime;
    }


    public int getTag() {
        return Tag;
    }

    public void setTag(int Tag) {
        this.Tag = Tag;
    }

    public int getSharedNum() {
        return SharedNum;
    }

    public void setSharedNum(int SharedNum) {
        this.SharedNum = SharedNum;
    }


    public String getCoverUrl() {
        return CoverUrl;
    }

    public void setCoverUrl(String CoverUrl) {
        this.CoverUrl = CoverUrl;
    }

    public List<NewsImage> getImages() {
        return Images;
    }

    public void setImages(List<NewsImage> images) {
        Images = images;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }


    private static final String TAG = "NewsEntity";
    @Override
    public int getItemType() {
        if (!TextUtils.isEmpty(VideoUrl)) {
            return NEWS_TYPE_VIDEO;
        }

        if (Images == null || Images.isEmpty()) {
            return 0;
        }

        if (Images.size() == 1) {
            return NEWS_TYPE_IMAGE_ONE;
        } else {
            return NEWS_TYPE_IMAGE_MULTI;
        }

    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}

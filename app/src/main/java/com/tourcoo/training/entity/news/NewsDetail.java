package com.tourcoo.training.entity.news;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月11日17:31
 * @Email: 971613168@qq.com
 */
public class NewsDetail {

    /**
     * Content : <p style="margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 25px; color: rgb(51, 51, 51); border: none; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 18px; line-height: 32px; font-family: &quot;MicroSoft YaHei&quot;, &quot;Hiragino Sans GB&quot;, Arial, sans-serif; white-space: normal; background-color: rgb(255, 255, 255);"><strong style="margin: 0px; padding: 0px;">多地中考取消体育测试，北京也会取消吗？官方回应来了！</strong>由<a href="http://bj.huatu.com/" style="margin: 0px; padding: 0px; color: rgb(246, 28, 58); text-decoration-line: none;">北京人事考试网</a>提供：更多关于多地中考取消体育测试,北京也会取消吗的内容请关注<a href="http://bj.huatu.com/" style="margin: 0px; padding: 0px; color: rgb(246, 28, 58); text-decoration-line: none;">北京人事考试网</a>！或关注北京华图微信公众号(bjhuatu)，北京华图咨询电话：400-010-1568。</p><p style="margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 25px; color: rgb(51, 51, 51); border: none; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 18px; line-height: 32px; font-family: &quot;MicroSoft YaHei&quot;, &quot;Hiragino Sans GB&quot;, Arial, sans-serif; white-space: normal; background-color: rgb(255, 255, 255);">　　据山西省招考中心，2020年山西全省中考暂停体育等科考试，总分由原来的730分调整为660分。此前，浙江省、河北省、福建省福州市等地也已取消2020年中考体育测试，陕西省、江苏省苏州市等地取消了体育测试中的长跑项目。</p><p style="margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 25px; color: rgb(51, 51, 51); border: none; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 18px; line-height: 32px; font-family: &quot;MicroSoft YaHei&quot;, &quot;Hiragino Sans GB&quot;, Arial, sans-serif; white-space: normal; background-color: rgb(255, 255, 255);"><img src="/ueditor/php/upload/image/20200509/1589003154502790.jpg"/></p><p><br/></p>
     * ID : 25a96e95b389c9ea0968b06d9749496a5b0825a3
     * Title : 最新消息多地中考取消体育测试
     * Time : 2020-05-09 13:46:02
     * SeeNum : 138
     * LikeNum : 5
     * SharedNum : 3
     * CoverUrl : 
     * VideoUrl : 
     * RecommendList : [{"ID":"a247a3f79c5b4ca8936d813c97d04a60bcab3e38","Title":"资讯0421","Url":"http://cdn-runtime.ggjtaq.com/upload/news/20200421/b52b6bf844454d9d4825a5f45a03b150752f4cb4.jpg","VideoCoverUrl":"","VideoUrl":"","Audited":1,"Auditor":"0000000000002512","AuditingTime":"2020-04-21 18:16:40","IsRelease":1,"MountainTop":1,"RecommendationTotal":2,"LikeTotal":-1,"ReadTotal":177,"CreateTime":"2020-04-21 18:16:40","SystemTime":"2020-05-11 17:31:06","time":"19天前","Tag":1,"SharedNum":2,"SeeNum":177,"LikeNum":-1,"Time":"2020-04-21 18:16:40","CoverUrl":"","Images":[{"ImageUrl":"http://cdn-runtime.ggjtaq.com/upload/news/20200421/b52b6bf844454d9d4825a5f45a03b150752f4cb4.jpg"}]},{"ID":"42ece6538d0a760a8adce29f397e2d61a47f9d0c","Title":"测试2","Url":"http://cdn-runtime.ggjtaq.com/upload/news/20200421/cb19ce5fcc027a6bc541f83b292e8cc6016a9233.jpeg","VideoCoverUrl":"http://cdn-runtime.ggjtaq.com/upload/news/20200421/17c8e1d82d2197bd23075aa02cd1fa480afe36f1.jpeg","VideoUrl":"http://cdn-runtime.ggjtaq.com/upload/news/20200421/a4fb72542ac87f74a3220be3ffe9fcd3b4910ecd.mp4","Audited":1,"Auditor":"0000000000002512","AuditingTime":"2020-04-21 14:54:05","IsRelease":1,"MountainTop":0,"RecommendationTotal":10,"LikeTotal":8,"ReadTotal":227,"CreateTime":"2020-04-21 13:28:20","SystemTime":"2020-05-11 17:31:06","time":"20天前","Tag":0,"SharedNum":10,"SeeNum":227,"LikeNum":8,"Time":"2020-04-21 13:28:20","CoverUrl":"http://cdn-runtime.ggjtaq.com/upload/news/20200421/17c8e1d82d2197bd23075aa02cd1fa480afe36f1.jpeg","Images":[{"ImageUrl":"http://cdn-runtime.ggjtaq.com/upload/news/20200421/cb19ce5fcc027a6bc541f83b292e8cc6016a9233.jpeg"}]}]
     */

    private String Content;
    private String ID;
    private String Title;
    private String Time;
    private int SeeNum;
    private int LikeNum;
    private int SharedNum;
    private String CoverUrl;
    private String VideoUrl;
    private List<NewsEntity> RecommendList;

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
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

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public int getSeeNum() {
        return SeeNum;
    }

    public void setSeeNum(int SeeNum) {
        this.SeeNum = SeeNum;
    }

    public int getLikeNum() {
        return LikeNum;
    }

    public void setLikeNum(int LikeNum) {
        this.LikeNum = LikeNum;
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

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String VideoUrl) {
        this.VideoUrl = VideoUrl;
    }

    public List<NewsEntity> getRecommendList() {
        return RecommendList;
    }

    public void setRecommendList(List<NewsEntity> RecommendList) {
        this.RecommendList = RecommendList;
    }

}

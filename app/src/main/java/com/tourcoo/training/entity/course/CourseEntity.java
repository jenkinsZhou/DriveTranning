package com.tourcoo.training.entity.course;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月18日10:20
 * @Email: 971613168@qq.com
 */
public class CourseEntity {
    /**
     * Id : 课程时长ID
     * CourseDurationDesc : 1课时（45分钟）
     */

    private String Id;
    private String CourseDurationDesc;
    private boolean select;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getCourseDurationDesc() {
        return CourseDurationDesc;
    }

    public void setCourseDurationDesc(String CourseDurationDesc) {
        this.CourseDurationDesc = CourseDurationDesc;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}

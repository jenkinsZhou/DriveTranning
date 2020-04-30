package com.tourcoo.training.ui.training.safe.online.web;

import com.tourcoo.training.entity.training.Course;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月30日13:57
 * @Email: 971613168@qq.com
 */
public class WebCourseTempHelper {

    private Course mCourse;

    private WebCourseTempHelper() {
    }

    private static class SingletonInstance {
        private static final WebCourseTempHelper INSTANCE = new WebCourseTempHelper();
    }

    public static WebCourseTempHelper getInstance() {
        return WebCourseTempHelper.SingletonInstance.INSTANCE;
    }

    public Course getCourse() {
        return mCourse;
    }

    public void setCourse(Course mCourse) {
        this.mCourse = mCourse;
    }
}

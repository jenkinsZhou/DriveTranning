package com.tourcoo.training.entity.study;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月07日16:28
 * @Email: 971613168@qq.com
 */
public class StudyDetail {


    /**
     * Title : 现场+线上测试
     * Start : {"Time":"2020-05-07 00:00:00","Address":"安徽省合肥市蜀山区高新技术产业开发区合肥国家大学科技园(黄山路)","SecurityOfficer":"王宇九","ClassDuration":900}
     * SignIn : {"Time":"2020-05-07 16:00:58"}
     * SignOut : {"Time":"2020-05-07 16:00:58"}
     * OnLine : {"Time":"2020-05-07 00:00:00","ClassDuration":900,"Progress":0}
     * Exam : {"Time":null,"Score":null}
     * End : {"Time":"2020-05-07 23:59:59"}
     * Subjects : ["道路交通安全法","雪天路滑，行车慎重"]
     */

    private String Title;
    private StudyInfo Start;
    private StudyInfo SignIn;
    private StudyInfo SignOut;
    private StudyInfo OnLine;
    private ExamInfo Exam;
    private StudyInfo End;
    private List<String> Subjects;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }






    public List<String> getSubjects() {
        return Subjects;
    }

    public void setSubjects(List<String> Subjects) {
        this.Subjects = Subjects;
    }


    public StudyInfo getStart() {
        return Start;
    }

    public void setStart(StudyInfo start) {
        Start = start;
    }

    public StudyInfo getSignIn() {
        return SignIn;
    }

    public void setSignIn(StudyInfo signIn) {
        SignIn = signIn;
    }

    public StudyInfo getSignOut() {
        return SignOut;
    }

    public void setSignOut(StudyInfo signOut) {
        SignOut = signOut;
    }

    public StudyInfo getOnLine() {
        return OnLine;
    }

    public void setOnLine(StudyInfo onLine) {
        OnLine = onLine;
    }

    public ExamInfo getExam() {
        return Exam;
    }

    public void setExam(ExamInfo exam) {
        Exam = exam;
    }

    public StudyInfo getEnd() {
        return End;
    }

    public void setEnd(StudyInfo end) {
        End = end;
    }
}

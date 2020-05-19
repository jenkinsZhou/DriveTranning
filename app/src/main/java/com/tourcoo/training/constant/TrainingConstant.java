package com.tourcoo.training.constant;

/**
 * @author :JenkinsZhou
 * @description :培训相关常量
 * @company :途酷科技
 * @date 2020年04月15日22:50
 * @Email: 971613168@qq.com
 */
public class TrainingConstant {
    /**
     * 培训计划ID
     */
    public static final String EXTRA_TRAINING_PLAN_ID = "EXTRA_TRAINING_PLAN_ID";

    /**
     * 人脸验证间隔时间
     */
    public static final String EXTRA_KEY_FACE_TIME = "EXTRA_KEY_FACE_TIME";
    /**
     * 考试ID
     */
    public static final String EXTRA_KEY_EXAM_ID = "EXTRA_EXAM_ID";
    /**
     * 当前课程key
     */
    public static final String EXTRA_COURSE_INFO = "EXTRA_COURSE_INFO";
    public static final String EXTRA_KEY_QR_SCAN_RESULT = "EXTRA_KEY_QR_SCAN_RESULT";
    public static final String BASE_SOCKET_URL_ = "wss://api.ggjtaq.com/v1.0/ws/training?token=";

    public static final String APP_ID = "wx3210e68d7d514a48";

    /**
     * 培训记录详情分享页面
     */
    public static final String STUDY_SHARE_URL = "https://cdn-runtime.ggjtaq.com/web-other/jtap200509/page/train_detail/train_detail.html";


    /**
     * 新闻详情分享页面
     */
    public static final String NEWS_SHARE_URL = "https://cdn-runtime.ggjtaq.com/web-other/jtap200509/page/news_detail/news_detail.html";


    /**
     * 学员
     */
    public static final int TRAIN_ROLE_STUDENT = 1;

    /**
     * 安全员
     */
    public static final int TRAIN_ROLE_TEACHER = 2;

    /**
     * 安全员+学员
     */
    public static final int TRAIN_ROLE_TEACHER_AND_STUDENT = 3;

    //未开始
    public static final int TRAIN_STATUS_NO_START = 0;
    //学员已签到
    public static final int TRAIN_STATUS_SIGNED = 1;
    //学员已签退
    public static final int TRAIN_STATUS_SIGN_OUT = 2;
    //已经转线上
    public static final int TRAIN_STATUS_TO_ONLINE = 3;
    //已结束
    public static final int TRAIN_STATUS_END = 4;
    //不合格
    public static final int TRAIN_STATUS_NO_PASS = 5;
    //待考试
    public static final int TRAIN_STATUS_WAIT_EXAM = 6;
    //被抽验
    public static final int TRAIN_STATUS_CHECK_STATUS = 7;
    //未完成
    public static final int TRAIN_STATUS_NO_COMPLETE = 8;


    /**
     * 培训相关action
     */
    public static final String EXTRA_TRAIN_ACTION_KEY = "EXTRA_TRAIN_ACTION_KEY";

    /**
     * 学生签到扫码
     */
    public static final String ACTION_STUDENT_SIGN = "ACTION_STUDENT_SIGN";

    /**
     * 学生签到扫码
     */
    public static final String ACTION_STUDENT_SIGN_OUT = "ACTION_STUDENT_SIGN_OUT";

    /**
     * 学生抽验扫码
     */
    public static final String ACTION_STUDENT_CHECK_STATUS = "ACTION_STUDENT_CHECK_STATUS";

    /**
     * 安全员签到扫码
     */
    public static final String ACTION_SAFE_MANAGER_SIGN = "ACTION_SAFE_MANAGER_SIGN";


    /**
     * 安全员签到scene
     */
    public static final int SCENE_SAFE_MANAGER_SIGN_IN = 121;

    /**
     * 安全员签退scene
     */
    public static final int SCENE_SAFE_MANAGER_SIGN_OUT = 120;

    /**
     * 学员签到scene
     */
    public static final int SCENE_STUDENT_SIGN_IN = 101;

    /**
     * 学员签退scene
     */
    public static final int SCENE_STUDENT_SIGN_OUT = 102;


    /**
     * 学员抽验scene
     */
    public static final int SCENE_STUDENT_CHECK_STATUS = 131;


    /**
     * HLS加密
     */
    public static final int TYPE_TYPE_COURSE_HLS = 0;
    /**
     * 车学堂加密
     */
    public static final int TYPE_COURSE_TYPE_DRIVE = 1;

    /**
     * HLS+车学堂加密
     */
    public static final int TYPE_COURSE_HLS_AND_DRIVE = 2;

    /**
     * 全部为HTML
     */
    public static final int TYPE_COURSE_HTML = 3;


    /**
     * 非加密课程(可能有html)
     */
    public static final int TYPE_COURSE_OTHER = 4;

    /**
     * 视频课件类型
     */
    public static final int MEDIA_TYPE_VIDEO = 0;

    /**
     * HTML课件类型
     */
    public static final int MEDIA_TYPE_HTML = 1;

    /**
     * 当前课程已学习完成
     */
    public static final int COURSE_STATUS_FINISH = 1;

    /**
     * 当前课程未完成
     */
    public static final int COURSE_STATUS_NOT_FINISH = 0;


    /**
     * 课程播放状态
     */
    public static final int COURSE_PLAY_STATUS_NO_COMPLETE = 0;
    public static final int COURSE_PLAY_STATUS_PLAYING = COURSE_STATUS_FINISH;
    public static final int COURSE_PLAY_STATUS_COMPLETE = 3;
}

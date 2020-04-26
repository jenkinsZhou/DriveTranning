package com.tourcoo.training.constant;

/**
 * @author :JenkinsZhou
 * @description :培训相关常量
 * @company :翼迈科技股份有限公司
 * @date 2020年04月15日22:50
 * @Email: 971613168@qq.com
 */
public class TrainingConstant {
    public static final String EXTRA_TRAINING_PLAN_ID = "EXTRA_TRAINING_PLAN_ID";
    public static final String EXTRA_KEY_QR_SCAN_RESULT = "EXTRA_KEY_QR_SCAN_RESULT";
    public static final String BASE_SOCKET_URL_ = "wss://api.ggjtaq.com/v1.0/ws/training?token=";

    public static final String APP_ID = "wx3210e68d7d514a48";

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
    //已签到
    public static final int TRAIN_STATUS_SIGNED = 1;
    //已签退
    public static final int TRAIN_STATUS_SIGN_OUT = 2;
    //已经转线上
    public static final int TRAIN_STATUS_TO_ONLINE = 3;
    //已结束
    public static final int TRAIN_STATUS_END = 4;
    //不合格
    public static final int TRAIN_STATUS_NO_PASS = 5;
    //待考试
    public static final int TRAIN_STATUS_WAIT_EXAM = 6;


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


}

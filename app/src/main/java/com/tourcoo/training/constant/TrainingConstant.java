package com.tourcoo.training.constant;

/**
 * @author :JenkinsZhou
 * @description :培训相关常量
 *  @company :翼迈科技股份有限公司
 * @date 2020年04月15日22:50
 * @Email: 971613168@qq.com
 */
public class TrainingConstant {
    public static final String EXTRA_TRAINING_PLAN_ID = "EXTRA_TRAINING_PLAN_ID";

    public static final String APP_ID = "wx3210e68d7d514a48";
    //未开始
    public static final int  TRAIN_STATUS_NO_START = 0;
    //已签到
    public static final int  TRAIN_STATUS_SIGNED = 1;
    //已签退
    public static final int  TRAIN_STATUS_SIGN_OUT = 2;
    //已经转线上
    public static final int  TRAIN_STATUS_TO_ONLINE = 3;
    //已结束
    public static final int  TRAIN_STATUS_END = 4;
    //不合格
    public static final int  TRAIN_STATUS_NO_PASS = 5;
    //待考试
    public static final int  TRAIN_STATUS_WAIT_EXAM = 6;


}

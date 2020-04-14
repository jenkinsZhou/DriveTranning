package com.tourcoo.training.entity.exam;

import com.tourcoo.training.entity.account.AccountHelper;
import com.tourcoo.training.entity.account.UserInfo;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月14日17:26
 * @Email: 971613168@qq.com
 */
public class ExamTempHelper {
    private ExamEntity examInfo;

    private ExamTempHelper() {
    }

    private static class SingletonInstance {
        private static final ExamTempHelper INSTANCE = new ExamTempHelper();
    }

    public ExamEntity getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(ExamEntity examInfo) {
        this.examInfo = examInfo;
    }

    public static ExamTempHelper getInstance() {
        return ExamTempHelper.SingletonInstance.INSTANCE;
    }
}

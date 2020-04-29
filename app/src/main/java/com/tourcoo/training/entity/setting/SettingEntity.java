package com.tourcoo.training.entity.setting;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月29日10:12
 * @Email: 971613168@qq.com
 */
public class SettingEntity {


    /**
     * Agreement : 富文本详情
     * Telephone : 0551-962122
     * ProgressSyncInterval : 10
     * Version : v0.0.1
     */

    private String Agreement;
    private String Telephone;
    private String ProgressSyncInterval;
    private String Version;

    public String getAgreement() {
        return Agreement;
    }

    public void setAgreement(String Agreement) {
        this.Agreement = Agreement;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String Telephone) {
        this.Telephone = Telephone;
    }

    public String getProgressSyncInterval() {
        return ProgressSyncInterval;
    }

    public void setProgressSyncInterval(String ProgressSyncInterval) {
        this.ProgressSyncInterval = ProgressSyncInterval;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }
}

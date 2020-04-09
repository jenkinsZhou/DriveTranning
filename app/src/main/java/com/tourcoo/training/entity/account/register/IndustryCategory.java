package com.tourcoo.training.entity.account.register;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月09日13:35
 * @Email: 971613168@qq.com
 */
public class IndustryCategory {
    private String fid;
    private String ID;
    private String Name;
    private String pid;
    private List<IndustryCategory> IndustryCategorys;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<IndustryCategory> getIndustryCategorys() {
        return IndustryCategorys;
    }

    public void setIndustryCategorys(List<IndustryCategory> industryCategorys) {
        IndustryCategorys = industryCategorys;
    }
}

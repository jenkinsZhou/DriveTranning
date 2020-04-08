package com.tourcoo.training.entity.account.register;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月08日17:26
 * @Email: 971613168@qq.com
 */
public class BusinessLicenseInfo {


    /**
     * Name : 小米科技有限责任公司
     * CreditCode : 91110108551385082Q
     * Address : 北京市海淀区西二旗中路33号院6号楼6层006号
     * ADCD : 110108
     * PhotoID : 142
     * Url : http://cdn-runtime.ggjtaq.com/business-license/20200408/677d7b5f75354e6cdfb26daeaca92729d301b156.jpg
     * Supervisors : []
     */

    private String Name;
    private String CreditCode;
    private String Address;
    private String ADCD;
    private String PhotoID;
    private String Url;
    private List<Supervisors> Supervisors;

    public List<Supervisors> getSupervisors() {
        return Supervisors;
    }

    public void setSupervisors(List<Supervisors> supervisors) {
        Supervisors = supervisors;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCreditCode() {
        return CreditCode;
    }

    public void setCreditCode(String CreditCode) {
        this.CreditCode = CreditCode;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getADCD() {
        return ADCD;
    }

    public void setADCD(String ADCD) {
        this.ADCD = ADCD;
    }

    public String getPhotoID() {
        return PhotoID;
    }

    public void setPhotoID(String PhotoID) {
        this.PhotoID = PhotoID;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }


}

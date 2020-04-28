package com.tourcoo.training.entity.training;

public class ProfessionalTwoTypeModel {


    /**
     * specialId : 1
     * ChildModuleId : 1
     * Title : 专项01
     * SubTitle : 专项01
     * Coins : 1
     * Type : 0
     */

    private String specialId;
    private String ChildModuleId;
    private String Title;
    private String SubTitle;
    private String Coins;
    private String Type;

    public String getSpecialId() {
        return specialId;
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId;
    }

    public String getChildModuleId() {
        return ChildModuleId;
    }

    public void setChildModuleId(String ChildModuleId) {
        this.ChildModuleId = ChildModuleId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public void setSubTitle(String SubTitle) {
        this.SubTitle = SubTitle;
    }

    public String getCoins() {
        return Coins;
    }

    public void setCoins(String Coins) {
        this.Coins = Coins;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
}

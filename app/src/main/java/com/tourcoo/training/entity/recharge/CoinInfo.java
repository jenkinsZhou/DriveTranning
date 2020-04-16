package com.tourcoo.training.entity.recharge;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月16日10:48
 * @Email: 971613168@qq.com
 */
public class CoinInfo {


    /**
     * ID : 5
     * Price : 1
     * Coins : 12
     * Gifts : 0
     * ExpiredTime : 2020-04-30 23:59:59
     * Desc : 12学币0.01元
     */

    private int ID;
    private String Price;
    private String Coins;
    private String Gifts;
    private String ExpiredTime;
    private String Desc;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getCoins() {
        return Coins;
    }

    public void setCoins(String Coins) {
        this.Coins = Coins;
    }

    public String getGifts() {
        return Gifts;
    }

    public void setGifts(String Gifts) {
        this.Gifts = Gifts;
    }

    public String getExpiredTime() {
        return ExpiredTime;
    }

    public void setExpiredTime(String ExpiredTime) {
        this.ExpiredTime = ExpiredTime;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String Desc) {
        this.Desc = Desc;
    }
}

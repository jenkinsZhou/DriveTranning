package com.tourcoo.training.entity.order;

/**
 * @author :JenkinsZhou
 * @description :订单实体
 * @company :途酷科技
 * @date 2020年05月08日14:20
 * @Email: 971613168@qq.com
 */
public class OrderEntity {


    /**
     * ID : 2423
     * OrderID : 27089
     * Status : 0
     * Amount : 1
     * Number : 1
     * Title : 学币充值
     * UnitPrice : 1.00
     */

    private int ID;
    private int OrderID;
    private int Status;
    private double Amount;
    private int Number;
    private String Title;
    private String UnitPrice;
    private String Channel;
    private int orderType;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int Number) {
        this.Number = Number;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(String UnitPrice) {
        this.UnitPrice = UnitPrice;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String channel) {
        Channel = channel;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}

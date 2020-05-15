package com.tourcoo.training.entity.pay;

public class CoursePayInfo {

    /**
     * Price : 0
     * Coins : 0
     * CoinRemain : 0
     * CompanyCoinRemain : 8069
     */

    private double Price;
    private int Coins;
    private int CoinRemain;
    private int CompanyCoinRemain;
    //支付方式
    private int PaymentMode;

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public int getCoins() {
        return Coins;
    }

    public void setCoins(int Coins) {
        this.Coins = Coins;
    }

    public int getCoinRemain() {
        return CoinRemain;
    }

    public void setCoinRemain(int CoinRemain) {
        this.CoinRemain = CoinRemain;
    }

    public int getCompanyCoinRemain() {
        return CompanyCoinRemain;
    }

    public void setCompanyCoinRemain(int CompanyCoinRemain) {
        this.CompanyCoinRemain = CompanyCoinRemain;
    }

    public int getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        PaymentMode = paymentMode;
    }
}

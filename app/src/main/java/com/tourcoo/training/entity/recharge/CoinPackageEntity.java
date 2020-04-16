package com.tourcoo.training.entity.recharge;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月16日11:17
 * @Email: 971613168@qq.com
 */
public class CoinPackageEntity {

    /**
     * CoinsTotal : 0
     * CoinsRemain : 0
     * CoinPackages : [{"ID":5,"Price":"1","Coins":12,"Gifts":0,"ExpiredTime":"2020-04-30 23:59:59","Desc":"12学币0.01元"},{"ID":10,"Price":"6000","Coins":3,"Gifts":0,"ExpiredTime":"2020-04-30 23:59:59","Desc":"3学币60元"}]
     */

    private int CoinsTotal;
    private int CoinsRemain;
    private List<CoinInfo> CoinPackages;

    public int getCoinsTotal() {
        return CoinsTotal;
    }

    public void setCoinsTotal(int CoinsTotal) {
        this.CoinsTotal = CoinsTotal;
    }

    public int getCoinsRemain() {
        return CoinsRemain;
    }

    public void setCoinsRemain(int CoinsRemain) {
        this.CoinsRemain = CoinsRemain;
    }

    public List<CoinInfo> getCoinPackages() {
        return CoinPackages;
    }

    public void setCoinPackages(List<CoinInfo> CoinPackages) {
        this.CoinPackages = CoinPackages;
    }


}

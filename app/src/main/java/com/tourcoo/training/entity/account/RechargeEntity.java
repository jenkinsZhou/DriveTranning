package com.tourcoo.training.entity.account;


/**
 * @author :JenkinsZhou
 * @description :充值实体类
 * @company :途酷科技
 * @date 2019年03月26日18:28
 * @Email: 971613168@qq.com
 */
public class RechargeEntity {
    /**
     * 充值金额
     */
    public String rechargeMoney;
    public String rechargeDesc;
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RechargeEntity(String rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public RechargeEntity(String rechargeMoney, boolean selected) {
        this.rechargeMoney = rechargeMoney;
        this.selected = selected;
    }
    /**
     * 账户余额
     */
    public String accountBalance;

    /**
     * 充值时间
     */

    public String rechargeTime;


    public RechargeEntity() {
    }

    /**
     * 是否选中
     */
    public boolean selected;

}

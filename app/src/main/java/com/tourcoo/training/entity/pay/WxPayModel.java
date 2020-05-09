package com.tourcoo.training.entity.pay;

public class WxPayModel {


    /**
     * appid : wx3210e68d7d514a48
     * mch_id : 1513948021
     * nonce_str : W4f79bMdK22PTtyb
     * prepay_id : wx2210281064269040c4181ee61483757800
     * result_code : SUCCESS
     * return_code : SUCCESS
     * return_msg : OK
     * sign : 9ED703908958968686283B751331D99E2A42EB354BA11FC976AEBE1B95D11A38
     * trade_type : APP
     */

    private String appid;
    private String partnerid;
    private String noncestr;
    private String prepayid;
    private String sign;
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }




    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }




    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartnerId() {
        return partnerid;
    }



    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }
}

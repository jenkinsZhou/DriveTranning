package com.tourcoo.training.entity.account;

public class PayInfo {

    /**
     * OrderID : 27158
     * ThirdPayInfo : "alipay_sdk=alipay-sdk-php-easyalipay-20191227&app_id=2019021463245405&biz_content=%7B%22body%22%3A%22%E5%AD%A6%E5%91%98%E5%AD%A6%E5%B8%81%22%2C%22subject%22%3A%22%E4%BA%A4%E9%80%9A%E5%AE%89%E5%9F%B9%22%2C%22out_trade_no%22%3A%2227158%22%2C%22timeout_express%22%3A%2230mm%22%2C%22total_amount%22%3A%220.00%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.ggjtaq.com%2Fv1.0%2Fpayment%2Fali_notify&sign_type=RSA2&timestamp=2020-04-21+13%3A09%3A33&version=1.0&sign=Hl2Y5fbKgKD2FF8ZRRqMpsjsjeqYaYNLJWoSqE8EcV5GQXDNsu0K9cZ5tmnp73Uvcazv5z1z4SaeUTxy6ZxdujsnUSCjLORE8DC%2FUuPze6ZU%2FR2We4wzTIWo2A9SVWMROHZ3%2FdSb5ppwg2yCihBY7GU8V3772SoKPHYi8WFkgAKwuMJNGlybCFd3k4xpy1X6sglGAE5eCZcQgjJ9%2FPV7gJRaf7KcIzxooKOykvXXSr6jdJnNNObtnVLv7pJb0QIcNN0x%2Bi%2FOBmUUdy2C%2Bu0RhS3Fpr9AQyiTw6wKmiWGUkC38oQuzFy%2Bc%2BozlKc3TpG9Yum%2FtUM4yp16ZujLibtx%2FA%3D%3D"
     */

    private String OrderID;
    private Object ThirdPayInfo;
    private String timeStamp;

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String OrderID) {
        this.OrderID = OrderID;
    }

    public Object getThirdPayInfo() {
        return ThirdPayInfo;
    }

    public void setThirdPayInfo(Object ThirdPayInfo) {
        this.ThirdPayInfo = ThirdPayInfo;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

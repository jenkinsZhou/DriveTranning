package com.tourcoo.training.utils;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;


import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.app.MyApplication;
import com.tourcoo.training.core.util.FormatUtil;

import java.text.DecimalFormat;

/**
 * @author :JenkinsZhou
 * @description :工具类
 * @company :途酷科技
 * @date 2019年08月20日10:27
 * @Email: 971613168@qq.com
 */
public class TourCooUtil {
    private static final String STRING_LINE = "/";
    private static final String STRING_EMPTY = "";
    private static final String URL_TAG = "http";
    private static final String URL_TAG_HTTPS = "https";
    private static final int LENGTH_PHONE = 11;

    public static int getColor(int colorId) {
        return ContextCompat.getColor(MyApplication.getContext(), colorId);
    }


    public static String getNotNullValue(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        return value;
    }

    public static String getNotNullValueLine(String value) {
        if (TextUtils.isEmpty(value)) {
            return "--";
        }
        return value;
    }


    public static Drawable getDrawable(int drawableId) {
        return ContextCompat.getDrawable(MyApplication.getContext(), drawableId);
    }


    public static String getUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return STRING_EMPTY;
        }
        if (url.contains(URL_TAG) || url.contains(URL_TAG_HTTPS)) {
            return url;
        } else {
            if (url.startsWith(STRING_LINE)) {
                return RequestConfig.BASE_URL_NO_LINE + url;
            } else {
                return RequestConfig.BASE_URL + url;
            }
        }
    }


    public static boolean isMobileNumber(String mobileNums) {
        if (TextUtils.isEmpty(mobileNums)) {
            return false;
        } else {
            String startValue = "1";
            return mobileNums.length() == LENGTH_PHONE && mobileNums.startsWith(startValue);
        }
    }



  /*  public static String doubleTransStringZhen(double d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((long) d);
        }
        //四舍五入 并保留两位小数
        double value = Double.parseDouble(FormatUtil.formatDoubleSize(d, 2));
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(value);
    }*/

    public static String doubleTransStringZhen(double d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((long) d);
        }
        //四舍五入 并保留两位小数
        double value = Double.parseDouble(FormatUtil.formatDoubleSize(d, 2));
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(value);
    }


    /**
     * 获取清晰度
     *
     * @return
     */
    public static String getDefinitionName(String arg) {
        String definition;
        switch (arg) {
            case "FD":
                definition = "流畅";
                break;
            case "LD":
                definition = "标清";
                break;
            case "SD":
                definition = "高清";
                break;
            case "HD":
                definition = "超清";
                break;
            case "OD":
                definition = "原画";
                break;
            case "2K":
                definition = "2K";
                break;
            case "4K":
                definition = "4K";
                break;
            case "AUTO":
            default:
                definition = "自适应码流";
                break;
        }

        return definition;
    }


}

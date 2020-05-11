package com.tourcoo.training.core.util;

import com.tourcoo.training.core.log.TourCooLogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月21日16:31
 * @Email: 971613168@qq.com
 */
public class TimeUtil {


    /**
     * 秒转换为指定格式的日期
     *
     * @param second
     * @param patten
     * @return
     */
    public static String secondToDate(long second, String patten) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(second * 1000);//转换为毫秒
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(patten);
        //解决08时区问题
        format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String dateString = format.format(date);
        return dateString;
    }

    /**
     * 返回日时分秒
     *
     * @param second
     * @return
     */
    public static String secondToTime(long second) {
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        if (0 < days) {
            return days + "天 " + hours + "小时 " + minutes + "分，" + second + "秒";
        } else {
            return hours + "小时 " + minutes + "分 " + second + "秒";
        }
    }


    public static String secondToTimeNoSecond(long second) {
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数

        if (0 < days) {
            return days + "天 " + hours + "小时 " + minutes + "分 " + second + "秒";
        } else {
//            + second + "秒"

            return hours + "小时 " + minutes + "分";
        }
    }

    public static double secondToHour(double second) {
        return second / 3600;
    }







    private static void changeSeconds(long seconds, int temp, StringBuffer sb) {
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");
        temp = (int) (seconds % 3600 % 60);
        sb.append((temp < 10) ? "0" + temp : "" + temp);
    }


    public static String getTime(long second) {
        second = second * 1000;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(second);
        return hms;
    }

}

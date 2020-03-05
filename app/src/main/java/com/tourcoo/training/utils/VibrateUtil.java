package com.tourcoo.training.utils;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年01月03日10:01
 * @Email: 971613168@qq.com
 */
public class VibrateUtil {
    /**
     * 让手机振动milliseconds毫秒
     */
    public static void vibrate(Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        if (vib != null && vib.hasVibrator()) {
            //判断手机硬件是否有振动器
            vib.vibrate(milliseconds);
        }
    }

    /**
     * 让手机以我们自己设定的pattern[]模式振动
     * long pattern[] = {1000, 20000, 10000, 10000, 30000};
     */
    public static void vibrate(Context context, long[] pattern, int repeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        if (vib.hasVibrator()) {
            vib.vibrate(pattern, repeat);
        }
    }

    /**
     * 取消震动
     */
    public static void vibrateCancel(Context context) {
        //关闭震动
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.cancel();
    }
}

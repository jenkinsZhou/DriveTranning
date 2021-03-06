package com.tourcoo.training.core.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.app.MyApplication;
import com.tourcoo.training.core.constant.FrameConstant;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.entity.certificate.CertificateInfo;
import com.tourcoo.training.entity.study.StudyRecord;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年12月26日16:19
 * @Email: 971613168@qq.com
 */
public class CommonUtil {

    public static final String TAG = "CommonUtil";
    private static final String STRING_LINE = "/";
    private static final String STRING_EMPTY = "";
    private static final String URL_TAG = "http";
    private static final String URL_TAG_HTTPS = "https";
    private static final int LENGTH_PHONE = 11;
    private static int ACTIVITY_SINGLE_FLAG = Intent.FLAG_ACTIVITY_SINGLE_TOP;
    public static final String SP_KEY_DEVICE = "SP_KEY_DEVICE";

    /**
     * 反射获取application对象
     *
     * @return application
     */
    public static Application getApplication() {
      /*  try {
            //兼容android P，直接调用@hide注解的方法来获取application对象
            Application app = ActivityThread.currentApplication();
            TourCooLogUtil.tag(TAG).e("getApplication0:" + app);
            TourCooLogUtil.tag(TAG).d(app);
            if (app != null) {
                return app;
            }
        } catch (Exception e) {
        }
        try {
            //兼容android P，直接调用@hide注解的方法来获取application对象
            Application app = AppGlobals.getInitialApplication();
            TourCooLogUtil.tag(TAG).e("getApplication1:" + app);
            if (app != null) {
                return app;
            }
        } catch (Exception e) {
        }*/
        return MyApplication.getContext();
    }


    /**
     * 获取应用名称
     *
     * @param context
     * @return
     */
    public static CharSequence getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getText(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            TourCooLogUtil.e(TAG, "getAppName:" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取 一定范围随机数
     *
     * @param max 最大值
     * @param min 最小值
     * @return
     */
    public static int getRandom(int max, int min) {
        // 定义随机类
        Random random = new Random();
        int result = random.nextInt(max) % (max - min + 1) + min;
        return result;
    }

    /**
     * 获取一定长度随机数
     *
     * @param length
     * @return
     */
    public static int getRandom(int length) {
        return getRandom(length, 1);
    }

    /**
     * 获取Activity 根布局
     *
     * @param activity
     * @return
     */
    public static View getRootView(Activity activity) {
        if (activity == null) {
            return null;
        }
        if (activity.findViewById(android.R.id.content) == null) {
            return null;
        }
        return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

    /**
     * 给一个Drawable变换线框颜色
     * {@link DrawableUtil#setTintDrawable(Drawable, int)}
     *
     * @param drawable 需要变换颜色的drawable
     * @param color    需要变换的颜色
     * @return
     */
    @Deprecated
    public static Drawable getTintDrawable(Drawable drawable, @ColorInt int color) {
        if (drawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                DrawableCompat.setTint(drawable, color);
            } else {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }
        return DrawableUtil.setTintDrawable(drawable, color);
    }

    /**
     * {@link DrawableUtil#setTintDrawable(Drawable, ColorStateList)}
     *
     * @param drawable
     * @param tint
     * @return
     */
    @Deprecated
    public static Drawable getTintDrawable(Drawable drawable, @Nullable ColorStateList tint) {
        return DrawableUtil.setTintDrawable(drawable, tint);
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (null != packageManager) {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                if (null != packageInfo) {
                    return packageInfo.versionName;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            TourCooLogUtil.e(TAG, "getVersionName:" + e.getMessage());
        }
        return "";
    }

    /**
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (null != packageManager) {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                if (null != packageInfo) {
                    return packageInfo.versionCode;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            TourCooLogUtil.e(TAG, "getVersionCode:" + e.getMessage());
        }
        return -1;
    }

    /**
     * 检查某个class是否存在
     *
     * @param className class的全路径包括包名+类名
     * @return
     */
    public static boolean isClassExist(String className) {
        boolean isExit = false;
        try {
            Class<?> cls = Class.forName(className);
            isExit = cls != null;
        } catch (ClassNotFoundException e) {
        }
        return isExit;
    }

    /**
     * {@link org.greenrobot.eventbus.EventBus} 要求注册之前, 订阅者必须含有一个或以上声明 {@link org.greenrobot.eventbus.Subscribe}
     * 注解的方法, 否则会报错, 所以如果要想完成在基类中自动注册, 避免报错就要先检查是否符合注册资格
     *
     * @param subscriber 订阅者
     * @return 返回 {@code true} 则表示含有 {@link org.greenrobot.eventbus.Subscribe} 注解, {@code false} 为不含有
     */
    public static boolean haveEventBusAnnotation(Object subscriber) {
        if (!isClassExist(FrameConstant.EVENT_BUS_CLASS)) {
            return false;
        }
        boolean skipSuperClasses = false;
        Class<?> clazz = subscriber.getClass();
        //查找类中符合注册要求的方法, 直到Object类
        while (clazz != null && !isSystemClass(clazz.getName()) && !skipSuperClasses) {
            Method[] allMethods;
            try {
                allMethods = clazz.getDeclaredMethods();
            } catch (Throwable th) {
                try {
                    allMethods = clazz.getMethods();
                } catch (Throwable th2) {
                    continue;
                } finally {
                    skipSuperClasses = true;
                }
            }
            for (int i = 0; i < allMethods.length; i++) {
                Method method = allMethods[i];
                Class<?>[] parameterTypes = method.getParameterTypes();
                //查看该方法是否含有 Subscribe 注解
                if (method.isAnnotationPresent(org.greenrobot.eventbus.Subscribe.class) && parameterTypes.length == 1) {
                    return true;
                }
            } //end for
            //获取父类, 以继续查找父类中符合要求的方法
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    private static boolean isSystemClass(String name) {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.");
    }

    public static boolean isRunningForeground(Context context) {
        return isRunningForeground(context, null);
    }

    /**
     * 检查某个应用是否前台运行
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isRunningForeground(Context context, String packageName) {
        if (context == null) {
            return false;
        }
        if (TextUtils.isEmpty(packageName)) {
            packageName = context.getPackageName();
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void jumpMarket(Context mContext) {
        jumpMarket(mContext, null);
    }

    /**
     * 跳转应用市场详情
     *
     * @param mContext
     * @param packageName
     */
    public static void jumpMarket(Context mContext, String packageName) {
        if (mContext == null) {
            return;
        }
        if (TextUtils.isEmpty(packageName)) {
            packageName = mContext.getPackageName();
        }
        String mAddress = "market://details?id=" + packageName;
        try {
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            mContext.startActivity(marketIntent);
        } catch (Exception e) {
            TourCooLogUtil.e(TAG, "jumpMarket:" + e.getMessage());
        }
    }

    /**
     * 设置Activity只启动一个Flag
     *
     * @param flag {@link Intent#FLAG_ACTIVITY_SINGLE_TOP}
     *             {@link Intent#FLAG_ACTIVITY_NEW_TASK}
     *             {@link Intent#FLAG_ACTIVITY_CLEAR_TOP}
     */
    public static void setActivitySingleFlag(int flag) {
        ACTIVITY_SINGLE_FLAG = flag;
    }

    /**
     * @param context
     * @param activity 跳转Activity
     * @param bundle
     * @param isSingle
     */
    public static void startActivity(Context context, Class<? extends Activity> activity, Bundle bundle, boolean isSingle) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, activity);
        intent.setFlags(isSingle ? ACTIVITY_SINGLE_FLAG : Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<? extends Activity> activity, Bundle bundle) {
        startActivity(context, activity, bundle, true);
    }

    public static void startActivity(Context context, Class<? extends Activity> activity) {
        startActivity(context, activity, null);
    }

    public static void startActivity(Context context, Class<? extends Activity> activity, boolean isSingle) {
        startActivity(context, activity, null, isSingle);
    }

    /**
     * 根据包名跳转应用
     *
     * @param context
     * @param packageName
     */
    public static void startApp(Context context, String packageName) {
        if (context == null) {
            return;
        }
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(launchIntent.getComponent());
        context.startActivity(intent);
    }

    /**
     * @param context 上下文
     * @param text    分享内容
     * @param title   分享标题
     */
    public static void startShareText(Context context, String text, CharSequence title) {
        if (context == null) {
            return;
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(Intent.createChooser(shareIntent, title));
    }

    /**
     * @param context 上下文
     * @param url     分享文字
     */
    public static void startShareText(Context context, String url) {
        startShareText(context, url, null);
    }

    /**
     * 拷贝到粘贴板
     *
     * @param context 上下文
     * @param str     需要拷贝的文字
     */
    public static void copyToClipboard(Context context, String str) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(str);
        } else {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText("content", str));
        }
    }


    public static int getColor(int colorId) {
        return ContextCompat.getColor(MyApplication.getContext(), colorId);
    }

    public static String getNotNullValue(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        return number;
    }


    //获得独一无二的Psuedo ID
    @SuppressLint("MissingPermission")
    public static String getUniquePsuedoID() {
        String cacheUuid = SPUtils.getInstance().getString(SP_KEY_DEVICE);
        if (!TextUtils.isEmpty(cacheUuid)) {
            return cacheUuid;
        }
        String uuid = getUuid();
        SPUtils.getInstance().put(SP_KEY_DEVICE, uuid);
        return uuid;
    }


    @SuppressLint("MissingPermission")
    private static String getUuid() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = android.os.Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
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


    public static String doubleTransStringZhen(double number) {
        if (Math.round(number) - number == 0) {
            return String.valueOf((long) number);
        }
        //四舍五入 并保留两位小数
        double value = Double.parseDouble(FormatUtil.formatDoubleSize(number, 2));
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(value);
   /*     String numberStr;
        if (((int) number * 1000) == (int) (number * 1000)) {
            //如果是一个整数
            numberStr = String.valueOf((int) number);
        } else {
            DecimalFormat df = new DecimalFormat("######0.00");
            numberStr = df.format(number);
        }
        return numberStr;*/

    }


    public static String doubleTransStringZhenOneZero(double number) {
        if (Math.round(number) - number == 0) {
            return String.valueOf((long) number);
        }
        //四舍五入 并保留两位小数
        double value = Double.parseDouble(FormatUtil.formatDoubleSize(number, 2));
        DecimalFormat df = new DecimalFormat("######0.0");
        return df.format(value);
   /*     String numberStr;
        if (((int) number * 1000) == (int) (number * 1000)) {
            //如果是一个整数
            numberStr = String.valueOf((int) number);
        } else {
            DecimalFormat df = new DecimalFormat("######0.00");
            numberStr = df.format(number);
        }
        return numberStr;*/

    }

    public static Map<String, ArrayList<CertificateInfo>> sort(List<CertificateInfo> list) {
        TreeMap tm = new TreeMap<String, ArrayList<CertificateInfo>>();

        for (int i = 0; i < list.size(); i++) {
            CertificateInfo certificateInfo = (CertificateInfo) list.get(i);
            if (certificateInfo == null || certificateInfo.getDate() == null) {
                continue;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(certificateInfo.getDate());

            String key;
            String month = "" + (calendar.get(Calendar.MONTH) + 1);
            if (month.length() == 1) {
                key = "" + calendar.get(Calendar.YEAR) + "-0" + month;
            } else {
                key = "" + calendar.get(Calendar.YEAR) + "-" + month;
            }
            boolean containsKey = tm.get(key) != null;
            if (containsKey) {
                List<CertificateInfo> l11 = (List<CertificateInfo>) tm.get(key);
                l11.add(certificateInfo);
            } else {
                ArrayList<CertificateInfo> tem = new ArrayList<>();
                if (certificateInfo.getDate() != null) {
                    tem.add(certificateInfo);
                    tm.put(key, tem);
                }
            }
        }

        List<Map.Entry<String, ArrayList<CertificateInfo>>> infoIds = new ArrayList<Map.Entry<String, ArrayList<CertificateInfo>>>(tm.entrySet());


        if (infoIds.size() == 1) {
            ArrayList<CertificateInfo> o1List = infoIds.get(0).getValue();
            Collections.sort(o1List, new Comparator<CertificateInfo>() {
                @Override
                public int compare(CertificateInfo o1, CertificateInfo o2) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date d1 = sdf.parse(o1.getCreateTime());
                        Date d2 = sdf.parse(o2.getCreateTime());

                        if (d1 == d2) {
                            return 0;
                        } else if (d1.before(d2)) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
        }

        // 对HashMap中的key 进行排序
        Collections.sort(infoIds, new Comparator<Map.Entry<String, ArrayList<CertificateInfo>>>() {
            @Override
            public int compare(Map.Entry<String, ArrayList<CertificateInfo>> o11, Map.Entry<String, ArrayList<CertificateInfo>> o21) {
                //key排序
                ArrayList<CertificateInfo> o1List = o11.getValue();
                ArrayList<CertificateInfo> o2List = o21.getValue();


                Collections.sort(o1List, new Comparator<CertificateInfo>() {
                    @Override
                    public int compare(CertificateInfo o1, CertificateInfo o2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date d1 = sdf.parse(o1.getCreateTime());
                            Date d2 = sdf.parse(o2.getCreateTime());

                            if (d1 == d2) {
                                return 0;
                            } else if (d1.before(d2)) {
                                return 1;
                            } else {
                                return -1;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                Collections.sort(o2List, new Comparator<CertificateInfo>() {
                    @Override
                    public int compare(CertificateInfo o1, CertificateInfo o2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date d1 = sdf.parse(o1.getCreateTime());
                            Date d2 = sdf.parse(o2.getCreateTime());

                            if (d1 == d2) {
                                return 0;
                            } else if (d1.before(d2)) {
                                return 1;
                            } else {
                                return -1;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                return 0;
            }
        });

        return tm.descendingMap();
    }


    public static Map<String, ArrayList<StudyRecord>> sortStudyRecord(List<StudyRecord> list) {
        TreeMap tm = new TreeMap<String, ArrayList<StudyRecord>>();

        for (int i = 0; i < list.size(); i++) {
            StudyRecord record = list.get(i);
            TourCooLogUtil.e(record);
            if (record == null || record.getTrainDate() == null) {
                continue;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(record.getTrainDate());

            String key;
            String month = "" + (calendar.get(Calendar.MONTH) + 1);
            if (month.length() == 1) {
                key = "" + calendar.get(Calendar.YEAR) + "-0" + month;
            } else {
                key = "" + calendar.get(Calendar.YEAR) + "-" + month;
            }
            boolean containsKey = tm.get(key) != null;

            if (containsKey) {
                List<StudyRecord> l11 = (List<StudyRecord>) tm.get(key);
                l11.add(record);
            } else {
                ArrayList<StudyRecord> tem = new ArrayList<>();
                if (record.getTrainDate() != null) {
                    tem.add(record);
                    tm.put(key, tem);
                }
            }
        }

        TourCooLogUtil.e(tm.entrySet());
        List<Map.Entry<String, ArrayList<StudyRecord>>> infoIds = new ArrayList<Map.Entry<String, ArrayList<StudyRecord>>>(tm.entrySet());

        if (infoIds.size() == 1) {
            ArrayList<StudyRecord> o1List = infoIds.get(0).getValue();
            Collections.sort(o1List, new Comparator<StudyRecord>() {
                @Override
                public int compare(StudyRecord o1, StudyRecord o2) {
                    Date d1 = o1.getTrainDate();
                    Date d2 = o2.getTrainDate();

                    if (d1 == d2) {
                        return 0;
                    } else if (d1.before(d2)) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }


        // 对HashMap中的key 进行排序
        Collections.sort(infoIds, new Comparator<Map.Entry<String, ArrayList<StudyRecord>>>() {
            @Override
            public int compare(Map.Entry<String, ArrayList<StudyRecord>> o11, Map.Entry<String, ArrayList<StudyRecord>> o21) {
                //key排序
                ArrayList<StudyRecord> o1List = o11.getValue();
                ArrayList<StudyRecord> o2List = o21.getValue();

                Collections.sort(o1List, new Comparator<StudyRecord>() {
                    @Override
                    public int compare(StudyRecord o1, StudyRecord o2) {
                        Date d1 = o1.getTrainDate();
                        Date d2 = o2.getTrainDate();

                        if (d1 == d2) {
                            return 0;
                        } else if (d1.before(d2)) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });

                Collections.sort(o2List, new Comparator<StudyRecord>() {
                    @Override
                    public int compare(StudyRecord o1, StudyRecord o2) {
                        Date d1 = o1.getTrainDate();
                        Date d2 = o2.getTrainDate();

                        if (d1 == d2) {
                            return 0;
                        } else if (d1.before(d2)) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });

                return 0;

            }
        });

        return tm.descendingMap();
    }

    /**
     * 利用HashMap key唯一，value可以重复的特点，把list中各种元素放到hashMap中
     */
    public static boolean checkDifferent(List<String> list, List<String> list1) {
        Map<String, Integer> map = new HashMap<>(list.size() + list1.size());
        if (list.size() != list1.size()) {
            return false;
        }
        for (String str : list) {
            map.put(str, 1);
        }
        for (String str : list1) {
            Integer cc = map.get(str);
            if (null != cc) {
                continue;
            }
            return false;
        }
        return true;
    }


    //按日期排序（降序）
    public static void listSortByDate(List<StudyRecord> list) {
        Collections.sort(list, new Comparator<StudyRecord>() {
            @Override
            public int compare(StudyRecord o1, StudyRecord o2) {
                Date d1 = o1.getTrainDate();
                Date d2 = o2.getTrainDate();

                if (d1 == d2) {
                    return 0;
                } else if (d1.before(d2)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

//        //Collections的sort方法默认是升序排列，如果需要降序排列时就需要重写compare方法
//        Collections.sort(list, new Comparator<StudyRecord>() {
//            @Override
//            public int compare(StudyRecord o1, StudyRecord o2) {
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
//                try {
//                    //获取体检日期，并把其类型由String转成Date，便于比较。
//                    Date dt1 = format.parse(o1.getTrainingPlanStartTime());
//                    Date dt2 = format.parse(o2.getTrainingPlanStartTime());
//
//                    //以下代码决定按日期降序排序，若将return“-1”与“1”互换，即可实现升序。
//                    //getTime 方法返回一个整数值，这个整数代表了从 1970 年 1 月 1 日开始计算到 Date 对象中的时间之间的毫秒数。
//                    if (dt1.getTime() > dt2.getTime()) {
//                        return -1;
//                    } else if (dt1.getTime() < dt2.getTime()) {
//                        return 1;
//                    } else {
//                        return 0;
//                    }
//
//                } catch (Exception e) {
//                    TourCooLogUtil.e("日期排序出错：" + e);
//                }
//                return 0;
//            }
//        });
    }


}

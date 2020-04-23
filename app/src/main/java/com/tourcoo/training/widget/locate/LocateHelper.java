package com.tourcoo.training.widget.locate;


import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tourcoo.training.core.app.MyApplication;

/**
 * @author :zhoujian
 * @description :定位帮助类
 * @company :翼迈科技
 * @date 2019年 03月 21日 20时39分
 * @Email: 971613168@qq.com
 */
public class LocateHelper {
    private static AMapLocationClient locationClient = null;
    private static AMapLocationClientOption locationOption = null;
    private static LocateHelper INSTANCE = new LocateHelper();


    /**
     * 定位监听
     */
    private AMapLocationListener locationListener;

    public static LocateHelper getInstance() {
        initLocation();
        return INSTANCE;
    }

    private LocateHelper() {
    }


    /**
     * 初始化定位
     */
    private static void initLocation() {
        //初始化client
        if (locationClient == null) {
            locationClient = new AMapLocationClient(MyApplication.getContext());
        }
        if (locationOption == null) {
            locationOption = getDefaultOption();
        }
        //设置定位参数
        locationClient.setLocationOption(locationOption);
    }


    /**
     * 获取默认的定位参数配置
     */
    private static AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setGpsFirst(false);
        //可选，设置网络请求超时时间。默认为10秒。在仅设备模式下无效
        mOption.setHttpTimeOut(1000);
        //可选，设置定位间隔。默认为2秒
        mOption.setInterval(2000);
        //可选，设置是否返回逆地理地址信息。默认是true
        mOption.setNeedAddress(true);
        //可选，设置是否单次定位。默认是false
        mOption.setOnceLocation(false);
        //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        mOption.setOnceLocationLatest(false);
        //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
        //可选，设置是否使用传感器。默认是false
        mOption.setSensorEnable(false);
        //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setWifiScan(true);
        //可选，设置是否使用缓存定位，默认为true
        mOption.setLocationCacheEnable(true);
        //可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);
        return mOption;
    }

    /**
     * 重新恢复定位参数
     */
    private void resetOption(AMapLocationClientOption option) {
        if (locationClient != null && option != null) {
            locationClient.setLocationOption(option);
        }
        // 设置是否需要显示地址信息
        /*  locationOption.setNeedAddress(true);
         *//**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         *//*
        locationOption.setGpsFirst(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(false);
        // 设置是否单次定位
        locationOption.setOnceLocation(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        //设置是否使用传感器
        locationOption.setSensorEnable(true);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
        locationOption.setInterval(2000);
        // 设置网络请求超时时间
        //todo 暂定10秒
        long strTimeout = 10 * 1000;
        locationOption.setHttpTimeOut(strTimeout);*/

    }


    /**
     * 开始定位
     */
    public void startLocation(AMapLocationListener listener) {
        // 设置定位参数
        if (locationClient != null) {
            locationListener = listener;
            if (locationListener != null) {
                locationClient.setLocationListener(locationListener);
            }
            // 启动定位
            locationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (locationClient != null) {
            // 停止定位
            locationClient.stopLocation();
        }
    }

    /**
     * 销毁定位实例
     */
    public void destroyLocationInstance() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
            locationListener = null;
        }
    }




}

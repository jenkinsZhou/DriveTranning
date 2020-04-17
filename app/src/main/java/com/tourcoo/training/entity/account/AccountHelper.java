package com.tourcoo.training.entity.account;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.entity.greendao.DaoSession;
import com.tourcoo.training.entity.greendao.GreenDaoHelper;
import com.tourcoo.training.entity.greendao.UserInfoDao;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月16日14:19
 * @Email: 971613168@qq.com
 */
public class AccountHelper {
    public static final String TAG = "AccountHelper";
    public static final String PREF_ACCESS_TOKEN = "access_token";

    private AccountHelper() {
    }

    /**
     * 访问需要的token
     */
    private String accessToken = "";
    private UserInfo userInfo;

    private static class SingletonInstance {
        private static final AccountHelper INSTANCE = new AccountHelper();
    }

    public static AccountHelper getInstance() {
        return AccountHelper.SingletonInstance.INSTANCE;
    }


    private void saveToDisk(UserInfo userInfo) {
        if (userInfo == null) {
            setUserInfo(null);
        }
        DaoSession daoSession = GreenDaoHelper.getInstance().getDaoSession();
        UserInfoDao userInfoDao = daoSession.getUserInfoDao();
        userInfoDao.deleteAll();
        if (userInfo != null) {
            userInfoDao.insert(userInfo);
            int size = userInfoDao.queryBuilder().build().list().size();
            LogUtils.i("用户信息已保存到本地 = " + size);
        } else {
            LogUtils.w("用户信息已被清空 ");
        }
    }


    public void setUserInfo(UserInfo userInfo) {
        TourCooLogUtil.i(TAG, userInfo);
        if (userInfo == null) {
            logout();
        } else {
            if (TextUtils.isEmpty(userInfo.getAccessToken())) {
                userInfo.setAccessToken(getAccessToken());
            } else {
                setAccessToken(userInfo.getAccessToken());
            }
            this.userInfo = userInfo;
            saveToDisk(userInfo);
        }
    }

    public UserInfo getUserInfo() {
        if (userInfo != null) {
            return userInfo;
        } else {
            //从本地获取用户信息
            userInfo = getUserInfoFromDisk();
            boolean isNull = userInfo != null;
            LogUtils.d("用户信息改为从缓存获取 本地是否有数据 ？" + isNull);
            return userInfo;
        }
    }

    private UserInfo getUserInfoFromDisk() {
        DaoSession daoSession = GreenDaoHelper.getInstance().getDaoSession();
        UserInfoDao userInfoDao = daoSession.getUserInfoDao();
        List<UserInfo> userInfoList = userInfoDao.queryBuilder().build().list();
        if (userInfoList != null && !userInfoList.isEmpty()) {
            return userInfoList.get(0);
        }
        return null;
    }

    /**
     * 退出登录
     */
    public void logout() {
        userInfo = null;
        deleteUserInfoFromDisk();
        EventBus.getDefault().post(new UserInfoEvent());
        TourCooLogUtil.e(TAG, "退出登录了");
    }

    private void deleteUserInfoFromDisk() {
        DaoSession daoSession = GreenDaoHelper.getInstance().getDaoSession();
        UserInfoDao userInfoDao = daoSession.getUserInfoDao();
        userInfoDao.deleteAll();
    }


    public boolean isLogin() {
        return getUserInfo() != null;
    }

    public String getAccessToken() {
        return SPUtils.getInstance().getString(PREF_ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        SPUtils.getInstance().put(PREF_ACCESS_TOKEN, accessToken);
    }
}

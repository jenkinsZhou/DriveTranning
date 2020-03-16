package com.tourcoo.training.entity.account;

import com.blankj.utilcode.util.LogUtils;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.entity.greendao.DaoSession;
import com.tourcoo.training.entity.greendao.GreenDaoHelper;
import com.tourcoo.training.entity.greendao.UserInfoDao;

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
    private UserInfo userInfo;
    private AccountHelper(){}

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
            userInfoDao.insertOrReplace(userInfo);
            int size = userInfoDao.queryBuilder().build().list().size();
            TourCooLogUtil.i(TAG,"用户信息已保存到本地 = " + size);
        } else {
            TourCooLogUtil.w(TAG,"用户信息已被清空 ");
        }
    }

    public void setUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            logout();
        } else {
            this.userInfo = userInfo;
            saveToDisk(userInfo);
        }
    }


    /**
     * 退出登录
     */
    public void logout() {
        userInfo = null;
        deleteUserInfoFromDisk();
        TourCooLogUtil.e(TAG, "退出登录了");
    }



    private void deleteUserInfoFromDisk() {
        DaoSession daoSession = GreenDaoHelper.getInstance().getDaoSession();
        UserInfoDao userInfoDao = daoSession.getUserInfoDao();
        userInfoDao.deleteAll();
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
}

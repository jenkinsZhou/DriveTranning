package com.tourcoo.training.entity.account;

import com.tourcoo.training.core.log.TourCooLogUtil;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月16日14:19
 * @Email: 971613168@qq.com
 */
public class AccountHelper {
     public static final String TAG = "AccountHelper";
    private AccountHelper(){}

    private static class SingletonInstance {
        private static final AccountHelper INSTANCE = new AccountHelper();
    }

    public static AccountHelper getInstance() {
        return AccountHelper.SingletonInstance.INSTANCE;
    }



   /* private void saveToDisk(UserInfoOld userInfoOld) {
        if (userInfoOld == null) {
            setUserInfoOld(null);
        }
        DaoSession daoSession = GreenDaoHelper.getInstance().getDaoSession();
        UserInfoDao userInfoDao = daoSession.getUserInfoDao();
        userInfoDao.deleteAll();
        if (userInfoOld != null) {
            userInfoDao.insertOrReplace(userInfoOld);
            int size = userInfoDao.queryBuilder().build().list().size();
            TourCooLogUtil.i(TAG,"用户信息已保存到本地 = " + size);
        } else {
            TourCooLogUtil.w(TAG,"用户信息已被清空 ");
        }
    }

    public void setUserInfoOld(UserInfoOld userInfoOld) {
        if (userInfoOld == null) {
            logout();
        } else {
            this.userInfoOld = userInfoOld;
            saveToDisk(userInfoOld);
        }
    }*/


    /**
     * 退出登录
     */
    public void logout() {
//        userInfoOld = null;
//        deleteUserInfoFromDisk();
        TourCooLogUtil.e(TAG, "退出登录了");
    }



   /* private void deleteUserInfoFromDisk() {
        DaoSession daoSession = GreenDaoHelper.getInstance().getDaoSession();
        UserInfoDao userInfoDao = daoSession.getUserInfoDao();
        userInfoDao.deleteAll();
    }*/


   /* private UserInfoOld getUserInfoFromDisk() {
        DaoSession daoSession = GreenDaoHelper.getInstance().getDaoSession();
        UserInfoDao userInfoDao = daoSession.getUserInfoDao();
        List<UserInfoOld> userInfoOldList = userInfoDao.queryBuilder().build().list();
        if (userInfoOldList != null && !userInfoOldList.isEmpty()) {
            return userInfoOldList.get(0);
        }
        return null;
    }*/


   /* public UserInfoOld getUserInfoOld() {
        if (userInfoOld != null) {
            return userInfoOld;
        } else {
            //从本地获取用户信息
            userInfoOld = getUserInfoFromDisk();
            boolean isNull = userInfoOld != null;
            LogUtils.d("用户信息改为从缓存获取 本地是否有数据 ？" + isNull);
            return userInfoOld;
        }
    }*/
}

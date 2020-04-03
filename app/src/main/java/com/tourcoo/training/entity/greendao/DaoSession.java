package com.tourcoo.training.entity.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.tourcoo.training.entity.account.CarInfo;
import com.tourcoo.training.entity.account.UserInfo;

import com.tourcoo.training.entity.greendao.CarInfoDao;
import com.tourcoo.training.entity.greendao.UserInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig carInfoDaoConfig;
    private final DaoConfig userInfoDaoConfig;

    private final CarInfoDao carInfoDao;
    private final UserInfoDao userInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        carInfoDaoConfig = daoConfigMap.get(CarInfoDao.class).clone();
        carInfoDaoConfig.initIdentityScope(type);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        carInfoDao = new CarInfoDao(carInfoDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);

        registerDao(CarInfo.class, carInfoDao);
        registerDao(UserInfo.class, userInfoDao);
    }
    
    public void clear() {
        carInfoDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
    }

    public CarInfoDao getCarInfoDao() {
        return carInfoDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

}
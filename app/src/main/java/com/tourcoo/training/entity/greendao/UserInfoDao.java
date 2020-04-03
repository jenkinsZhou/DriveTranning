package com.tourcoo.training.entity.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.tourcoo.training.entity.account.CarInfo;
import com.tourcoo.training.entity.account.UserInfo.CarConverter;

import com.tourcoo.training.entity.account.UserInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_INFO".
*/
public class UserInfoDao extends AbstractDao<UserInfo, Void> {

    public static final String TABLENAME = "USER_INFO";

    /**
     * Properties of entity UserInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property AccessToken = new Property(0, String.class, "AccessToken", false, "ACCESS_TOKEN");
        public final static Property UserType = new Property(1, int.class, "UserType", false, "USER_TYPE");
        public final static Property IsAuthenticate = new Property(2, int.class, "IsAuthenticate", false, "IS_AUTHENTICATE");
        public final static Property Status = new Property(3, int.class, "Status", false, "STATUS");
        public final static Property SelectCourseDuration = new Property(4, int.class, "SelectCourseDuration", false, "SELECT_COURSE_DURATION");
        public final static Property SelectTradeType = new Property(5, int.class, "SelectTradeType", false, "SELECT_TRADE_TYPE");
        public final static Property Name = new Property(6, String.class, "Name", false, "NAME");
        public final static Property LearnCurrency = new Property(7, int.class, "LearnCurrency", false, "LEARN_CURRENCY");
        public final static Property Avatar = new Property(8, String.class, "Avatar", false, "AVATAR");
        public final static Property Phone = new Property(9, String.class, "Phone", false, "PHONE");
        public final static Property IdCard = new Property(10, String.class, "IdCard", false, "ID_CARD");
        public final static Property CompanyName = new Property(11, String.class, "CompanyName", false, "COMPANY_NAME");
        public final static Property IdCardImg = new Property(12, String.class, "IdCardImg", false, "ID_CARD_IMG");
        public final static Property LearnProcess = new Property(13, int.class, "LearnProcess", false, "LEARN_PROCESS");
        public final static Property LearnLevel = new Property(14, int.class, "LearnLevel", false, "LEARN_LEVEL");
        public final static Property Pid = new Property(15, Long.class, "pid", false, "PID");
        public final static Property Car = new Property(16, String.class, "Car", false, "CAR");
    }

    private final CarConverter CarConverter = new CarConverter();

    public UserInfoDao(DaoConfig config) {
        super(config);
    }
    
    public UserInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_INFO\" (" + //
                "\"ACCESS_TOKEN\" TEXT," + // 0: AccessToken
                "\"USER_TYPE\" INTEGER NOT NULL ," + // 1: UserType
                "\"IS_AUTHENTICATE\" INTEGER NOT NULL ," + // 2: IsAuthenticate
                "\"STATUS\" INTEGER NOT NULL ," + // 3: Status
                "\"SELECT_COURSE_DURATION\" INTEGER NOT NULL ," + // 4: SelectCourseDuration
                "\"SELECT_TRADE_TYPE\" INTEGER NOT NULL ," + // 5: SelectTradeType
                "\"NAME\" TEXT," + // 6: Name
                "\"LEARN_CURRENCY\" INTEGER NOT NULL ," + // 7: LearnCurrency
                "\"AVATAR\" TEXT," + // 8: Avatar
                "\"PHONE\" TEXT," + // 9: Phone
                "\"ID_CARD\" TEXT," + // 10: IdCard
                "\"COMPANY_NAME\" TEXT," + // 11: CompanyName
                "\"ID_CARD_IMG\" TEXT," + // 12: IdCardImg
                "\"LEARN_PROCESS\" INTEGER NOT NULL ," + // 13: LearnProcess
                "\"LEARN_LEVEL\" INTEGER NOT NULL ," + // 14: LearnLevel
                "\"PID\" INTEGER," + // 15: pid
                "\"CAR\" TEXT);"); // 16: Car
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserInfo entity) {
        stmt.clearBindings();
 
        String AccessToken = entity.getAccessToken();
        if (AccessToken != null) {
            stmt.bindString(1, AccessToken);
        }
        stmt.bindLong(2, entity.getUserType());
        stmt.bindLong(3, entity.getIsAuthenticate());
        stmt.bindLong(4, entity.getStatus());
        stmt.bindLong(5, entity.getSelectCourseDuration());
        stmt.bindLong(6, entity.getSelectTradeType());
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(7, Name);
        }
        stmt.bindLong(8, entity.getLearnCurrency());
 
        String Avatar = entity.getAvatar();
        if (Avatar != null) {
            stmt.bindString(9, Avatar);
        }
 
        String Phone = entity.getPhone();
        if (Phone != null) {
            stmt.bindString(10, Phone);
        }
 
        String IdCard = entity.getIdCard();
        if (IdCard != null) {
            stmt.bindString(11, IdCard);
        }
 
        String CompanyName = entity.getCompanyName();
        if (CompanyName != null) {
            stmt.bindString(12, CompanyName);
        }
 
        String IdCardImg = entity.getIdCardImg();
        if (IdCardImg != null) {
            stmt.bindString(13, IdCardImg);
        }
        stmt.bindLong(14, entity.getLearnProcess());
        stmt.bindLong(15, entity.getLearnLevel());
 
        Long pid = entity.getPid();
        if (pid != null) {
            stmt.bindLong(16, pid);
        }
 
        CarInfo Car = entity.getCar();
        if (Car != null) {
            stmt.bindString(17, CarConverter.convertToDatabaseValue(Car));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserInfo entity) {
        stmt.clearBindings();
 
        String AccessToken = entity.getAccessToken();
        if (AccessToken != null) {
            stmt.bindString(1, AccessToken);
        }
        stmt.bindLong(2, entity.getUserType());
        stmt.bindLong(3, entity.getIsAuthenticate());
        stmt.bindLong(4, entity.getStatus());
        stmt.bindLong(5, entity.getSelectCourseDuration());
        stmt.bindLong(6, entity.getSelectTradeType());
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(7, Name);
        }
        stmt.bindLong(8, entity.getLearnCurrency());
 
        String Avatar = entity.getAvatar();
        if (Avatar != null) {
            stmt.bindString(9, Avatar);
        }
 
        String Phone = entity.getPhone();
        if (Phone != null) {
            stmt.bindString(10, Phone);
        }
 
        String IdCard = entity.getIdCard();
        if (IdCard != null) {
            stmt.bindString(11, IdCard);
        }
 
        String CompanyName = entity.getCompanyName();
        if (CompanyName != null) {
            stmt.bindString(12, CompanyName);
        }
 
        String IdCardImg = entity.getIdCardImg();
        if (IdCardImg != null) {
            stmt.bindString(13, IdCardImg);
        }
        stmt.bindLong(14, entity.getLearnProcess());
        stmt.bindLong(15, entity.getLearnLevel());
 
        Long pid = entity.getPid();
        if (pid != null) {
            stmt.bindLong(16, pid);
        }
 
        CarInfo Car = entity.getCar();
        if (Car != null) {
            stmt.bindString(17, CarConverter.convertToDatabaseValue(Car));
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public UserInfo readEntity(Cursor cursor, int offset) {
        UserInfo entity = new UserInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // AccessToken
            cursor.getInt(offset + 1), // UserType
            cursor.getInt(offset + 2), // IsAuthenticate
            cursor.getInt(offset + 3), // Status
            cursor.getInt(offset + 4), // SelectCourseDuration
            cursor.getInt(offset + 5), // SelectTradeType
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Name
            cursor.getInt(offset + 7), // LearnCurrency
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // Avatar
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // Phone
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // IdCard
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // CompanyName
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // IdCardImg
            cursor.getInt(offset + 13), // LearnProcess
            cursor.getInt(offset + 14), // LearnLevel
            cursor.isNull(offset + 15) ? null : cursor.getLong(offset + 15), // pid
            cursor.isNull(offset + 16) ? null : CarConverter.convertToEntityProperty(cursor.getString(offset + 16)) // Car
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserInfo entity, int offset) {
        entity.setAccessToken(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUserType(cursor.getInt(offset + 1));
        entity.setIsAuthenticate(cursor.getInt(offset + 2));
        entity.setStatus(cursor.getInt(offset + 3));
        entity.setSelectCourseDuration(cursor.getInt(offset + 4));
        entity.setSelectTradeType(cursor.getInt(offset + 5));
        entity.setName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setLearnCurrency(cursor.getInt(offset + 7));
        entity.setAvatar(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setPhone(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setIdCard(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCompanyName(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setIdCardImg(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setLearnProcess(cursor.getInt(offset + 13));
        entity.setLearnLevel(cursor.getInt(offset + 14));
        entity.setPid(cursor.isNull(offset + 15) ? null : cursor.getLong(offset + 15));
        entity.setCar(cursor.isNull(offset + 16) ? null : CarConverter.convertToEntityProperty(cursor.getString(offset + 16)));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(UserInfo entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(UserInfo entity) {
        return null;
    }

    @Override
    public boolean hasKey(UserInfo entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
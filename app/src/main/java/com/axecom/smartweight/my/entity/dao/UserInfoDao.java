package com.axecom.smartweight.my.entity.dao;


import android.content.Context;

import com.axecom.smartweight.my.entity.UserInfo;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * 说明：操作article表的DAO类
 * 作者：User_luo on 2018/4/23 10:47
 * 邮箱：424533553@qq.com
 */
@SuppressWarnings("ALL")
public class UserInfoDao {
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<UserInfo, Integer> dao;

    public UserInfoDao(Context context) {
        try {
            this.dao = OrmliteBaseHelper.getInstance(context.getApplicationContext()).getDao(UserInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static UserInfoDao baseDao;

    public static UserInfoDao getInstance(Context context) {
        if (baseDao == null) {
            baseDao = new UserInfoDao(context);
        }
        return baseDao;
    }

    // 添加数据
    public int insert(UserInfo data) {
        try {
            //noinspection unchecked
            return dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 删除数据
    public void delete(UserInfo data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除数据
    public int deleteAll() {
        try {
            return dao.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 修改数据
    public int update(UserInfo data) {
        int rows = -1;
        try {
            rows = dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // 修改或者插入数据
    public boolean updateOrInsert(UserInfo data) {
        try {
            Dao.CreateOrUpdateStatus createOrUpdateStatus = dao.createOrUpdate(data);
            return createOrUpdateStatus.isCreated() || createOrUpdateStatus.isUpdated();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 数据表是否存在
    public boolean isTableExists() {
        try {
            return dao.isTableExists();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //    // 通过条件查询文章集合（通过用户ID查找）
    public List<UserInfo> queryByName(String name) {
        try {
            return dao.queryBuilder().where().eq("Status", 0).and().like("SpellCode", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 通过ID查询一条数据
    public UserInfo queryById(int id) {
        UserInfo article = null;
        try {
            article = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    // 通过条件查询文章集合（通过用户ID查找）
    public List<UserInfo> queryByColumnName(String columnName, Object value) {
        try {
            return dao.queryBuilder().where().eq(columnName, value).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 查找所有bean
    public List<UserInfo> queryAll() {
        try {
//            return dao.queryBuilder().where().eq("Status", 0).query();
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
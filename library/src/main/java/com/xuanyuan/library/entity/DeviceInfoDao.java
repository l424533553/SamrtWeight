package com.xuanyuan.library.entity;


import android.content.Context;
import android.database.Cursor;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * 说明：操作article表的DAO类
 * 作者：User_luo on 2018/4/23 10:47
 * 邮箱：424533553@qq.com
 */
@SuppressWarnings("ALL")
public class DeviceInfoDao {
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<Deviceinfo, Integer> dao;
    OrmliteLibraryHelper   ormliteBaseHelper;

    public DeviceInfoDao(Context context) {
        try {
            ormliteBaseHelper = OrmliteLibraryHelper.getInstance(context);
            this.dao = ormliteBaseHelper.getDao(Deviceinfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void queryBySql(String name) {
        String[] str = new String[0];

//        String sql = "SELECT DATE_FORMAT(f.time,'%Y-%m-%d') time  FROM Deviceinfo f   GROUP BY DATE_FORMAT(f.time,'%Y-%m-%d')";
        String sql = "SELECT f.time  FROM Deviceinfo f   GROUP BY time";
        Cursor cursor = ormliteBaseHelper.getReadableDatabase().rawQuery(sql, str);
//        while(cursor.moveToNext()){
//            int id=cursor.getInt(cursor.getColumnIndex("id"));
//            String name=cursor.getString(cursor.getColumnIndex("name"));
//            String number=cursor.getString(cursor.getColumnIndex("number"));
//            Person person=new Person(id,name,number);
//            persons.add(person);
//        }

        cursor.close();
    }

    private static DeviceInfoDao baseDao;

    public static DeviceInfoDao getInstance(Context context) {
        if (baseDao == null) {
            baseDao = new DeviceInfoDao(context);
        }
        return baseDao;
    }

    // 添加数据
    public int insert(Deviceinfo data) {
        try {
            //noinspection unchecked
            return dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 删除数据
    public void delete(Deviceinfo data) {
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
    public int update(Deviceinfo data) {
        int rows = -1;
        try {
            rows = dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // 修改数据
    public int update(String billcode) {
        int rows = -1;
        try {
            UpdateBuilder updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq("billcode", billcode);
            return updateBuilder.updateColumnValue("state", 1).update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // 修改或者插入数据
    public boolean updateOrInsert(Deviceinfo data) {
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
    public List<Deviceinfo> queryByState() {
        try {
//            return dao.queryBuilder().where().eq("Status", 0).and().like("SpellCode", "%" + name + "%").query();
            return dao.queryBuilder().where().eq("state", 0).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 通过ID查询一条数据
    public Deviceinfo queryById(int id) {
        Deviceinfo article = null;
        try {
            article = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    // 通过条件查询文章集合（通过用户ID查找）
    public List<Deviceinfo> queryByUserName(String COLUMNNAME_NAME, String userName) {
        try {
            return dao.queryBuilder().where().eq(COLUMNNAME_NAME, userName).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 查找所有bean
    public List<Deviceinfo> queryAll() {
        try {
//            return dao.queryBuilder().where().eq("Status", 0).query();
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //    // 查找所有bean
    public List<Deviceinfo> queryByDay(String day) {
        try {
            return dao.queryBuilder().orderBy("hour", true).where().like("time", day + "%").query(); //参数false表示降序，true表示升序。
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param day    2018-01-09格式
     * @param isAsce true 升序 ，false 降序
     * @return
     */
    public List<Deviceinfo> queryByDay(String day, boolean isAsce) {
        try {
            return dao.queryBuilder().orderBy("time", false).where().like("time", day + "%").query(); //参数false表示降序，true表示升序。
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param day
     * @param isAsce
     * @param limitNum
     * @return
     */
    public List<Deviceinfo> queryByDay(boolean isAsce, long limitNum) {
        try {
            return dao.queryBuilder().orderBy("time", false).limit(limitNum).query(); //参数false表示降序，true表示升序。
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //    // 查找所有bean
    public List<Deviceinfo> queryByMonth(String day) {
        try {
            return dao.queryBuilder().orderBy("day", true).where().like("time", day + "%").query(); //参数false表示降序，true表示升序。
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
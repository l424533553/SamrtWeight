package com.axecom.smartweight.my.entity.dao;


import android.content.Context;
import android.database.Cursor;

import com.axecom.smartweight.my.entity.netresult.TraceNoBean;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * 说明：操作article表的DAO类
 * 作者：User_luo on 2018/4/23 10:47
 * 邮箱：424533553@qq.com
 */
@SuppressWarnings("ALL")
public class TraceNoDao {
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<TraceNoBean, Integer> dao;
    OrmliteBaseHelper ormliteBaseHelper;

    public TraceNoDao(Context context) {
        try {
            ormliteBaseHelper = OrmliteBaseHelper.getInstance(context.getApplicationContext());
            this.dao = ormliteBaseHelper.getDao(TraceNoBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void queryBySql(String name) {
        String[] str = new String[0];

//        String sql = "SELECT DATE_FORMAT(f.time,'%Y-%m-%d') time  FROM TraceNoBean f   GROUP BY DATE_FORMAT(f.time,'%Y-%m-%d')";
        String sql = "SELECT f.time  FROM TraceNoBean f   GROUP BY time";
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


    private static TraceNoDao baseDao;

    public static TraceNoDao getInstance(Context context) {
        if (baseDao == null) {
            baseDao = new TraceNoDao(context);
        }
        return baseDao;
    }

    // 添加数据
    public int insert(TraceNoBean data) {
        try {
            //noinspection unchecked
            return dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 添加数据
    public int insert(List<TraceNoBean> data) {
        try {
            //noinspection unchecked
            return dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


//    database.execSQL("DELETE FROM " + TABLE_NAME);

    // 删除数据
    public void delete(TraceNoBean data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除数据,不是删除表
    public void deleteTableData() {
        ormliteBaseHelper.getWritableDatabase().execSQL("DELETE FROM traceNo");
    }

    // 修改数据
    public int update(TraceNoBean data) {
        int rows = -1;
        try {
            rows = dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // 修改或者插入数据
    public boolean updateOrInsert(TraceNoBean data) {
        try {
            Dao.CreateOrUpdateStatus createOrUpdateStatus = dao.createOrUpdate(data);

//            dao.updateBuilder().updateColumnValue("name","zzz").where().eq("user_id", 1)

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
    public List<TraceNoBean> queryByName(String name) {
        try {
            return dao.queryBuilder().where().eq("Status", 0).and().like("SpellCode", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 通过ID查询一条数据
    public TraceNoBean queryById(int id) {
        TraceNoBean article = null;
        try {
            article = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    // 通过条件查询文章集合（通过用户ID查找）
    public List<TraceNoBean> queryByUserName(String COLUMNNAME_NAME, String userName) {
        try {
            return dao.queryBuilder().where().eq(COLUMNNAME_NAME, userName).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 查找所有bean
    public List<TraceNoBean> queryAll() {
        try {
//            return dao.queryBuilder().where().eq("Status", 0).query();
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //    // 查找所有bean
    public List<TraceNoBean> queryByDay(String day) {
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
    public List<TraceNoBean> queryByDay(String day, boolean isAsce) {
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
    public List<TraceNoBean> queryByDay(boolean isAsce, long limitNum) {
        try {
            return dao.queryBuilder().orderBy("time", false).limit(limitNum).query(); //参数false表示降序，true表示升序。
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //    // 查找所有bean
    public List<TraceNoBean> queryByMonth(String day) {
        try {
            return dao.queryBuilder().orderBy("day", true).where().like("time", day + "%").query(); //参数false表示降序，true表示升序。
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
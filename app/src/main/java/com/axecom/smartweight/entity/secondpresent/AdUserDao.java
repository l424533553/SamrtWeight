package com.axecom.smartweight.entity.secondpresent;


import android.content.Context;
import android.database.Cursor;

import com.axecom.smartweight.entity.dao.OrmliteBaseHelper;
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
public class AdUserDao {
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<AdUserBean, Integer> dao;
    private OrmliteBaseHelper ormliteBaseHelper;

    public AdUserDao(Context context) {
        try {
            ormliteBaseHelper = OrmliteBaseHelper.getInstance();
            this.dao = ormliteBaseHelper.getDao(AdUserBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private AdUserDao baseDao;

    public AdUserDao getInstance(Context context) {
        if (baseDao == null) {
            baseDao = new AdUserDao(context);
        }
        return baseDao;
    }

    // 添加数据
    public int insert(AdUserBean data) {
        try {
            //noinspection unchecked
            return dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 添加数据集
    public int inserts(List<AdUserBean> datas) {
        try {
            //noinspection unchecked
            return dao.create(datas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    // 删除数据
    public int delete(AdUserBean data) {
        try {
            return dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 删除表中的数据，非删除表
    public int deleteAll() {
        try {
            return dao.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 删除数据
    public void delete2(AdUserBean data) {
        try {

//            SELECT DATE_FORMAT(f.created_at,'%Y-%m-%d') days
//            FROM samplerules f   GROUP BY DATE_FORMAT(f.created_at,'%Y-%m-%d')

            dao.queryBuilder().groupBy("f").query();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 有条件的更新字段值
     *
     * @param conditionColumnName 条件列名
     * @param conditionValue      条件值
     * @param updateColumnName    更新列名
     * @param updateValue         更新值
     * @return
     */
    public int update(String conditionColumnName, Object conditionValue, String updateColumnName, Object updateValue) {
        int rows = -1;
        try {
            UpdateBuilder updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq(conditionColumnName, conditionValue);
            return updateBuilder.updateColumnValue(updateColumnName, updateValue).update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // 修改数据
    public int update(AdUserBean data) {
        int rows = -1;
        try {
            rows = dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 更新 数据集
     *
     * @param datas 数据集
     * @return
     */
    public int updates(List<AdUserBean> datas) {
        int rows = -1;
        try {
            for (AdUserBean bean : datas) {
                rows += dao.update(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // 修改或者插入数据
    public boolean updateOrInsert(AdUserBean data) {
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

    //    // 通过列名对应值查找
    public List<AdUserBean> queryByColumnName(String columnName, Object value) {
        try {
            return dao.queryBuilder().where().like(columnName, "%" + value + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 通过ID查询一条数据
    public AdUserBean queryById(int id) {
        AdUserBean bean = null;
        try {
            bean = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    // 通过ID查询一条数据
    public List<AdUserBean> queryById2(int id) {
        try {
            return dao.queryBuilder().where().eq("id", id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 通过条件查询文章集合（通过用户ID查找）
    public List<AdUserBean> queryByUserName(String COLUMNNAME_NAME, String userName) {
        try {
            return dao.queryBuilder().where().eq(COLUMNNAME_NAME, userName).query();
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
    public List<AdUserBean> queryByDay(String day, boolean isAsce) {
        try {
            return dao.queryBuilder().orderBy("time", false).limit(3L).where().like("time", day + "%").query(); //参数false表示降序，true表示升序。
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 查找所有bean
    public List<AdUserBean> queryAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void queryBySql(String name) {
        String[] str = new String[0];
//        String sql = "SELECT DATE_FORMAT(f.time,'%Y-%m-%d') time  FROM orderinfo f   GROUP BY DATE_FORMAT(f.time,'%Y-%m-%d')";
        String sql = "SELECT f.time  FROM orderinfo f   GROUP BY time";
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


    // 查找所有bean
    public List<AdUserBean> queryAllTest() {
        try {
//            return dao.queryBuilder().where().eq("Status", 0).query();

//       GenericRawResults<T[]> afa= dao.query("","fasf");


            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
//package com.luofx.entity.dao;
//
//
//import android.content.Context;
//
//import com.j256.ormlite.dao.Dao;
//
//import java.sql.SQLException;
//import java.util.List;
//
///**
// * 说明：操作article表的DAO类
// * 作者：User_luo on 2018/4/23 10:47
// * 邮箱：424533553@qq.com
// */
//@SuppressWarnings("ALL")
//public class BaseDao<T> {
//    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
//    private Dao<T, Integer> dao;
//
//    public BaseDao(Context context, Class<T> clazz) {
//        try {
//            this.dao = OrmliteBaseHelper.getInstance(context.getApplicationContext()).getDao(clazz);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static BaseDao baseDao;
//    public static BaseDao getInstance(Context context, Class clazz) {
//        if (baseDao == null) {
//            baseDao = new BaseDao(context, clazz);
//        }
//        return baseDao;
//    }
//
//    // 添加数据
//    public void insert(T data) {
//        try {
//            //noinspection unchecked
//            dao.create(data);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 删除数据
//    public void delete(T data) {
//        try {
//            dao.delete(data);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 修改数据
//    public int update(T data) {
//        int rows = -1;
//        try {
//            rows = dao.update(data);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return rows;
//    }
//
//    // 数据表是否存在
//    public boolean isTableExists() {
//        try {
//            return dao.isTableExists();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    //    // 通过条件查询文章集合（通过用户ID查找）
//    public List<T> queryByName(String name) {
//        try {
//            return dao.queryBuilder().where().eq("Status", 0).and().like("SpellCode", "%" + name + "%").query();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    // 通过ID查询一条数据
//    public T queryById(int id) {
//        T article = null;
//        try {
//            article = (T) dao.queryForId(id);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return article;
//    }
//
//    // 通过条件查询文章集合（通过用户ID查找）
//    public List<T> queryByUserName(String COLUMNNAME_NAME,String userName) {
//        try {
//            return dao.queryBuilder().where().eq(COLUMNNAME_NAME, userName).query();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    // 查找所有bean
//    public List<T> queryAll() {
//        try {
////            return dao.queryBuilder().where().eq("Status", 0).query();
//            return dao.queryForAll();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
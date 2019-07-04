package com.axecom.smartweight.entity.dao;

import com.axecom.smartweight.entity.project.OrderBean;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * 说明：操作article表的DAO类
 * 作者：User_luo on 2018/4/23 10:47
 * 邮箱：424533553@qq.com
 */
@SuppressWarnings("ALL")
public class OrderBeanDao {
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<OrderBean, Integer> dao;

    public OrderBeanDao() {
        try {
            this.dao = OrmliteBaseHelper.getInstance().getDao(OrderBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static OrderBeanDao baseDao;

    public static OrderBeanDao getInstance() {
        if (baseDao == null) {
            baseDao = new OrderBeanDao();
        }
        return baseDao;
    }

    // 添加数据
    public int insert(OrderBean data) {
        try {
            //noinspection unchecked
            return dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 添加数据
    public int insert(List<OrderBean> data) {
        try {
            //noinspection unchecked
            return dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 删除数据
    public void delete(OrderBean data) {
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
    public int update(OrderBean data) {
        int rows = -1;
        try {
            rows = dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // 修改或者插入数据
    public boolean updateOrInsert(OrderBean data) {
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
    public List<OrderBean> queryByName(String name) {
        try {
            return dao.queryBuilder().where().eq("Status", 0).and().like("SpellCode", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 通过ID查询一条数据
    public OrderBean queryById(int id) {
        OrderBean article = null;
        try {
            article = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    // 通过条件查询文章集合（通过用户ID查找）
    public List<OrderBean> queryByUserName(String COLUMNNAME_NAME, String userName) {
        try {
            return dao.queryBuilder().where().eq(COLUMNNAME_NAME, userName).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 查找所有bean
    public List<OrderBean> queryAll() {
        try {
//            return dao.queryBuilder().where().eq("Status", 0).query();
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 2018-01-09格式，
     *
     * @param timeFrom 开始时间
     * @param timeTo   结束时间
     * @param isAsce   true： 升序 ， false 降序
     * @return  返回数据
     */
    public List<OrderBean> queryByDay(String timeFrom, String timeTo, boolean isAsce) {
        try {
            return dao.queryBuilder().orderBy("time", isAsce).where().ge("time", timeFrom).and().le("time",  timeTo).query(); //参数false表示降序，true表示升序。
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
    public List<OrderBean> queryByDay(String day, boolean isAsce) {
        try {
            return dao.queryBuilder().orderBy("time", isAsce).where().like("time", day + "%").query(); //参数false表示降序，true表示升序。
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
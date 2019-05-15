package com.axecom.smartweight.my.entity.dao;


import android.content.Context;
import android.util.Log;

import com.axecom.smartweight.my.entity.HotGood;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 说明：操作article表的DAO类
 * 作者：User_luo on 2018/4/23 10:47
 * 邮箱：424533553@qq.com
 */
@SuppressWarnings("ALL")
public class HotGoodsDao {
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<HotGood, Integer> dao;

    public HotGoodsDao(Context context) {
        try {
            OrmliteBaseHelper ormliteBaseHelper = OrmliteBaseHelper.getInstance(context.getApplicationContext());
            this.dao = ormliteBaseHelper.getDao(HotGood.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static HotGoodsDao baseDao;

    public static HotGoodsDao getInstance(Context context) {
        if (baseDao == null) {
            baseDao = new HotGoodsDao(context);
        }
        return baseDao;
    }

    // 添加数据
    public int insert(HotGood data) {
        try {
            //noinspection unchecked
            return dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 添加数据
    public long count() {
        try {
            return dao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 添加数据
    public int insert(List<HotGood> datas) {
        try {
            //noinspection unchecked
            return dao.create(datas);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return -1;
    }


    // 删除数据
    public void delete(HotGood data) {
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

    // 删除数据
    public void delete2(HotGood data) {
        try {

//            SELECT DATE_FORMAT(f.created_at,'%Y-%m-%d') days
//            FROM samplerules f   GROUP BY DATE_FORMAT(f.created_at,'%Y-%m-%d')

            dao.queryBuilder().groupBy("f").query();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修改数据
    public int update(HotGood data) {
        int rows = -1;
        try {
            rows = dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // 修改或者插入数据
    public boolean updateOrInsert(HotGood data) {
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
    public List<HotGood> queryByName(String name) {
        try {
            return dao.queryBuilder().where().eq("Status", 0).and().like("SpellCode", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //    // 通过条件查询文章集合（通过用户ID查找）
    public List<HotGood> queryByTypeId(int id) {
        try {
            return dao.queryBuilder().where().eq("typeid", id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 通过ID查询一条数据
    public HotGood queryById(int id) {
        HotGood article = null;
        try {
            article = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    // 通过条件查询文章集合（通过用户ID查找）
    public List<HotGood> queryByUserName(String COLUMNNAME_NAME, String userName) {
        try {
            return dao.queryBuilder().where().eq(COLUMNNAME_NAME, userName).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 查找所有bean
    public List<HotGood> queryAll() {
        try {
//            return dao.queryBuilder().where().eq("Status", 0).query();
            List<HotGood> hotHotGoodList = dao.queryForAll();
            //重新排序，排序规则可以在界面拖拽更新
            Collections.sort(hotHotGoodList, new Comparator<HotGood>() {
                @Override
                public int compare(HotGood hotGood, HotGood t1) {
                    //按id升序排序(这个id是排序用的,并非唯一标志)
                    return hotGood.getId() - t1.getId();
                }
            });
            return hotHotGoodList;
        } catch (SQLException e) {
            Log.e("rzl", "query all goods error:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    // 查找所有bean
    public List<HotGood> queryAllTest() {
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
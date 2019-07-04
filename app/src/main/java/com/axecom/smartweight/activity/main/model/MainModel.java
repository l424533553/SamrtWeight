package com.axecom.smartweight.activity.main.model;

import com.axecom.smartweight.entity.dao.HotGoodsDao;
import com.axecom.smartweight.entity.dao.TraceNoDao;
import com.axecom.smartweight.entity.project.HotGood;

import java.util.List;

/**
 * 作者：罗发新
 * 时间：2019/6/10 0010    星期一
 * 邮件：424533553@qq.com
 * 说明：
 */
public class MainModel implements IMainModel {
    public MainModel() {
    }

    private HotGoodsDao hotGoodsDao;

    private HotGoodsDao getHotGoodsDao() {
        if (hotGoodsDao == null) {
            hotGoodsDao = new HotGoodsDao();
        }
        return hotGoodsDao;
    }

    @Override
    public List<HotGood> getHotGoods() {
        return  getHotGoodsDao().queryAll();
    }
}

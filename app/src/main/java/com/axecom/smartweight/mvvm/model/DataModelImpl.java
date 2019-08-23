package com.axecom.smartweight.mvvm.model;

import com.axecom.smartweight.entity.dao.OrderBeanDao;
import com.axecom.smartweight.entity.project.OrderBean;
import com.axecom.smartweight.mvvm.viewmodel.DataListener;
import com.xuanyuan.library.utils.MyDateUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： 周旭 on 2017年10月18日 0018.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class DataModelImpl implements IDataModel {

    private static final String TAG = "NetDataModelImpl";
    private List<GoodsTradeBean> simpleNewsBeanList = new ArrayList<>();
    private OrderBeanDao orderBeanDao;

    private OrderBeanDao getOrderBeanDao() {
        if (orderBeanDao == null) {
            orderBeanDao = new OrderBeanDao();
        }
        return orderBeanDao;
    }

    public DataModelImpl() {

    }

    @Override
    public void loadData(int page) {
//        RetrofitHttpUtils.getGoodsTradeInfo()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableObserver<GoodsTradeBean>() {
//                    @Override
//                    public void onNext(GoodsTradeBean goodsTradeBean) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    /**
     * 上下文。使用上下文
     */
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.

    /**
     * @param time 导入时间
     */
    @Override
    public List<GoodsTradeBean> loadData(long time, DataListener dataListener) {
        String timeDate = MyDateUtils.getYYMMDD(time);
        List<OrderBean> list = getOrderBeanDao().queryByDay(timeDate, false);
        return dealData(list);
    }

    /**
     * 对获取到的数据 进行判断处理
     * @return
     */
    private List<GoodsTradeBean> dealData(List<OrderBean> list) {
        List<GoodsTradeBean> goodsTradeBeans = null;
        if (list != null && list.size() > 0) {
            goodsTradeBeans = new ArrayList<>();
            boolean hasFindData;
            for (OrderBean orderBean : list) {
                hasFindData = false;
                for (GoodsTradeBean goodsTradeBean : goodsTradeBeans) {
                    if (orderBean.getName().equals(goodsTradeBean.getGoodName())) {
                        hasFindData = true;
                        goodsTradeBean.setTradeCount(goodsTradeBean.getTradeCount() + 1);
                        Float money = Float.valueOf(goodsTradeBean.getTotalMoney()) + Float.valueOf(orderBean.getMoney());
                        Float weight = Float.valueOf(goodsTradeBean.getTotalWeight()) + Float.valueOf(orderBean.getWeight());
                        float price = money / weight;
                        goodsTradeBean.setTotalWeight(String.valueOf(weight));
                        goodsTradeBean.setTotalMoney(String.valueOf(money));
                        goodsTradeBean.setPrice(decimalFormat.format(price));
                        break;
                    }
                }

                if (!hasFindData) {
                    GoodsTradeBean goodsTradeBean = new GoodsTradeBean();
                    goodsTradeBean.setGoodName(orderBean.getName());
                    goodsTradeBean.setTradeDate(orderBean.getTime());
                    goodsTradeBean.setTradeCount(1);
                    goodsTradeBean.setTotalWeight(orderBean.getWeight());
                    goodsTradeBean.setTotalMoney(orderBean.getMoney());
                    goodsTradeBean.setPrice(orderBean.getPrice());
                    goodsTradeBeans.add(goodsTradeBean);
                }
            }
        }
        return goodsTradeBeans;
    }

    /**
     *
     * @return   使用，
     */
    @Override
    public List<GoodsTradeBean> loadData(String timeFrom, String timeTo, DataListener dataListener) {
        List<OrderBean> list = getOrderBeanDao().queryByDay(timeFrom, timeTo, false);
        return dealData(list);
    }

}

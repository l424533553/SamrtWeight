package com.axecom.smartweight.my;

import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.shangtongyin.tools.serialport.EpsPrint;
import com.shangtongyin.tools.serialport.IConstants_ST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * author: luofaxin
 * date： 2018/10/26 0026.
 * email:424533553@qq.com
 * describe:  爱普生 打印机命令
 */
public class MyEPSPrinter implements IConstants_ST {

    private EpsPrint epsPrint;

    public MyEPSPrinter(EpsPrint epsPrint) {
        this.epsPrint = epsPrint;
    }

    /**
     * shangtong 打印机打印
     */
    public void printOrder(ExecutorService executorService, final OrderInfo orderInfo) {
        if (epsPrint == null) {
            return;
        }
        if (orderInfo == null) {
            return;
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<OrderBean> orderBeans = orderInfo.getOrderItem();
                if (orderBeans == null) {
                    orderBeans = new ArrayList<>(orderInfo.getOrderBeans());
                }
                StringBuilder sb = new StringBuilder();
                sb.append("------------交易明细------------\n");

                sb.append("市场名称：").append(orderInfo.getMarketName()).append("\n");
                sb.append("交易日期：").append(orderInfo.getTime()).append("\n");
                sb.append("交易单号：").append(orderInfo.getBillcode()).append("\n");

                if (1 == orderInfo.getSettlemethod()) {
                    sb.append("结算方式：微信支付\n");
                }
                if (2 == orderInfo.getSettlemethod()) {
                    sb.append("结算方式：支付宝支付\n");
                }
                if (3 == orderInfo.getSettlemethod()) {
                    sb.append("结算方式：现金支付\n");
                }

                sb.append("卖方名称：").append(orderInfo.getSeller()).append("\n");
                sb.append("摊位号：").append(orderInfo.getStallNo()).append("\n");
                sb.append("商品名\b" + "单价/元\b" + "重量/kg\b" + "金额/元" + "\n");

                for (int i = 0; i < orderBeans.size(); i++) {
                    OrderBean goods = orderBeans.get(i);
                    goods.setOrderInfo(orderInfo);
                    sb.append(goods.getName()).append("\t").append(goods.getPrice()).append("\t").append(goods.getWeight()).append("\t").append(goods.getMoney()).append("\n");
                }

                sb.append("--------------------------------\n");
                sb.append("合计(元)：").append(orderInfo.getTotalamount()).append("\n");
                sb.append("司磅员：").append(orderInfo.getSeller()).append("\t").append("秤号：").append(orderInfo.getTerid() + "\n");
                sb.append(BASE_COMPANY_NAME);
                sb.append("\n\n");

//                boolean isAvailable = NetworkUtil.isAvailable(context);// 有网打印二维码
//                if (!isAvailable) {
//                    sb.append("\n\n");
//                    epsPrint.setLineSpacing((byte) 32);
//                    epsPrint.PrintString(sb.toString());
//                } else {
                epsPrint.setLineSpacing((byte) 32);
                epsPrint.PrintString(sb.toString());

                String qrString = "http://data.axebao.com/smartsz/trace/?no=" + orderInfo.getBillcode();
                byte[] bytes = epsPrint.getbyteData(qrString, 32, 32);

                try {
                    if (bytes != null) {
                        epsPrint.PrintltString("扫一扫获取追溯信息：");
                        epsPrint.printQR(bytes);
//                        epsPrint.PrintltString("--------------------------------\n\n\n");
                        epsPrint.PrintltString("\n\n\n");
                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
//                }
            }
        });
    }

//    private  void saveOrderInfo(){
//
//
//    }


}

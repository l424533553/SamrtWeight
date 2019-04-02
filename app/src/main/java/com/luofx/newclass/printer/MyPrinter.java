package com.luofx.newclass.printer;

import com.axecom.smartweight.my.config.IConstants;

/**
 * author: luofaxin
 * date： 2018/10/26 0026.
 * email:424533553@qq.com
 * describe:  爱普生 打印机命令
 */
public class MyPrinter implements IConstants {

    private MyBasePrinter printer;

    public MyPrinter(MyBasePrinter epsPrint) {
        this.printer = epsPrint;
    }

//    /**
//     * 同一的    打印机打印以商通打印机为模板 修改而得
//     */
//    public void printOrder(ExecutorService executorService, final OrderInfo orderInfo) {
//        if (printer == null) {
//            return;
//        }
//        if (orderInfo == null) {
//            return;
//        }
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                List<OrderBean> orderBeans = orderInfo.getOrderItem();
//                if (orderBeans == null) {
//                    orderBeans = new ArrayList<>(orderInfo.getOrderBeans());
//                }
//                StringBuilder sb = new StringBuilder();
//                sb.append("------------交易明细------------\n");
//
//                sb.append("市场名称：").append(orderInfo.getMarketName()).append("\n");
//                sb.append("交易日期：").append(orderInfo.getTime()).append("\n");
//                sb.append("交易单号：").append(orderInfo.getBillcode()).append("\n");
//
//                if (1 == orderInfo.getSettlemethod()) {
//                    sb.append("结算方式：微信支付\n");
//                }
//                if (2 == orderInfo.getSettlemethod()) {
//                    sb.append("结算方式：支付宝支付\n");
//                }
//                if (3 == orderInfo.getSettlemethod()) {
//                    sb.append("结算方式：现金支付\n");
//                }
//
//                sb.append("卖方名称：").append(orderInfo.getSeller()).append("\n");
//                sb.append("摊位号：").append(orderInfo.getStallNo()).append("\n");
//                sb.append("商品名\b" + "单价/元\b" + "重量/kg\b" + "金额/元" + "\n");
//
//                for (int i = 0; i < orderBeans.size(); i++) {
//                    OrderBean goods = orderBeans.get(i);
//                    goods.setOrderInfo(orderInfo);
//                    sb.append(goods.getName()).append("\t").append(goods.getPrice()).append("\t").append(goods.getWeight()).append("\t").append(goods.getMoney()).append("\n");
//                }
//
//                sb.append("--------------------------------\n");
//                sb.append("合计(元)：").append(orderInfo.getTotalamount()).append("\n");
//                sb.append("司磅员：").append(orderInfo.getSeller()).append("\t").append("秤号：").append(orderInfo.getTerid() + "\n");
//                sb.append(BASE_COMPANY_NAME);
//                sb.append("\n\n");
//
//                printer.setLineSpacing((byte) 12);
//                printer.printString(sb.toString());
//
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                String qrString = "http://data.axebao.com/smartsz/trace/?no=" + orderInfo.getBillcode();
//                printer.printltString("扫一扫获取追溯信息：");
//                printer.printBitmapQR(qrString, 36, 36, 384);
//            }
//        });
//    }


}

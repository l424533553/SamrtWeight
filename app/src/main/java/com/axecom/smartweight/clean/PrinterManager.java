//package com.axecom.smartweight.manager;
//
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbManager;
//import android.text.TextUtils;
//
//import com.axecom.smartweight.bean.LocalSettingsBean;
//import com.axecom.smartweight.bean.OrderListResultBean;
//import com.axecom.smartweight.utils.SPUtils;
//import com.gprinter.command.EscCommand;
//import com.gprinter.command.LabelCommand;
//import com.gprinter.io.PortManager;
//import com.gprinter.io.SerialPort;
//import com.gprinter.io.UsbPort;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Vector;
//
///**
// * Created by Administrator on 2018/8/2.
// */
//
//public class PrinterManager {
//    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
//
//    private Context context;
//    private UsbManager usbManager;
//    private String usbName;
//    private int id = 0;
//    private PendingIntent mPermissionIntent;
//    private PortManager mUsbPort, mSerialPort;
//
//    public PrinterManager(Context context) {
//        this.context = context;
//    }
//
////    public static PrinterManager getInstances(Context context){
////        PrinterManager.context = context;
////        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
////
////        synchronized (PrinterManager.class){
////            return new PrinterManager();
////
////        }
////    }
//
//
//
//    /**
//     * usb连接
//     *
//     * @param usbDevice
//     */
//    private void usbConn(UsbDevice usbDevice) {
//        new DeviceConnFactoryManager.Build()
//                .setId(id)
//                .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.USB)
//                .setUsbDevice(usbDevice)
//                .setContext(context)
//                .build();
//        mUsbPort = new UsbPort(context, usbDevice);
//        boolean isOpenPort = mUsbPort.openPort();
//        if (isOpenPort) {
//        }
//
////        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
//    }
//
//    public void openGpinter() {
//        //获取波特率
//        int baudrate = 115200;
//        //获取串口号
//        String path = "/dev/ttyS4";
//
//        if (baudrate != 0 && !TextUtils.isEmpty(path)) {
//            //初始化DeviceConnFactoryManager
//            new DeviceConnFactoryManager.Build()
//                    //设置连接方式
//                    .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.SERIAL_PORT)
//                    .setId(0)
//                    //设置波特率
//                    .setBaudrate(baudrate)
//                    //设置串口号
//                    .setSerialPort(path)
//                    .build();
//            //打开端口
//            mSerialPort = new SerialPort(path, baudrate, 0);
//            boolean b = mSerialPort.openPort();
//            if (b) {
////
//            }
////            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].openPort();
//        }
//    }
//
////    public void sendLabel(String orderNo, String payId, String operator, String price, Bitmap bitmap,
////                          List<HotKeyBean> seledtedGoodsList) {
////        LabelCommand tsc = new LabelCommand();
////        tsc.addSize(60, 100); // 设置标签尺寸，按照实际尺寸设置
////        tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
////        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
////        tsc.addReference(0, 0);// 设置原点坐标
////        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
////        tsc.addCls();// 清除打印缓冲区
////
////        int y = 10;
////
////        tsc.addText(10, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                "深圳市安鑫宝科技发展有限公司");
////        tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                "交易日期：" + getCurrentTime());
////        tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                "交易单号：" + orderNo);
////        if (TextUtils.equals(payId, "1"))
////            tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                    "结算方式：微信");
////        if (TextUtils.equals(payId, "2"))
////            tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                    "结算方式：支付宝");
////        if (TextUtils.equals(payId, "4"))
////            tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                    "结算方式：现金");
////        tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                "卖方名称：" + operator);
////        tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                "商品名\t\t\t\t" + "单价\t\t\t\t\t" + "重量\t\t\t\t\t" + "金额");
////        tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                "\t\t\t\t\t\t\t" + "（元/斤）\t\t\t" + "(斤)\t\t\t\t\t" + "(元)");
////        tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                "-----------------------------------");
////        for (int i = 0; i < seledtedGoodsList.size(); i++) {
////            HotKeyBean goods = seledtedGoodsList.get(i);
////            tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                    goods.name + "\t\t\t\t" + goods.price + "\t\t\t\t" + goods.weight + "\t\t\t\t" + goods.grandTotal);
////        }
////        tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                "-----------------------------------");
////        tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                "合计：" + price);
////        tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                "-----------------------------------");
////        tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                "司磅员：" + operator + "\t\t\t\t\t\t\t" + "秤号：" + AccountManager.getInstance().getScalesId());
////        tsc.addText(10, y += 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
////                "追溯信息：");
////
//////        tsc.addText(180, 30, LabelCommand.FONTTYPE.TRADITIONAL_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
//////                "繁體字");
////
////        // 绘制图片
////        Bitmap b = bitmap;
////        if (bitmap != null)
////            tsc.addBitmap(20, 60, LabelCommand.BITMAP_MODE.OVERWRITE, b.getWidth(), b);
////        //绘制二维码
//////        tsc.addQRCode(105, 75, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, " www.smarnet.cc");
////        // 绘制一维条码
//////        tsc.add1DBarcode(50, 350, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, "SMARNET");
////        tsc.addPrint(1, 1); // 打印标签
////        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
////        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
////        Vector<Byte> datas = tsc.getCommand(); // 发送数据
////        byte[] bytes = GpUtils.ByteTo_byte(datas);
////        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
////        // 发送数据
//////        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null) {
//////            return;
//////        }
//////        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
////        try {
////            mUsbPort.writeDataImmediately(datas, 0, datas.size());
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//
// /*   public void printerOrderOfDayAndMonth(List<ReportResultBean.list> dataList, ReportResultBean reportResultBean) {
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines((byte) 3);
//        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
//        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
//
////        esc.addText("时间\t笔数\t重量(kg)    小计(元)    实收(元)\n");
//        for (int i = 0; i < dataList.size(); i++) {
//            esc.addText("时间：" + dataList.get(i).times + "\t\t");
//            esc.addText("笔数：" + dataList.get(i).all_num + "\n");
//            esc.addText("重量(kg)：" + dataList.get(i).total_weight + "\t\t");
//            esc.addText("小计(元)：" + dataList.get(i).total_amount + "\n");
//            esc.addText("实收(元)：" + dataList.get(i).total_amount + "\n\n");
//        }
//        esc.addText("总计\n");
//        esc.addText("笔数：" + reportResultBean.total_num + "\t\t");
//        esc.addText("重量(kg)：" + reportResultBean.total_weight + "\n");
//        esc.addText("小计(元)：" + reportResultBean.total_amount + "\t");
//        esc.addText("实收(元)：" + reportResultBean.total_amount + "\n");
//        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
//        esc.addPrintAndFeedLines((byte) 8);
//        esc.addCutPaper();
//        esc.addQueryPrinterStatus();
//        Vector<Byte> datas = esc.getCommand();
//        try {
//            mSerialPort.writeDataImmediately(datas, 0, datas.size());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }*/
//
//    public void printerOrderDetails(List<OrderListResultBean.list> orderList, OrderListResultBean orderListResultBean) {
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines((byte) 3);
//        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
//        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
//
//        for (int i = 0; i < orderList.size(); i++) {
//                esc.addText("商品名：" + orderList.get(i).goods_name + "\t\t");
//                esc.addText("时间：" + orderList.get(i).times + "\n");
//                esc.addText("重量(kg)：" + orderList.get(i).goods_weight + "\t\t");
//                esc.addText("单价/件数：" + orderList.get(i).price_number + "\n");
//                esc.addText("小计：" + orderList.get(i).total_amount + "\t\t");
//                esc.addText("结算方式：" + orderList.get(i).payment_type + "\n\n");
//        }
//        esc.addText("总计\n");
//        esc.addText("笔数：" + orderListResultBean.total + "\t\t");
//        esc.addText("总计(元)：" + orderListResultBean.total_amount + "\t");
//        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
//        esc.addPrintAndFeedLines((byte) 8);
//        esc.addCutPaper();
//        esc.addQueryPrinterStatus();
//        Vector<Byte> datas = esc.getCommand();
//        try {
//            mSerialPort.writeDataImmediately(datas, 0, datas.size());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//   /* public void printer(String orderNo, String payId, String operator, String price, Bitmap bitmap, List<HotKeyBean> seledtedGoodsList) {
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines((byte) 3);
//
//        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
//        // 设置为倍高倍宽
//        esc.addSelectPrintModes(EscCommand.FONT.FONTB, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
//        // 打印文字
//        esc.addText("深圳市安鑫宝科技发展有限公司\n");
//        esc.addPrintAndLineFeed();
//
//        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
//        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
//        esc.addText("交易日期：" + getCurrentTime() + "\n");
//        esc.addText("交易单号：" + orderNo + "\n");
//        if (TextUtils.equals(payId, "1"))
//            esc.addText("结算方式：微信\n");
//        if (TextUtils.equals(payId, "2"))
//            esc.addText("结算方式：支付宝\n");
//        if (TextUtils.equals(payId, "4"))
//            esc.addText("结算方式：现金\n");
//        esc.addText("卖方名称：" + operator + "\n");
//        esc.addText("商品名\t" + "单价(元/斤)\t" + "重量(斤)\t" + "金额(元)\n");
//        esc.addText("------------------------------------------------\n");
////        esc.addText("\t\t" + "（元/斤）\t" + "(斤)\t" + "(元)"+"\n");
//        for (int i = 0; i < seledtedGoodsList.size(); i++) {
//            HotKeyBean goods = seledtedGoodsList.get(i);
//            esc.addText(goods.name + "\t\t" + goods.price + "\t" + goods.weight + "\t\t" + goods.grandTotal + "\n");
//        }
//        esc.addText("------------------------------------------------\n\n");
//        esc.addText("合计：" + price + "\n");
//        esc.addText("------------------------------------------------\n");
//        esc.addText("司磅员：" + operator + "\t" + "秤号：" + AccountManager.getInstance().getScalesId() + "\n");
//        esc.addText("追溯信息：\n");
//        if (bitmap != null)
//            esc.addRastBitImage(bitmap, 200, 0);
//        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
//        esc.addPrintAndFeedLines((byte) 8);
//        esc.addCutPaper();
//        esc.addQueryPrinterStatus();
//        Vector<Byte> datas = esc.getCommand();
//        try {
//            mSerialPort.writeDataImmediately(datas, 0, datas.size());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].sendDataImmediately(datas);
////        runOnUiThread(new Runnable() {
////            @Override
////            public void run() {
////                clear(1);
////            }
////        });
//    }
//*/

//}

package com.axecom.smartweight.helper.printer;

import com.axecom.smartweight.entity.project.OrderBean;
import com.axecom.smartweight.entity.project.OrderInfo;
import com.xuanyuan.library.MyLog;
import com.xuanyuan.library.help.qr.MyPrinterAXEHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.axecom.smartweight.config.IConstants.BASE_COMPANY_NAME;

/**
 * 炜煌打印机，支持E39，E42，E47  ，和 WHPrinter15一致
 */
public class WHPrinterAXE extends MyBasePrinter {
    // 15.6 香山秤黑色  称重模块 参数 /dev/ttyS4", 19200,

    // 15.6 香山秤黑色  打印机参数
    public final static int BAUDRATE_AXE = 9600;
    public static final String PATH_AXE = "/dev/ttySAC4";

    public WHPrinterAXE(String path, int baudrate) {
        this.path = path;
        this.baudrate = baudrate;
    }

    @Override
    public boolean open() {
        return super.open(path, baudrate);
    }

    /**
     * 设置行距
     *
     * @param n 0~255  像素点间隔
     */
    @Override
    public void setLineSpacing(byte n) {
        write(new byte[]{27, 51, n});
    }

    /**
     * 准备二维码打印前的工作准备
     */
    @Override
    protected void readyPrinterQR() {
        write(new byte[]{27, 49, 0, 27, 51, 0});
    }

    /**
     * 打印完恢复 默认指令
     *
     * @param n 打印完 走纸几行
     */
    @Override
    public void finishPrinterQR(byte n) {
        write(new byte[]{27, 49, 3, 27, 51, 3});
    }

    /**
     * 点阵打印
     */
    public void printMatrixQR(String contents1, int width, int height, int maxPix) {
        byte[] head = {27, 75, (byte) 128, 1};
        List<byte[]> bytes = getRQMatrixData(contents1, width, height, maxPix, head);
        if (bytes != null) {
            readyPrinterQR();
            write(bytes);
            finishPrinterQR((byte) 8);
        }
    }


    public void printMatrixQR2(String contents1) {
        byte[] data = createPixelsQR(contents1, 32, 32);
        if (data != null) {
//            flush();
            readyPrinterQR();
            write(data);
            finishPrinterQR((byte) 4);
        }
    }


    /**
     * 香山 打印机  二维码 生成打印指令
     */
    public static byte[] createPixelsQR(String contents1, int width, int height) {
        return MyPrinterAXEHelper.createPixelsQR(contents1, width, height);
    }


    /**
     * 点行打印
     */
    public void printMatrixLineQR(String contents1, int width, int height, int maxPix) {
        byte[] head = {28, 75, 0, (byte) (maxPix / 8)};
        List<byte[]> bytes = getRQMatrixData(contents1, width, height, maxPix, head);
        if (bytes != null) {
            readyPrinterQR();
            write(bytes);
            finishPrinterQR((byte) 8);
        }
    }

    /**
     * 位圖打印
     */
    @Override
    public void printBitmapQR(String contents1, int width, int height, int maxPix) {
        byte[] head = {27, 42, 33, (byte) 128, 1};
        byte[] data = getRQBitmapData(contents1, width, height, maxPix, head);
        if (data != null) {
//            flush();
            readyPrinterQR();
            write(data);
            finishPrinterQR((byte) 4);
        }
    }


    /**
     * 位图打印方式 8*8
     */
    public void printBitmap88QR(String contents1, int width, int height, int maxPix) {
        byte[] head = {27, 42, 1, (byte) 128, 1};
        byte[] data = getRQBitmapData(contents1, width, height, maxPix, head);
        if (data != null) {
            flush();
            readyPrinterQR();
            write(data);
            finishPrinterQR((byte) 4);
        }
    }

    /* 普通 功能方法 ****************************************************/
    /**
     * 跳行  n 空白
     *
     * @param n 跳 空白的行数
     */
    public void printJumpLine(byte n) {
        byte[] by = {27, 74, n};
        write(by);
    }

    /**
     * 设置左限
     */
    public void setLeftLimit(byte n) {
        byte[] by = {27, 108, n};
        write(by);
    }

    public void setRightLimit(byte n) {
        byte[] by = {27, 81, n};
        write(by);
    }

    /**
     * 同一的    打印机打印以商通打印机为模板 修改而得
     */
    @Override
    public void printOrder(ExecutorService executorService, final OrderInfo orderInfo) {
        if (orderInfo == null) {
            return;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                printer15();
            }

            private void printer15() {
                int printSize = 0;
                List<OrderBean> orderBeans = orderInfo.getOrderItem();
                if (orderBeans == null) {
                    orderBeans = new ArrayList<>(orderInfo.getOrderBeans());
                }

                alignment((byte) 1);
                setLineSpacing((byte) 15);
                printJumpLine((byte) 2);
                printString(orderInfo.getMarketName() + ",欢迎你!\n");


                alignment((byte) 0);
                setLineSpacing((byte) 9);
                StringBuilder sb1 = new StringBuilder();
                sb1.append("交易日期：").append(orderInfo.getTime()).append("\n");
                sb1.append("交易单号：").append(orderInfo.getBillcode()).append("\n");

                if (1 == orderInfo.getSettlemethod()) {
                    sb1.append("结算方式：扫码支付\n");
                }
                if (2 == orderInfo.getSettlemethod()) {
                    sb1.append("结算方式：扫码支付\n");
                }
                if (0 == orderInfo.getSettlemethod()) {
                    sb1.append("结算方式：现金支付\n");
                }
                try {
                    sb1.append("摊位号：").append(orderInfo.getStallNo()).append("\n");
                    setLineSpacing((byte) 9);
                    printSize += printString(sb1.toString());


                    printSize += write(cropByte("司磅员：" + orderInfo.getSeller(), 18, true));
                    printSize += write(cropByte("秤号：" + orderInfo.getTerid(), 12, true));
//                    setLineSpacing((byte) 21);
                    printJumpLine((byte) 12);
                    setLineSpacing((byte) 9);
                    printSize += printString("------------商品明细------------\n");

                    setLeftLimit((byte) 0);
                    setRightLimit((byte) 0);
                    printSize += printString("商品名  " + "单价/元 " + "重量/kg " + "金额/元 " + "\n");

                    for (int i = 0; i < orderBeans.size(); i++) {
                        OrderBean goods = orderBeans.get(i);
                        goods.setOrderInfo(orderInfo);
                        byte[] by = cropByte(goods.getName(), 8, true);

                        printSize += write(cropByte(goods.getName(), 8, true));
                        printSize += write(cropByte(goods.getPrice(), 7, true));
                        printSize += write(cropByte(goods.getWeight(), 7, true));
                        printSize += write(cropByte(goods.getMoney(), 7, false));
//                    sb.append(goods.getName()).append("\t").append(goods.getPrice()).append("\t").
//                            append(goods.getWeight()).append("\t").append(goods.getMoney()).append("\n");
                    }

                    alignment((byte) 2);
                    String sb2 = "--------------------------------\n" +
                            "合计(元)：" + orderInfo.getTotalamount() + "\n";
                    printSize += printString(sb2);
                    alignment((byte) 0);
                    printSize += printString(BASE_COMPANY_NAME + "\n");

//                    String sb2 = "--------------------------------\n" +
//                            "合计(元)：" + orderInfo.getTotalamount() + "\n" +
//                            "司磅员：" + orderInfo.getSeller() + "\n" + "秤号：" + orderInfo.getTerid() + "\n" +
//                            BASE_COMPANY_NAME +
//                            "\n\n";
//                    printSize += printString(sb2);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (!isNoQR) {
                    String qrString = "http://data.axebao.com/smartsz/trace/?no=" + orderInfo.getBillcode();
                    printltString("扫一扫获取追溯信息：");
                    MyLog.myInfo(" 打印数据长度====" + printSize);
                    int time;
                    if (printSize < 300) {
                        time = 50;
                    } else if (printSize < 400) {
                        time = 65;
                    } else if (printSize < 500) {
                        time = 80;
                    } else if (printSize < 600) {
                        time = 95;
                    } else if (printSize < 700) {
                        time = 110;
                    } else if (printSize < 1400) {
                        time = 125;
                    } else {
                        time = 140;
                    }
                    printQR(qrString, 24, 24, time, true);
                    printJumpLine((byte) 36);
                    printString("\n\n");
                }
            }

      /*      private void printer8() {
                int printSize = 0;
                List<OrderBean> orderBeans = orderInfo.getOrderItem();
                if (orderBeans == null) {
                    orderBeans = new ArrayList<>(orderInfo.getOrderBeans());
                }

//                MyLog.myInfo(" 打印数据长度====" + printSize);
//                int time = 0;
//                if (printSize < 300) {
//                    time = 50;
//                } else if (printSize < 400) {
//                    time = 65;
//                } else if (printSize < 500) {
//                    time = 80;
//                } else if (printSize < 600) {
//                    time = 95;
//                } else if (printSize < 700) {
//                    time = 110;
//                } else if (printSize < 1400) {
//                    time = 125;
//                } else {
//                    time = 140;
//                }

                setLineSpacing((byte) 9);
                alignment((byte) 0);
                if (!isNoQR) {
                    String qrString = "http://data.axebao.com/smartsz/trace/?no=" + orderInfo.getBillcode();
                    printQR(qrString, 24, 24, 50, false);

//                    setLeftLimit((byte) 0);
//                    setRightLimit((byte) 0);
//                    setLineSpacing((byte) 9);
////                    alignment((byte) 0);

                    printltString("扫一扫获取追溯信息：\n");
                }
                String sb2 = BASE_COMPANY_NAME + "\n";
                setLineSpacing((byte) 9);
                printSize += printString(sb2);

                alignment((byte) 2);
                String sb3 = "合计(元)：" + orderInfo.getTotalamount() + "\n" + "--------------------------------\n";
                printSize += printString(sb3);

                alignment((byte) 0);
                try {
                    for (int i = orderBeans.size() - 1; i >= 0; i--) {
                        OrderBean goods = orderBeans.get(i);
                        goods.setOrderInfo(orderInfo);
                        byte[] by = cropByte(goods.getName(), 8, true);
                        printSize += write(cropByte(goods.getName(), 8, true));
                        printSize += write(cropByte(goods.getPrice(), 7, true));
                        printSize += write(cropByte(goods.getWeight(), 7, true));
                        printSize += write(cropByte(goods.getMoney(), 7, false));
//                    sb.append(goods.getName()).append("\t").append(goods.getPrice()).append("\t").
//                            append(goods.getWeight()).append("\t").append(goods.getMoney()).append("\n");
                    }

                    alignment((byte) 0);
                    setLineSpacing((byte) 9);
                    printString("商品名  " + "单价/元 " + "重量/kg " + "金额/元 " + "\n");

                    setLineSpacing((byte) 9);
                    printString("------------商品明细------------\n");
                    printJumpLine((byte) 12);

//                  sb.append("秤号：").append(orderInfo.getTerid()).append("\n");
//                  sb.append("司磅员：").append(orderInfo.getSeller()).append("\n");

                    printSize += write(cropByte("司磅员：" + orderInfo.getSeller(), 18, true));
                    printSize += write(cropByte("秤号：" + orderInfo.getTerid(), 12, true));
                    printString("摊位号：" + orderInfo.getStallNo() + "\n");

                    StringBuilder sb = new StringBuilder();
                    if (1 == orderInfo.getSettlemethod()) {
                        sb.append("结算方式：扫码支付\n");
                    }
                    if (2 == orderInfo.getSettlemethod()) {
                        sb.append("结算方式：扫码支付\n");
                    }
                    if (0 == orderInfo.getSettlemethod()) {
                        sb.append("结算方式：现金支付\n");
                    }
                    sb.append("交易单号：").append(orderInfo.getBillcode()).append("\n");
                    sb.append("交易日期：").append(orderInfo.getTime()).append("\n\n");

                    alignment((byte) 0);
                    setLineSpacing((byte) 9);
                    printSize += printString(sb.toString());

                    *//* ******************************************************************************//*
                    alignment((byte) 1);
                    printJumpLine((byte) 12);
                    setLineSpacing((byte) 9);
                    printString(orderInfo.getMarketName() + ",欢迎你!\n");
                    setLineSpacing((byte) 12);
                    printString("\n\n\n");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }*/
        });
    }

    private byte[] cropByte(String str, int length, boolean isEmpty) throws UnsupportedEncodingException {
        byte[] data = new byte[length + 1];
        for (int i = 0; i < data.length; i++) {
            data[i] = 32;
        }
        if (!isEmpty) {
            data[length] = 10;
        }
//        byte[] by = str.getBytes();
        byte[] by = str.getBytes("GBK");
        int destlengt;
        if (by.length > length) {
            destlengt = length;
        } else {
            destlengt = by.length;
        }
        System.arraycopy(by, 0, data, 0, destlengt);
        return data;
    }

    /**
     * @param printMsg    打印数据
     * @param width       宽
     * @param height      高
     * @param time        打印时间间隔
     * @param isInversion 是否倒置打印  true 15寸打法
     */
    private void printQR(String printMsg, int width, int height, int time, boolean isInversion) {
        List<byte[]> data = createQR222(printMsg, width, height);
//        MyLog.blue("二维码数据大小" + data.size());
        byte[] by0 = {27, 49, 0, 27, 51, 0};// 设置行距 0 像素
        write(by0);

        for (int i = 0; i < data.size(); i++) {
            write(data.get(i));
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        finishPrinterQR((byte) 0);

    }

    public static List<byte[]> createQR222(String contents1, int width, int height) {
        return MyPrinterAXEHelper.createQR222(contents1, width, height);
    }
}



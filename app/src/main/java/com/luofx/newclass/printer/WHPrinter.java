package com.luofx.newclass.printer;

import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.my.entity.OrderInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.xuanyuan.library.MyLog;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.axecom.smartweight.my.config.IConstants.BASE_COMPANY_NAME;

/**
 * 炜煌打印机，支持E39，E42，E47
 */
public class WHPrinter extends MyBasePrinter {
    // 15.6 香山秤黑色  称重模块 参数 /dev/ttyS4", 19200,

    // 15.6 香山秤黑色  打印机参数
    public final static int BAUDRATE_XS15 = 115200;
    public static final String PATH_XS15 = "/dev/ttyS1";

    // 8 香山秤黑色  打印机参数
    public final static int BAUDRATE_XS8 = 9600;
    public static final String PATH_XS8 = "/dev/ttyS3";
    private int typePrinter;

    public WHPrinter(String path, int baudrate) {
        this.path = path;
        this.baudrate = baudrate;
        if (PATH_XS8.equalsIgnoreCase(path) && BAUDRATE_XS8 == baudrate) {
            typePrinter = 1;
        } else {
            typePrinter = 0;
        }
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
     *
     * @return 返回指令
     */
    @Override
    protected void readyPrinterQR() {
        write(new byte[]{27, 49, 0, 27, 51, 0});
    }

    /**
     * 打印完恢复 默认指令
     *
     * @param n 打印完 走纸几行
     * @return
     */
    @Override
    public void finishPrinterQR(byte n) {
        write(new byte[]{27, 49, 3, 27, 51, 3, 27, 100, n});
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
     *
     * @param contents1
     * @param width
     * @param height
     * @return
     */
    public static byte[] createPixelsQR(String contents1, int width, int height) {
        //判断URL合法性
        if (contents1 == null || "".equals(contents1) || contents1.length() < 1) {
            return null;
        }

//        List<byte[]> list = new ArrayList<>();
//        Bitmap bitmap = null;
        int QR_WIDTH = width * 8;  //  二维码宽
        int QR_HEIGHT = height * 8; //  二维码高
        byte[] bytes = new byte[(32 * 8 + 4) * height];
        try {

            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "gbk");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(contents1, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，两个for循环是图片横列扫描的结果

            int srcPos = 0;
            for (int i = height - 1; i >= 0; i--) {
                byte[] rowByte = new byte[width * 8 + 4];// 384+4
                rowByte[0] = 27;
                rowByte[1] = 75;
                rowByte[2] = (byte) 128;
                rowByte[3] = 1;
                for (int j = QR_WIDTH + 3; j >= 4; j--) {
                    int[] dest = new int[8];
                    dest[7] = getPixels(bitMatrix, i * 8, j - 4);
                    dest[6] = getPixels(bitMatrix, i * 8 + 1, j - 4);
                    dest[5] = getPixels(bitMatrix, i * 8 + 2, j - 4);
                    dest[4] = getPixels(bitMatrix, i * 8 + 3, j - 4);
                    dest[3] = getPixels(bitMatrix, i * 8 + 4, j - 4);
                    dest[2] = getPixels(bitMatrix, i * 8 + 5, j - 4);
                    dest[1] = getPixels(bitMatrix, i * 8 + 6, j - 4);
                    dest[0] = getPixels(bitMatrix, i * 8 + 7, j - 4);
                    rowByte[j] = dealArray(dest);
                }
                System.arraycopy(rowByte, 0, bytes, srcPos, width * 8 + 4);
                srcPos += width * 8 + 4;
//                list.add(rowByte);
            }

//          //生成二维码图片的格式，使用ARGB_8888
//          Bitmap  bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ALPHA_8);
//          bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
//          list = getPrintData(pixels, QR_WIDTH, QR_HEIGHT);

            //显示到一个ImageView上面
        } catch (WriterException e) {
            e.printStackTrace();
        }
//        return list;
        return bytes;
    }

    private static int getPixels(BitMatrix bitMatrix, int y, int x) {
        if (bitMatrix.get(x, y)) {
            return 1;
        } else {
            return 0;
        }
    }

    private static byte dealArray(int[] dest) {
        int be = 0;
        int temp;
        for (int j = 0; j < 8; j++) {
            temp = dest[j];
            if (j == 0) {
                be = temp;
            } else {
                be = (be << 1) | temp;
            }
        }
        return (byte) be;
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

//    /**
//     * 位圖打印
//     */
//    @Override
//    public void printBitmapQR(String contents1, int width, int height, int maxPix) {
//        byte[] head = {27, 42, 33, (byte) 128, 1};
//        List<byte[]> data = getRQBitmapDataCopy(contents1, width, height, maxPix, head);
//        if (data != null) {
//            readyPrinterQR();
//            flush();
//            write(data);
//            finishPrinterQR((byte) 4);
//        }
//    }

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
     *
     * @param n
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
                if (typePrinter == 0) {
                    printer15();
                } else if (typePrinter == 1) {
                    printer8();
                }

                //308 KEYI   474
//                 printBitmapQR(qrString, 48, 48, 384);
//                printMatrixQR2(qrString);
//                printMatrixLineQR(qrString, 32, 32, 256);
            }

            private void printer15() {
                int printSize = 0;
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
                sb.append("商品名  " + "单价/元 " + "重量/kg " + "金额/元 " + "\n");
                setLeftLimit((byte) 0);
                setRightLimit((byte) 0);
                setLineSpacing((byte) 9);

                printSize += printString(sb.toString());

                try {
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


                    String sb2 = "--------------------------------\n" +
                            "合计(元)：" + orderInfo.getTotalamount() + "\n" +
                            "司磅员：" + orderInfo.getSeller() + "\n" + "秤号：" + orderInfo.getTerid() + "\n" +
                            BASE_COMPANY_NAME +
                            "\n\n";
                    printSize += printString(sb2);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(!isNoQR){
                    String qrString = "http://data.axebao.com/smartsz/trace/?no=" + orderInfo.getBillcode();
                    printltString("扫一扫获取追溯信息：");
                    MyLog.myInfo(" 打印数据长度====" + printSize);
                    int time = 0;
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

                    printQR(qrString, 32, 32, time, true);
                }
            }

            private void printer8() {
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
                if(!isNoQR){
                    String qrString = "http://data.axebao.com/smartsz/trace/?no=" + orderInfo.getBillcode();
                    printQR(qrString, 32, 32, 50, false);

                    setLeftLimit((byte) 0);
                    setRightLimit((byte) 0);
//                setLineSpacing((byte) 9);
                    printltString("扫一扫获取追溯信息：");
                }

                String sb2 = "\n" + BASE_COMPANY_NAME + "\n秤号：" + orderInfo.getTerid() + "\n"
                        + "司磅员：" + orderInfo.getSeller() + "\n"
                        + "合计(元)：" + orderInfo.getTotalamount() + "\n"
                        + "--------------------------------\n";
                printSize += printString(sb2);

                try {
                    for (int i = orderBeans.size()-1; i >=0 ; i--) {
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
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                StringBuilder sb = new StringBuilder();
                sb.append("商品名  " + "单价/元 " + "重量/kg " + "金额/元 " + "\n");
                sb.append("摊位号：").append(orderInfo.getStallNo()).append("\n");
                sb.append("卖方名称：").append(orderInfo.getSeller()).append("\n");
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
                sb.append("交易日期：").append(orderInfo.getTime()).append("\n");
                sb.append("市场名称：").append(orderInfo.getMarketName()).append("\n");

                sb.append("------------交易明细------------\n");
                printSize += printString(sb.toString());
                printString(" \n\n\n\n\n");

            }
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
        MyLog.blue("二维码数据大小" + data.size());
        byte[] by0 = {27, 49, 0};// 设置行距 0 像素
        write(by0);

        if (isInversion) {
            for (int i = data.size() - 1; i >= 0; i--) {
                write(data.get(i));
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            finishPrinterQR((byte) 4);
            byte[] by2 = {10, 10};//二维码生成完 恢复默认间距 3像素
            write(by2);

        } else {
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
    }

    public static List<byte[]> createQR222(String contents1, int width, int height) {
        //判断URL合法性
        if (contents1 == null || "".equals(contents1) || contents1.length() < 1) {
            return null;
        }
        String contents = null;
        List<byte[]> list = new ArrayList<>();
//        Bitmap bitmap = null;
        int QR_WIDTH = width * 8;  //  二维码宽
        int QR_HEIGHT = height * 8; //  二维码高
        try {
            contents = new String(contents1.getBytes("UTF-8"), "gbk");
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "gbk");

            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.MARGIN, 0);  //设置白边
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            MyLog.blue("图片大小   getHeight=" + bitMatrix.getHeight() + " getRowSize= " + bitMatrix.getRowSize() + "   bitMatrix.getWidth()=" + bitMatrix.getWidth());

            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果

            for (int i = height - 1; i >= 0; i--) {
//                int length = QR_WIDTH + 4;
//                byte[] rowByte = new byte[length];// 384+4
                byte[] rowByte = new byte[384 + 4];// 384+4
                rowByte[0] = 0x1B;
                rowByte[1] = 0x4B;
                rowByte[2] = (byte) 0x80;
                rowByte[3] = 0x01;
                for (int j = 4; j < QR_WIDTH + 4; j++) {
                    int[] dest = new int[8];
                    dest[0] = getPixels(bitMatrix, i * 8, j - 4);
                    dest[1] = getPixels(bitMatrix, i * 8 + 1, j - 4);
                    dest[2] = getPixels(bitMatrix, i * 8 + 2, j - 4);
                    dest[3] = getPixels(bitMatrix, i * 8 + 3, j - 4);
                    dest[4] = getPixels(bitMatrix, i * 8 + 4, j - 4);
                    dest[5] = getPixels(bitMatrix, i * 8 + 5, j - 4);
                    dest[6] = getPixels(bitMatrix, i * 8 + 6, j - 4);
                    dest[7] = getPixels(bitMatrix, i * 8 + 7, j - 4);
                    rowByte[j] = dealArray(dest);
                }
//                rowByte[length-2]=10;
//                rowByte[length-1]=13;
                list.add(rowByte);
//                MyLog.myInfo("byte数组数据==="+rowByte.length+" 数据实体***   " + Arrays.toString(rowByte));
            }
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//      MyLog.myInfo("byte数组数据===" + list.size());
        return list;
    }

}

//472


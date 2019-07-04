package com.axecom.smartweight.helper.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.axecom.smartweight.entity.project.OrderBean;
import com.axecom.smartweight.entity.project.OrderInfo;
import com.xuanyuan.library.help.qr.MyHDDPrinterHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.axecom.smartweight.config.IConstants.BASE_COMPANY_NAME;

/**
 * 不知道哪里来的 杂牌子 打印机
 */
public class HDDPrinter extends MyBasePrinter {

    // 15.6 香山秤黑色  打印机参数
//    public final static int BAUDRATE_XS15 = 57600;
    public final static int BAUDRATE_XS15 = 115200;
    public static final String PATH_XS15 = "/dev/ttyS1";

    public HDDPrinter(String path, int baudrate) {
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
        return MyHDDPrinterHelper.createPixelsQR(contents1, width, height);
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
//            finishPrinterQR((byte) 4);
        }

        Log.i("yyy", "data555==" + Arrays.toString(data));
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
        List<byte[]> data = getRQBitmap88Data(contents1, width, height, maxPix, head);
        if (data != null) {
            flush();
            readyPrinterQR();
            write(data);
//            finishPrinterQR((byte) 4);
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
     * @param n 设置字体的大小
     */
    public void setSize(byte n) {
        byte[] by = {29, 33, n};
        write(by);
    }

    public void setBase(byte n) {
        byte[] by = {27, 33, n};
        write(by);
    }

    /**
     * 清空缓存
     */
    public void setClearCarsh() {
        byte[] by = {27, 64};
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
     * 测试打印两张bitmap拼接打印
     */
    public void printTest(Context context) {
        printString("样品编号：F1508A00237\n");
        printString("样品名称：原味蛋糕\n");
        printString("样品状态：留样 复测 待测 已测\n");

//        printJumpLine((byte) 2);
//        printString("正样");
//        printString("欢迎你欢迎你欢迎你欢迎你欢迎你欢迎你欢迎你欢迎你\n");
////
////        printJumpLine((byte) 3);
//        setSize((byte) 16);
//        alignment((byte) 2);
//        printString("正样\n");

//      String qrString = "http://data.axebao.com/smartsz/trace/?no=";
        String qrString = "这是一个测试数据\n这是一个测试数据\n这是一个测试数据\n";
        printQRTest(context, qrString, 48, 48);

        setSize((byte) 0);
//      setClearCarsh();
//      printJumpLine((byte) 1);
//      printString("字体变大了字体变大了字体变大了字体变大了\n\n\n\n");

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
                printString(orderInfo.getMarketName() + ",欢迎你!\n");
                setLineSpacing((byte) 32);

                alignment((byte) 0);
                setLineSpacing((byte) 24);
                printJumpLine((byte) 12);
                printSize += printString("交易日期："+orderInfo.getTime()+"\n");
                printJumpLine((byte) 9);
                printSize += printString("交易单号："+orderInfo.getBillcode()+"\n");
                printJumpLine((byte) 9);

                if (1 == orderInfo.getSettlemethod()) {
                    printSize += printString("结算方式：扫码支付\n");
                    printJumpLine((byte) 9);
                }
                if (2 == orderInfo.getSettlemethod()) {
                    printSize += printString("结算方式：扫码支付\n");
                    printJumpLine((byte) 9);
                }
                if (0 == orderInfo.getSettlemethod()) {
                    printSize += printString("结算方式：现金支付\n");
                    printJumpLine((byte) 9);
                }
                printSize += printString("摊位号："+orderInfo.getStallNo()+"\n");
                printJumpLine((byte) 9);
                printSize += printString("司磅员："+orderInfo.getSeller()+"\n");
                printJumpLine((byte) 9);
                printSize += printString("秤号："+orderInfo.getTerid()+"\n");
                printJumpLine((byte) 9);

                try {
                    printSize += printString("------------商品明细------------\n");
                    printJumpLine((byte) 12);
                    setLeftLimit((byte) 0);
                    setRightLimit((byte) 0);

                    printSize += printString("商品名  " + "单价/元 " + "重量/kg " + "金额/元 " + "\n");
                    printJumpLine((byte) 12);
                    for (int i = 0; i < orderBeans.size(); i++) {
                        OrderBean goods = orderBeans.get(i);
                        goods.setOrderInfo(orderInfo);
                        byte[] by = cropByte(goods.getName(), 8, true);

                        printSize += write(cropByte(goods.getName(), 8, true));
                        printSize += write(cropByte(goods.getPrice(), 7, true));
                        printSize += write(cropByte(goods.getWeight(), 7, true));
                        printSize += write(cropByte(goods.getMoney(), 7, false));
                    }

                    setLineSpacing((byte) 24);
                    alignment((byte) 2);
                    String sb2 = "--------------------------------\n" +
                            "合计(元)：" + orderInfo.getTotalamount() + "\n";
                    printSize += printString(sb2);
                    setLineSpacing((byte) 24);
                    printJumpLine((byte) 12);
                    alignment((byte) 0);
                    printSize += printString(BASE_COMPANY_NAME + "\n");
                    printJumpLine((byte) 12);

//                    printSize += printString(sb2);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (!isNoQR) {
                    String qrString = "http://data.axebao.com/smartsz/trace/?no=" + orderInfo.getBillcode();
                    printltString("扫一扫获取追溯信息：");
//                    MyLog.myInfo(" 打印数据长度====" + printSize);
                    int time;
                    if (printSize < 300) {
                        time = 350;
                    } else if (printSize < 400) {
                        time = 450;
                    } else if (printSize < 500) {
                        time = 550;
                    } else if (printSize < 600) {
                        time = 650;
                    } else if (printSize < 700) {
                        time = 700;
                    } else if (printSize < 1400) {
                        time = 800;
                    } else {
                        time = 900;
                    }

                    try {
                        Thread.sleep(100);
                        printQR(qrString, 24, 24, time, true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    printString("\n\n");
                    printJumpLine((byte) 48);
                }
            }
        });
    }

    /**
     * 复制数据数组
     *
     * @param str     内容部分
     * @param length  数据长度
     * @param isEmpty 是否为空
     * @return 返回字节数组
     * @throws UnsupportedEncodingException 字节编码异常
     */
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
    private void printQR(String printMsg, int width, int height, int time, boolean isInversion) throws InterruptedException {

        printJumpLine((byte) 2);
        byte[] head = {0x1B, 0x2A, 33, (byte) 0x80, 1};
//        printBitmapQR(printMsg, 24, 24, 384);

        flush();

        int sleepTime = time;
        List<byte[]> bytes = getRQBitmapDataCopy22(printMsg, width, height, 384, head);
        for (int i = 0; i < bytes.size(); i++) {
            if (sleepTime > 0) {
                Thread.sleep(sleepTime);
            }
            write(bytes.get(i));
            sleepTime -= 50;
        }
    }

    private void printQRTest(Context context, String printMsg, int width, int height) {
        List<byte[]> data = MyHDDPrinterHelper.getBytes(context, printMsg, width, height);
//        MyLog.blue("二维码数据大小" + data.size());
        byte[] by0 = {27, 49, 0, 27, 51, 0};// 设置行距 0 像素
        write(by0);
        try {
            for (int i = 0; i < data.size(); i++) {
//            if (i %3==2) {
                write(data.get(i));
                Thread.sleep(80);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//            finishPrinterQR((byte) 4);
//            byte[] by2 = {10, 10};//二维码生成完 恢复默认间距 3像素
//            write(by2);

        setBase((byte) 0);
        finishPrinterQR((byte) 0);
    }


    public static List<byte[]> createQRXS(String contents1, int width, int height) {
        return MyHDDPrinterHelper.createQRXS(contents1, width, height);
    }

    public static List<byte[]> createQRHHD(String contents1, int width, int height) {
        return MyHDDPrinterHelper.createQRHHD(contents1, width, height);
    }

    /***  待删除  ***********************************************************/
    private static int[] getPixels(Context context, String text) {
        Bitmap secondBitmap = createQRBitmap(text);
        Bitmap firstBitmap = textAsBitmap(text, 20);
        Bitmap test = add2Bitmap(firstBitmap, secondBitmap);

        int QR_WIDTH = 384;
        int QR_HEIGHT = 384;
        int[] pixels = new int[QR_WIDTH * QR_HEIGHT];

        for (int i = 0; i < test.getHeight(); i++) {
            for (int j = 0; j < test.getWidth(); j++) {
                if (test.getPixel(j, i) == -1) {
                    pixels[i * test.getWidth() + j] = 0;
                } else if (test.getPixel(j, i) == -16777216) {
                    pixels[i * test.getWidth() + j] = 1;
                } else if (test.getPixel(j, i) != 0) {
                    pixels[i * test.getWidth() + j] = 1;
                }
//                System.out.print(test.getPixel(j, i));
            }
//            System.out.println("\n");
        }

        return pixels;
    }

    /**
     * 横向拼接
     * <功能详细描述>
     *
     * @param first
     * @param second
     * @return
     */
    private static Bitmap add2Bitmap(Bitmap first, Bitmap second) {
        // 原始数据
//        int width = first.getWidth() + second.getWidth();
//        int height = Math.max(first.getHeight(), second.getHeight());

        int width = 384;
        int height = 384;
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, first.getWidth(), 0, null);
        return result;
    }

    /**
     * @param contents 二维码内容，可以是url网址，可以是普通字符串
     */
    public static Bitmap createQRBitmap(String contents) {
        return MyHDDPrinterHelper.createQRBitmap(contents);
    }

    /**
     * 将文字生成图片
     *
     * @param text
     * @param textSize
     * @return
     */
    public static Bitmap textAsBitmap(String text, float textSize) {
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        StaticLayout layout = new StaticLayout(text, textPaint, 250,
                Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.0f, true);

        Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
        layout.draw(canvas);
        return bitmap;
    }

//    public static List<byte[]> getBytes(Context context, String contents1, int width, int height) {
//        List<byte[]> list = new ArrayList<>();
//        int[] data = getPixels(context, contents1);
//        int QR_WIDTH = width * 8;  //  二维码宽
//        int QR_HEIGHT = height * 8; //  二维码高
//
//        for (int i = 0; i < height; i++) {
////                int length = QR_WIDTH + 4;
////                byte[] rowByte = new byte[length];// 384+4
//            byte[] rowByte = new byte[384 + 4];// 384+4
//            rowByte[0] = 0x1B;
//            rowByte[1] = 0x4B;
//            rowByte[2] = (byte) 0x80;
//            rowByte[3] = 0x01;
//            for (int j = 4; j < QR_WIDTH + 4; j++) {
//                int[] dest = new int[8];
//                dest[0] = data[i * 8 * QR_WIDTH + j - 4];
//                dest[1] = data[(i * 8 + 1) * QR_WIDTH + j - 4];
//                dest[2] = data[(i * 8 + 2) * QR_WIDTH + j - 4];
//                dest[3] = data[(i * 8 + 3) * QR_WIDTH + j - 4];
//                dest[4] = data[(i * 8 + 4) * QR_WIDTH + j - 4];
//                dest[5] = data[(i * 8 + 5) * QR_WIDTH + j - 4];
//                dest[6] = data[(i * 8 + 6) * QR_WIDTH + j - 4];
//                dest[7] = data[(i * 8 + 7) * QR_WIDTH + j - 4];
//
//                rowByte[j] = dealArray(dest);
//            }
////                rowByte[length-2]=10;
////                rowByte[length-1]=13;
//            list.add(rowByte);
//            MyLog.myInfo("byte数组数据===" + rowByte.length + " 数据实体***   " + Arrays.toString(rowByte));
//        }
//        return list;
//    }

    //TODO  测试方法，待删除
    public static List<byte[]> createTest(String contents1, int width, int height) {
        return MyHDDPrinterHelper.createTest(contents1, width, height);
    }

}

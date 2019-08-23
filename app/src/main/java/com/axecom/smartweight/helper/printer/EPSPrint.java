package com.axecom.smartweight.helper.printer;

import android.content.Context;
import android.text.TextUtils;

import com.axecom.smartweight.entity.project.OrderBean;
import com.axecom.smartweight.entity.project.OrderInfo;
import com.xuanyuan.library.help.qr.MyEPSPrintHelper;
import com.xuanyuan.library.help.qr.QRHelper2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.axecom.smartweight.config.IConstants.BASE_COMPANY_NAME;

/**
 * Fun： 愛普生的打印机
 * Author：Linus_Xie
 * Date：2018/8/2 14:22
 */
public class EPSPrint extends MyBasePrinter {

//    final byte[] PRINT_Big = new byte[]{0x1d, 0x21, 0x11};//打印大字
//    final byte[] PRINT_Big3 = new byte[]{0x1d, 0x21, 0x01};//打印大字(不加宽)
//    final byte[] PRINT_Big2 = new byte[]{0x1d, 0x21, 0x10};//打印大字(不加高)
//    final byte[] PRINT_Center = new byte[]{0x1B, 0x61, 0x49};//居中
//    final byte[] PRINT_Num = new byte[6];//数量

    private final byte[] PRINT_Small = new byte[]{0x1d, 0x21, 0x00};//打印正常字
    private final byte[] PRINT_Reset = new byte[]{0x1b, 0x40};//重置
    private final byte[] PRINT_Wrap = new byte[]{0x0A};//换行  LF

    private android_serialport_api.SerialPort mSerialPortBalance = null;

    @Override
    public void printBitmapQR(String contents1, int width, int height, int maxPix) {

    }

    @Override
    public boolean open() {
        return super.open(path, baudrate);
    }

    public EPSPrint(String path, int baudrate) {
        this.path = path;
        this.baudrate = baudrate;
    }

    public final static int BAUDRATE_ST = 9600;
    public static final String PATH_ST = "/dev/ttyS1";

    public void PrintST(Context context) {
        center("====+++++++++++++++++++++++++++++++++====", true);
    }

    /**
     * 打字并换行  ,打完之后跳4行
     */
    public void PrintltString(String msg) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                byte[] by1 = {27, 51, 0};
                getSerialPortPrinter().getOutputStream().write(by1);
                byte[] by = (msg + "\n").getBytes("GBK");
                getSerialPortPrinter().getOutputStream().write(by);
//            PrintJumpLine((byte) 4);
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 打字并换行  ,打完之后跳4行
     */
    private void PrintltString(String msg, byte spaceLine) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                byte[] by1 = {27, 51, spaceLine};
                getSerialPortPrinter().getOutputStream().write(by1);
                byte[] by = (msg + "\n").getBytes("GBK");
                getSerialPortPrinter().getOutputStream().write(by);
////            PrintJumpLine((byte) 4);
//            Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置打印机
     */
    public void reset() throws IOException {
        getSerialPortPrinter().getOutputStream().write(PRINT_Reset);
    }

    /**
     * @param n 用于设置打印字符的方式。默认值是0
     *          位0：保留
     *          位1：1：字体反白
     *          位2：1：字体上下倒置
     *          位3：1：字体加粗
     *          位4：1：双倍高度
     *          位5：1：双倍宽度
     *          位6：1：删除线
     *          位7：1：下划线
     */
    private void setWordMode(byte n) throws IOException {
        byte[] by = {27, 33, n};
        getSerialPortPrinter().getOutputStream().write(by);
    }

    /**
     * 0 不加粗  ，1 加粗
     *
     * @param n 27 69 n
     *          n最低位有效，
     *          等于0时取消字体加粗
     *          非0时设置字体加粗
     */
    public void isWordBlod(byte n) throws IOException {
        byte[] by = {27, 69, n};
        getSerialPortPrinter().getOutputStream().write(by);
    }

    public void PrintNetLine(Context context) {
        byte[] by = {61, 43, 43, 43, 61};
//        resetBalance(PRINT_Wrap);
//        resetBalance(PRINT_Reset);
        resetBalance(PRINT_Small);
        resetBalance(by);
//        resetBalance(PRINT_Wrap);
        resetBalance(by);
    }


    /**
     * 跳行  n 空白
     *
     * @param n 跳 空白的行数
     */
    public void PrintJumpLine(byte n) throws IOException {
        byte[] by = {27, 100, n};
        getSerialPortPrinter().getOutputStream().write(by);
    }


    /**
     * 设置打印机的 打印间隔
     *
     * @param n 0~255  像素点间隔
     */
    public void setLineSpacing(byte n) {
        byte[] by = {27, 51, n};
        try {
            getSerialPortPrinter().getOutputStream().write(by);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void readyPrinterQR() {

    }

    @Override
    public void finishPrinterQR(byte n) {

    }

    /**
     * 打印二维码
     */
    private void printQR(byte[] bytes) {
        try {
            //设置间距 为 0
            byte[] by = {27, 51, 0};
            getSerialPortPrinter().getOutputStream().write(by);
//            int width = 32;
//            int height = 32;
//            byte[] bytes = QRHelper2.createPixelsQR(message, width, height);
//            List<byte[]> data = QRHelper2.createPixelsQR(message, width, height);
//            byte[] bytes = new byte[(32 * 8 + 5) * height];

//            int srcPos = 0;
//            for (int i = 0; i < data.size(); i++) {
////                System.arraycopy(bytes, srcPos, data.get(i), 0, );
//
//                System.arraycopy( data.get(i) , 0,  bytes, srcPos, width * 8 + 5);
//                srcPos += width * 8 + 5;

////                getSerialPort().getOutputStream().write(data.get(i));
////                getSerialPort().getOutputStream().flush();
////                Thread.sleep(5);
//            }

            getSerialPortPrinter().getOutputStream().write(bytes);
//            getSerialPort().getOutputStream().write(data.get(i));
            getSerialPortPrinter().getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getbyteData(String message, int width, int height) {
        return QRHelper2.createPixelsQR(message, width, height);
    }

    public byte[] getbyteData2(String message, int width, int height) {
        return QRHelper2.createPixelsQR2(message, width, height);
    }

    /**
     * @param printMsg    打印数据
     * @param width       宽
     * @param height      高
     * @param printSize   根据数据大小   确定打印时间间隔
     * @param isInversion 是否倒置打印  true 15寸打法
     */
    private void printQR(String printMsg, int width, int height, int printSize, boolean isInversion) {
        int time;
        if (printSize < 300) {
            time = 105;
        } else if (printSize < 400) {
            time = 110;
        } else if (printSize < 500) {
            time = 115;
        } else if (printSize < 600) {
            time = 120;
        } else if (printSize < 700) {
            time = 125;
        } else if (printSize < 1400) {
            time = 130;
        } else {
            time = 140;
        }
        List<byte[]> data = createQR222(printMsg, width, height);

        byte[] by0 = {27, 49, 0};// 设置行距 0 像素
//        byte[] by0 = {27, 51, 0, 27, 49, 0, 27, 74, 0};// 设置行距 0 像素
        write(by0);

        flush();
        for (int i = 0; i < data.size(); i++) {
            try {
                Thread.sleep(time);
                write(data.get(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        finishPrinterQR((byte) 0);
    }


    public static List<byte[]> createQR222(String contents1, int width, int height) {
        return MyEPSPrintHelper.createQR222(contents1, width, height);
    }

    public static byte[] createQR333(String contents1, int width, int height) {
        return MyEPSPrintHelper.createQR333(contents1, width, height);
    }

    public void PrintNetLine2(Context context) throws UnsupportedEncodingException {
        byte[] by = {61, 43, 43, 43, 61};
        byte[] wrap = new byte[]{10, 61, 61, 43, 43, 43, 61, 61, 10};//换行  LF
//      resetBalance(PRINT_Wrap);
//      resetBalance(PRINT_Reset);
////    resetBalance(PRINT_Wrap);
//      resetBalance(wrap);

        String sty = "==你打奶的打得完==\n";
        byte[] s1_byte = sty.getBytes();
        byte[] s2_byte = sty.getBytes("GBK");
        resetBalance(s1_byte);
        resetBalance(s2_byte);
    }

    //居中
    private void center(String s1, boolean big) {
        int i = big ? 2 : 1;
        if (big) {
            resetBalance(PRINT_Small);
        } else {
            resetBalance(PRINT_Reset);
        }
        try {
            byte[] s1_byte = s1.getBytes("GBK");
            int length = s1_byte.length;
            if (length < 32 / i) {
                byte[] s2_byte = new byte[32 / i];
                System.arraycopy(s1_byte, 0, s2_byte, ((32 / i - s1_byte.length) / 2), s1_byte.length);
                resetBalance(s2_byte);
            } else {
                resetBalance(s1_byte);
            }
            resetBalance(PRINT_Wrap);
            resetBalance(PRINT_Reset);
            resetBalance(PRINT_Wrap);
            resetBalance(PRINT_Wrap);
            resetBalance(PRINT_Wrap);
            resetBalance(PRINT_Wrap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //粗细是否加粗
    private void alignment22(byte n) {
        byte[] by = {27, 69, n};
        try {
            getSerialPortPrinter().getOutputStream().write(by);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    //粗细是否加粗

    private void alignmen33(byte n) {
        byte[] by = {27, 74, n};
        try {
            getSerialPortPrinter().getOutputStream().write(by);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置  重新平衡指令
     *
     * @param order 命令指令
     */
    public void resetBalance(byte[] order) {
        for (int i = 0; i < order.length; i++) {
            if (order[i] == 0x00) {
                order[i] = 0x20;
            }
        }

        try {
            getSerialPortPrinter().getOutputStream().write(order);
            Thread.sleep(100);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * shangtong 打印机打印
     */
    @Override
    public void printOrder(ExecutorService executorService, final OrderInfo orderInfo) {
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

                setLineSpacing((byte) 24);
                alignment((byte) 1);
//                alignment22((byte) 1);
                printString(orderInfo.getMarketName() + ",欢迎你!\n");
                setLineSpacing((byte) 48);
                alignment((byte) 0);
                printString("交易日期：" + orderInfo.getTime() + "\n");

                StringBuilder sb = new StringBuilder();
                sb.append("交易单号：").append(orderInfo.getBillcode()).append("\n");
                if (1 == orderInfo.getSettlemethod()) {
                    sb.append("结算方式：扫码支付\n");
                }
                if (2 == orderInfo.getSettlemethod()) {
                    sb.append("结算方式：扫码支付\n");
                }
                if (0 == orderInfo.getSettlemethod()) {
                    sb.append("结算方式：现金支付\n");
                }

                sb.append("摊位号：").append(orderInfo.getStallNo()).append("\n");
                sb.append("司磅员：").append(orderInfo.getSeller()).append("\t\b\b\b秤号：").append(orderInfo.getTerid()).append("\n");
                setLineSpacing((byte) 32);
                alignment((byte) 0);
                alignment22((byte) 0);
                printString(sb.toString());
                setLineSpacing((byte) 42);
                printString("------------商品明细------------\n");

                StringBuilder sb1 = new StringBuilder();
                sb1.append("商品名\b" + "单价/元\b" + "重量/kg\b" + "金额/元" + "\n");

                for (int i = 0; i < orderBeans.size(); i++) {
                    OrderBean goods = orderBeans.get(i);
                    goods.setOrderInfo(orderInfo);
                    sb1.append(goods.getName()).append("\t").append(goods.getPrice()).append("\t").append(goods.getWeight()).append("\t").append(goods.getMoney()).append("\n");
                }
                setLineSpacing((byte) 32);
                alignment((byte) 0);
                alignment22((byte) 0);
                printString(sb1.toString());
                setLineSpacing((byte) 22);
                printString("--------------------------------");
                alignment((byte) 2);
                alignment22((byte) 0);
                printString("合计(元)：" + orderInfo.getTotalamount() + "\b\b\b\n");
                setLineSpacing((byte) 42);
                alignment((byte) 0);
                alignment22((byte) 0);
                printString(BASE_COMPANY_NAME + "\n");

//                boolean isAvailable = NetworkUtil.isAvailable(context);// 有网打印二维码
//                if (!isAvailable) {
//                    sb.append("\n\n");
//                    epsPrint.setLineSpacing((byte) 32);
//                    epsPrint.PrintString(sb.toString());
//                } else {


                if (!isNoPrinterQR) {
                    String qrString = "http://data.axebao.com/smartsz/trace/?no=" + orderInfo.getBillcode();
                    setLineSpacing((byte) 8);
                    printString("扫一扫获取溯源信息：\n");

//                  epsPrint.PrintltString("--------------------------------\n\n\n");
                    byte[] bytes = createQR333(qrString, 24, 24);
                    printQR(bytes);

                    // 使用 测试

//                    printQR(qrString, 24, 24, printSize, false);

                    printString("\n\n\n\n");
                }
            }
        });
    }
}

package com.xuanyuan.library.help.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * 作者：罗发新
 * 时间：2019/6/12 0012    星期三
 * 邮件：424533553@qq.com
 * 说明：
 */
public class MyPrinterAXEHelper {

    public static List<byte[]> createQR222(String contents1, int width, int height) {
        //判断URL合法性
        if (contents1 == null || "".equals(contents1) || contents1.length() < 1) {
            return null;
        }

        List<byte[]> list = new ArrayList<>();
//        Bitmap bitmap = null;
        int QR_WIDTH = width * 8;  //  二维码宽
        int QR_HEIGHT = height * 8; //  二维码高
        try {
            String contents = new String(contents1.getBytes(), "gbk");
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "gbk");
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.MARGIN, 0);  //设置白边
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果

            for (int i = 0; i < height; i++) {
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

    /**
     * 香山 打印机  二维码 生成打印指令
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



}

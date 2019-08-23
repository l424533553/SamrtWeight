package com.xuanyuan.library.help.qr;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import com.xuanyuan.library.MyLog;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


/**
 * Fun： 愛普生的打印机
 * Author：Linus_Xie
 * Date：2018/8/2 14:22
 */
public class MyEPSPrintHelper  {

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
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 0);  //设置白边
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            MyLog.blue("图片大小   getHeight=" + bitMatrix.getHeight() + " getRowSize= " + bitMatrix.getRowSize() + "   bitMatrix.getWidth()=" + bitMatrix.getWidth());

            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果

            for (int i = 0; i < height; i++) {
//                int length = QR_WIDTH + 4;
//                byte[] rowByte = new byte[length];// 384+4
                byte[] rowByte = new byte[width * 8 + 6];// 384+4
                rowByte[0] = 27;
                rowByte[1] = 42;
                rowByte[2] = 1;
                rowByte[3] = (byte) 192;
                rowByte[4] = 0;
                for (int j = 5; j < width * 8 + 5; j++) {
                    int[] dest = new int[8];
                    dest[0] = getPixels(bitMatrix, i * 8, j - 5);
                    dest[1] = getPixels(bitMatrix, i * 8 + 1, j - 5);
                    dest[2] = getPixels(bitMatrix, i * 8 + 2, j - 5);
                    dest[3] = getPixels(bitMatrix, i * 8 + 3, j - 5);
                    dest[4] = getPixels(bitMatrix, i * 8 + 4, j - 5);
                    dest[5] = getPixels(bitMatrix, i * 8 + 5, j - 5);
                    dest[6] = getPixels(bitMatrix, i * 8 + 6, j - 5);
                    dest[7] = getPixels(bitMatrix, i * 8 + 7, j - 5);
                    rowByte[j] = dealArray(dest);
                }
                rowByte[width * 8 + 5] = 10;
//                rowByte[length-2]=10;
//                rowByte[length-1]=13;
                list.add(rowByte);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//      MyLog.myInfo("byte数组数据===" + list.size());
        return list;
    }

    public static byte[] createQR333(String contents1, int width, int height) {
        //判断URL合法性
        if (contents1 == null || "".equals(contents1) || contents1.length() < 1) {
            return null;
        }
        byte[] bytes = null;

//        Bitmap bitmap = null;
        int QR_WIDTH = width * 8;  //  二维码宽
        int QR_HEIGHT = height * 8; //  二维码高
        try {
            String contents = new String(contents1.getBytes(), "gbk");
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

            bytes = new byte[(width * 8 + 6) * height];

            int srcPos = 0;
            for (int i = 0; i < height; i++) {
//                int length = QR_WIDTH + 4;
//                byte[] rowByte = new byte[length];// 384+4

                byte[] rowByte = new byte[width * 8 + 6];// 384+4
                rowByte[0] = 27;
                rowByte[1] = 42;
                rowByte[2] = 1;
                rowByte[3] = (byte) 192;
                rowByte[4] = 0;
                for (int j = 5; j < width * 8 + 5; j++) {
                    int[] dest = new int[8];
                    dest[0] = getPixels(bitMatrix, i * 8, j - 5);
                    dest[1] = getPixels(bitMatrix, i * 8 + 1, j - 5);
                    dest[2] = getPixels(bitMatrix, i * 8 + 2, j - 5);
                    dest[3] = getPixels(bitMatrix, i * 8 + 3, j - 5);
                    dest[4] = getPixels(bitMatrix, i * 8 + 4, j - 5);
                    dest[5] = getPixels(bitMatrix, i * 8 + 5, j - 5);
                    dest[6] = getPixels(bitMatrix, i * 8 + 6, j - 5);
                    dest[7] = getPixels(bitMatrix, i * 8 + 7, j - 5);
                    rowByte[j] = dealArray(dest);
                }
                rowByte[width * 8 + 5] = 10;

                System.arraycopy(rowByte, 0, bytes, srcPos, width * 8 + 6);
                srcPos += width * 8 + 6;
            }
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
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

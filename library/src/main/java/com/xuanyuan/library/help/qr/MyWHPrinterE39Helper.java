package com.xuanyuan.library.help.qr;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.xuanyuan.library.MyLog;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * 炜煌打印机，支持E39，E42，E47
 */
public class MyWHPrinterE39Helper   {


    /**
     * 香山 打印机  二维码 生成打印指令
     */
    public static byte[] createPixelsQR(String contents1, int width, int height) {
        //判断URL合法性
        if (contents1 == null || "".equals(contents1) || contents1.length() < 1) {
            return null;
        }
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




    private static int getPixels(BitMatrix bitMatrix, int y, int x) {
        if (bitMatrix.get(x, y)) {
            return 1;
        } else {
            return 0;
        }
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

    public static List<byte[]> getBytes(Context context, String contents1, int width, int height) {
        List<byte[]> list = new ArrayList<>();
        int[] data = getPixels(context, contents1);
        int QR_WIDTH = width * 8;  //  二维码宽
        int QR_HEIGHT = height * 8; //  二维码高

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
                dest[0] = data[i * 8 * QR_WIDTH + j - 4];
                dest[1] = data[(i * 8 + 1) * QR_WIDTH + j - 4];
                dest[2] = data[(i * 8 + 2) * QR_WIDTH + j - 4];
                dest[3] = data[(i * 8 + 3) * QR_WIDTH + j - 4];
                dest[4] = data[(i * 8 + 4) * QR_WIDTH + j - 4];
                dest[5] = data[(i * 8 + 5) * QR_WIDTH + j - 4];
                dest[6] = data[(i * 8 + 6) * QR_WIDTH + j - 4];
                dest[7] = data[(i * 8 + 7) * QR_WIDTH + j - 4];

                rowByte[j] = dealArray(dest);
            }
            list.add(rowByte);
        }
        return list;
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

    public static List<byte[]> createQRXS(String contents1, int width, int height) {
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

    public static List<byte[]> createQRHHD(String contents1, int width, int height) {
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
                byte[] rowByte = new byte[384 + 5];// 384+4
                rowByte[0] = 0x1B;
                rowByte[1] = 42;
                rowByte[2] = 0;
                rowByte[3] = (byte) 0x80;
                rowByte[4] = 0x01;
                for (int j = 5; j < QR_WIDTH + 5; j++) {
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
     * @param contents 二维码内容，可以是url网址，可以是普通字符串
     */
    public static Bitmap createQRBitmap(String contents) {
        int QR_WIDTH = 128;  //二维码宽
        int QR_HEIGHT = 128;  //二维码高
        try {
            //判断URL合法性
            if (TextUtils.isEmpty(contents)) {
                return null;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "gbk");
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.MARGIN, 0);  //设置白边
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                        System.out.println("数据0xff000000= " + pixels[y * QR_WIDTH + x]);
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                        System.out.println("数据0xffffffff= " + pixels[y * QR_WIDTH + x]);
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
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


    //TODO  测试方法，待删除
    public static List<byte[]> createTest(String contents1, int width, int height) {
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
                MyLog.myInfo("byte数组数据===" + rowByte.length + " 数据实体***   " + Arrays.toString(rowByte));
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



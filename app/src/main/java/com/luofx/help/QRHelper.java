package com.luofx.help;//package com.luofx.help;


import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

/**
 * Created by Administrator on 2018/8/26 0026.
 * <p>
 * 主要可用于  初级打印机的二维码指令
 * <p>
 * 需要zxing的框架支持
 * implementation 'com.google.zxing:core:3.3.3'
 */
public class QRHelper {


    /**
     * @param contents1 二维码内容，可以是url网址，可以是普通字符串
     *                  width 最大为 48 ,高应该和宽 保持一致
     */
//    public static List<byte[]> createPixelsQR(String contents1, int width, int height) {
//        //判断URL合法性
//        if (contents1 == null || "".equals(contents1) || contents1.length() < 1) {
//            return null;
//        }
//        String contents = null;
//        List<byte[]> list = new ArrayList<>();
////        Bitmap bitmap = null;
//        width = 24;  //  二维码宽
//        height = 24; //  二维码高
//        int QR_WIDTH = width * 8;  //  二维码宽
//        int QR_HEIGHT = height * 8; //  二维码高
//        try {
//
//            contents = new String(contents1.getBytes(), "UTF-8");
//            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
//            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//            //图像数据转换，使用了矩阵转换
//            BitMatrix bitMatrix = new QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
//            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
//            //下面这里按照二维码的算法，逐个生成二维码的图片，两个for循环是图片横列扫描的结果
//
//            for (int i = height - 1; i >= 0; i--) {
//                byte[] rowByte = new byte[width * 8 + 5];// 384+4
//
//                rowByte[0] = 18;
//                rowByte[1] = 42;
//                rowByte[2] = 24;
//                rowByte[3] = (byte) 192;
//
//                for (int j = 4; j < QR_WIDTH + 4; j++) {
//                    int[] dest = new int[8];
//                    dest[7] = getPixels(bitMatrix, i * 8, j - 5);
//                    dest[6] = getPixels(bitMatrix, i * 8 + 1, j - 5);
//                    dest[5] = getPixels(bitMatrix, i * 8 + 2, j - 5);
//                    dest[4] = getPixels(bitMatrix, i * 8 + 3, j - 5);
//                    dest[3] = getPixels(bitMatrix, i * 8 + 4, j - 5);
//                    dest[2] = getPixels(bitMatrix, i * 8 + 5, j - 5);
//                    dest[1] = getPixels(bitMatrix, i * 8 + 6, j - 5);
//                    dest[0] = getPixels(bitMatrix, i * 8 + 7, j - 5);
//                    rowByte[j] = dealArray(dest);
//                }
//                rowByte[width * 8 + 4] = 10;
//
//                list.add(rowByte);
//            }
//
////          //生成二维码图片的格式，使用ARGB_8888
////          Bitmap  bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ALPHA_8);
////          bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
////          list = getPrintData(pixels, QR_WIDTH, QR_HEIGHT);
//
//            //显示到一个ImageView上面
//        } catch (WriterException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    /**
     * 商通打印机  二维码 生成打印指令
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
        String contents = null;
//        List<byte[]> list = new ArrayList<>();
//        Bitmap bitmap = null;
        int QR_WIDTH = width * 8;  //  二维码宽
        int QR_HEIGHT = height * 8; //  二维码高
        byte[] bytes = new byte[(32 * 8 + 5) * height];
        try {
            contents = new String(contents1.getBytes(), "gbk");
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "gbk");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(contents1, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，两个for循环是图片横列扫描的结果
            int srcPos = 0;
            for (int i = height - 1; i >= 0; i--) {
                byte[] rowByte = new byte[width * 8 + 5];// 384+4
                rowByte[0] = 27;
                rowByte[1] = 42;
                rowByte[2] = 1;
                rowByte[3] = (byte) 255;
                rowByte[4] = 0;
                for (int j = QR_WIDTH + 4; j >= 5; j--) {
                    int[] dest = new int[8];
                    dest[7] = getPixels(bitMatrix, i * 8, j - 5);
                    dest[6] = getPixels(bitMatrix, i * 8 + 1, j - 5);
                    dest[5] = getPixels(bitMatrix, i * 8 + 2, j - 5);
                    dest[4] = getPixels(bitMatrix, i * 8 + 3, j - 5);
                    dest[3] = getPixels(bitMatrix, i * 8 + 4, j - 5);
                    dest[2] = getPixels(bitMatrix, i * 8 + 5, j - 5);
                    dest[1] = getPixels(bitMatrix, i * 8 + 6, j - 5);
                    dest[0] = getPixels(bitMatrix, i * 8 + 7, j - 5);
                    rowByte[j] = dealArray(dest);
                }
//                rowByte[width * 8 + 5] = 10;

                System.arraycopy(rowByte, 0, bytes, srcPos, width * 8 + 5);
                srcPos += width * 8 + 5;

//                list.add(rowByte);
            }

//          //生成二维码图片的格式，使用ARGB_8888
//          Bitmap  bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ALPHA_8);
//          bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
//          list = getPrintData(pixels, QR_WIDTH, QR_HEIGHT);

            //显示到一个ImageView上面
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        return list;
        return bytes;
    }

    /**
     * 商通打印机  二维码 生成打印指令
     *
     * @param contents1
     * @param width
     * @param height
     * @return
     */
    public static byte[] createPixelsQR3(String contents1, int width, int height) {
        //判断URL合法性
        if (contents1 == null || "".equals(contents1) || contents1.length() < 1) {
            return null;
        }
        String contents = null;
//        List<byte[]> list = new ArrayList<>();
//        Bitmap bitmap = null;
        int QR_WIDTH = width * 8;  //  二维码宽
        int QR_HEIGHT = height * 8; //  二维码高
        byte[] bytes = new byte[32 * 8 * height + 5];

        bytes[0] = 27;
        bytes[1] = 42;
        bytes[2] = 1;
        bytes[3] = (byte) 255;
        bytes[4] = 0;

        try {
            contents = new String(contents1.getBytes(), "gbk");
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "gbk");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(contents1, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，两个for循环是图片横列扫描的结果
            int srcPos = 5;
            for (int i = height - 1; i >= 0; i--) {
                byte[] rowByte = new byte[width * 8];// 384+4

                for (int j = QR_WIDTH - 1; j >= 0; j--) {
                    int[] dest = new int[8];
                    dest[7] = getPixels(bitMatrix, i * 8, j);
                    dest[6] = getPixels(bitMatrix, i * 8 + 1, j);
                    dest[5] = getPixels(bitMatrix, i * 8 + 2, j);
                    dest[4] = getPixels(bitMatrix, i * 8 + 3, j);
                    dest[3] = getPixels(bitMatrix, i * 8 + 4, j);
                    dest[2] = getPixels(bitMatrix, i * 8 + 5, j);
                    dest[1] = getPixels(bitMatrix, i * 8 + 6, j);
                    dest[0] = getPixels(bitMatrix, i * 8 + 7, j);
                    rowByte[j] = dealArray(dest);
                }
                System.arraycopy(rowByte, 0, bytes, srcPos, width * 8);
                srcPos += width * 8;
            }

//          //生成二维码图片的格式，使用ARGB_8888
//          Bitmap  bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ALPHA_8);
//          bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
//          list = getPrintData(pixels, QR_WIDTH, QR_HEIGHT);

            //显示到一个ImageView上面
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        return list;
        return bytes;
    }


    /**
     * 商通打印机  二维码 生成打印指令
     *
     * @param contents1
     * @param width
     * @param height
     * @return
     */
    public static byte[] createPixelsQR2(String contents1, int width, int height) {
        //判断URL合法性
        if (contents1 == null || "".equals(contents1) || contents1.length() < 1) {
            return null;
        }
        String contents = null;
        int QR_WIDTH = width * 8;  //  二维码宽
        int QR_HEIGHT = height * 8; //  二维码高
        byte[] bytes = new byte[(32 * 8) * height];
        try {

            contents = new String(contents1.getBytes(), "gbk");
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "gbk");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(contents1, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，两个for循环是图片横列扫描的结果
            int srcPos = 0;
            for (int i = height - 1; i >= 0; i--) {
                byte[] rowByte = new byte[width * 8];// 384+4
//                rowByte[0] = 27;
//                rowByte[1] = 42;
//                rowByte[2] = 1;
//                rowByte[3] = (byte) 255;
//                rowByte[4] = 0;
                for (int j = QR_WIDTH - 1; j >= 0; j--) {
                    int[] dest = new int[8];
                    dest[7] = getPixels(bitMatrix, i * 8, j);
                    dest[6] = getPixels(bitMatrix, i * 8 + 1, j);
                    dest[5] = getPixels(bitMatrix, i * 8 + 2, j);
                    dest[4] = getPixels(bitMatrix, i * 8 + 3, j);
                    dest[3] = getPixels(bitMatrix, i * 8 + 4, j);
                    dest[2] = getPixels(bitMatrix, i * 8 + 5, j);
                    dest[1] = getPixels(bitMatrix, i * 8 + 6, j);
                    dest[0] = getPixels(bitMatrix, i * 8 + 7, j);
                    rowByte[j] = dealArray(dest);
                }

                System.arraycopy(rowByte, 0, bytes, srcPos, width * 8);
                srcPos += width * 8;

            }
            //显示到一个ImageView上面
        } catch (WriterException e) {
            e.printStackTrace();
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
        }

        return bytes;
    }


    public static Bitmap createQR(String contents1, int width, int height) {
        //判断URL合法性
        if (contents1 == null || "".equals(contents1) || contents1.length() < 1) {
            return null;
        }
        Bitmap bitmap = null;

//        Bitmap bitmap = null;
        int QR_WIDTH = width * 8;  //  二维码宽
        int QR_HEIGHT = height * 8; //  二维码高
        try {

//            contents = new String(contents1.getBytes("UTF-8"), "gbk");
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "gbk");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(contents1, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果

////            //生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ALPHA_8);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);


            //显示到一个ImageView上面
        } catch (WriterException e) {
            e.printStackTrace();
        }

//        MyLog.myInfo("byte数组数据===" + list.size());
        return bitmap;
    }

    /**
     * @param imageView 显示二维码的ImageView 控件
     * @param contents  二维码内容，可以是url网址，可以是普通字符串
     */
    public static void createQRImage(ImageView imageView, String contents) {
        int QR_WIDTH = 200;  //二维码宽
        int QR_HEIGHT = 200;  //二维码高
        try {
            //判断URL合法性
            if (contents == null || "".equals(contents) || contents.length() < 1) {
                return;
            }

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        int a = 0xff000000;
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }

            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param contents  二维码内容，可以是url网址，可以是普通字符串
     */
    public static Bitmap createQRImage(String contents) {
        int QR_WIDTH = 300;  //二维码宽
        int QR_HEIGHT = 300;  //二维码高
        try {
            //判断URL合法性
            if (contents == null || "".equals(contents) || contents.length() < 1) {
                return null;
            }

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        int a = 0xff000000;
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }

            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            return  bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static int getPixels(BitMatrix bitMatrix, int y, int x) {
        if (bitMatrix.get(x, y)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 将 int[] 转成一个byte  如   int[] arr= {-1,-16777216,-16777216,-1,-1,-1,-16777216,-16777216}  转为 01100011  即为99
     * 获得每行 的数据 ，并确定了bytee数组的长度
     * 8个长度的 int 数组 ，一个字节
     */
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


//     -16777216


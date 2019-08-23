package com.axecom.smartweight.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    16:23
 * 邮件：424533553@qq.com
 * 说明： 测试问题，测试情况
 */
public class Test {

    public static void main(String[] args) {
//        byte[] by1 = {0, 15, -24, -115};
//        String str = "000623F8";
//        Integer in = Integer.valueOf(str,16);
//        System.out.println("数据"+in);

        List<Student> list = new ArrayList<>();
        Student stu1 = new Student("罗新", 18);
        Student stu2 = stu1.clone();
        list.add(stu2);
        System.out.println(Arrays.toString(list.toArray()));
        stu1.setName("小李");
        stu1.setAge(15);
        System.out.println(Arrays.toString(list.toArray()));
    }

    /**
     * 转数组 数据
     * @param bytes 参数
     */
    public static String byteToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        if (!isEmpty(bytes)) {
            for (byte aByte : bytes) {
                sb.append(String.format("%02X", aByte));
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param bytes  参数  数据组
     */
    public static boolean isEmpty(byte[] bytes) {
        return bytes == null || bytes.length == 0;
    }

    /**
     * @param bArray 数组对象
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将一个字节转成3个字节，使用结果。
     * 数据功能区
     *
     * @param sum 校验
     */
    private static byte[] test11(int sum) {
        byte[] by = {48, 48, 48};
        int a;
        int b;
        int c;
        if (sum >= 100) {
            a = sum / 100 + 48;
            c = sum % 10 + 48;
            b = (sum >> 1) % 10 + 48;
            by[0] = (byte) a;
            by[1] = (byte) b;
            by[2] = (byte) c;
        } else if (sum >= 10) {
            b = (byte) (sum / 10 + 48);
            c = (byte) (sum % 10 + 48);
            by[1] = (byte) b;
            by[2] = (byte) c;
        } else {
            c = (byte) (sum % 10 + 48);
            by[2] = (byte) c;
        }
        return by;
    }


}

//        AA FE [00 0D 40 A4           00 06 07 BF   00 26 1E 4C  00  05 00 05 00  000000000000000000000000000000000000000000
//        AA FE [00 07 04 AB](459947) [00 06 EF 1C] [00 47 BC 70] [00 0A 00 05] F2 (有托盘)
//        AA FE [00 02 7E 42] [00 06 EF 1C] [00 47 BC 70] [00 0A 00 05] FE (无托盘)
//        AA FE [00 47 D1 61](4706657) [00 06 EF 1C]( 454428)  [00 47 BC 70] (4701296) [00 0A 00 05](2400005) B5 (10Kg)
//        AA FE [00 27 69 03](2582787) [00 06 EF 1C]( 454428)  [00 47 BC 70] (4701296) [00 0A 00 05](2400005) CF ( 5Kg)
//        AA FE [00 47 CC CA](4705482) [00 06 EF 1C]( 454428)  [00 47 BC 70] (4701296) [00 0A 00 05](2400005) 19 (10Kg)
//        AA FE [01 FF 8E 3D](  -2913) [00 05 6A 64]( 354916)  [00 45 A8 72] (4565106) [00 0A 00 05](2400005) B5
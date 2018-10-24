package com.shangtongyin.tools.test;//package com.shangtongyin.tools.serialport;
//
//import java.io.UnsupportedEncodingException;
//import java.util.Arrays;
//
//public class test {
//    public static void main(String args[]) {
//        String sty = "ESC3n\n";
////        try {
////            byte[] s1_byte = sty.getBytes("UTF-8");
////            byte[] s2_byte = sty.getBytes("GBK");
////            System.out.println(Arrays.toString(s1_byte));
////            System.out.println(Arrays.toString(s2_byte));
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        }
//
//        int[] dest = new int[]{1,1,1,1 ,1,1,1,1};
//
//        byte aa=dealArray(dest);
//
//        System.out.println("aa   "+aa);
//
//    }
//
//    private static byte dealArray(int[] dest) {
//        int be = 0;
//        int temp;
//        for (int j = 0; j < 8; j++) {
//            temp = dest[j];
//            if (j == 0) {
//                be = temp;
//            } else {
//                be = (be << 1) | temp;
//            }
//        }
//        return (byte) be;
//    }
//}

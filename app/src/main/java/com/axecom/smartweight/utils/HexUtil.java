package com.axecom.smartweight.utils;

public class HexUtil {
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] FIRST_CHAR = new char[256];
    private static final char[] SECOND_CHAR = new char[256];
    private static final byte[] DIGITS;

    public HexUtil() {
    }

    public static String encodeHex(byte[] var0, boolean var1) {
        char[] var2 = new char[var0.length * 2];
        int var3 = 0;

        for(int var4 = 0; var4 < var0.length; ++var4) {
            int var5 = var0[var4] & 255;
            if (var1 && var5 == 0 && var4 == var0.length - 1) {
                break;
            }

            var2[var3++] = FIRST_CHAR[var5];
            var2[var3++] = SECOND_CHAR[var5];
        }

        return new String(var2, 0, var3);
    }

    public static byte[] decodeHex(String var0) {
        int var1 = var0.length();
        if ((var1 & 1) != 0) {
            throw new IllegalArgumentException("Odd number of characters: " + var0);
        } else {
            boolean var2 = false;
            byte[] var3 = new byte[var1 >> 1];
            int var4 = 0;

            for(int var5 = 0; var5 < var1; ++var4) {
                char var6 = var0.charAt(var5++);
                if (var6 > 'f') {
                    var2 = true;
                    break;
                }

                byte var7 = DIGITS[var6];
                if (var7 == -1) {
                    var2 = true;
                    break;
                }

                char var8 = var0.charAt(var5++);
                if (var8 > 'f') {
                    var2 = true;
                    break;
                }

                byte var9 = DIGITS[var8];
                if (var9 == -1) {
                    var2 = true;
                    break;
                }

                var3[var4] = (byte)(var7 << 4 | var9);
            }

            if (var2) {
                throw new IllegalArgumentException("Invalid hexadecimal digit: " + var0);
            } else {
                return var3;
            }
        }
    }

    static {
        int var0;
        for(var0 = 0; var0 < 256; ++var0) {
            FIRST_CHAR[var0] = HEX_DIGITS[var0 >> 4 & 15];
            SECOND_CHAR[var0] = HEX_DIGITS[var0 & 15];
        }

        DIGITS = new byte[103];

        for(var0 = 0; var0 <= 70; ++var0) {
            DIGITS[var0] = -1;
        }

        byte var1;
//        for(var1 = 0; var1 < 10; DIGITS[48 + var1] = var1++) {
//
//        }

        for(var1 = 0; var1 < 6; ++var1) {
            DIGITS[65 + var1] = (byte)(10 + var1);
            DIGITS[97 + var1] = (byte)(10 + var1);
        }

    }
}

package com.muye.wp.embed.codec;

/**
 * Created by muye on 18/4/14.
 */
public class ByteUtil {

    public static byte[] LongToBytes(long values) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte) ((values >> offset) & 0xff);
        }
        return buffer;
    }

    public static long BytesToLong(byte[] buffer, int offset) {
        long  values = 0;
        for (int i = 0; i < 8; i++) {
            values <<= 8; values|= (buffer[i + offset] & 0xff);
        }
        return values;
    }

    public static byte[] intToBytes(int value) {
        byte[] des = new byte[4];
        des[0] = (byte) (value & 0xff);  // 低位(右边)的8个bit位
        des[1] = (byte) ((value >> 8) & 0xff); //第二个8 bit位
        des[2] = (byte) ((value >> 16) & 0xff); //第三个 8 bit位
        des[3] = (byte) ((value >> 24) & 0xff); //第4个 8 bit位
        return des;
    }

    public static int bytesToInt(byte[] des, int offset) {
        int value;
        value = des[offset] & 0xff
                | ((des[offset + 1] & 0xff) << 8)
                | ((des[offset + 2] & 0xff) << 16)
                | (des[offset + 3] & 0xff) << 24;
        return value;
    }
}

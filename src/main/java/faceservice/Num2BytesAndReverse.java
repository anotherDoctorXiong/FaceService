package faceservice;

import java.util.Arrays;

public class Num2BytesAndReverse {
    public Num2BytesAndReverse() {
    }

    public static byte[] intToBytes(int value) {
        byte[] src = new byte[]{(byte)(value & 255), (byte)(value >> 8 & 255), (byte)(value >> 16 & 255), (byte)(value >> 24 & 255)};
        return src;
    }

    public static byte[] shortToBytes(short value) {
        byte[] src = new byte[]{(byte)(value & 255), (byte)(value >> 8 & 255)};
        return src;
    }

    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[]{(byte)(value >> 24 & 255), (byte)(value >> 16 & 255), (byte)(value >> 8 & 255), (byte)(value & 255)};
        return src;
    }

    public static byte[] shortToBytes2(short value) {
        byte[] src = new byte[]{(byte)(value >> 8 & 255), (byte)(value & 255)};
        return src;
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value = src[offset] & 255 | (src[offset + 1] & 255) << 8 | (src[offset + 2] & 255) << 16 | (src[offset + 3] & 255) << 24;
        return value;
    }

    public static short bytesToShort(byte[] src, int offset) {
        short value = (short)(src[offset] & 255 | (src[offset + 1] & 255) << 8);
        return value;
    }

    public static int bytesToInt2(byte[] src, int offset) {
        int value = (src[offset] & 255) << 24 | (src[offset + 1] & 255) << 16 | (src[offset + 2] & 255) << 8 | src[offset + 3] & 255;
        return value;
    }

    public static short bytesToShort2(byte[] src, int offset) {
        short value = (short)((src[offset + 0] & 255) << 8 | src[offset + 1] & 255);
        return value;
    }

    public static void main(String[] args) {
        System.out.println("---------int 2 bytes-----------");
        int intNum = 2;


        byte[] m1B = intToBytes(intNum);
        System.out.println("m1B:" + Arrays.toString(m1B));
        byte[] m1L = intToBytes2(intNum);
        System.out.println("m1L:" + Arrays.toString(m1L));
        System.out.println("---------short 2 bytes-----------");
        short shortNum = 2;


        byte[] m2B = shortToBytes(shortNum);
        System.out.println("m2B:" + Arrays.toString(m2B));
        byte[] m2L = shortToBytes2(shortNum);
        System.out.println("m2L:" + Arrays.toString(m2L));
        System.out.println("---------bytes 2 int-----------");
        int intNum2B = bytesToInt(m1B, 0);
        System.out.println("m1B->Int:" + intNum2B);
        int intNum2L = bytesToInt2(m1L, 0);
        System.out.println("m1L->Int:" + intNum2L);
        System.out.println("---------bytes 2 short-----------");
        int shortNum2B = bytesToShort(m2B, 0);
        System.out.println("m2->short:" + shortNum2B);
        int shortNum2L = bytesToShort2(m2L, 0);
        System.out.println("m2->short:" + shortNum2L);
    }
}

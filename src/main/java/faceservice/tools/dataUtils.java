package faceservice.tools;

public class dataUtils {
    public static int getInteger(byte[] b,int offset){
        byte[] num=new byte[4];
        System.arraycopy(b, offset, num, 0, 4);

        return  num[0] & 0xFF |
                (num[1] & 0xFF) << 8 |
                (num[2] & 0xFF) << 16 |
                (num[3] & 0xFF) << 24;
    }
}

package faceservice.tools.Constraint;


import java.net.InetAddress;


public class Host {
    public static boolean isHostConnectable(String host) {
        InetAddress ad = null;
        try {
            ad = InetAddress.getByName(host);
            boolean state = ad.isReachable(1000);//测试是否可以达到该地址 ,判断ip是否可以连接 1000是超时时间
            if(state){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

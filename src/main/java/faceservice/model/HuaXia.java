package faceservice.model;

import java.io.Serializable;

public class HuaXia implements Serializable {
    private String id;
    private String ip;
    private String mac;
    public HuaXia(String id, String ip, String mac) {
        this.id = id;
        this.ip = ip;
        this.mac = mac;
    }
    public HuaXia(HostAddRequest addRequest){
        this.id=addRequest.getId();
        this.ip=addRequest.getIp();
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}

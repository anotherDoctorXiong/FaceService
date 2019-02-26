package faceservice.model;

public class EI {
    private String id;
    private String ip;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    private String device;
    public EI(String id, String ip, String device) {
        this.id = id;
        this.ip = ip;
        this.device = device;
    }
    public EI(EIAddRequest addRequest){
        this.id=addRequest.getId();
        this.ip=addRequest.getIp();
        this.device = addRequest.getDevice();
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
}

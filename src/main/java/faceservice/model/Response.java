package faceservice.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {
    private int code;
    private String message;
    private long timestamp;
    private List<Map<String, Object>> data;
    private String mac;
    public final static Map<Integer, String> ErrorMessage = new HashMap<Integer, String>() {
        {
            put(0, "success");
            put(1, "check params");
            put(1501,"ID_HAS_BEEN_USED");
            put(1502,"INVALID_GROUP_NAME");
            put(1503,"ID_IS_NOT_EXIT");
            put(1504,"IP_IS_INVALID");
            put(104202, "FACE_TOKEN_NOT_EXIST");
            put(104200, "FACE_NOT_FOUND");
            put(104201, "BAD_QUALITY");
            put(104000, "ILLEGAL_JSON_BODY");
            put(104001, "ILLEGAL_ARGUMENT");
            put(104002, "MISSING_ARGUMENT");
            put(104100, "UNSUPPORTED_IMAGE_FORMAT");
            put(104101, "INVALID_IMAGE_RESOLUTION");
            put(104102, "INVALID_IMAGE_FILE_SIZE");
            put(104301, "GROUP_NAME_NOT_EXIST");
        }
    };


    public Response() {
        this.code = 0;
        this.message = "";
        this.timestamp=new Date().getTime();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        this.message = ErrorMessage.get(this.code);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message =message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMac() {
        return mac;
    }
}
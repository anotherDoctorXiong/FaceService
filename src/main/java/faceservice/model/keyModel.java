package faceservice.model;

import java.io.Serializable;

public class keyModel implements Serializable {
    private String key_id;
    private String face_token;
    private String group;
    private byte[] face;
    public keyModel() {
    }

    public keyModel(String key_id, String face_token,String group, byte[] face) {
        this.key_id = key_id;
        this.face_token = face_token;
        this.group=group;
        this.face = face;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getFace_token() {
        return face_token;
    }

    public void setFace_token(String face_token) {
        this.face_token = face_token;
    }

    public byte[] getFace() {
        return face;
    }

    public void setFace(byte[] face) {
        this.face = face;
    }
    public String toString(){
        return  "key_id="+key_id+"\nface_token"+face_token+"\ngroup"+group;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}

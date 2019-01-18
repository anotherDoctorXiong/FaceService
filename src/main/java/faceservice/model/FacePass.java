package faceservice.model;

import java.io.Serializable;

public class FacePass implements Serializable {
    private String id;
    private String face_token;
    private String group;
    public FacePass(FacePassAddRequest addRequest) {
        this.id = addRequest.getId();
        this.group=addRequest.getGroup();
    }
    public FacePass(String id, String face_token,String group) {
        this.id = id;
        this.face_token = face_token;
        this.group=group;
    }
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFace_token() {
        return face_token;
    }

    public void setFace_token(String face_token) {
        this.face_token = face_token;
    }
}

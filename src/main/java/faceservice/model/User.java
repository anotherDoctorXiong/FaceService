package faceservice.model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String id;
    private String name;
    private byte[] face;

    public  User(FacePassAddRequest facePassAddRequest){
        this.id=facePassAddRequest.getId();
        this.name=facePassAddRequest.getName();
        this.face=getBytes(facePassAddRequest.getImage());
    }

    public User(String id, String name,  byte[] face) {
        this.id = id;
        this.name = name;
        this.face = face;
    }

    public String getId() {
        return id;
    }

    public void setId(String key_id) {
        this.id = key_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFace() {
        return face;
    }

    public void setFace(byte[] face) {
        this.face = face;
    }
    public Map<String,Object> getUserMap(String group){
        Map<String,Object> map=new HashMap();
        map.put("id",this.id);
        map.put("name",this.name);
        map.put("group",group);
        map.put("face",this.face);
        return map;
    }
    private static  byte[] getBytes(File file){
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

}

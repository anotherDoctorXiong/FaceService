package faceservice.service;

import net.sf.json.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
public class ParkingService {
    @Autowired
    private HttpService httpService;
    public String parkingFaceAdd(Map<String,String> map,MultipartFile image)throws IOException{
        JSONObject json=JSONObject.fromObject(map);
        Map<String,String> map1=new HashMap<>();

        map1.put("person",json.toString());
        ResponseEntity res= httpService.sendUrlencoded("person/add",map1);
        if(checkRespon(res)){
            Map<String,String> map2=new HashMap<>();
            map2.put("faceId",map.get("faceId"));
            map2.put("pic",new String(Base64.encodeBase64(image.getBytes())));
            return getMessage(httpService.sendUrlencoded("face/base/add",map2));
        }else
            return getMessage(res);
    }
    public String parkingFaceDelete(String faceId)throws IOException{
        Map<String,String> map=new HashMap<>();
        map.put("faceId",faceId);
        return getMessage(httpService.sendUrlencoded("person/delete",map));
    }
    public String parkingFaceUpdate(Map<String,String> map,MultipartFile image)throws IOException{
        map.put("pic",new String(Base64.encodeBase64(image.getBytes())));
        return getMessage(httpService.sendUrlencoded("face/base/update",map));
    }
    public String parkingFaceQuery(String faceId)throws IOException{
        Map<String,String> map=new HashMap<>();
        map.put("faceId",faceId);
        return getMessage(httpService.sendUrlencoded("face/query",map));
    }
    public String parkingGetFace(String faceId)throws IOException{
        Map<String,String> map=new HashMap<>();
        map.put("faceId",faceId);
        return getData(httpService.sendUrlencoded("face/base/query",map));
    }
    public String parkingGetMac()throws IOException{
        return getData(httpService.url1Get("face/base/query",null));
    }

    private static String getMessage(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return json.getString("msg");
    }
    private static String getData(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return json.getString("data");
    }
    private static Boolean checkRespon(ResponseEntity response){
        if(response.getStatusCode().value()!=200){
            return false;
        }else
            return true;
    }

}

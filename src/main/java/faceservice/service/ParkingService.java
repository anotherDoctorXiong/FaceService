package faceservice.service;

import net.sf.json.JSONObject;
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
        Map<String,Object> map1=new HashMap<>();

        map1.put("person",json.toString());
        ResponseEntity res= httpService.sendUrlencoded("person/add",map1);
        if(checkRespon(res)){
            Map<String,Object> map2=new HashMap<>();
            map2.put("faceId",map.get("faceId"));
            map2.put("feature","");
            httpService.sendUrlencoded("face/add",map2);
            return getMessage(httpService.sendForm("face/pic/add",map2.get("faceId").toString(),image));
        }else
            return getMessage(res);
    }
    private static String getMessage(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return json.getString("msg");
    }
    private static Boolean checkRespon(ResponseEntity response){
        if(response.getStatusCode().value()!=200){
            return false;
        }else
            return true;
    }

}

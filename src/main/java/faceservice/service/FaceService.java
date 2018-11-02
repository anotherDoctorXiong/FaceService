package faceservice.service;

import faceservice.mapper.keyMapper;
import faceservice.model.keyModel;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FaceService {
    @Autowired
    private keyMapper mapper;
    @Autowired
    private HttpService httpService;
    @Autowired
    private TcpService tcpService;

    public boolean addFace(String type,String key_id,MultipartFile image)throws Exception{
        keyModel model=new keyModel();

        if(mapper.getOne(key_id)!=null){
            return false;
        }else
            try {
                if(type.equals("1")) {
                    ResponseEntity res = httpService.sendPic(image);
                    if (checkRespon(res)) {
                        String face_token = getToken(res);
                        Map<String,String> map1=new HashMap();
                        map1.put("group_name",key_id);
                        map1.put("face_token",face_token);
                        ResponseEntity res1=httpService.sendJson("/api/group/v1/bind",map1);
                        System.out.println(getMessage(res1));
                        if(checkRespon(res1)){
                            System.out.println(getMessage(res1));
                            model.setKey_id(key_id);
                            model.setFace_token(face_token);
                            model.setFace(image.getBytes());
                            mapper.insert(model);
                        }

                    } else
                        System.out.println(getMessage(res));
                }else {
                    tcpService.register(image);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
    }
    private static String getToken(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        JSONObject data=(JSONObject)JSONObject.fromObject(json).get("data");
        return data.getString("face_token");
    }
    private static String getMessage(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return json.getString("message");
    }
    private static Boolean checkRespon(ResponseEntity response){
        if(response.getStatusCode().value()!=200){
            return false;
        }else
            return true;
    }

}

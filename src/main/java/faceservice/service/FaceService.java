package faceservice.service;

import faceservice.mapper.keyMapper;
import faceservice.model.keyModel;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    /**
      * @name:
      * @discraption:      添加注册人脸 face++注册人脸需要与分组进行绑定操作 注册成功后将相关信息存储到数据库
      * @param:            type<String>(1是使用facepass)</>id<String>(人脸id),group<String>(分组id),image<MultipartFile>(人脸图片)
      * @return:           成功返回200 失败返回失败信息
      **/
    public ResponseEntity<String> addFace(String type,String id, String group,MultipartFile image)throws Exception{
        keyModel model=new keyModel();
        if(mapper.getOne(id)!=null){
            return new ResponseEntity("id was already used", HttpStatus.OK);
        }else{
            try {
                if(type.equals("1")) {
                    ResponseEntity res = httpService.sendPic(image);
                    if (checkRespon(res)) {
                        Map<String,String> map=new HashMap();
                        map.put("group_name",group);
                        ResponseEntity res1 = httpService.sendJson("/api/group/v1/query",map);
                        if(!checkRespon(res1)){
                            httpService.sendJson("/api/group/v1/create",map);
                        }
                        String face_token = getToken(res);
                        Map<String,String> map1=new HashMap();
                        map1.put("group_name",group);
                        map1.put("face_token",face_token);
                        ResponseEntity res2=httpService.sendJson("/api/group/v1/bind",map1);
                        if(checkRespon(res2)){
                            model.setId(id);
                            model.setFace_token(face_token);
                            model.setGroup(group);
                            model.setFace(image.getBytes());
                            mapper.insert(model);
                            return new ResponseEntity("add success id="+id,HttpStatus.OK);
                        }else
                            System.out.println(getMessage(res2));
                        return res2;

                    }else
                        System.out.println(getMessage(res));
                    return res;
                }else{
                    if(id.length()!=10&&group.length()!=6){
                        return new ResponseEntity("group length only allowed was 6,id length only allowed was 10",HttpStatus.BAD_REQUEST);
                    }else{
                        String result=tcpService.register(id,group,image);
                        model.setId(id);
                        model.setGroup(group);
                        model.setFace(image.getBytes());
                        mapper.insert(model);
                        if(result=="0"){
                            return new ResponseEntity("add success id="+id,HttpStatus.OK);
                        }
                        return new ResponseEntity(result,HttpStatus.BAD_REQUEST);
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity("internal error",HttpStatus.BAD_REQUEST);
            }
        }

    }
    public ResponseEntity<String> deleteFace(String id)throws InterruptedException{
        keyModel model=new keyModel();
        model=mapper.getOne(id);
        if(model==null){
            return new ResponseEntity("id was not found",HttpStatus.BAD_REQUEST);
        }else{
            if(model.getFace_token()!=null){
                Map<String,String> map=new HashMap();
                map.put("face_token",model.getFace_token());
                ResponseEntity res=httpService.sendJson("/api/face/v1/delete",map);
                if(checkRespon(res)){
                    mapper.delete(id);
                    return new ResponseEntity("delete success id="+id,HttpStatus.OK);
                }else
                    return new ResponseEntity(getMessage(res),HttpStatus.INTERNAL_SERVER_ERROR);
            }else{
                String str=tcpService.delete(id);
                if (str == "0") {
                    mapper.delete(id);
                    return new ResponseEntity("delete success id="+id,HttpStatus.OK);
                }else
                    return new ResponseEntity(str,HttpStatus.BAD_REQUEST);
            }
        }
    }
    public ResponseEntity<String> queryFace(String id)throws InterruptedException{
        keyModel model=new keyModel();
        model=mapper.getOne(id);
        if(model==null){
            return new ResponseEntity("this id is not exit",HttpStatus.OK);
        }else{
            if(model.getFace_token()!=null){
                Map<String,String> map=new HashMap();
                map.put("face_token",model.getFace_token());
                ResponseEntity res=httpService.sendJson("/api/face/v1/query",map);
                if(checkRespon(res)){
                    return new ResponseEntity("id="+model.getId()+"\n"+"group="+model.getGroup(),HttpStatus.OK);
                }else
                    return new ResponseEntity(getMessage(res),HttpStatus.INTERNAL_SERVER_ERROR);
            }else{
                String str=tcpService.query(model.getGroup(),id);
                if (str == "0") {
                    return new ResponseEntity("id="+model.getId()+"\n"+"group="+model.getGroup(),HttpStatus.OK);
                }else
                    return new ResponseEntity(str,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

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

package faceservice.service;

import faceservice.mapper.HuaXiaMapper;
import faceservice.mapper.UserMapper;
import faceservice.model.HostAddRequest;
import faceservice.model.HuaXia;
import faceservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static faceservice.tools.getInfoFromJsonBody.*;
@Service
@Slf4j
public class HuaXiaService {
    @Autowired
    private HttpService httpService;
    @Autowired
    private HuaXiaMapper huaXiaMapper;
    @Autowired
    private UserMapper userMapper;
    private String message;
    private String macAddress;
    public int addFace(HostAddRequest addRequest){
        if(userMapper.getOne(addRequest.getId())!=null){
            return 1501;
        }
        Map add = new HashMap<String, String>() {{
            put("faceId", addRequest.getId());
            put("username", addRequest.getName());
            put("valid", "9999");
            put("wgId", "");
        }};
        Map<String, String> person = new HashMap<String, String>() {{
            put("person", getJosnString(add));
        }};
        ResponseEntity res=httpService.sendUrlencoded(addRequest.getIp()+":8080/person/add",person);
        if(getResult(res)){
            Map<String, String> base64Add = new HashMap<String, String>() {{
                put("faceId", addRequest.getId());
                put("pic",new String(Base64.encodeBase64(addRequest.getBytes())));
            }};
            ResponseEntity res1=httpService.sendUrlencoded(addRequest.getIp()+":8080/face/base/add ",base64Add);
            if(getResult(res1)){
                userMapper.insert(new User(addRequest));
                HuaXia huaXia=new HuaXia(addRequest);
                huaXia.setMac(getMac(res1));
                huaXiaMapper.insert(huaXia);
                return 0;
            }else {
                setMessage(getMsg(res1));
                return 1;
            }
        }else{
            Map faceId=new HashMap<String,String>(){{
                put("faceId",addRequest.getId());
            }};
            httpService.sendUrlencoded(addRequest.getIp()+":8080/person/delete",faceId);
            setMessage(getMsg(res));
            return 1;
        }
    }
    public int faceDelete(String id){
        HuaXia huaXia=huaXiaMapper.getOne(id);
        if(huaXia==null){
            if(userMapper.getOne(id)!=null){
                userMapper.delete(id);
            }
            return 1503;
        }
        String ip=huaXia.getIp();
        Map faceId=new HashMap<String,String>(){{
            put("faceId",id);
        }};
        ResponseEntity res=httpService.sendUrlencoded(ip+":8080/person/delete",faceId);
        if(getResult(res)){
            userMapper.delete(id);
            huaXiaMapper.delete(id);
            return 0;
        }else{
            setMessage(getMsg(res));
            if(getMessage().equals("该faceId不存在！")){
                userMapper.delete(id);
                huaXiaMapper.delete(id);
            }
            return 1;
        }

    }
    public int updataFace(String id,byte[] image){
        HuaXia huaXia=huaXiaMapper.getOne(id);
        if(huaXia==null){
            return 1503;
        }
        String ip=huaXia.getIp();
        Map updataFace=new HashMap<String,String>(){{
            put("faceId",id);
            put("pic",new String(Base64.encodeBase64(image)));
        }};
        ResponseEntity res=httpService.sendUrlencoded(ip+":8080/face/base/update",updataFace);
        if(getResult(res)){
            User user=new User(id,image);
            userMapper.update(user);
            return 0;
        }else{
            setMessage(getMsg(res));
            if(getMessage().equals("未找到此FaceId的人员信息，请先添加人员！")){
                userMapper.delete(id);
                huaXiaMapper.delete(id);
            }
            return 1;
        }

    }
    public int queryFace(String id){
        HuaXia huaXia=huaXiaMapper.getOne(id);
        if(huaXia==null){
            return 1503;
        }
        String ip=huaXia.getIp();
        Map queryFace=new HashMap<String,String>(){{
            put("faceId",id);
        }};
        ResponseEntity res=httpService.sendUrlencoded(ip+":8080/face/query",queryFace);
        if(getResult(res)){
            return 0;
        }else{
            setMessage(getMsg(res));
            if(getMessage().equals("FaceId未找到！")){
                userMapper.delete(id);
                huaXiaMapper.delete(id);
            }
            return 1;
        }
    }
    public List<Map<String, Object>>  queryAll(String ip){
        ResponseEntity res=httpService.sendUrlencoded(ip+":8080/feature/query/all",null);
        return getData(res);
    }
    public int deleteAll(String ip) {
        ResponseEntity res=httpService.sendUrlencoded(ip+":8080/person/clean/all",null);
        if(getResult(res)){
            return 0;
        }else{
            setMessage(getMsg(res));
            return 1;
        }
    }
    public List<Map<String,Object>> getUserData(String id){
        User user=userMapper.getOne(id);
        String ip=huaXiaMapper.getOne(id).getIp();
        List<Map<String,Object>> list=new ArrayList<>();
        list.add(user.getHuaXiaUserMap(ip));
        return list;
    }
    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public String getMacAddress(String id) {
        HuaXia h = huaXiaMapper.getOne(id);
        if (h != null) {
            return h.getMac();
        } else
            return "";
    }


}


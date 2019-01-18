package faceservice.service;


import faceservice.mapper.FacePassMapper;
import faceservice.model.FacePass;
import faceservice.model.FacePassAddRequest;
import faceservice.model.User;
import faceservice.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class FacePassService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FacePassMapper facePassMapper;
    @Autowired
    private HttpService httpService;

    @Transactional
    public int addFace(FacePassAddRequest addRequest) throws IOException {
        User user = new User(addRequest);
        FacePass facePass = new FacePass(addRequest);
        if (userMapper.getOne(user.getId()) != null) {
            return 1501;
        } else {
            ResponseEntity sendPicReturn = httpService.sendPic(addRequest.getImage());
            if (checkRespon(sendPicReturn)) {
                Map<String, String> group_name = new HashMap<String, String>() {{
                    put("group_name", addRequest.getGroup());
                }};
                int a = getCode(httpService.sendJson("/api/group/v1/create", group_name));
                if (a == 0 || a == 104300) {
                    String face_token = getToken2(sendPicReturn);
                    group_name.put("face_token", face_token);
                    ResponseEntity bindReturn = httpService.sendJson("/api/group/v1/bind", group_name);
                    if (checkRespon(bindReturn)) {
                        userMapper.insert(user);
                        facePass.setFace_token(face_token);
                        facePassMapper.insert(facePass);
                    }
                    return getCode(bindReturn);
                }
                return a;
            } else
                return 1502;

        }
    }



    @Transactional
    public int deleteFace(String id) {
        if (userMapper.getOne(id)==null) {
            return 1503;
        } else {
            String face_token = facePassMapper.getFace_token(id);
            Map<String, String> token = new HashMap<String, String>() {{
                put("face_token", face_token);
            }};
            ResponseEntity res = httpService.sendJson("/api/face/v1/delete", token);
            if (checkRespon(res)||getCode(res)==104202) {
                facePassMapper.delete(id, null);
                userMapper.delete(id);
                return 0;
            } else
                return getCode(res);
        }
    }

    @Transactional
    public int queryFace(String id) {
        if (facePassMapper.getOne(id) == null) {
            return 1503;
        } else {
            String face_token = facePassMapper.getFace_token(id);
            Map<String, String> token = new HashMap<String, String>() {{
                put("face_token", face_token);
            }};
            ResponseEntity res = httpService.sendJson("/api/face/v1/query", token);
            if (checkRespon(res)) {
                return 0;
            } else
                return getCode(res);
        }

    }

    public int queryGroup(String group) {
        Map<String, String> group_name = new HashMap<String, String>() {{
            put("group_name", group);
        }};
        ResponseEntity res = httpService.sendJson("/api/group/v1/query", group_name);
        if (checkRespon(res)) {
            List<String> list = getTokenList(res);
            ExtraTokenInServer(facePassMapper.getFace_TokenList(group), list).forEach(p -> {
                facePassMapper.delete(null, p);
            });
            return 0;
        } else
            return getCode(res);
    }
    public int deleteGroup(String group) {
        Map<String, String> group_name = new HashMap<String, String>() {{
            put("group_name", group);
        }};
        ResponseEntity res = httpService.sendJson("/api/group/v1/query", group_name);
        if (checkRespon(res)||getCode(res)==104301){
            userMapper.deleteGroup(facePassMapper.getIdList(group));
            facePassMapper.deleteGroup(group);
            return 0;
        }else
            return getCode(res);
    }
    public List<Map<String,Object>> getUserData(String id){
        User user=userMapper.getOne(id);
        String group=facePassMapper.getOne(id).getGroup();
        List<Map<String,Object>> list=new ArrayList<>();
        list.add(user.getUserMap(group));
        return list;
    }
    public List<Map<String,Object>> getGroupData(String group){
        return facePassMapper.getGroup(group);
    }
    private static String getToken2(ResponseEntity response)throws JSONException{
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        JSONObject data=(JSONObject)JSONObject.fromObject(json).get("data");
        return data.getString("face_token");
    }
    private static String getToken1(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return json.getString("face_token");
    }
    private static String getMessage(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return json.getString("message");
    }
    private static int getCode(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return Integer.valueOf(json.getString("code"));
    }
    private static Boolean checkRespon(ResponseEntity response){
        if(response.getStatusCode().value()!=200){
            return false;
        }else
            return true;
    }
    private static List<String> getTokenList(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        JSONObject data=(JSONObject)JSONObject.fromObject(json).get("data");
        JSONArray faces=(JSONArray)JSONObject.fromObject(data).get("faces");
        return faces;
    }
    private static List<String> ExtraTokenInServer(List<String> serverList, List<String> facepassList) {
        Map<String,Integer> map = new HashMap(facepassList.size());
        List<String> extraList = new ArrayList();
        for(String resource : facepassList){
            map.put(resource,1);
        }
        for(String resource1 : serverList){
            if(map.get(resource1)==null){
                extraList.add(resource1);
            }
        }
        return extraList;
    }


}

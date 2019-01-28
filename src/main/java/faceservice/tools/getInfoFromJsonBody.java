package faceservice.tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class getInfoFromJsonBody {
    public static String getTokenFromData(ResponseEntity response)throws JSONException {
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        JSONObject data=(JSONObject)JSONObject.fromObject(json).get("data");
        return data.getString("face_token");
    }
    public static String getToken(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return json.getString("face_token");
    }
    public static String getMessage(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return json.getString("message");
    }
    public static int getCode(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return Integer.valueOf(json.getString("code"));
    }
    public static Boolean checkRespon(ResponseEntity response){
        if(response.getStatusCode().value()!=200){
            return false;
        }else
            return true;
    }
    public static List<String> getTokenList(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        JSONObject data=(JSONObject)JSONObject.fromObject(json).get("data");
        JSONArray faces=(JSONArray)JSONObject.fromObject(data).get("faces");
        return faces;
    }
    public static List<String> ExtraTokenInServer(List<String> serverList, List<String> hostList) {
        Map<String,Integer> map = new HashMap(hostList.size());
        List<String> extraList = new ArrayList();
        for(String resource : hostList){
            map.put(resource,1);
        }
        for(String resource1 : serverList){
            if(map.get(resource1)==null){
                extraList.add(resource1);
            }
        }
        return extraList;
    }
    public static String getMsg(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return json.getString("msg");
    }
    public static boolean getResult(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return (Boolean) json.get("success");

    }
    public static String getJosnString(Map<String,String> map){
        JSONObject json=JSONObject.fromObject(map);
        return json.toString();
    }
    public static List<String> getIdList(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        List<String> idList=new ArrayList<>();
        if(json.has("data")){
            ListIterator array1=json.getJSONArray("data").listIterator();
            while(array1.hasNext()){
                JSONObject face=JSONObject.fromObject(array1.next());
                idList.add(face.getString("faceId"));
            }
        }
        return idList;
    }
    public static List<Map<String, Object>> getData(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        List<Map<String, Object>> list=new ArrayList<>();
        if(json.has("data")){
            list=(List<Map<String, Object>>)json.get("data");
        }
        return list;
    }
    public static String getMac(ResponseEntity response){
        String message=response.getBody().toString();
        JSONObject json=JSONObject.fromObject(message);
        return json.getString("mac");
    }
}

package faceservice.service;

import faceservice.mapper.EIMapper;
import faceservice.mapper.UserMapper;
import faceservice.model.EI;
import faceservice.model.EIAddRequest;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import static faceservice.tools.getInfoFromJsonBody.getJosnString;
import static faceservice.tools.getInfoFromJsonBody.getResultCode;
import static faceservice.tools.getInfoFromJsonBody.getResultMessage;

import faceservice.model.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EIService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EIMapper eiMapper;


    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public String message;
    private int validtime=86400*365;
    private int packageNO=1;
    public int addFace(EIAddRequest addRequest)throws IOException {
        if (userMapper.getOne(addRequest.getId()) != null) {
            return 1501;
        }
        String ip=addRequest.getIp();
        EISocket socket=new EISocket(new Socket(ip,18026));
        long time = new Date().getTime();
        int nowTimeStamp =(int)(time / 1000);
        if(addRequest.getDays()!=null){
            validtime=86400*Integer.valueOf(addRequest.getDays());
        }
        Map addFace = new HashMap<String, Object>() {{
            put("device_id", addRequest.getDevice());
            put("credence_type","FACE");
            put("user_id",addRequest.getId().substring(0,8));
            put("user_name",addRequest.getName());
            put("credence_id",addRequest.getId());
            put("picture",new String(Base64.encodeBase64(addRequest.getBytes())));
            put("timestamp",nowTimeStamp);
            put("start_time",nowTimeStamp);
            put("end_time",nowTimeStamp+validtime);
        }};
        List<Map<String,Object>> list=new ArrayList<>();
        list.add(addFace);
        Map data=new HashMap<String,Object>(){{
            put("data",list);
        }};
        String jsondata=getJosnString(data);
        byte[] byteJsonData=jsondata.getBytes();
        int len=byteJsonData.length;
        socket.respond(920,byteJsonData,len,packageNO);
        if(socket.read()){
            packageNO+=1;
            String mess=socket.getData();
            socket.shutdownAndClose();
            if(getResultCode(mess)==0){
                userMapper.insert(new User(addRequest));
                eiMapper.insert(new EI(addRequest));
                return 0;
            }else{
                return getResultCode(mess);
            }
        }else {
            return 1001000;
        }

    }
    @Transactional
    public int faceDelete(String id)throws IOException {
        EI ei=eiMapper.getOne(id);
        User user=userMapper.getOne(id);
        if (ei == null || user == null) {
            if(ei==null){
                userMapper.delete(id);
            }else
                eiMapper.delete(id);
            return 1503;
        }
        EISocket socket=new EISocket(new Socket(ei.getIp(),18026));
        long time = new Date().getTime();
        int nowTimeStamp =(int)(time / 1000);
        Map deleteFace = new HashMap<String, Object>() {{
            put("device_id", ei.getDevice());
            put("credence_type","FACE");
            put("credence_id",ei.getId());
            put("user_id",ei.getId().substring(0,8));
            put("user_name",user.getName());
            put("timestamp",nowTimeStamp);
        }};
        List<Map<String,Object>> list=new ArrayList<>();
        list.add(deleteFace);
        Map data=new HashMap<String,Object>(){{
            put("data",list);
        }};
        String jsondata=getJosnString(data);
        byte[] byteJsonData=jsondata.getBytes();
        int len=byteJsonData.length;
        socket.respond(922,byteJsonData,len,packageNO);
        if(socket.read()){
            packageNO+=1;
            String mess=socket.getData();
            socket.shutdownAndClose();
            if(getResultCode(mess)==0){
                userMapper.delete(id);
                eiMapper.delete(id);
                return 0;
            }else{
                setMessage(getResultMessage(mess));
                return 1;
            }
        }
        return 1;
    }
}

package faceservice.controller;

import faceservice.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class MyController {
    @Autowired
    private MyService myService;
    /*
    * get方法
    * */
    @RequestMapping(value = "/TestGet", method = RequestMethod.GET)
    @ResponseBody
    public void getUrl(@RequestParam Map<String, Object> map){
        myService.get("api/version/v1",map);
    }
    /*
    * post  form使用实例
    * */
    @RequestMapping(value = "/TestSendForm", method = RequestMethod.GET)
    @ResponseBody
    public void postForm(@RequestParam Map<String, Object> map){
        myService.get("api/version/v1",map);
    }
    /*
    * post json使用实例
    * */
    @RequestMapping(value = "/TestSendJson", method = RequestMethod.GET)
    @ResponseBody
    public void postJson(@RequestParam Map<String,String> map){
        map.put("face_token","djsfjsjfksfjfjsjfhsl==");
        myService.sendJson("api/face/v1/delete",map);
    }
}

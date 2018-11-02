package faceservice.controller;

import faceservice.mapper.keyMapper;
import faceservice.service.FaceService;
import faceservice.service.HttpService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;

@Controller
public class MyController {
    @Autowired
    private HttpService httpService;
    @Autowired
    private FaceService faceService;
    @Autowired
    private keyMapper keyMapper;
    /*
    * get方法
    * */
    @RequestMapping(value = "/TestGet", method = RequestMethod.GET)
    @ResponseBody
        public void getUrl(@RequestParam Map<String, Object> map){
        httpService.get("api/version/v1",map);
    }
    /*
    * post  form使用实例
    * */
    @RequestMapping(value = "/addFace", method = RequestMethod.POST)
    @ResponseBody
    public void postForm(@RequestParam Map<String,String> map,MultipartFile image)throws Exception{
        String key_id=map.get("key_id").toString();
        String type=map.get("type").toString();
        if(key_id!=null&&key_id!=""&&!image.isEmpty()){
            faceService.addFace(type,key_id,image);
        }
    }
    /*
    * post json使用实例
    * */
    @RequestMapping(value = "/TestSendJson", method = RequestMethod.GET)
    @ResponseBody
    public void postJson(@RequestParam Map<String,String> map){
        map.put("face_token","djsfjsjfksfjfjsjfhsl==");
        httpService.sendJson("api/face/v1/delete",map);
    }
    /*
   * post json使用实例
   * */
    @RequestMapping(value = "/Test", method = RequestMethod.GET)
    @ResponseBody
    public void Test(@RequestParam Map<String,String> map){
        System.out.println(keyMapper.getAll().size());
        System.out.println(keyMapper.getOne("sdisfasf==").toString());
    }
}

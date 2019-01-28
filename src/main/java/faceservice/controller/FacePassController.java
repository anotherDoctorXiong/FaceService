package faceservice.controller;

import faceservice.mapper.UserMapper;
import faceservice.model.FacePassAddRequest;
import faceservice.model.Response;
import faceservice.service.FacePassService;
import faceservice.service.FaceService;
import faceservice.service.ParkingService;
import faceservice.tools.Constraint.Host;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping(value = "/server/facepass")
@Slf4j
public class FacePassController {

    @Autowired
    private FacePassService service;
    @Autowired
    private FaceService faceService;
    @Autowired
    private UserMapper UserMapper;
    @Autowired
    private ParkingService parkingService;
    @Autowired
    private Host host;



    /**
     * @name: addFace
     * @discraption: 接收http post请求 参数放form里面
     * @param: type<String>(1是使用facepass)</>id<String>(人脸id),group<String>(分组id),image<MultipartFile>(人脸图片)
     * @return: 添加成功返回200, 失败返回400包含错误信息
     **/
    @RequestMapping(value = "/face/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> addFace(@Valid FacePassAddRequest addRequest, BindingResult bindingResult) throws Exception {
        Response res=new Response();
        //对参数进行校验
        if(bindingResult.hasErrors()){
            res.setCode(1);
            res.setMessage(bindingResult.getFieldError().getDefaultMessage());
            return new ResponseEntity(res,HttpStatus.BAD_REQUEST);
        }else{
            res.setCode(service.addFace(addRequest));
            return new ResponseEntity(res,HttpStatus.OK);
        }

    }

    /**
     * @name: deleteFace
     * @discraption: 接收http  json数据
     * @param: id<String>(人脸id)
     * @return: 删除成功返回200  失败返回失败信息
     **/
    @RequestMapping(value = "/face/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> deleteFace(@RequestParam  String id){
        Response res=new Response();
        res.setCode(service.deleteFace(id));
        return new ResponseEntity(res,HttpStatus.OK);
    }
    /**
      * @name:          queryFace
      * @discraption:   通过人脸id查询该人脸是否存在
      * @param:         人脸id
      * @return:        返回200该人脸已被添加，失败返回错误信息
      **/
    @RequestMapping(value = "/face/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> queryFace(@RequestParam  String id) {
        Response res=new Response();
        int a=service.queryFace(id);
        if(a==0){
            res.setData(service.getUserData(id));
        }
        res.setCode(a);
        return new ResponseEntity(res,HttpStatus.OK);
    }
    /**
      * @name:          updateFace
      * @discraption:   通过人脸id更新图片
      * @param:         人脸id,人脸图片
      * @return:        成功返回200,失败返回失败信息
      **/
    @RequestMapping(value = "/face/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> updateFace(@Valid FacePassAddRequest addRequest, BindingResult bindingResult) throws Exception {
        Response res=new Response();
        //对参数进行校验
        if(bindingResult.hasErrors()){
            res.setCode(1);
            res.setMessage(bindingResult.getFieldError().getDefaultMessage());
            return new ResponseEntity(res,HttpStatus.BAD_REQUEST);
        }else{
            int a=service.deleteFace(addRequest.getId());
            if(a==0||a==1503){
                res.setCode(service.addFace(addRequest));
            }
            return new ResponseEntity(res,HttpStatus.OK);
        }

    }
    /**
      * @name:          queryGroup
      * @discraption:   通过group查询该分组的所有人脸id
      * @param:         group<String></>
      * @return:        200返回List(id)
      **/
    @RequestMapping(value = "/group/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity queryGroup(@RequestParam  String group){
        Response res=new Response();
        int a=service.queryGroup(group);
        if(a==0){
            res.setData(service.getGroupData(group));
        }
        res.setCode(a);
        return new ResponseEntity(res,HttpStatus.OK);

    }
}

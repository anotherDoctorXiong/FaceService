package faceservice.controller;


import faceservice.model.FacePassAddRequest;
import faceservice.model.HostAddRequest;
import faceservice.model.Response;
import faceservice.service.HuaXiaService;
import faceservice.tools.Constraint.Host;
import faceservice.tools.Imagehandle.ImageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import static faceservice.tools.Imagehandle.ImageTools.isPic;

@RequestMapping(value = "/host/huaxia")
@RestController
public class HuaXiaController {
    @Autowired
    private HuaXiaService huaXiaService;
    @RequestMapping(value = "/face/add", method = RequestMethod.POST)
    public ResponseEntity<Response> addFace(@Valid HostAddRequest addRequest, BindingResult bindingResult) throws Exception {
        Response res=new Response();
        //对参数进行校验
        if(bindingResult.hasErrors()){
            res.setCode(1);
            res.setMessage(bindingResult.getFieldError().getDefaultMessage());
            return new ResponseEntity(res, HttpStatus.BAD_REQUEST);
        }else{
            int a=huaXiaService.addFace(addRequest);
            if(a==0){
                res.setCode(a);
                res.setMac(huaXiaService.getMacAddress(addRequest.getId()));
            }else if(a==1){
                res.setCode(a);
                res.setMessage(huaXiaService.getMessage());
            }else
                res.setCode(a);
            return new ResponseEntity(res,HttpStatus.OK);
        }
    }
    @RequestMapping(value = "/face/delete", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteFace(@RequestParam String id) {
        Response res=new Response();
        //对参数进行校验
        int a=huaXiaService.faceDelete(id);
        if(a==0){
            res.setMac(huaXiaService.getMacAddress(id));
            res.setCode(a);
        }else if(a==1) {
            res.setCode(a);
            res.setMessage(huaXiaService.getMessage());
        }else
            res.setCode(a);
        return new ResponseEntity(res,HttpStatus.OK);
    }
    @RequestMapping(value = "/face/updata", method = RequestMethod.POST)
    public ResponseEntity<Response> updataFace(String id, MultipartFile image)throws IOException{
        Response res=new Response();
        //对参数进行校验
        if(id==null||image==null||image.isEmpty()){
            res.setCode(1);
            return new ResponseEntity(res,HttpStatus.OK);
        }
        HostAddRequest addRequest=new HostAddRequest();
        addRequest.setImage(image);
        if(!isPic(addRequest.getImage())){
            res.setCode(1);
            res.setMessage("image is only allowed jpg/jpeg/png");
            return new ResponseEntity(res,HttpStatus.OK);
        }
        int a=huaXiaService.updataFace(id,addRequest.getBytes());
        if(a==0){
            res.setCode(a);
            res.setMac(huaXiaService.getMacAddress(id));
        }else if(a==1){
            res.setCode(a);
            res.setMessage(huaXiaService.getMessage());
        }else
            res.setCode(a);
        return new ResponseEntity(res,HttpStatus.OK);
    }
    @RequestMapping(value = "/face/query", method = RequestMethod.POST)
    public ResponseEntity<Response> queryFace(@RequestParam String id) {
        Response res=new Response();
        //对参数进行校验
        int a=huaXiaService.queryFace(id);
        if(a==0){
            res.setCode(a);
            res.setMac(huaXiaService.getMacAddress(id));
            res.setData(huaXiaService.getUserData(id));
        }else if(a==1){
            res.setCode(a);
            res.setMessage(huaXiaService.getMessage());
        }else
            res.setCode(a);
        return new ResponseEntity(res,HttpStatus.OK);
    }
    @RequestMapping(value = "/face/group", method = RequestMethod.POST)
    public ResponseEntity<Response> queryGroup(@RequestParam String ip) {
        Host host=new Host();
        Response res=new Response();
        if(host.isHostConnectable(ip)){
            //对参数进行校验
            res.setMessage(huaXiaService.getMessage());
            res.setData(huaXiaService.queryAll(ip));
        }else
            res.setCode(1504);
        return new ResponseEntity(res,HttpStatus.OK);
    }
}

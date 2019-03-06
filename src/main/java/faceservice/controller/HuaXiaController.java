package faceservice.controller;


import faceservice.model.HuaXiaAddRequest;
import faceservice.model.Response;
import faceservice.service.HuaXiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

import static faceservice.tools.Constraint.Host.isHostConnectable;
import static faceservice.tools.Imagehandle.ImageTools.isPic;

@RequestMapping(value = "/host/huaxia")
@RestController
public class HuaXiaController {
    @Autowired
    private HuaXiaService huaXiaService;
    @RequestMapping(value = "/face/add", method = RequestMethod.POST)
    public ResponseEntity<Response> addFace(@Valid HuaXiaAddRequest addRequest, BindingResult bindingResult) throws Exception {
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
        String mac=huaXiaService.getMacAddress(id);
        int a=huaXiaService.faceDelete(id);
        if(a==0){
            res.setMac(mac);
            res.setCode(a);
        }else if(a==1) {
            res.setCode(a);
            res.setMessage(huaXiaService.getMessage());
        }else
            res.setCode(a);
        return new ResponseEntity(res,HttpStatus.OK);
    }
    @RequestMapping(value = "/face/updata", method = RequestMethod.POST)
    public ResponseEntity<Response> updataFace(String id,String name, MultipartFile image)throws IOException{
        Response res=new Response();
        //对参数进行校验
        if(id==null||image==null||image.isEmpty()||name==null||name==""){
            res.setCode(1);
            return new ResponseEntity(res,HttpStatus.OK);
        }
        HuaXiaAddRequest addRequest=new HuaXiaAddRequest();
        addRequest.setImage(image);
        if(!isPic(addRequest.getImage())){
            res.setCode(1);
            res.setMessage("image is only allowed jpg/jpeg/png");
            return new ResponseEntity(res,HttpStatus.OK);
        }
        int a=huaXiaService.updataFace(id,name,addRequest.getBytes());
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
    @RequestMapping(value = "/group/query", method = RequestMethod.POST)
    public ResponseEntity<Response> queryGroup(@RequestParam String ip) {
        Response res=new Response();
        if(isHostConnectable(ip)){
            //对参数进行校验
            res.setMessage(huaXiaService.getMessage());
            res.setData(huaXiaService.queryAll(ip));
        }else
            res.setCode(1504);
        return new ResponseEntity(res,HttpStatus.OK);
    }
    @RequestMapping(value = "/group/delete", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteGroup(@RequestParam String ip) {
        Response res=new Response();
        if(isHostConnectable(ip)){
            int a=huaXiaService.deleteAll(ip);
            if(a==0){
                res.setCode(a);
            }else
                res.setMessage(huaXiaService.getMessage());
        }else
            res.setCode(1504);
        return new ResponseEntity(res,HttpStatus.OK);
    }
}

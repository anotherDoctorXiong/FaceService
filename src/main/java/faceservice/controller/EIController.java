package faceservice.controller;

import faceservice.model.EIAddRequest;
import faceservice.model.Response;
import faceservice.service.EIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
@Slf4j
@RestController
@RequestMapping(value = "/host/ei")
public class EIController {
    @Autowired
    private EIService service;
    @RequestMapping(value = "/face/add", method = RequestMethod.POST)
    public ResponseEntity<Response> addFace(@Valid EIAddRequest addRequest, BindingResult bindingResult) throws Exception {
        Response res=new Response();
        //对参数进行校验
        if(bindingResult.hasErrors()){
            res.setCode(1);
            res.setMessage(bindingResult.getFieldError().getDefaultMessage());
            return new ResponseEntity(res, HttpStatus.BAD_REQUEST);
        }else{
            int a=service.addFace(addRequest);
            if (a == 0) {
                res.setData(service.getData());
            }
            res.setCode(a);
            return new ResponseEntity(res,HttpStatus.OK);
        }
    }
    @RequestMapping(value = "/face/delete", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteFace(@RequestParam String id)throws IOException {
        Response res=new Response();
        //对参数进行校验
        int a=service.faceDelete(id);
        if (a == 0) {
            res.setData(service.getData());
        }
        res.setCode(a);
        return new ResponseEntity(res,HttpStatus.OK);
    }
}

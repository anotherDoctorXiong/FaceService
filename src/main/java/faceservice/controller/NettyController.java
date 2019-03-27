package faceservice.controller;

import faceservice.NettyClient.NettyPoolClient;
import faceservice.model.Response;
import faceservice.nettyserver.router.RequestMapping;
import faceservice.nettyserver.util.RequestUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class NettyController {
    @Autowired
    private NettyPoolClient client;
    @RequestMapping(uri = "/test/netty", method = "POST")
    public Response addFace(FullHttpRequest request) throws Exception {
        Response res=new Response();
        System.out.println(res.getTimestamp());
        Map<String,Object> map=RequestUtil.getFormParams(request);
        if(map==null){
            res.setCode(1);
        }else {
            map.forEach((k,v)->{
                System.out.println(k+"----"+v);
            });
        }
        return res;
    }
}

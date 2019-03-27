package faceservice.nettyserver;

import faceservice.model.Response;
import faceservice.nettyserver.router.Action;
import faceservice.nettyserver.router.HttpLabel;
import faceservice.nettyserver.router.HttpRouter;
import faceservice.nettyserver.util.ResponseUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;


import java.util.Map;

public class MainServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

    HttpRouter httpRouter;
    public MainServerHandler(HttpRouter httpRouter){
        this.httpRouter=httpRouter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.uri();
        if (uri.contains("?")) {
            uri = uri.substring(0, uri.indexOf("?"));
        }
        //根据不同的请求API做不同的处理(路由分发)
        Action<Response> action = httpRouter.getRoute(new HttpLabel(uri, request.method()));
        if (action != null) {
            if (action.isInjectionFullhttprequest()) {
                ctx.writeAndFlush(action.call(request));
            } else {
                ctx.writeAndFlush(action.call());
            }
        } else {
            //错误处理
            Response res=new Response();
            ResponseUtil.response(ctx, request,res);
        }

    }
}

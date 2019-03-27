package faceservice.nettyserver;

import faceservice.model.Response;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.*;
import net.sf.json.JSONObject;

import java.util.List;

import static io.netty.util.CharsetUtil.UTF_8;

public class HttpJsonResponse extends MessageToMessageEncoder<Response> {



    @Override
    protected void encode(ChannelHandlerContext ctx, Response response, List<Object> list) throws Exception {
        String jsonStr = JSONObject.fromObject(response).toString();
        ByteBuf body = Unpooled.copiedBuffer(jsonStr,UTF_8);
        int code=response.getCode();
        FullHttpResponse res = null;
        if (body != null)  {
            res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, (code==1||code==0)?HttpResponseStatus.OK:HttpResponseStatus.BAD_REQUEST,
                    body);
            res.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json");
            HttpUtil.setContentLength(res, body.readableBytes());
        }
        System.out.println(res.retain());
        list.add(res);
    }
}
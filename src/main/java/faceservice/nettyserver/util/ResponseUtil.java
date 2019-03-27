package faceservice.nettyserver.util;

import faceservice.model.Response;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import net.sf.json.JSONObject;

public class ResponseUtil {
    public static void response(ChannelHandlerContext ctx, FullHttpRequest request, Response jsonresponse) {

        boolean keepAlive = HttpUtil.isKeepAlive(request);
        byte[] jsonByteByte = JSONObject.fromObject(jsonresponse).toString().getBytes();
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(jsonByteByte));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.write(response);
        }
        ctx.flush();
    }
}

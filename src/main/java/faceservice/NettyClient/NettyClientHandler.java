package faceservice.NettyClient;

import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;


public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    public static final AttributeKey<String> KEY = AttributeKey.valueOf("netty.channel");
    private ChannelHandlerContext ctx;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx=ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("call back"+msg);
        Attribute<String> attr=ctx.attr(KEY);
        attr.setIfAbsent(msg.toString());
    }

}


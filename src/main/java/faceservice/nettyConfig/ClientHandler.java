package faceservice.nettyConfig;

import faceservice.model.Msg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.CountDownLatch;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private CountDownLatch lathc;
    private Msg message;
    public ClientHandler(CountDownLatch lathc) {
        this.lathc = lathc;
    }


    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Msg) {
            message = (Msg)msg;
            System.out.println("Client Received :" + message);
            lathc.countDown();
        } else {
            System.err.println("received msg's type is not a legal Msg type!");
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
    public void resetLatch(CountDownLatch lathc) {
        this.lathc = lathc;
    }

    public String getResult() {
        return message.getMessage();
    }

}

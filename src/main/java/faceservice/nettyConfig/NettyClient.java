package faceservice.nettyConfig;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

@Configuration
public class NettyClient {
    private EventLoopGroup group;
    private Bootstrap b;
    private ChannelFuture cf ;
    private CountDownLatch lathc;
    private HandlerInit handlerInit;
    public NettyClient(){
        lathc=new CountDownLatch(0);
        handlerInit=new HandlerInit(lathc);
        group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(handlerInit);

    }
    public void connect(){
        try {
            this.cf = b.connect("192.168.2.107", 10882).sync();
            System.out.println("远程服务器已经连接, 可以进行数据交换..");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ChannelFuture getChannelFuture(){
        //如果管道没有被开启或者被关闭了，那么重连
        if(this.cf == null){
            this.connect();
        }
        if(!this.cf.channel().isActive()){
            this.connect();
        }
        return this.cf;
    }
    public HandlerInit getHandlerInit(){
        return handlerInit;
    }

}

package faceservice.NettyClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class NettyPoolClient {
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;
    EventLoopGroup group=new NioEventLoopGroup();
    Bootstrap b=new Bootstrap();
    ChannelPoolMap<InetSocketAddress,SimpleChannelPool> poolMap;
    public NettyPoolClient(){
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.SO_KEEPALIVE,true);
        poolMap=new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
            @Override
            protected SimpleChannelPool newPool(InetSocketAddress addr) {
                return new FixedChannelPool(b.remoteAddress(addr),
                        new NettyChannelPoolHandler(),2);
            }
        };
    }
    public void sendMessage(String host,String message){
        ChannelFuture Myfuture;
        SimpleChannelPool pool = poolMap.get(new InetSocketAddress(host,9955));
        Future<Channel> f = pool.acquire();
        f.addListener((FutureListener<Channel>) f1 -> {
            if (f1.isSuccess()) {
                Channel ch = f1.getNow();
                ChannelFuture future=ch.writeAndFlush(message).channel().closeFuture();
                future.addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        Attribute<String> attr=f.getNow().attr(NettyClientHandler.KEY);
                        System.out.println(attr.get());
                    }
                });
                pool.release(ch);
            }
        });
        /*int time=(int)(new Date().getTime()/1000);
        for(;;){
            if(f.getNow().closeFuture().isSuccess()){
                Attribute<String> attr=f.getNow().attr(NettyClientHandler.KEY);
                System.out.println(attr.get());
                break;
            }
            if(time-(int)(new Date().getTime()/1000)>1){
                System.out.println("超出1S");
                break;
            }
        }*/

    }

    public static void main(String[] args) {
        NettyPoolClient client=new NettyPoolClient();
        client.sendMessage("127.0.0.1","hello");
    }

}

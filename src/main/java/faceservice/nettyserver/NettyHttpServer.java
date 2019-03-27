package faceservice.nettyserver;

import faceservice.nettyserver.router.HttpRouter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class NettyHttpServer implements CommandLineRunner {
    public void run(String... args){
        HttpRouter router=new HttpRouter();
        router.addRouter("faceservice.controller.NettyController");
        EventLoopGroup group=new NioEventLoopGroup();
        ServerBootstrap b=new ServerBootstrap();
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                        ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                        ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                        ch.pipeline().addLast("map-to-json", new HttpJsonResponse());
                        ch.pipeline().addLast("handler", new MainServerHandler(router));
                    }
                });
        try {
            ChannelFuture cf=b.bind(8088).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}

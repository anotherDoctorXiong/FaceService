package faceservice.demo;

import faceservice.HandlerInit;
import faceservice.mapper.keyMapper;
import faceservice.model.Msg;
import faceservice.model.keyModel;

import faceservice.service.HttpService;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;



@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	private static String SERVER_IP = "192.168.2.107";
	private static int SERVER_PORT = 10882;
	static ChannelFuture cf;
	static Bootstrap bootstrap;
	static EventLoopGroup eventLoopGroup;

	@Test
	public  void Test() throws Exception {
		try {
			connect();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}

	private static void connect() throws Exception {
		bootstrap = new Bootstrap();
		eventLoopGroup = new NioEventLoopGroup();
		bootstrap.group(eventLoopGroup);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new HandlerInit());
		cf = bootstrap.connect(SERVER_IP, SERVER_PORT).sync();
		System.out.println("Netty Client " + cf.channel().localAddress() + " started. connected to " + SERVER_IP + ":" + SERVER_PORT);
		cf.awaitUninterruptibly();
		queryUser();
		cf.channel().closeFuture().sync();
	}



/*	private static void register() throws Exception {
		short cmd = 501;
		String imageToSend = "1.jpg";
		File imageFile = new File(imageToSend);
		if (!imageFile.exists()) {
			System.err.println("imageFile \"" + imageToSend + "\" doesn't exists.");
		}

		ImageBytes im = new ImageBytes();
		byte[] imageBytes = im.image2Bytes(imageToSend);
		String content1 = "0115011501030402";
		byte[] contentBytes = (new AppendBytes()).append(content1.getBytes(), imageBytes);
		int length = 3 + contentBytes.length;
		SzsqMsg msg2Send = new SzsqMsg();
		msg2Send.setMsgLength(length);
		msg2Send.setCmd(cmd);
		msg2Send.setContent(contentBytes);
		cf.channel().writeAndFlush(msg2Send);
	}

	private static void deletePhoto() {
		short cmd = 509;
		String content = "1501030401";
		byte[] contentBytes = content.getBytes();
		System.out.println("content's length:" + contentBytes.length);
		int length = 3 + content.length();
		SzsqMsg msg2Send = new SzsqMsg();
		msg2Send.setMsgLength(length);
		msg2Send.setCmd(cmd);
		msg2Send.setContent(contentBytes);
		cf.channel().writeAndFlush(msg2Send);
	}

	private static void updateBuildInfo() {
	}

	private static void updatePhoto() throws Exception {
		short cmd = 505;
		String imageToSend = "test2.jpg";
		File imageFile = new File(imageToSend);
		if (!imageFile.exists()) {
			System.err.println("imageFile \"" + imageToSend + "\" doesn't exists.");
		}

		ImageBytes im = new ImageBytes();
		byte[] imageBytes = im.image2Bytes(imageToSend);
		System.out.println("images' length:" + imageBytes.length);
		String content1 = "1501030401";
		byte[] contentBytes = (new AppendBytes()).append(content1.getBytes(), imageBytes);
		System.out.println("content's length:" + contentBytes.length);
		int length = 3 + contentBytes.length;
		SzsqMsg msg2Send = new SzsqMsg();
		msg2Send.setMsgLength(length);
		msg2Send.setCmd(cmd);
		msg2Send.setContent(contentBytes);
		cf.channel().writeAndFlush(msg2Send);
	}*/

	private static void queryUser() {

		String content = "150103040581";
		short cmd = 503;
		byte[] contentBytes = content.getBytes();
		int length = 3 + content.length();
		Msg msg2Send = new Msg();
		msg2Send.setMsgLength(length);
		msg2Send.setCmd(cmd);
		msg2Send.setContent(contentBytes);
		cf.channel().writeAndFlush(msg2Send);
	}


}

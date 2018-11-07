package faceservice.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


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

		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}



}

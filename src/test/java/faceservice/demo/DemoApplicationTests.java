package faceservice.demo;

import faceservice.service.HttpService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	private static String SERVER_IP = "192.168.2.107";
	private static int SERVER_PORT = 10882;
	static ChannelFuture cf;
	static Bootstrap bootstrap;
	static EventLoopGroup eventLoopGroup;
	@Autowired
	private HttpService httpService;

	@Test
	public  void Test() {
		Map<String,String> map=new HashMap<>();
		map.put("faceId","005");
		map.put("idcard","360122199909091212");
		map.put("wgId","6");

	}



}

package faceservice.service;

import faceservice.nettyConfig.HandlerInit;
import faceservice.nettyConfig.NettyClient;
import faceservice.model.Msg;
import io.netty.channel.ChannelFuture;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.concurrent.CountDownLatch;

@Service
public class TcpService {
    private ChannelFuture cf;
    private HandlerInit handlerInit;

    public TcpService(NettyClient n) {
        this.cf = n.getChannelFuture();
        this.handlerInit = n.getHandlerInit();
    }


    /*private static void register() throws Exception {
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
    }*/
    public String register(String id, String group, MultipartFile file) throws Exception {
        short cmd = 501;
        File image = new File(ResourceUtils.getURL("classpath:Face/face.jpg").getPath());
        file.transferTo(image);
        byte[] imageBytes = getBytes(image);
        String content1 = group+id;
        byte[] contentBytes = (appendBytes(content1.getBytes(), imageBytes));
        int length = 3 + contentBytes.length;
        Msg msg2Send = new Msg();
        msg2Send.setMsgLength(length);
        msg2Send.setCmd(cmd);
        msg2Send.setContent(contentBytes);
        cf.channel().writeAndFlush(msg2Send);
        CountDownLatch lathc = new CountDownLatch(1);
        handlerInit.resetLathc(lathc);
        lathc.await();//开始等待结果返回后执行下面的代码
        return handlerInit.getServerResult();
    }

    public String delete(String id) throws InterruptedException {
        short cmd = 509;
        String content = id;
        byte[] contentBytes = content.getBytes();
        System.out.println("content's length:" + contentBytes.length);
        int length = 3 + content.length();
        Msg msg2Send = new Msg();
        msg2Send.setMsgLength(length);
        msg2Send.setCmd(cmd);
        msg2Send.setContent(contentBytes);
        cf.channel().writeAndFlush(msg2Send);
        CountDownLatch lathc = new CountDownLatch(1);
        handlerInit.resetLathc(lathc);
        lathc.await();//开始等待结果返回后执行下面的代码
        return handlerInit.getServerResult();
    }

    /*
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
    public void Test() {

        //ChannelFuture cf = c.getChannelFuture();
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

    public String query(String group,String id) throws InterruptedException{
        String content =group+id;
        short cmd = 503;
        byte[] contentBytes = content.getBytes();
        int length = 3 + content.length();
        Msg msg2Send = new Msg();
        msg2Send.setMsgLength(length);
        msg2Send.setCmd(cmd);
        msg2Send.setContent(contentBytes);
        cf.channel().writeAndFlush(msg2Send);
        CountDownLatch lathc = new CountDownLatch(1);
        handlerInit.resetLathc(lathc);
        lathc.await();//开始等待结果返回后执行下面的代码
        return handlerInit.getServerResult();
    }

    private static byte[] getBytes(File file) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        boolean var4 = false;

        int len;
        while ((len = fin.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        byte[] bytes = out.toByteArray();
        fin.read(bytes);
        fin.close();
        return bytes;
    }

    public static byte[] appendBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }


}

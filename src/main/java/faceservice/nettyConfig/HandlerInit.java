package faceservice.nettyConfig;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;
import java.util.concurrent.CountDownLatch;

public class HandlerInit extends ChannelInitializer<SocketChannel> {
    private static final ByteOrder BYTE_ORDER;
    private static final int MAX_FRAME_LENGTH = 2147483647;
    private static final int LENGTH_FIELD_OFFSET = 2;
    private static final int LENGTH_FIELD_LENGTH = 4;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTESTOSTRIP = 0;
    private static final boolean FAIL_FAST = true;
    private CountDownLatch lathc;
    private ClientHandler handler;

    static {
        BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    }

    public HandlerInit(CountDownLatch lathc) {
        this.lathc = lathc;
    }

    protected void initChannel(SocketChannel ch) throws Exception {
        handler=new ClientHandler(lathc);
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("lengthFieldDecoder", new LengthFieldBasedFrameDecoder(BYTE_ORDER, 2147483647, 2, 4, 0, 0, true));
        pipeline.addLast("msgDecoder", new Decoder());
        pipeline.addLast("msgEncoder", new Encoder());
        pipeline.addLast(handler);
    }
    public String getServerResult(){
        return handler.getResult();
    }
    public void resetLathc(CountDownLatch lathc) {
        handler.resetLatch(lathc);
    }

}

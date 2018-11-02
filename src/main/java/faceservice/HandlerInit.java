package faceservice;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

public class HandlerInit extends ChannelInitializer<SocketChannel> {
    private static final ByteOrder BYTE_ORDER;
    private static final int MAX_FRAME_LENGTH = 2147483647;
    private static final int LENGTH_FIELD_OFFSET = 2;
    private static final int LENGTH_FIELD_LENGTH = 4;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTESTOSTRIP = 0;
    private static final boolean FAIL_FAST = true;

    static {
        BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    }

    public HandlerInit() {
    }

    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("lengthFieldDecoder", new LengthFieldBasedFrameDecoder(BYTE_ORDER, 2147483647, 2, 4, 0, 0, true));
        pipeline.addLast("msgDecoder", new Decoder());
        pipeline.addLast("msgEncoder", new Encoder());
        pipeline.addLast(new ChannelHandler[]{new ClientHandler()});
    }
}

package faceservice.nettyConfig;

import faceservice.model.Msg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Encoder extends MessageToByteEncoder<Msg> {
    public void Encoder(){

    }
    protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) throws Exception {
        ByteBuf messageData = Unpooled.buffer();
        messageData.clear();
        messageData.writeShort(1976);
        int length = msg.getMsgLength() + 11;
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.asIntBuffer().put(length);
         length = Num2BytesAndReverse.bytesToInt(buffer.array(), 0);
        messageData.writeInt(length);
        messageData.writeBytes(("req=" + msg.getCmd() + "&query=").getBytes(), 0, 14);
        messageData.writeBytes(msg.getContent());
        out.writeBytes(messageData, messageData.readerIndex(), messageData.readableBytes());
    }
}

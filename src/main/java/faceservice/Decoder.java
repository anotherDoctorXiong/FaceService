package faceservice;

import faceservice.model.Msg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class Decoder extends ByteToMessageDecoder {
    public void Decoder(){

    }
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.readShort();
        int length = in.readInt();
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.asIntBuffer().put(length);
        length = Num2BytesAndReverse.bytesToInt(buffer.array(), 0);
        byte[] b = new byte[14];
        in.readBytes(b, 0, 14);
        String cmdPackage = new String(b);
        String cmdStr = cmdPackage.substring(4, 7);
        byte[] content = new byte[length - 14];
        in.readBytes(content);
        Msg msg = new Msg();
        msg.setMsgLength(length - 11);
        int cmd = Integer.valueOf(cmdStr, 10).intValue();
        msg.setCmd((short)cmd);
        msg.setContent(content);
        out.add(msg);
    }
}

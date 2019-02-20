package faceservice.model;



public class Msg {
    public static final short HEADER = 1976;
    public static final short HEADER_LENGTH = 2;
    public static final Integer LENGTHFIELD_LENGTH = Integer.valueOf(4);
    public static final short CMD_LENGTH = 3;
    private int msgLength;
    private short cmd;
    private byte[] content;

    public Msg() {
    }

    public Msg(int msgLength, short cmd, byte[] content) {
        this.cmd = cmd;
        this.msgLength = msgLength;
        this.content = content;
    }

    public short getCmd() {
        return this.cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }

    public int getMsgLength() {
        return this.msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] content) {
        this.content = new byte[this.msgLength - 2 - 3];
        this.content = content;
    }

    public String toString() {
        return "Msg [msgtLength=" + this.msgLength + ", cmd=" + this.cmd + ", content=" + new String(this.content) + "]";
    }
    public String getMessage(){
        String str=new String(this.content);
        if(str.contains("ok")){
            return "0";
        }else
            return str;
    }

}


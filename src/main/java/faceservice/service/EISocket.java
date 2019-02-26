package faceservice.service;






import lombok.extern.slf4j.Slf4j;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;

import static faceservice.tools.dataUtils.getInteger;
@Slf4j
public class EISocket {
    private final Object writeLock = new Object();
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private Socket socket;
    private byte[] remoteIp;
    private int packageNo;
    private int length;
    private int cmd;
    private String data;
    private int HEADER_LEN=20;

    public EISocket(Socket socket) {
        this.socket = socket;
        setRemoteIp(this.socket.getInetAddress().getAddress());
        try {
            dataInputStream = new DataInputStream(this.socket.getInputStream());
            dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRemoteIp(byte[] ip) {
        remoteIp = ip;
    }

    public byte[] getRemoteIp() {
        return remoteIp;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(int packageNo) {
        this.packageNo = packageNo;
    }

    private void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getCmd() {
        return cmd;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private static boolean isAjbJsonHeader(byte[] data) {
        return (data[0] == (byte) 0x0a) && (data[1] == (byte) 0xb8) &&
                (data[16] == (byte) 'p') && (data[17] == (byte) 'l') && (data[18] == (byte) 'd');
    }

    public static byte[] buildJsonHeader(int cmd, int dataLen, int packageNo) {
        String header;
        header = String.format(Locale.US, "!!----!!!!%3d---pld=", cmd);
        byte[] buffer = header.getBytes();
        int length = dataLen + 14;
        buffer[0] = (byte) 0x0a;
        buffer[1] = (byte) 0xb8;
        buffer[2] = (byte) length ;
        buffer[3] = (byte) (length >>> 8);
        buffer[4] = (byte) (length >>> 16);
        buffer[5] = (byte) (length >>> 24);
        buffer[6] = (byte) packageNo;
        buffer[7] = (byte) (packageNo >>> 8);
        buffer[8] = (byte) (packageNo >>> 16);
        buffer[9] = (byte) (packageNo >>> 24);
        buffer[13] = (byte) 0x01;
        buffer[14] = (byte) 0x00;
        buffer[15] = (byte) 0x00;
        return buffer;
    }

    public boolean read() {
        //获取包头
        int receiveBytes;
        int readOffset = 0;
        byte[] headerBuf = new byte[HEADER_LEN];
        do {

            try {
                if (0 > (receiveBytes = dataInputStream.read(headerBuf, readOffset,
                        HEADER_LEN - readOffset))) {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            readOffset += receiveBytes;
        } while (readOffset < HEADER_LEN);

        //获取数据长度

        int length = getInteger(headerBuf, 2) - 14;


        if (length == 0) {
            return false;
        }
        setLength(length);
        //获取包序号
        int packageNo = getInteger(headerBuf, 6);

        setPackageNo(packageNo);
        //获取命令字
        if (0 > (cmd = cmdOf(headerBuf))) {
            return false;
        }
        setCmd(cmd);
        //获取数据
        receiveBytes = -1;
        readOffset = 0;
        byte[] readData = new byte[length];
        do {
            try {
                if (0 > (receiveBytes = dataInputStream.read(readData, readOffset,
                        length - readOffset))) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            readOffset += receiveBytes;
        } while (readOffset < length);
        setData(new String(readData));
        log.info("response data:{}",this.getData());
        return true;
    }

    public void write(byte[] data) {
        try {
            synchronized (writeLock) {
                dataOutputStream.write(data, 0, data.length);
            }
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void respond(int cmd, byte[] data, int length, int packageNo) {
        int headerLen = HEADER_LEN;
        int dataLen = length > data.length ? data.length : length;
        if (dataLen < 1024) {
            byte[] buffer = new byte[headerLen + dataLen];
            System.arraycopy(buildJsonHeader(cmd, dataLen, packageNo), 0, buffer, 0, headerLen);
            System.arraycopy(data, 0, buffer, headerLen, dataLen);
            try {
                log.info("send data:{}",new String(data));
                synchronized (writeLock) {
                    dataOutputStream.write(buffer);
                }
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.info("send data:{}",new String(data));
            try {
                synchronized (writeLock) {
                    dataOutputStream.write(buildJsonHeader(cmd, dataLen, packageNo));
                    dataOutputStream.write(data, 0, dataLen);
                }
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdownAndClose()throws IOException {
        this.socket.close();
    }

    public static int cmdOf(byte[] data) {
        try {
            return Integer.parseInt(new String(data, 10, 3));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
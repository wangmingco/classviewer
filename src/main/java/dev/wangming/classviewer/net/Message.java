package dev.wangming.classviewer.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;

public class Message {

    private static final Logger logger = LogManager.getLogger(Message.class);

    private int type;

    private String data;

    public static Message read(InputStream in) {
        try {
            int type = in.read();
            if (type == -1) {
                return null;
            }

            byte[] lengthBytes = new byte[4];
            in.read(lengthBytes);
            int length = byteArrayToInt(lengthBytes);

            logger.debug("读取消息 type:" + type + " length:" + length);

            String content = null;
            if (length != 0) {
                byte[] data = new byte[length];
                in.read(data);
                content = new String(data);
            }

            Message message = new Message();
            message.type = type;
            message.data = content;
            return message;
        } catch (Exception e) {
            logger.error("异常", e);
            return null;
        }
    }

    public static void write(OutputStream outputStream, int type, String data) {
        try {
            outputStream.write(type);
            byte[] length = null;
            if (data == null) {
                length = intToByteArray(0);
            } else {
                length = intToByteArray(data.getBytes().length);
            }

            outputStream.write(length);
            if (data != null) {
                outputStream.write(data.getBytes());
            }

            logger.debug("写入消息 type:" + type + " length:" + length);

            outputStream.flush();
        } catch (Exception e) {
            logger.error("异常", e);
        }
    }

    private static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    private static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public int getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", data='" + data + '\'' +
                '}';
    }
}

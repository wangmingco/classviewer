package dev.wangming.classviewer.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TcpClient {

    private static final Logger logger = LogManager.getLogger(Message.class);

    private static final Map<String, Socket> CLIENTS = new HashMap<>();

    public static Socket getSocket(String pid) {
        return CLIENTS.get(pid);
    }

    public static void close(String pid) {
        Socket socket = CLIENTS.get(pid);
        if (socket == null) {
            return;
        }
        try {
            logger.info("{} 关闭连接: {}", pid, socket.getRemoteSocketAddress());
            socket.close();
        } catch (IOException e) {
            logger.error("异常", e);
        }
        CLIENTS.remove(pid);
    }

    public static synchronized boolean connectServer(String pid, int port) {
        try {
            TimeUnit.SECONDS.sleep(2);
            logger.info("{} 连接开始 {}", pid, port);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", port), 1000 * 30);
            logger.info("{} 连接完成", pid);
            CLIENTS.put(pid, socket);
            return true;
        } catch (Exception e) {
            logger.error("异常", e);
            return false;
        }
    }

}

package dev.wangming.classviewer.net;

import dev.wangming.classviewer.net.handler.Handler;
import dev.wangming.classviewer.net.handler.HandlerRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TcpServer {

    private static final Logger logger = LogManager.getLogger(TcpServer.class);

    public static boolean started = false;

    public static final Map<String, ServerSocket> map = new HashMap<>();
    public static void start(int port) {

        logger.info("开始启动tcpserver: {}", port);
        try (ServerSocket serverSocket = new ServerSocket();) {

            serverSocket.bind(new InetSocketAddress(port));
            started = true;

            Consumer<ServerSocket> consumer = (ss) -> {
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };

            logger.info("tcpserver开始监听23141");
            while (true) {
                if (serverSocket.isClosed()) {
                    break;
                }
                SocketHodler socketHodler = new SocketHodler();
                try {
                    socketHodler.socket = serverSocket.accept();
                } catch (Exception e) {
                    try {
                        serverSocket.close();
                    } catch (Exception exception) {

                    }
                    break;
                }

                logger.info("接受到新的连接: {}", socketHodler.socket.getRemoteSocketAddress());

                new Thread(() -> {
                    Socket socket = socketHodler.socket;
                    while (!socket.isClosed() &&
                            !socket.isInputShutdown() &&
                            !socket.isOutputShutdown()
                    ) {

                        try {
                            socket.sendUrgentData(0xFF);
                        } catch (Exception ex) {
                            logger.error("客户端断开连接");
                            consumer.accept(serverSocket);
                            break;
                        }

                        try {
                            Message message = Message.read(socket.getInputStream());

                            if (message == null) {
                                continue;
                            }

                            Handler handler = HandlerRegister.get(message.getType());
                            handler.response(socket, message);
                        } catch (Exception e) {
                            logger.error("异常", e);
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            logger.error("异常", e);
        }
    }

    private static class SocketHodler {
        public Socket socket;
    }
}

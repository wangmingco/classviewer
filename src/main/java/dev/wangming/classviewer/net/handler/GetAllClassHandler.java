package dev.wangming.classviewer.net.handler;

import dev.wangming.classviewer.agent.AgentMain;
import dev.wangming.classviewer.net.Message;
import dev.wangming.classviewer.ui.Cache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GetAllClassHandler extends Handler {

    private static final Logger logger = LogManager.getLogger(GetAllClassHandler.class);

    @Override
    public void request(Socket socket, String data) {

        logger.info("开始请求GetAllClass");

        try {
            Message.write(socket.getOutputStream(), 1, null);
            while (true) {
                if (isClose(socket)) {
                    logger.info("socket 断开连接: {}", socket.getRemoteSocketAddress());
                    break;
                }
                Message message = Message.read(socket.getInputStream());
                if (message == null) {
                    break;
                }
                logger.debug("读取消息: {}", message);

                Cache.addClassName(message.getData());
            }

        } catch (Exception e) {
            logger.error("异常", e);
        }
    }

    @Override
    public void response(Socket socket, Message message) {

        logger.info("开始应答GetAllClass");

        Class[] classes = AgentMain.getAllLoadedClasses();
        List<String> names = Arrays.stream(classes).map(clazz -> clazz.getCanonicalName()).collect(Collectors.toList());

        logger.info("GetAllClass类名数量:{}", names.size());

        try {
            for (String name : names) {
                if (isClose(socket)) {
                    logger.info("socket 断开连接: {}", socket.getRemoteSocketAddress());
                    break;
                }
                Message.write(socket.getOutputStream(), 1, name);
                logger.debug("发送消息: {} - {}", 1, name);
            }
        } catch (Exception e) {
            logger.error("应答GetAllClass异常", e);
        }
    }
}

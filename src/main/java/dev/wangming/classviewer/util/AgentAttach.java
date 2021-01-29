package dev.wangming.classviewer.util;

import com.sun.tools.attach.VirtualMachine;
import dev.wangming.classviewer.net.TcpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class AgentAttach {

    public static final String JAR_FILE = "classviewer-1.0-jar-with-dependencies.jar";
    private static final Logger logger = LogManager.getLogger(AgentAttach.class);

    public static void attach(String pid, Consumer callback) {
        Integer port = findPort();
        new Thread(() -> {
            VirtualMachine targetVM = null;
            try {
                targetVM = VirtualMachine.attach(pid);
            } catch (Exception e) {
                logger.error("异常", e);
                return;
            }

            String path = new File(".").getAbsolutePath();
            String agentPath = path.substring(0, path.length() - 1) + JAR_FILE;
            try {
                logger.info("loadAgentPath {}, {}, {} ", pid, agentPath, new File(agentPath).exists());
                targetVM.loadAgent(agentPath, port + "");
            } catch (Exception agentLoadException) {
                logger.info("agent 加载异常: {}", agentPath, agentLoadException);
                return;
            }
        }).start();

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            logger.error("", e);
        }

        new Thread(() -> {
            boolean result = TcpClient.connectServer(pid, port);
            if (result) {
                callback.accept(null);
            }

        }).start();
    }

    private static Integer findPort() {
        for (int i = 50000; i < 60000; i++) {
            try (ServerSocket socket = new ServerSocket(i)) {
                return socket.getLocalPort();
            } catch (IOException e) {
                continue;
            }
        }
        return -1;
    }
}

package dev.wangming.classviewer.net.handler;

import dev.wangming.classviewer.net.Message;
import dev.wangming.classviewer.net.TcpClient;

import java.net.Socket;

public abstract class Handler {

    public void request(String pid, String data) {
        Socket socket = TcpClient.getSocket(pid);

        request(socket, data);
    }

    public boolean isClose(Socket socket) {
        try {
            socket.sendUrgentData(0xFF);
            return false;
        } catch (Exception ex) {
            return true;
        }
    }

    protected abstract void request(Socket socket, String data);

    public abstract void response(Socket socket, Message message);
}

package dev.wangming.classviewer.net.handler;

import dev.wangming.classviewer.net.handler.Handler;

import java.util.HashMap;
import java.util.Map;

public class HandlerRegister {

    private static final Map<Integer, Handler> HANDLERS = new HashMap<>();

    public static void register(Integer type, Handler handler) {
        HANDLERS.put(type, handler);
    }

    public static Handler get(Integer type) {
        return HANDLERS.get(type);
    }
}

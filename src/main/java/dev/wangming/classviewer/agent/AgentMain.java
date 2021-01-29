package dev.wangming.classviewer.agent;

import dev.wangming.classviewer.net.handler.HandlerRegister;
import dev.wangming.classviewer.net.TcpServer;
import dev.wangming.classviewer.net.handler.GetAllClassHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.jar.JarFile;

public class AgentMain {

    private static final Logger logger = LogManager.getLogger(AgentMain.class);

    private static Instrumentation instrumentation;

    public static void agentmain(String agentArgs, Instrumentation inst) {
        logger.info("进入代理, {}", agentArgs);

        instrumentation = inst;

        int port = Integer.parseInt(agentArgs);
        HandlerRegister.register(1, new GetAllClassHandler());

        TcpServer.start(port);
    }

    public static void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {
        throw new RuntimeException();
    }

    public static void addTransformer(ClassFileTransformer transformer) {
        throw new RuntimeException();
    }

    public static boolean removeTransformer(ClassFileTransformer transformer) {
        throw new RuntimeException();
    }

    public static boolean isRetransformClassesSupported() {
        throw new RuntimeException();
    }

    public static void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {
        throw new RuntimeException();
    }

    public static boolean isRedefineClassesSupported() {
        throw new RuntimeException();
    }

    public static void redefineClasses(ClassDefinition... definitions) throws ClassNotFoundException, UnmodifiableClassException {
        throw new RuntimeException();
    }

    public static boolean isModifiableClass(Class<?> theClass) {
        throw new RuntimeException();
    }

    public static Class[] getAllLoadedClasses() {
        return instrumentation.getAllLoadedClasses();
    }

    public static Class[] getInitiatedClasses(ClassLoader loader) {
        return instrumentation.getInitiatedClasses(loader);
    }

    public static long getObjectSize(Object objectToSize) {
        throw new RuntimeException();
    }

    public static void appendToBootstrapClassLoaderSearch(JarFile jarfile) {
        throw new RuntimeException();
    }

    public static void appendToSystemClassLoaderSearch(JarFile jarfile) {
        throw new RuntimeException();
    }

    public static boolean isNativeMethodPrefixSupported() {
        throw new RuntimeException();
    }

    public static void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {
        throw new RuntimeException();
    }
}

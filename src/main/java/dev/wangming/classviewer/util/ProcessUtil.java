package dev.wangming.classviewer.util;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import dev.wangming.classviewer.ui.Cache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ProcessUtil {

    private static final Logger logger = LogManager.getLogger(ProcessUtil.class);

    private static List<String> OLD_PROCESS = new ArrayList<>();

    public static String currentPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return pid;
    }

    public static void process() {

        new Thread(() -> {
            while (true) {
                List<VirtualMachineDescriptor> listAfter = VirtualMachine.list();
                List<String> process = listAfter.stream()
                        .map(desc -> desc.id() + " " + desc.displayName())
                        .map(desc -> desc.substring(0, desc.length() > 100 ? 100 : desc.length()))
                        .collect(Collectors.toList());

                for (String s : process) {
                    if (!OLD_PROCESS.contains(s)) {
                        Cache.addProcess(s);
                    }
                }
                for (String oldProcess : OLD_PROCESS) {
                    if (!process.contains(oldProcess)) {
                        Cache.removeProcess(oldProcess);
                    }
                }

                OLD_PROCESS = process;

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    logger.error("异常", e);
                }
            }

        }).start();

    }
}

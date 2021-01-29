package dev.wangming.classviewer.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.jvm.hotspot.memory.SystemDictionary;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.tools.Tool;
import sun.jvm.hotspot.tools.jcore.ClassFilter;
import sun.jvm.hotspot.tools.jcore.ClassWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ClassDumper extends Tool {

    private static final Logger logger = LogManager.getLogger(ClassDumper.class);

    private ClassFilter classFilter;
    private String savePath;

    public static void dump(String pid, String className, String savePath) {
        ClassDumper classDump = new ClassDumper();
        classDump.setClassFilter(instanceKlass -> instanceKlass.getName().asString().equals(className));
        classDump.savePath = savePath;
        classDump.execute(pid);
    }

    public void setClassFilter(ClassFilter cf) {
        classFilter = cf;
    }

    public void run() {
        SystemDictionary dict = VM.getVM().getSystemDictionary();
        dict.classesDo(k -> {
            if (k instanceof InstanceKlass) {
                try {
                    dumpKlass((InstanceKlass) k);
                } catch (Exception e) {
                    logger.error("dumpKlass error:{}", k.getName().asString(), e);
                }
            }
        });
    }

    private void dumpKlass(InstanceKlass kls) {
        if (classFilter != null && !classFilter.canInclude(kls)) {
            return;
        }
        String klassName = kls.getName().asString();
        try(OutputStream os = new FileOutputStream(savePath + "/" + klassName + ".class");) {
            ClassWriter cw = new ClassWriter(kls, os);
            cw.write();
        } catch (IOException exp) {
            logger.error(exp);
        }
    }

    protected void execute(String pid) {
        execute(new String[]{pid});
    }
}

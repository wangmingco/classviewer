import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import dev.wangming.classviewer.util.AgentAttach;
import dev.wangming.classviewer.util.ProcessUtil;

import java.util.List;

public class AttachMainTest {

    public static void main(String[] args) {

        String pid = ProcessUtil.currentPid();
        System.out.println("Pid is: " + pid);

        List<VirtualMachineDescriptor> listAfter = VirtualMachine.list();
        for (VirtualMachineDescriptor descriptor : listAfter) {
            if (!descriptor.displayName().equals("TestApplication")) {
                continue;
            }
            System.out.println(descriptor.displayName() + " -> " + descriptor.id());

            AgentAttach.attach(descriptor.id(), (c) -> System.out.println("ok"));
        }
    }
}

import dev.wangming.classviewer.util.ProcessUtil;

import java.util.concurrent.TimeUnit;

public class TestApplication {

    public static void main(String[] args) throws InterruptedException {

        String pid = ProcessUtil.currentPid();
        System.out.println("Pid is: " + pid);

        TimeUnit.HOURS.sleep(10);
    }

}

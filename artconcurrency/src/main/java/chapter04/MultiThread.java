package chapter04;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import java.lang.management.*;
import java.util.List;

/**
 * 6-1
 */
public class MultiThread {

    public static void main(String[] args) {
        // 获取Java线程管理MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的monitor和synchronizer信息，仅仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        // 遍历线程信息，仅打印线程ID和线程名称信息
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
        }

        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            System.out.println(memoryPoolMXBean.getName() + " ===" + memoryPoolMXBean.getCollectionUsage());
        }

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        System.out.println(heapMemoryUsage);


        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        System.out.println(operatingSystemMXBean.getArch());
        System.out.println(operatingSystemMXBean.getAvailableProcessors());
        System.out.println(operatingSystemMXBean.getSystemLoadAverage());



    }
}

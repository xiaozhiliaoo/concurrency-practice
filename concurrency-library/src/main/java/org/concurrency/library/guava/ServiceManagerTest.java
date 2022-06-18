package org.concurrency.library.guava;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lili
 * @date 2020/5/30 18:42
 * @description
 * @notes
 */
class MyExecutionThreadService extends AbstractExecutionThreadService {
    @Override
    protected void run() throws Exception {
        while (isRunning()) {
            // perform a unit of work
        }
    }
}

class MyIdleService extends AbstractIdleService {
    @Override
    protected void startUp() {
        // do something during startup
    }

    @Override
    protected void shutDown() {
        // do something during shutdown
    }
}

class MyScheduledService extends AbstractScheduledService {

    volatile Scheduler scheduler = Scheduler.newFixedDelaySchedule(0, 10, TimeUnit.MILLISECONDS);

    class MyExecutionThreadService extends AbstractExecutionThreadService {
        @Override
        protected void run() throws Exception {
            while (isRunning()) {
                // perform a unit of work
            }
        }
    }

    @Override
    protected void runOneIteration() throws Exception {
        // perform a unit of work
    }

    @Override
    protected Scheduler scheduler() {
        // Typically, you will use one of the provided schedules from AbstractScheduledService.Scheduler:
        // either newFixedRateSchedule(initialDelay, delay, TimeUnit) or newFixedDelaySchedule(initialDelay, delay, TimeUnit),
        // corresponding to the familiar methods in ScheduledExecutorService.
        // Custom schedules can be implemented using CustomScheduler; see the Javadoc for details.
        return scheduler;
    }
}

class MyService extends AbstractService {

    @Override
    protected void doStart() {
    }

    @Override
    protected void doStop() {
    }
}


public class ServiceManagerTest {

    public static void usingServiceManagerWithMultipleService() {
        List<MyExecutionThreadService> services = Lists.newArrayList();
        services.add(new MyExecutionThreadService());

        ServiceManager serviceManager = new ServiceManager(services);
        serviceManager.addListener(new ServiceManager.Listener() {
            @Override
            public void failure(Service service) {
            }

            @Override
            public void healthy() {
            }

            @Override
            public void stopped() {
            }
        });
        serviceManager.startAsync().awaitHealthy();

        serviceManager.isHealthy();  // check if all services are running
        serviceManager.servicesByState();  // return a consistent snapshot of all the services indexed by their state
        serviceManager.startupTimes(); // returns a map from Service under management to how long it took for that service to start in milliseconds.

        // do stuff

        serviceManager.stopAsync();
        serviceManager.awaitStopped();
    }


    public static void main(String[] args) {
        MyExecutionThreadService service = new MyExecutionThreadService();
        service.startAsync().awaitRunning();
        // do stuff
        service.stopAsync();
        service.awaitTerminated();
    }
}

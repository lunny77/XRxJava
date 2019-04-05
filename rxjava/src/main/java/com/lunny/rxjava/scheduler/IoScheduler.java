package com.lunny.rxjava.scheduler;

import java.util.concurrent.ExecutorService;

public class IoScheduler extends Scheduler {
    private ExecutorService executorService;

    public IoScheduler(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void schedule(Runnable runnable) {
        executorService.execute(runnable);
    }
}

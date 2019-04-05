package com.lunny.rxjava.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public final class Schedulers {

    public static final Scheduler IO;

    static {
        IO = new IoScheduler(Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName("scheduler-io-" + System.currentTimeMillis());
                return thread;
            }
        }));
    }

}

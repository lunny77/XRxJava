package com.lunny.rxjava;

import com.lunny.rxjava.dispose.Disposable;
import com.lunny.rxjava.scheduler.Scheduler;

public class ObservableSubscribeOn<T> extends AbstractObservableWithUpstream<T, T> {
    private Scheduler scheduler;

    public ObservableSubscribeOn(ObservableSource<T> source, Scheduler scheduler) {
        super(source);
        this.scheduler = scheduler;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        final SubscribeOnObserver<T> parent = new SubscribeOnObserver<>(observer);
        observer.onSubscribe(parent);
        scheduler.schedule(new SubscribeTask(parent));
    }

    final static class SubscribeOnObserver<T> implements Disposable, Observer<T> {
        private Disposable upstream;
        private Observer<? super T> downstream;
        private volatile boolean disposed;

        public SubscribeOnObserver(Observer<? super T> downstream) {
            this.downstream = downstream;
        }

        @Override
        public void onSubscribe(Disposable disposable) {
            this.upstream = disposable;
        }

        @Override
        public void onNext(T t) {
            downstream.onNext(t);
        }

        @Override
        public void onError(Throwable throwable) {
            downstream.onError(throwable);
        }

        @Override
        public void onComplete() {
            downstream.onComplete();
        }

        @Override
        public void dispose() {
            disposed = true;
            upstream.dispose();
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }
    }

    final class SubscribeTask implements Runnable {
        SubscribeOnObserver<T> parent;

        public SubscribeTask(SubscribeOnObserver<T> parent) {
            this.parent = parent;
        }

        @Override
        public void run() {
            source.subscribe(parent);
        }
    }
}

package com.lunny.rxjava;

import com.lunny.rxjava.dispose.Disposable;

import java.util.Iterator;

public class ObservableFromIterable<T> extends Observable<T> {
    private Iterable<T> iterable;

    public ObservableFromIterable(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    @Override
    public void subscribeActual(Observer observer) {
        Iterator<T> iterator = iterable.iterator();
        if (iterator.hasNext()) {
            FromIterableDisposable disposable = new FromIterableDisposable<>(observer, iterator);
            observer.onSubscribe(disposable);
            disposable.run();
        }
    }

    static class FromIterableDisposable<T> implements Disposable {
        Observer downstream;
        Iterator<T> iterator;
        boolean disposed;

        public FromIterableDisposable(Observer actual, Iterator<T> iterator) {
            this.downstream = actual;
            this.iterator = iterator;
        }

        @Override
        public void dispose() {
            disposed = true;
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

        public void run() {
            while (iterator.hasNext()) {
                if (disposed) return;
                T value = iterator.next();
                if (value == null) {
                    downstream.onError(new NullPointerException("The iterator returned a null value"));
                    return;
                }
                downstream.onNext(value);
                if (disposed) return;
            }
            downstream.onComplete();
        }
    }

}

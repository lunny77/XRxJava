package com.lunny.rxjava;

import com.lunny.rxjava.dispose.Disposable;

public class ObservableFromArray<T> extends Observable<T> {
    private T[] items;

    public ObservableFromArray(T[] items) {
        this.items = items;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        FromArrayDisposable<T> disposable = new FromArrayDisposable<>(items, observer);
        observer.onSubscribe(disposable);
        disposable.run();
    }

    static class FromArrayDisposable<T> implements Disposable {
        private T[] items;
        private Observer<? super T> downstream;
        private boolean disposed;

        public FromArrayDisposable(T[] items, Observer<? super T> actual) {
            this.items = items;
            this.downstream = actual;
        }

        @Override
        public void dispose() {
            disposed = true;
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

        void run() {
            int n = items.length;
            for (int i = 0; i < n && !disposed; i++) {
                T value = items[i];
                if (value == null) {
                    downstream.onError(new NullPointerException("The " + i + " th element is null"));
                    return;
                }
                downstream.onNext(value);
            }
            if (!disposed) {
                downstream.onComplete();
            }
        }
    }
}

package com.lunny.rxjava;

import com.lunny.rxjava.dispose.Disposable;

public class ObservableCreate<T> extends Observable<T> {
    private ObservableOnSubscribe<T> source;

    public ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        CreateEmitter<T> parent = new CreateEmitter<>(observer);
        observer.onSubscribe(parent);
        try {
            source.subscribe(parent);
        } catch (Throwable th) {
            parent.oError(th);
        }
    }

    static class CreateEmitter<T> implements ObservableEmitter<T>, Disposable {
        Observer<? super T> downstream;
        volatile boolean disposed;

        public CreateEmitter(Observer<? super T> actual) {
            this.downstream = actual;
        }

        @Override
        public void onNext(T value) {
            if (value == null) {
                tryOnError(new NullPointerException("onNext called with null. Null values are generally not allowed."));
                return;
            }
            downstream.onNext(value);
        }

        @Override
        public void oError(Throwable error) {
            tryOnError(error);
        }

        private void tryOnError(Throwable th) {
            if (!disposed) {
                downstream.onError(th);
            }
        }

        @Override
        public void onComplete() {
            if (!disposed) {
                downstream.onComplete();
                disposed = true;
            }
        }

        @Override
        public void dispose() {
            disposed = true;
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }
    }
}

package com.lunny.rxjava;

import com.lunny.rxjava.dispose.ScalarDisposable;

public class ObservableJust<T> extends Observable<T> {
    private T value;

    public ObservableJust(T value) {
        this.value = value;
    }

    @Override
    public void subscribeActual(Observer observer) {
        ScalarDisposable<T> scalarDisposable = new ScalarDisposable<>(observer, value);
        observer.onSubscribe(scalarDisposable);
        scalarDisposable.run();
    }
}

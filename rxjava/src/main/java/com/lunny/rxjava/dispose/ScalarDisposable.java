package com.lunny.rxjava.dispose;

import com.lunny.rxjava.Observer;

public class ScalarDisposable<T> implements Disposable, Runnable {
    private Observer<T> observer;
    private T value;

    private int state = 0;
    private int FUSED = 1;
    private int ON_NEXT = 2;
    private int ON_COMPLETE = 3;

    public ScalarDisposable(Observer<T> observer, T value) {
        this.observer = observer;
        this.value = value;
        this.state = FUSED;
    }

    @Override
    public void dispose() {
        this.state = ON_COMPLETE;
    }

    @Override
    public boolean isDisposed() {
        return state == ON_COMPLETE;
    }

    @Override
    public void run() {
        if (state == FUSED) {
            observer.onNext(value);
            if (state != ON_COMPLETE) {
                observer.onComplete();
            }
        }
    }
}

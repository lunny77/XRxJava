package com.lunny.rxjava;

public interface Emitter<T> {

    void onNext(T value);

    void oError(Throwable error);

    void onComplete();

}

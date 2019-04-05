package com.lunny.rxjava;

import com.lunny.rxjava.dispose.Disposable;

public interface Observer<T> {

    void onSubscribe(Disposable disposable);

    void onNext(T t);

    void onError(Throwable throwable);

    void onComplete();

}

package com.lunny.rxjava;

public interface ObservableOnSubscribe<T> {

    void subscribe(ObservableEmitter<T> emitter);

}

package com.lunny.rxjava;

import com.lunny.rxjava.functions.BiFunction;
import com.lunny.rxjava.functions.Function;

public class RxJavaPlugins {

    private static Function<Observable, Observable> onObservableAssembly;
    private static BiFunction<Observable, Observer, Observer> onObservableSubscribe;

    public static <T> Observable<T> onAssembly(Observable<T> observable) {
        if (onObservableAssembly != null) {
            return onObservableAssembly.apply(observable);
        }
        return observable;
    }

    public static <T> Observer<? super T> onSubscribe(Observable<T> observable, Observer<? super T> observer) {
        if (onObservableSubscribe != null) {
            onObservableSubscribe.apply(observable, observer);
        }
        return observer;
    }
}

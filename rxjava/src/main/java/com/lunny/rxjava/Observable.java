package com.lunny.rxjava;

import com.lunny.rxjava.functions.Function;
import com.lunny.rxjava.scheduler.Scheduler;
import com.lunny.rxjava.utils.ObjectHelper;

public abstract class Observable<T> implements ObservableSource<T> {


    public static <T> Observable<T> just(T item) {
        ObjectHelper.checkNonNull(item, "item is null");
        ObservableJust<T> observableJust = new ObservableJust<>(item);
        return RxJavaPlugins.onAssembly(observableJust);
    }

    public static <T> Observable<T> fromArray(T... items) {
        ObjectHelper.checkNonNull(items, "items is null");
        if (items.length == 0) {
            return empty();
        }
        if (items.length == 1) {
            return Observable.just(items[0]);
        }
        Observable<T> observableFromArray = new ObservableFromArray<>(items);
        return RxJavaPlugins.onAssembly(observableFromArray);
    }

    public static <T> Observable<T> fromIterable(Iterable<T> iterable) {
        ObjectHelper.checkNonNull(iterable, "iterable is null");
        ObservableFromIterable<T> observableFromIterable = new ObservableFromIterable<>(iterable);
        return RxJavaPlugins.onAssembly(observableFromIterable);
    }

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        ObjectHelper.checkNonNull(source, "source is null");
        Observable<T> observableCreate = new ObservableCreate<>(source);
        return RxJavaPlugins.onAssembly(observableCreate);
    }

    private static <T> Observable<T> empty() {
        return null;
    }

    @Override
    public void subscribe(Observer<? super T> observer) {
        ObjectHelper.checkNonNull(observer, "observer is null");
        observer = RxJavaPlugins.onSubscribe(this, observer);
        ObjectHelper.checkNonNull(observer, "The RxJavaPlugins.onSubscribe hook returned a null Observer. Please change the handler provided to RxJavaPlugins.setOnObservableSubscribe for invalid null returns.");
        subscribeActual(observer);
    }

    public <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
        ObjectHelper.checkNonNull(mapper, "mapper is null!");
        ObservableMap<T, R> observableMap = new ObservableMap<>(this, mapper);
        return RxJavaPlugins.onAssembly(observableMap);
    }

    public <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        ObjectHelper.checkNonNull(mapper, "mapper is null!");
        ObservableFlatMap<T, R> observableFlatMap = new ObservableFlatMap<>(this, mapper);
        return RxJavaPlugins.onAssembly(observableFlatMap);
    }

    public Observable<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.checkNonNull(scheduler, "scheduler is null");
        ObservableSubscribeOn<T> observableSubscribeOn = new ObservableSubscribeOn<T>(this, scheduler);
        return RxJavaPlugins.onAssembly(observableSubscribeOn);
    }

    public abstract void subscribeActual(Observer<? super T> observer);

}

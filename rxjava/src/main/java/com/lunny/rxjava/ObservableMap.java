package com.lunny.rxjava;

import com.lunny.rxjava.dispose.Disposable;
import com.lunny.rxjava.functions.Function;

public class ObservableMap<T, R> extends AbstractObservableWithUpstream<T, R> {
    private Function<? super T, ? extends R> mapper;

    public ObservableMap(ObservableSource<T> source, Function<? super T, ? extends R> mapper) {
        super(source);
        this.mapper = mapper;
    }

    @Override
    public void subscribeActual(Observer<? super R> observer) {
        source.subscribe(new MapObserver<T, R>(observer, mapper));
    }

    public static class MapObserver<T, R> implements Observer<T> {
        Observer<? super R> downstream;
        Function<? super T, ? extends R> mapper;

        public MapObserver(Observer<? super R> observer, Function<? super T, ? extends R> mapper) {
            this.downstream = observer;
            this.mapper = mapper;
        }

        @Override
        public void onSubscribe(Disposable disposable) {
            downstream.onSubscribe(disposable);
        }

        @Override
        public void onNext(T t) {
            downstream.onNext(mapper.apply(t));
        }

        @Override
        public void onError(Throwable throwable) {
            downstream.onError(throwable);
        }

        @Override
        public void onComplete() {
            downstream.onComplete();
        }
    }
}

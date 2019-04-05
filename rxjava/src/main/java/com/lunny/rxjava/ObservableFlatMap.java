package com.lunny.rxjava;

import com.lunny.rxjava.dispose.Disposable;
import com.lunny.rxjava.functions.Function;

import java.util.ArrayList;
import java.util.List;

public class ObservableFlatMap<T, R> extends AbstractObservableWithUpstream<T, R> {
    private Function<? super T, ? extends ObservableSource<? extends R>> mapper;

    public ObservableFlatMap(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        super(source);
        this.mapper = mapper;
    }

    @Override
    public void subscribeActual(Observer<? super R> observer) {
        source.subscribe(new FlatMapObserver<>(observer, mapper));
    }

    static class FlatMapObserver<T, R> implements Observer<T> {
        private Observer<? super R> downstream;
        private Function<? super T, ? extends ObservableSource<? extends R>> mapper;
        private List<ObservableSource<? extends R>> observableSourceList;

        public FlatMapObserver(Observer<? super R> downstream, Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
            this.downstream = downstream;
            this.mapper = mapper;
            observableSourceList = new ArrayList<>();
        }

        @Override
        public void onSubscribe(Disposable disposable) {
            downstream.onSubscribe(disposable);
        }

        @Override
        public void onNext(T t) {
            ObservableSource<? extends R> os = mapper.apply(t);
            observableSourceList.add(os);
        }

        @Override
        public void onError(Throwable throwable) {
            downstream.onError(throwable);
        }

        @Override
        public void onComplete() {
            for (ObservableSource<? extends R> observableSource : observableSourceList) {
                DownStreamObserver<T, R> temp = new DownStreamObserver<>(downstream);
                observableSource.subscribe(temp);
            }
            downstream.onComplete();
        }
    }

    static class DownStreamObserver<T, R> implements Observer<R> {
        private Observer<? super R> downstream;

        public DownStreamObserver(Observer<? super R> downstream) {
            this.downstream = downstream;
        }

        @Override
        public void onSubscribe(Disposable disposable) {
        }

        @Override
        public void onNext(R r) {
            downstream.onNext(r);
        }

        @Override
        public void onError(Throwable throwable) {
        }

        @Override
        public void onComplete() {
        }
    }
}

package com.lunny.rxjava;

public abstract class AbstractObservableWithUpstream<T, U> extends Observable<U>{
    protected final ObservableSource<T> source;

    public AbstractObservableWithUpstream(ObservableSource<T> source) {
        this.source = source;
    }
}

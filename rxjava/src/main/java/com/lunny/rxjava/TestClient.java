package com.lunny.rxjava;

import com.lunny.rxjava.dispose.Disposable;
import com.lunny.rxjava.functions.Function;
import com.lunny.rxjava.scheduler.Schedulers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestClient {

    public static void main(String[] args) {
//        testJust();

//        testArray();

//        testIterable();

//        testFlatMap();

//        testCreate();

//        testCreate_Map();

        testScheduler();

    }

    private static <T> void test(List<T> list) {

    }

    private static void testJust() {
        Observable.just(1).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                System.out.println("apply(" + integer + ")");
                return "test-" + integer;
            }
        }).subscribe(new TestObserver<String>());

    }

    private static void testArray() {
        Observable.fromArray(1, 3, 5, 7, 9).subscribe(new TestObserver<Integer>());
    }

    private static void testIterable() {
        List<Integer> list = Arrays.asList(2, 4, 6, 8, 10);
        Observable.fromIterable(list).subscribe(new TestObserver<Integer>());
    }


    private static void testFlatMap() {
        List<Integer> s1 = Arrays.asList(1, 3, 5, 7, 9);
        List<Integer> s2 = Arrays.asList(2, 4, 6, 8, 10);
        Observable.fromArray(s1, s2).flatMap(new Function<List<Integer>, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(List<Integer> integers) {
                return Observable.fromIterable(integers);
            }
        }).subscribe(new TestObserver<>());
    }

    private static void testCreate() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                emitter.onNext("one");
                emitter.onNext("two");
                emitter.onNext("three");
                emitter.onComplete();
            }
        }).subscribe(new TestObserver<>());
    }

    private static void testCreate_Map() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return "int: " + integer;
            }
        }).subscribe(new TestObserver<>());
    }

    private static void testScheduler() {
        Observable.just(12).subscribeOn(Schedulers.IO).subscribe(new TestObserver<>());
    }

    static class TestObserver<T> implements Observer<T> {
        @Override
        public void onSubscribe(Disposable disposable) {
            System.out.println("thread: " + Thread.currentThread() + "  onSubscribe()");
        }

        @Override
        public void onNext(T t) {
            System.out.println("thread: " + Thread.currentThread() + "  onNext(): " + t);
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("thread: " + Thread.currentThread() + "  onError(): " + throwable.getMessage());
        }

        @Override
        public void onComplete() {
            System.out.println("thread: " + Thread.currentThread() + "  onComplete()");
        }
    }
}

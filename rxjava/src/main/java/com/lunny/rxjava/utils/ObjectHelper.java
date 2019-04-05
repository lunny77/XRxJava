package com.lunny.rxjava.utils;

public class ObjectHelper {

    public static <T> T checkNonNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

}

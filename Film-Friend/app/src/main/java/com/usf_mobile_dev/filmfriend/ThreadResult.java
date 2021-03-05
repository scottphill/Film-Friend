package com.usf_mobile_dev.filmfriend;

public class ThreadResult<T> {
    private ThreadResult() {}

    public static final class Success<T> extends ThreadResult<T> {
        public T data;

        public Success(T data) {
            this.data = data;
        }
    }

    public static final class Error<T> extends ThreadResult<T> {
        public Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }
    }
}

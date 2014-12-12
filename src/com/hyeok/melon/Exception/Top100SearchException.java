package com.hyeok.melon.Exception;

public class Top100SearchException extends Exception {
    private static final long serialVersionUID = -5649056897487532479L;

    public Top100SearchException(String message) {
        super(message);
    }

    public Top100SearchException(String message, Throwable throwable) {
        super(message, throwable);
    }

}

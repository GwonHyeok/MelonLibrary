package com.hyeok.melon.Exception;

/**
 * Created by GwonHyeok on 15. 1. 3..
 */
public class GetSongDataException extends Exception {
    public GetSongDataException(String message) {
        super(message);
    }

    public GetSongDataException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

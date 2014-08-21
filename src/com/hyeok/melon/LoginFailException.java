package com.hyeok.melon;

public class LoginFailException extends Exception {
	private static final long serialVersionUID = -5573288175005421922L;

	public LoginFailException(String message) {
		super(message);
	}

	public LoginFailException(String message, Throwable throwable) {
		super(message, throwable);
	}
}

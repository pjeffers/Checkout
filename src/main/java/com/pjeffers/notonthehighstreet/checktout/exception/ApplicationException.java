package com.pjeffers.notonthehighstreet.checktout.exception;


public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(String message, Exception cause) {
		super(message, cause);
	}

}

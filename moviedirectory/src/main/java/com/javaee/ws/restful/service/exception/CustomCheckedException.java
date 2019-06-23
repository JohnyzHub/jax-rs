package com.javaee.ws.restful.service.exception;

/**
 * @author johnybasha
 *
 */
public class CustomCheckedException extends Exception {

	private static final long serialVersionUID = 1L;

	public CustomCheckedException(String message) {
		super(message);
	}

	public CustomCheckedException(Throwable cause) {
		super(cause);
	}

	public CustomCheckedException(String message, Throwable cause) {
		super(message, cause);
	}

}

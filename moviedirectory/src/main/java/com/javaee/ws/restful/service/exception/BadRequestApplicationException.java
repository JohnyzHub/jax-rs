package com.javaee.ws.restful.service.exception;

/**
 * @author johnybasha
 *
 */
public class BadRequestApplicationException extends Exception {

	private static final long serialVersionUID = 1L;

	public BadRequestApplicationException(String message) {
		super(message);
	}

	public BadRequestApplicationException(Throwable cause) {
		super(cause);
	}

	public BadRequestApplicationException(String message, Throwable cause) {
		super(message, cause);
	}
}

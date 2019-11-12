package com.javaee.ws.restful.service.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author johnybasha
 *
 */
public class BadRequestWebApplicationException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public BadRequestWebApplicationException() {
		super(Response.status(Status.BAD_REQUEST).build());
	}

	public BadRequestWebApplicationException(String message) {
		super(Response.status(Status.BAD_REQUEST).entity(message).build());
	}
}

/**
 * 
 */
package com.javaee.ws.restful.service.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author johnybasha
 *
 */
public class CustomException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public CustomException() {
		super(Response.status(Status.BAD_REQUEST).build());
	}

	public CustomException(String message) {
		super(Response.status(Status.BAD_REQUEST).entity(message).build());
	}
}

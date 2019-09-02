/**
 * 
 */
package com.javaee.ws.restful.service.exception;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author johnybasha
 *
 */
@Provider
public class ArithmenticExceptionMapper implements ExceptionMapper<ArithmeticException> {

	@Override
	public Response toResponse(ArithmeticException exception) {
		Map<String, String> response = new HashMap<String, String>();
		response.put("code", "ERR-????");
		response.put("type", "General Error");
		response.put("message", exception.getMessage());

		return Response.status(Status.BAD_REQUEST).entity(response).type(MediaType.APPLICATION_JSON).build();
	}

}

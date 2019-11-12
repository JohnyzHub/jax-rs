package com.javaee.ws.restful.service.exception;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author johnybasha
 *
 */
public class MovieExceptionCases {

	public MovieExceptionCases() {

	}

	@GET
	@Path("webappex")
	public Response raiseWebApplicationException() {
		throw new BadRequestWebApplicationException("Insufficient Details");
	}

	@GET
	@Path("appex")
	public Response raiseApplicationException() throws BadRequestApplicationException {
		throw new BadRequestApplicationException("Incomplete request");
	}

	@GET
	@Path("mapped/{id}")
	public Response raiseArthmeticException(@PathParam("id") int id) {
		int x = 100 * (1 / id);
		return Response.ok(x).build();
	}

	@GET
	public Response test() {
		return Response.ok("MovieExceptionCases:: Subresource identified").build();
	}

}

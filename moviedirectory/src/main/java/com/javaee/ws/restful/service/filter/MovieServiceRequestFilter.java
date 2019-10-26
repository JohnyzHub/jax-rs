package com.javaee.ws.restful.service.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

/**
 * @author johnybasha
 *
 */
@Provider
@PreMatching
public class MovieServiceRequestFilter implements ContainerRequestFilter {

	private String lOCATIONString = "BERMUDA";

	public MovieServiceRequestFilter() {
	}

	/**
	 * This method will be triggered after the jax-rs container identifying the
	 * matching resource for the request by deault. If the class is annotated
	 * with @PreMatching, the filter will be triggered by the jax-rs container
	 * before identifying the matching resource.
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String locationString = requestContext.getHeaderString(HttpHeaders.LOCATION);
		if (locationString != null && locationString.equalsIgnoreCase(lOCATIONString)) {
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Access Unauthorized").build());
		}
	}
}

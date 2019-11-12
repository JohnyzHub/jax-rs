package com.javaee.ws.restful.service.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

/**
 * @author johnybasha
 *
 */
@Provider
public class MovieServiceResponseFilter implements ContainerResponseFilter {

	private String locationString = "USA";

	public MovieServiceResponseFilter() {
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {

		String headerString = requestContext.getHeaderString(HttpHeaders.LOCATION);
		if (headerString == null || headerString.equalsIgnoreCase(locationString)) {
			responseContext.getHeaders().add(HttpHeaders.CONTENT_LANGUAGE, "ENGLISH");
		}
	}
}

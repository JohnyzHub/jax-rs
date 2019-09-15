package com.javaee.ws.restful.client.filter;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author johnybasha
 *
 */
public class MovieResourceClientRequestFilter implements ClientRequestFilter {

	public MovieResourceClientRequestFilter() {
	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		String languageString = requestContext.getHeaderString(HttpHeaders.CONTENT_LANGUAGE);
		if (!(languageString == null || languageString.equalsIgnoreCase("ENGLISH"))) {
			requestContext.abortWith(Response.status(Status.BAD_REQUEST).build());
		}

	}

}

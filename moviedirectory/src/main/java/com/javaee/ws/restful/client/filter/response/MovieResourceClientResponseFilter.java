package com.javaee.ws.restful.client.filter.response;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Response.Status;

/**
 * @author johnybasha
 *
 */
public class MovieResourceClientResponseFilter implements ClientResponseFilter {

	public MovieResourceClientResponseFilter() {
	}

	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		if (requestContext.getUri().getPath().contains("movie") 
				&& responseContext.getStatusInfo().getStatusCode() != Status.OK.getStatusCode()) {
			System.out.println(
					"Request: Http Method: " + requestContext.getMethod());
			
			System.out.println(
					"Response:\n"+
					"Status: " + responseContext.getStatus() +
					", Reason: " + responseContext.getStatusInfo().getReasonPhrase() +
					", Headers: " + responseContext.getHeaders());
		}
	}
}
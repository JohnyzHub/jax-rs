package com.javaee.ws.restful.client.filter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.javaee.ws.restful.client.filter.request.MovieResourceClientRequestFilter;
import com.javaee.ws.restful.client.filter.response.MovieResourceClientResponseFilter;

/**
 * @author johnybasha
 *
 */
public class MovieResourceFilterClient {

	private static final String BASE_URI = "http://localhost:8080/moviedirectory/rest/directory/movies";

	public static void main(String[] args) {
		MovieResourceFilterClient client = new MovieResourceFilterClient();
		client.test_movie_service_Filters();
	}

	public void test_movie_service_Filters() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.register(MovieResourceClientRequestFilter.class)
				.register(MovieResourceClientResponseFilter.class).target(BASE_URI);

		Response response = null;

		System.out.println("***** Testing without header *****");
		response = webTarget.queryParam("number", 2).request().get();
		System.out.println("Response::\n" + response.getStatusInfo().getReasonPhrase() + ":: "
				+ response.readEntity(String.class));
		System.out.println("Content-Language::" + response.getHeaderString(HttpHeaders.CONTENT_LANGUAGE));

		MultivaluedMap<String, Object> headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.LOCATION, "USA");
		headersMap.add(HttpHeaders.CONTENT_LANGUAGE, "ENGLISH");

		System.out.println("\n***** Testing with valid header *****");
		System.out.println("Request Headers: " + headersMap);
		response = webTarget.queryParam("number", 2).request().headers(headersMap).get();
		System.out.println("Response::\n" + response.getStatusInfo().getReasonPhrase() + ":: "
				+ response.readEntity(String.class));
		System.out.println("Content-Language::" + response.getHeaderString(HttpHeaders.CONTENT_LANGUAGE));

		System.out.println("\n***** Testing with invalid header *****");
		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.CONTENT_LANGUAGE, "ENGLISH");
		headersMap.add(HttpHeaders.LOCATION, "BERMUDA");
		response = webTarget.request().headers(headersMap).get();
		System.out.println("Error: " + response.readEntity(String.class));
	}
}
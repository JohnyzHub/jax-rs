package com.javaee.ws.restful.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @author johnybasha
 *
 */
public class MovieExceptionsClient {

	private static final String BASE_URI = "http://localhost:8080/moviedirectory/rest/directory/exceptions";

	public MovieExceptionsClient() {
	}

	public static void main(String[] args) {
		MovieExceptionsClient client = new MovieExceptionsClient();
		// client.testException();
		// client.testWebApplicationException();
		// client.testMappedException();
		client.test();
	}

	public void test() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI);
		Response response = webTarget.request().get();
		System.out.println(response.readEntity(String.class));
	}

	public void testApplicationException() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("appex");
		Response response = webTarget.request().get();
		System.out.println(response);
	}

	public void testWebApplicationException() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("webappex");
		Response response = webTarget.request().get();
		System.out.println(response);
	}

	public void testMappedException() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("mapped/0");
		Response response = webTarget.request().get();
		System.out.println(response.readEntity(String.class));
	}

}

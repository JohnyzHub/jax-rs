package com.javaee.ws.restful.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @author johnybasha
 *
 */
public class MovieSubResourceClient {

	private static final String BASE_URI = "http://localhost:8080/moviedirectory/rest/directory/inventory";

	public MovieSubResourceClient() {
	}

	public static void main(String[] args) {
		MovieSubResourceClient client = new MovieSubResourceClient();
		client.testSubResource_get_artist();
		client.testSubResource_get_technician();

		/*-
		 * client.testSubResource_get_technician();
		 * client.testSubResource_get_technician_all();
		 */
	}

	public void testSubResource_get_artist() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("artist");

		Response response = webTarget.queryParam("name", "Artist1").request().get();

		String uriString = webTarget.getUri().toString();
		System.out.println("\n" + uriString + ":: is being processed ::\n" + response.readEntity(String.class));
	}

	public void testSubResource_get_artist_all() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("artist").path("all");

		Response response = webTarget.request().get();

		String uriString = webTarget.getUri().toString();
		System.out.println("\n" + uriString + ":: is being processed ::\n" + response.readEntity(String.class));
	}

	public void testSubResource_get_technician() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("technician");

		Response response = webTarget.queryParam("name", "Technician1").request().get();

		String uriString = webTarget.getUri().toString();
		System.out.println("\n" + uriString + ":: is being processed ::\n" + response.readEntity(String.class));
	}

	public void testSubResource_get_technician_all() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("technician").path("all");

		Response response = webTarget.request().get();

		String uriString = webTarget.getUri().toString();
		System.out.println("\n" + uriString + ":: is being processed ::\n" + response.readEntity(String.class));
	}
}

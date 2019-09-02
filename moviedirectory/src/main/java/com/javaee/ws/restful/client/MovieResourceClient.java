package com.javaee.ws.restful.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.javaee.ws.restful.service.entity.Movie;
import com.javaee.ws.restful.service.entityprovider.CSVMessageBodyReaderWriter;
import com.javaee.ws.restful.service.entityprovider.XMLMessageBodyReaderWriter;

/**
 * @author johnybasha
 *
 */
public class MovieResourceClient {

	private static final String BASE_URI = "http://localhost:8080/moviedirectory/rest/directory";

	public MovieResourceClient() {
	}

	public static void main(String[] args) throws Exception {
		MovieResourceClient client = new MovieResourceClient();

		client.findMovie();
		client.updateMovie();
		client.findAllMovies();

	}

	public void findMovie() throws Exception {
		Client client = ClientBuilder.newBuilder().register(new XMLMessageBodyReaderWriter()).build();
		WebTarget webTarget = client.target(BASE_URI).path("movie");

		Movie movie = webTarget.request(MediaType.APPLICATION_XML).get(new GenericType<Movie>() {
		});

		String uriString = webTarget.getUri().toString();
		System.out.println("\n" + uriString + ":: is being processed synchronously::\n" + movie);
	}

	public void updateMovie() throws Exception {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("movie");
		Movie movie = new Movie(1, "movie1", 10);
		Response updatedMovieResponse = webTarget.request().put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("\nMovie entry is Updated: " + updatedMovieResponse.readEntity(String.class));
	}

	public void findAllMovies() throws Exception {
		Client client = ClientBuilder.newBuilder().register(CSVMessageBodyReaderWriter.class).build();
		WebTarget webTarget = client.target(BASE_URI);
		List<Movie> movies = webTarget.request("application/csv").get(new GenericType<List<Movie>>() {
		});

		String uriString = webTarget.getUri().toString();
		System.out.println("\n" + uriString + ":: is being processed synchronously::\n" + movies);
	}
}

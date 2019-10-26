package com.javaee.ws.restful.client;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.javaee.ws.restful.service.entity.Movie;
import com.javaee.ws.restful.service.entityprovider.CSVMessageBodyReaderWriter;
import com.javaee.ws.restful.service.entityprovider.YamlMessageBodyReaderWriter;

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
		client.findAllMovies_yaml();
		client.findAllMovies_csv();
		/*-
		client.findAllMovies();
		client.findAllMovies_yaml();
		client.findAllMovies_csv();
		client.findMovie();
		
		client.createMovie();
		client.findAllMovies(); 
		client.updateMovie();
		client.findAllMovies();
		*/

	}

	public void findMovie() throws Exception {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("movie");

		Movie movie = webTarget.request().get(new GenericType<Movie>() {
		});

		String uriString = webTarget.getUri().toString();
		System.out.println("\n" + uriString + ":: is being processed synchronously::\n" + movie);
	}

	public void createMovie() throws Exception {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("movie");

		LocalDate localDate = LocalDate.of(2019, Month.SEPTEMBER, 10);
		LocalTime localTime = LocalTime.of(05, 20, 30);
		LocalDateTime lastModifiedDate = LocalDateTime.of(localDate, localTime);

		Movie movie = new Movie(3, "IronMan", 40, lastModifiedDate);
		Response createdMovieResponse = webTarget.request().post(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("\nMovie entry is Updated: " + createdMovieResponse.readEntity(String.class));
	}

	public void updateMovie() throws Exception {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("movie");

		LocalDate localDate = LocalDate.of(2019, Month.SEPTEMBER, 20);
		LocalTime localTime = LocalTime.of(10, 20, 30);
		LocalDateTime lastModifiedDate = LocalDateTime.of(localDate, localTime);

		Movie movie = new Movie(1, "Avengers", 10, lastModifiedDate);
		Response updatedMovieResponse = webTarget.request().put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("\nMovie entry is Updated: " + updatedMovieResponse.readEntity(String.class));
	}

	public void findAllMovies() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI);

		Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
		String uriString = webTarget.getUri().toString();
		System.out.println("\n" + uriString + "::\n" + response.readEntity(String.class));
	}

	public void findAllMovies_yaml() {
		Client client = ClientBuilder.newBuilder().register(YamlMessageBodyReaderWriter.class).build();
		WebTarget webTarget = client.target(BASE_URI).path("yaml");

		Response response = webTarget.request("application/yaml").get();
		String uriString = webTarget.getUri().toString();
		System.out.println("\n" + uriString + "::\n" + response.readEntity(String.class));
	}

	public void findAllMovies_csv() {
		Client client = ClientBuilder.newBuilder().register(CSVMessageBodyReaderWriter.class).build();
		WebTarget webTarget = client.target(BASE_URI).path("csv");

		Response response = webTarget.request("application/csv").get();
		String uriString = webTarget.getUri().toString();
		System.out.println("\n" + uriString + "::\n" + response.readEntity(String.class));
	}

}

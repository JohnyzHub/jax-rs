package com.javaee.ws.restful.client;

import java.util.List;
import java.util.concurrent.Future;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;

import com.javaee.ws.restful.service.entity.Movie;
import com.javaee.ws.restful.service.entityprovider.CSVMessageBodyReaderWriter;
import com.javaee.ws.restful.service.entityprovider.XMLMessageBodyReaderWriter;

/**
 * @author johnybasha
 *
 */
public class MovieAsyncClient {

	private static final String BASE_URI = "http://localhost:8080/moviedirectory/rest/directory";

	public MovieAsyncClient() {
	}

	public static void main(String[] args) throws Exception {
		MovieAsyncClient client = new MovieAsyncClient();
		client.findAllMoviesInAsync();
		client.findMovie();

	}

	public void findMovie() throws Exception {
		Client client = ClientBuilder.newBuilder().register(new XMLMessageBodyReaderWriter()).build();
		WebTarget webTarget = client.target(BASE_URI).path("async").path("movie");

		AsyncInvoker asyncInvoker = webTarget.request("application/xml").async();

		String uriString = webTarget.getUri().toString();
		System.out.println(uriString + ":: is being processed asynchronously");

		Future<Movie> responseFuture = asyncInvoker.get(new InvocationCallback<Movie>() {

			@Override
			public void completed(Movie response) {
				System.out.println("\n" + uriString + "::Response:\n" + response);
				client.close();

			}

			@Override
			public void failed(Throwable throwable) {
				System.out.println("Invocation Failed");
				throwable.printStackTrace();
			}
		});
	}

	public void findAllMoviesInAsync() throws Exception {
		Client client = ClientBuilder.newBuilder().register(CSVMessageBodyReaderWriter.class).build();
		WebTarget webTarget = client.target(BASE_URI).path("async").path("all");
		AsyncInvoker asyncInvoker = webTarget.request("application/csv").async();

		String uriString = webTarget.getUri().toString();
		System.out.println(uriString + ":: is being processed asynchronously");

		Future<List<Movie>> responseFuture = asyncInvoker.get(new InvocationCallback<List<Movie>>() {

			@Override
			public void completed(List<Movie> response) {
				System.out.println("\n" + uriString + "::Response::");
				response.forEach(System.out::println);
				client.close();

			}

			@Override
			public void failed(Throwable throwable) {
				System.out.println("Invocation Failed");
				throwable.printStackTrace();
			}
		});
	}
}
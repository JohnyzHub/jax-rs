package com.javaee.ws.restful.service.async;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.javaee.ws.restful.service.entity.Movie;

/**
 * @author johnybasha
 *
 */
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/yaml" })
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/yaml" })
@Path("directory/async")
public class MovieAsyncService {

	List<Movie> movies;

	public MovieAsyncService() {
		Movie movie1 = new Movie(1, "Titanic", 35);
		Movie movie2 = new Movie(2, "Matrix", 40);
		movies = new ArrayList<>(Arrays.asList(movie1, movie2));
	}

	@GET
	@Path("all")
	@Produces("application/yaml")
	public void findAllMovies(@Suspended final AsyncResponse asyncResponse) {
		TimeoutHandler timeoutHandler = new MovieTimeoutHandler(asyncResponse);
		asyncResponse.setTimeout(5, TimeUnit.SECONDS);
		asyncResponse.setTimeoutHandler(timeoutHandler);

		ExecutorService executorService = Executors.newFixedThreadPool(4);
		Runnable task = () -> {
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			GenericEntity<List<Movie>> entity = new GenericEntity<List<Movie>>(movies) {
			};
			asyncResponse.resume(Response.ok().entity(entity).build());
		};
		executorService.submit(task);
	}

	@GET
	@Path("movie")
	public void findMovie(@Suspended final AsyncResponse asyncResponse) {
		TimeoutHandler timeoutHandler = new MovieTimeoutHandler(asyncResponse);
		asyncResponse.setTimeout(5, TimeUnit.SECONDS);
		asyncResponse.setTimeoutHandler(timeoutHandler);

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Runnable task = () -> {
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			asyncResponse.resume(Response.ok().entity(new Movie(3, "Holliday", 100)).build());
		};
		executorService.submit(task);
	}
}

class MovieTimeoutHandler implements TimeoutHandler {

	private AsyncResponse asyncResponse;

	MovieTimeoutHandler(AsyncResponse asyncResponse) {
		this.asyncResponse = asyncResponse;
	}

	@Override
	public void handleTimeout(AsyncResponse asyncResponse) {
		asyncResponse.resume(Response.status(Status.SERVICE_UNAVAILABLE).entity("Operation time out.").build());
	}

}

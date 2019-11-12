package com.javaee.ws.restful.service.async;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
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

@Path("directory/async")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovieAsyncService {

	List<Movie> movies;

	public MovieAsyncService() {
		LocalDate localDate = LocalDate.of(2019, Month.JULY, 20);
		LocalTime localTime = LocalTime.of(10, 20, 30);
		LocalDateTime lastModifiedDate = LocalDateTime.of(localDate, localTime);

		Movie movie1 = new Movie(1, "Titanic", 35, lastModifiedDate);
		Movie movie2 = new Movie(2, "Matrix", 40, lastModifiedDate);
		movies = new ArrayList<>(Arrays.asList(movie1, movie2));
	}

	@GET
	@Path("all")
	public void findAllMovies(@Suspended final AsyncResponse asyncResponse) {
		asyncResponse.register(new MovieConnectionCallback(), new MovieCompletionCallback());
		TimeoutHandler timeoutHandler = new MovieTimeoutHandler(asyncResponse);
		asyncResponse.setTimeout(5, TimeUnit.SECONDS);
		asyncResponse.setTimeoutHandler(timeoutHandler);

		ExecutorService executorService = Executors.newFixedThreadPool(4);
		Runnable task = () -> {
			sleepingThread(3);
			GenericEntity<List<Movie>> entity = new GenericEntity<List<Movie>>(movies) {
			};
			asyncResponse.resume(Response.ok().entity(entity).build());
		};
		executorService.submit(task);
	}

	@GET
	@Path("movie")
	public void findMovie(@Suspended final AsyncResponse asyncResponse,
			@DefaultValue("1") @QueryParam("number") int number) {
		asyncResponse.register(new MovieConnectionCallback(), new MovieCompletionCallback());
		TimeoutHandler timeoutHandler = new MovieTimeoutHandler(asyncResponse);
		asyncResponse.setTimeout(5, TimeUnit.SECONDS);
		asyncResponse.setTimeoutHandler(timeoutHandler);

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Runnable task = () -> {
			sleepingThread(7);
			Optional<Movie> MovieOptional = movies.stream().filter(m -> m.getNumber() == number).findFirst();
			Movie movie = new Movie();
			if (MovieOptional.isPresent()) {
				movie = MovieOptional.get();
			}
			asyncResponse.resume(Response.ok().entity(movie).build());
		};
		executorService.submit(task);
	}

	private void sleepingThread(int delay) {
		try {
			TimeUnit.SECONDS.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class MovieTimeoutHandler implements TimeoutHandler {

	private AsyncResponse asyncResponse;

	MovieTimeoutHandler(AsyncResponse asyncResponse) {
		this.asyncResponse = asyncResponse;
	}

	@Override
	public void handleTimeout(AsyncResponse asyncResponse) {
		asyncResponse.resume(Response.status(Status.REQUEST_TIMEOUT).entity("Operation time out.").build());
	}

}

class MovieCompletionCallback implements CompletionCallback {

	@Override
	public void onComplete(Throwable throwable) {
		if (throwable != null) {
			System.out.println(throwable.getMessage());
		} else {
			System.out.println("MovieCompletionCallback:: Async process finished successfully..");
		}
	}
}

class MovieConnectionCallback implements ConnectionCallback {

	@Override
	public void onDisconnect(AsyncResponse disconnected) {
		System.out.println("MovieConnectionCallback:: Async process connection disconnected ..");

	}
}

package com.javaee.ws.restful.service.cached;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.javaee.ws.restful.service.entity.Movie;
import com.javaee.ws.restful.service.entity.Person;

/**
 * @author johnybasha
 *
 */
@ApplicationScoped
@Consumes({ "application/csv", MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ "application/csv", MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Path("directory/cache")
public class MovieCachedService {

	List<Movie> movies = null;
	Person person = null;

	public MovieCachedService() {
		LocalDate localDate = LocalDate.of(2019, Month.JULY, 20);
		Date lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		Movie movie1 = new Movie(1, "Titanic", 30);
		movie1.setLastModifieDate(lastModified);
		Movie movie2 = new Movie(2, "Matrix", 60);
		movie2.setLastModifieDate(lastModified);
		movies = new ArrayList<>(Arrays.asList(movie1, movie2));
		person = new Person("Person1");
		for (Movie movie : movies) {
			person.addMovie(movie.getTitle());
		}
		System.out.println("Inside Constructor: " + movies);
	}

	@GET
	public Response listMovies() {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(5 * 60);
		cacheControl.setPrivate(true);

		return Response.ok(movies).lastModified(new Date()).cacheControl(cacheControl).build();
	}

	@GET
	@Path("movie/{id}")
	public Response listMovies(@Min(1) @Max(2) @PathParam("id") int id) {

		Movie movie = movies.get(id - 1);
		EntityTag entityTag = new EntityTag(Integer.toString(movie.hashCode()));
		Date lastModifieDate = movie.getLastModifieDate();

		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(5 * 60);
		cacheControl.setPrivate(true);
		System.out.println("Inside movie/{id}:" + movie);
		return Response.ok(movie).tag(entityTag).lastModified(lastModifieDate).cacheControl(cacheControl).build();
	}

	@GET
	@Path("modified/movie")
	public Response getMovie(@Min(1) @Max(2) @QueryParam("id") int movieId, @Context Request request) {

		Movie movie = movies.get(movieId - 1);
		Date lastModifieDate = null;
		if (movie != null) {
			lastModifieDate = movie.getLastModifieDate();
		}

		if (lastModifieDate == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		ResponseBuilder responseBuilder = request.evaluatePreconditions(lastModifieDate);
		System.out.println("ResponseBuilder: " + responseBuilder);
		if (responseBuilder == null) { // modified date changed, send the latest entity and date.
			CacheControl cacheControl = new CacheControl();
			cacheControl.setMaxAge(5 * 60);
			cacheControl.setPrivate(true);
			responseBuilder = Response.ok(movie).lastModified(lastModifieDate).cacheControl(cacheControl);
		}
		return responseBuilder.build();
	}

	@GET
	@Path("unmodified/movie/{title}")
	public Response getMovie(@NotNull @PathParam("title") String name, @Context Request request) {
		Date lastModifiedDateDate = null;

		Optional<Movie> optionalMovie = movies.stream().filter(movie -> movie.getTitle().equalsIgnoreCase(name))
				.findFirst();

		Movie resultedMovie = null;
		if (optionalMovie.isPresent()) {
			resultedMovie = optionalMovie.get();
			lastModifiedDateDate = resultedMovie.getLastModifieDate();
		}
		if (lastModifiedDateDate == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		ResponseBuilder responseBuilder = request.evaluatePreconditions(lastModifiedDateDate);

		if (responseBuilder == null) {
			CacheControl cacheControl = new CacheControl();
			cacheControl.setMaxAge(5 * 60);
			cacheControl.setPrivate(true);
			responseBuilder = Response.ok(resultedMovie).lastModified(lastModifiedDateDate).cacheControl(cacheControl);
		}
		return responseBuilder.build();
	}

	@PUT
	@Path("unmodified/movie/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMovie_unmodified(@NotNull @PathParam("title") String name, Movie movie,
			@Context Request request) {
		Date lastModifiedDateDate = new Date();

		Optional<Movie> optionalMovie = movies.stream().filter(m -> m.getTitle().equalsIgnoreCase(name)).findFirst();

		Movie resultedMovie = null;
		if (optionalMovie.isPresent()) {
			resultedMovie = optionalMovie.get();
			lastModifiedDateDate = resultedMovie.getLastModifieDate();
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}

		ResponseBuilder responseBuilder = request.evaluatePreconditions(lastModifiedDateDate);
		System.out.println("ResponseBuilder: " + responseBuilder);

		if (responseBuilder == null) { // modified date not changed.
			responseBuilder = Response.status(Status.NOT_MODIFIED);
		} else { // modified date changed, send the latest entity and date.
			CacheControl cacheControl = new CacheControl();
			cacheControl.setMaxAge(5 * 60);
			cacheControl.setPrivate(true);
			resultedMovie.setPrice(movie.getPrice());
			responseBuilder = Response.ok(resultedMovie).lastModified(lastModifiedDateDate).cacheControl(cacheControl);
		}
		return responseBuilder.build();
	}

	@GET
	@Path("etag/person")
	public Response getActorInfo(@Context Request request) {
		String hashCodeString = Integer.toString(person.hashCode());
		EntityTag entityTag = new EntityTag(hashCodeString);
		ResponseBuilder responseBuilder = request.evaluatePreconditions(entityTag);

		if (responseBuilder == null) {
			responseBuilder = Response.ok(person).tag(entityTag);
		}
		return responseBuilder.build();
	}

	@PUT
	@Path("etag/modifieddate/movie")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMovie(@NotNull Movie movie, @Context Request request) {
		Date lastModifiedDateDate = null;

		Optional<Movie> optionalMovie = movies.stream().filter(m -> m.getTitle().equalsIgnoreCase(movie.getTitle()))
				.findFirst();

		Movie resultedMovie = null;
		if (optionalMovie.isPresent()) {
			resultedMovie = optionalMovie.get();
			lastModifiedDateDate = resultedMovie.getLastModifieDate();
		}

		if (lastModifiedDateDate == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		EntityTag entityTag = new EntityTag(Integer.toString(resultedMovie.hashCode()));

		ResponseBuilder responseBuilder = request.evaluatePreconditions(lastModifiedDateDate, entityTag);

		if (responseBuilder == null) { // Either etag or last modified date condition not met.
			resultedMovie.setPrice(movie.getPrice());
			entityTag = new EntityTag(Integer.toString(resultedMovie.hashCode()));
			responseBuilder = Response.ok(resultedMovie).tag(entityTag).lastModified(lastModifiedDateDate);
		}
		return responseBuilder.build();
	}

}
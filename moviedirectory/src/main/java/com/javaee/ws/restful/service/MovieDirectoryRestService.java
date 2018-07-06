package com.javaee.ws.restful.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.javaee.ws.restful.service.exception.CustomException;

/**
 * @author shaikjb
 *
 */
@ApplicationScoped
@Path("directoryservice")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class MovieDirectoryRestService {

	private static Map<Integer, Movie> movies;
	static {
		movies = new HashMap<Integer, Movie>(0);
		movies.put(1, new Movie("Movie1", 1));
		movies.put(2, new Movie("Movie2", 2));
	}

	@GET
	public Collection<Movie> listMovies() {
		return movies.values();
	}

	@GET
	@Path("movie")
	public Movie getMovie(@DefaultValue("1") @QueryParam("number") int number) {
		if (movies.containsKey(number)) {
			return movies.get(number);
		}
		return new Movie();
	}

	@PUT
	@Path("movie")
	public Response updateMovie(Movie movie) {
		Status status = Status.ACCEPTED;
		int number = movie.getNumber();
		if (movies.containsKey(number)) {
			movies.put(number, movie);
		} else {
			status = Status.NOT_MODIFIED;
			throw new CustomException(status.toString());
		}
		return Response.status(status).build();
	}

	@POST
	@Path("movie")
	public Response createMovie(Movie movie) {
		Status status = Status.CREATED;
		if (movies.containsKey(movie.getNumber())) {
			status = Status.NOT_ACCEPTABLE;
		} else {
			movies.put(movie.getNumber(), movie);
		}
		return Response.status(status).build();
	}

	@GET
	@Path("price/{discount}")
	public int getTicketPrice(@PathParam("discount") int discount) {
		return 50 / discount;

	}
}
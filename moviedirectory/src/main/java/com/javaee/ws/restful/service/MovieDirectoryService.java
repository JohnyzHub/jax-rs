package com.javaee.ws.restful.service;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import com.javaee.ws.restful.service.subresource.ArtistInventory;
import com.javaee.ws.restful.service.subresource.Inventory;
import com.javaee.ws.restful.service.subresource.TechnicianInventory;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shaikjb
 *
 */

@ApplicationScoped
@Path("directory")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/csv" })
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/csv" })
public class MovieDirectoryService implements DiscountService {

	private static Map<Integer, Movie> movies;
	static {
		movies = new HashMap<>(2);
		movies.put(1, new Movie(1, "Movie1"));
		movies.put(2, new Movie(2, "Movie2"));
	}

	@GET
	public Response listMovies() {
		List<Movie> movieList = movies.values().stream().collect(Collectors.toList());
		return Response.ok(movieList).status(Status.FOUND).type("Application/csv").build();
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

	@DELETE
	@Path("movie/{number}")
	public Response deleteMovie(@PathParam("number") int number) {
		Status status = Status.NOT_FOUND;
		Movie result = null;
		if (movies.containsKey(number)) {
			result = movies.remove(number);
			status = Status.ACCEPTED;
		}
		return Response.accepted(result).status(status).build();
	}

	@Override
	public int getTicketPrice(int discount) {
		return 50 / discount;
	}

	@Path("inventory/{person}")
	public Class<? extends Inventory> findArtist(@PathParam("person") String person) {
		if (person.equalsIgnoreCase("artist")) {
			return ArtistInventory.class;
		} else {
			return TechnicianInventory.class;
		}
	}
}
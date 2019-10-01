package com.javaee.ws.restful.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.javaee.ws.restful.service.entity.Movie;
import com.javaee.ws.restful.service.exception.CustomException;
import com.javaee.ws.restful.service.subresource.ArtistInventory;
import com.javaee.ws.restful.service.subresource.Inventory;
import com.javaee.ws.restful.service.subresource.TechnicianInventory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author shaikjb
 *
 */

@ApplicationScoped
@Path("directory")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/yaml" })
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/yaml" })
@Api(value = "directory", consumes = "application/json", produces = "Application/Json")
public class MovieDirectoryService implements DiscountService {

	private static Map<Integer, Movie> movies;
	static {
		LocalDate localDate = LocalDate.of(2019, Month.JULY, 20);
		Date lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		Movie movie1 = new Movie(1, "Titanic", 30);
		movie1.setLastModifieDate(lastModified);
		Movie movie2 = new Movie(2, "Matrix", 60);
		movie2.setLastModifieDate(lastModified);
		movies = new HashMap<>();
		movies.put(movie1.getNumber(), movie1);
		movies.put(movie2.getNumber(), movie2);
	}

	@GET
	@Produces("application/yaml")
	public Response listMovies() {
		List<Movie> movieList = movies.values().stream().collect(Collectors.toList());
		return Response.ok(movieList).build();
	}

	@GET
	@Path("movie")
	@ApiOperation(value = "This method returns the matching movie from the existing movie catalog.", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON, response = Movie.class)
	@ApiResponses(value = {@ApiResponse(message = "Returns the matching movie from the existing movie catalog.", code = 200)})
	public Response getMovie(@DefaultValue("1") @QueryParam("number") int number) {
		Movie movie = new Movie();
		if (movies.containsKey(number)) {
			movie = movies.get(number);
		}
		Response response = Response.status(Status.OK).entity(movie).build();
		return response;
	}

	@PUT
	@Path("movie")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_XML })
	@ApiOperation(value = "This method updates the existing Movie object with new values and returns the response.", consumes = MediaType.APPLICATION_XML, produces = MediaType.APPLICATION_XML, response = Movie.class)
	@ApiResponses(value = { @ApiResponse(code = 304, message = "Movie id doesn't exist. "),
			@ApiResponse(code = 202, message = "Movie is updated."), })
	public Response updateMovie(@ApiParam(name = "updateMovie", type = "Movie", value = "New Movie data") Movie movie) {
		Status status = Status.ACCEPTED;
		int number = movie.getNumber();
		if (movies.containsKey(number)) {
			movies.put(number, movie);
		} else {
			status = Status.NOT_MODIFIED;
			throw new CustomException(status.toString());
		}
		System.out.println("Inside update: " + movie);
		return Response.status(status).entity(movie).build();
	}

	@POST
	@Path("movie")
	@ApiOperation(value = "This method creates a new Movie object with supplied new values, adds it to the movie catalog and returns the response.", response = Movie.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Movie Object is successfully created . "),
			@ApiResponse(code = 201, message = "Creates new movie. "),
			@ApiResponse(code = 406, message = "Movie creation failed."), })
	public Response createMovie(@ApiParam(name = "createMovie", type = "Movie", value = "New Movie data") Movie movie) {
		Status status = Status.CREATED;
		if (movies.containsKey(movie.getNumber())) {
			status = Status.NOT_ACCEPTABLE;
		} else {
			int newPrice = getDiscountedPrice(movie.getPrice());
			movie.setPrice(newPrice);
			movies.put(movie.getNumber(), movie);
		}
		return Response.status(status).entity(movie).build();
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
	public int getDiscountedPrice(int price) {
		return (price - 5);
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
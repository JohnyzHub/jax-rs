package com.javaee.ws.restful.service.subresource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author johnybasha
 *
 */
public class ArtistInventory {

	private static List<Artist> artists = new ArrayList<>(0);

	public ArtistInventory() {
	}

	@GET
	public Response findArtist(@QueryParam("name") String artist) {
		Status status = Status.NOT_FOUND;
		Artist artistObj = getArtist(artist);
		if (artistObj == null) {
			return Response.status(status).build();
		}
		status = Status.FOUND;
		return Response.ok(artistObj).status(status).build();
	}

	private Artist getArtist(String artist) {
		if (artists == null || artists.isEmpty()) {
			return null;
		}
		List<Artist> result = artists.stream().filter(a -> a.getName().equalsIgnoreCase(artist))
				.collect(Collectors.toList());
		return result.get(0);
	}

	@POST
	public Response addArtist(@QueryParam("name") String artist, @QueryParam("movie") String movie) {
		Status status = Status.BAD_REQUEST;
		Artist artistObj = getArtist(artist);
		if (artistObj == null) {
			artistObj = new Artist(artist);
			artistObj.addMovie(movie);
			artists.add(artistObj);
			status = Status.CREATED;
		}
		return Response.ok(artistObj).status(status).build();
	}
}

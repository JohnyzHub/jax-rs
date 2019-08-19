package com.javaee.ws.restful.service.subresource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.javaee.ws.restful.service.entity.Person;

/**
 * @author johnybasha
 *
 */
@Path("artistinventory")
public class ArtistInventory implements Inventory {

	private static List<Person> artists = new ArrayList<>(0);

	public ArtistInventory() {
	}

	@Override
	public Response findRecord(String person, String artist) {
		Status status = Status.NOT_FOUND;
		Person artistObj = getArtist(artist);
		if (artistObj == null) {
			return Response.status(status).build();
		}
		status = Status.FOUND;
		return Response.ok(artistObj).status(status).build();
	}

	private Person getArtist(String artist) {
		if (artists == null || artists.isEmpty()) {
			return null;
		}
		Optional<Person> result = artists.stream().filter(a -> a.getName().equalsIgnoreCase(artist)).findFirst();
		;
		return result.orElse(null);
	}

	@Override
	public Response addRecord(String person, String artist, String movie) {
		Status status = Status.BAD_REQUEST;
		Person artistObj = getArtist(artist);
		if (artistObj == null) {
			artistObj = new Person(artist);
			artistObj.addMovie(movie);
			artists.add(artistObj);
			status = Status.CREATED;
		}
		return Response.ok(artistObj).status(status).build();
	}
}
package com.javaee.ws.restful.service.subresource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.javaee.ws.restful.service.entity.Person;

/**
 * @author johnybasha
 *
 */
@ApplicationScoped
public class ArtistInventory implements Inventory {

	private List<Person> artists = new ArrayList<>(0);

	public ArtistInventory() {
		Person artist1 = new Person("Artist1");
		artist1.addMovie("Movie1");
		Person artist2 = new Person("Artist2");
		artist2.addMovie("Movie2");
		artists.addAll(new ArrayList<>(Arrays.asList(artist1, artist2)));
	}

	@Override
	public Response findAll() {
		return Response.ok(artists).build();
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
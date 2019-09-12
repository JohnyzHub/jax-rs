package com.javaee.ws.restful.client;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.javaee.ws.restful.service.entity.Movie;
import com.javaee.ws.restful.service.entityprovider.XMLMessageBodyReaderWriter;

/**
 * @author johnybasha
 *
 */
public class MovieCacheClient {

	private static final String BASE_URI = "http://localhost:8080/moviedirectory/rest/directory/cache";

	public MovieCacheClient() {
	}

	public static void main(String[] args) throws Exception {
		MovieCacheClient client = new MovieCacheClient();

		// client.findMovies_maxage();
		// client.findMovie_ifModified();
		// client.findMovie_ifUnmodified();
		// client.updateMovie_ifUnmodified();
		 client.findActor_etag_unmatch();
		client.findActor_etag_match();

		// client.updateMovie_etag_NM_lastModified_NM();
		// client.updateMovie_etag_M_lastModified_NM();
		// client.updateMovie_etag_NM_lastModified_M();
		// client.updateMovie_etag_M_lastModified_M();

	}

	public void findMovies_maxage() throws Exception {
		Client client = ClientBuilder.newBuilder().register(new XMLMessageBodyReaderWriter()).build();
		WebTarget webTarget = client.target(BASE_URI);

		Response response = webTarget.request().get();

		String uriString = webTarget.getUri().toString();
		System.out.println("\n" + uriString + ":: is being processed ::\n" + response.readEntity(String.class));
	}

	/**
	 * Testing the cache mechanism with if-modified-since header
	 */
	public void findMovie_ifModified() {
		Client client = ClientBuilder.newBuilder().register(new XMLMessageBodyReaderWriter()).build();
		WebTarget webTarget = client.target(BASE_URI).path("modified").path("movie").queryParam("id", 1);

		String uriString = webTarget.getUri().toString();

		System.out.println("First request::\n" + uriString);
		Response response = webTarget.request().get();
		System.out.println("\nResponse: " + response.readEntity(String.class));

		System.out.println("\nTesting If-Modified-Since 13th July::");
		LocalDate localDate = LocalDate.of(2019, Month.JULY, 13);
		Date lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		MultivaluedMap<String, Object> headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_MODIFIED_SINCE, lastModified);

		response = webTarget.request().headers(headersMap).get();

		System.out.println("\n--Modified::\n" + response.readEntity(String.class));

		System.out.println("\nTesting If-Modified-Since 23th July::");
		localDate = LocalDate.of(2019, Month.JULY, 23);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_MODIFIED_SINCE, lastModified);

		response = webTarget.request().headers(headersMap).get();

		System.out.println("\n--Unmodified::\n" + response.getStatusInfo().getReasonPhrase());
	}

	/**
	 * Testing the cache mechanism with if-unmodified-since header
	 */
	public void findMovie_ifUnmodified() {
		Client client = ClientBuilder.newBuilder().register(new XMLMessageBodyReaderWriter()).build();
		WebTarget webTarget = client.target(BASE_URI).path("unmodified").path("movie").path("Titanic");

		String uriString = webTarget.getUri().toString();

		System.out.println("First request::\n" + uriString);
		Response response = webTarget.request().get();
		System.out.println("\nResponse: " + response.readEntity(String.class));

		System.out.println("\nTesting If-Unmodified-Since 13th July::");
		LocalDate localDate = LocalDate.of(2019, Month.JULY, 13);
		Date lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		MultivaluedMap<String, Object> headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_UNMODIFIED_SINCE, lastModified);

		response = webTarget.request().headers(headersMap).get();

		System.out.println("\n--Modified--\n" + response.readEntity(String.class));
		System.out.println("Cache-Control:" + response.getHeaderString(HttpHeaders.CACHE_CONTROL));

		System.out.println("\nTesting If-Unmodified-Since 23th July::");
		localDate = LocalDate.of(2019, Month.JULY, 23);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_UNMODIFIED_SINCE, lastModified);

		response = webTarget.request().headers(headersMap).get();

		System.out.println("\n--Unmodified--\n" + response.readEntity(String.class));
		System.out.println("Cache-Control:" + response.getHeaderString(HttpHeaders.CACHE_CONTROL));
	}

	/**
	 * Testing the cache mechanism with if-unmodified-since header
	 */
	public void updateMovie_ifUnmodified() {
		Client client = ClientBuilder.newBuilder().register(new XMLMessageBodyReaderWriter()).build();
		WebTarget webTarget = client.target(BASE_URI).path("movie").path("1");

		String uriString = webTarget.getUri().toString();

		System.out.println("First request::\n" + uriString);
		Response response = webTarget.request().get();
		System.out.println("\nResponse: " + response.readEntity(String.class));

		System.out.println("\nTesting If-Unmodified-Since 13th July::");
		LocalDate localDate = LocalDate.of(2019, Month.JULY, 13);
		Date lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Movie movie = new Movie(1, "Titanic", 100);
		MultivaluedMap<String, Object> headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_UNMODIFIED_SINCE, lastModified);
		webTarget = client.target(BASE_URI).path("unmodified").path("movie").path("Titanic");
		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));

		System.out.println("\n--Modified--\n" + response.readEntity(String.class));
		System.out.println("Cache-Control:" + response.getHeaderString(HttpHeaders.CACHE_CONTROL));

		System.out.println("\nTesting If-Unmodified-Since 23th July::");
		localDate = LocalDate.of(2019, Month.JULY, 23);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_UNMODIFIED_SINCE, lastModified);

		response = webTarget.request().put(Entity.entity(movie, MediaType.APPLICATION_JSON));

		System.out.println("\n--Unmodified--\n" + response.getStatusInfo().getReasonPhrase());
		System.out.println("Cache-Control:" + response.getHeaderString(HttpHeaders.CACHE_CONTROL));
	}

	/**
	 * Testing the cache mechanism using entity tag Sets the if-none-match header
	 * with etag for validation.
	 */
	public void findActor_etag_unmatch() {

		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("etag").path("person");

		String uriString = webTarget.getUri().toString();

		System.out.println("First request::\n" + uriString);
		Response response = webTarget.request().get();
		EntityTag entityTag = response.getEntityTag();
		System.out.println("\nResponse: " + response.readEntity(String.class) + ", eTag: " + entityTag);

		System.out.println("****   Testing ETag with IF_NONE_MATCH  header *****");

		MultivaluedMap<String, Object> headersMap = null;
		System.out.println("\nwith matching etag ::" + entityTag);
		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_NONE_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).get();
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		System.out.println("\nResponse: " + response.getStatusInfo().getReasonPhrase() + ", eTag: " + entityTag);

		entityTag = getEntityTag();
		System.out.println("\nwith non-matching etag ::" + entityTag);
		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_NONE_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).get();
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		System.out.println("\nResponse: " + response.readEntity(String.class) + ", eTag: " + entityTag);
	}

	/**
	 * Testing the cache mechanism using entity tag Sets the if-match header with
	 * etag for validation.
	 */
	public void findActor_etag_match() {

		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI).path("etag").path("person");

		String uriString = webTarget.getUri().toString();

		System.out.println("First request::\n" + uriString);
		Response response = webTarget.request().get();
		EntityTag entityTag = response.getEntityTag();
		System.out.println("\nResponse: " + response.readEntity(String.class) + ", eTag: " + entityTag);

		System.out.println("****   Testing ETag with IF_MATCH  header *****");

		MultivaluedMap<String, Object> headersMap = null;
		System.out.println("\nwith matching etag ::" + entityTag);
		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).get();
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		System.out.println("\nResponse: " + response.readEntity(String.class) + ", eTag: " + entityTag);

		entityTag = getEntityTag();
		System.out.println("\nwith non-matching etag ::" + entityTag);
		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).get();
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		System.out.println("\nResponse: " + response.getStatusInfo().getReasonPhrase() + ", eTag: " + entityTag);
	}

	/**
	 * NONE_MATCH: EntityTag;; IF_UNMODIFIED_SINCE: lastmodified date
	 * 
	 */
	public void updateMovie_etag_NM_lastModified_NM() {
		Client client = ClientBuilder.newBuilder().register(new XMLMessageBodyReaderWriter()).build();

		WebTarget webTarget = client.target(BASE_URI).path("movie").path("2");

		String uriString = webTarget.getUri().toString();

		System.out.println("First request::\n" + uriString);
		Response response = webTarget.request().accept(MediaType.APPLICATION_XML).get();
		Movie movie = null;
		Object resultedEntity = response.readEntity(Movie.class);
		if (resultedEntity != null && resultedEntity instanceof Movie) {
			movie = (Movie) resultedEntity;
		}
		EntityTag entityTag = response.getEntityTag();
		Date lastModifiedDate = response.getLastModified();
		System.out.println("\nMovie: " + movie);
		System.out.println("\nEtag: " + entityTag + ", lastModified: " + lastModifiedDate);

		System.out.println("Testing with matching Etag and pre-modified date");
		LocalDate localDate = LocalDate.of(2019, Month.JULY, 13);
		Date lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		MultivaluedMap<String, Object> headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_UNMODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_NONE_MATCH, entityTag);

		movie = new Movie(2, "Matrix", 160);
		webTarget = client.target(BASE_URI).path("etag").path("modifieddate").path("movie");
		response = webTarget.request().headers(headersMap).accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(movie, MediaType.APPLICATION_JSON));

		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

		System.out.println("Testing with matching Etag and post-modified date");
		localDate = LocalDate.of(2019, Month.AUGUST, 13);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_UNMODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_NONE_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

		System.out.println("Testing with non-matching Etag and pre-modified date");
		entityTag = getEntityTag();
		localDate = LocalDate.of(2019, Month.JULY, 13);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_UNMODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_NONE_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

		System.out.println("Testing with non-matching Etag and post-modified date");
		entityTag = getEntityTag();
		localDate = LocalDate.of(2019, Month.AUGUST, 13);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_UNMODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_NONE_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

	}

	/**
	 * MATCH: EntityTag;; IF_UNMODIFIED_SINCE: lastmodified date
	 * 
	 */
	public void updateMovie_etag_M_lastModified_NM() {
		Client client = ClientBuilder.newBuilder().register(new XMLMessageBodyReaderWriter()).build();

		WebTarget webTarget = client.target(BASE_URI).path("movie").path("2");

		String uriString = webTarget.getUri().toString();

		System.out.println("First request::\n" + uriString);
		Response response = webTarget.request().accept(MediaType.APPLICATION_XML).get();
		Movie movie = null;
		Object resultedEntity = response.readEntity(Movie.class);
		if (resultedEntity != null && resultedEntity instanceof Movie) {
			movie = (Movie) resultedEntity;
		}
		EntityTag entityTag = response.getEntityTag();
		Date lastModifiedDate = response.getLastModified();
		System.out.println("\nMovie: " + movie);
		System.out.println("\nEtag: " + entityTag + ", lastModified: " + lastModifiedDate);

		System.out.println("Testing with matching Etag and pre-modified date");
		LocalDate localDate = LocalDate.of(2019, Month.JULY, 13);
		Date lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		MultivaluedMap<String, Object> headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_UNMODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_MATCH, entityTag);

		movie = new Movie(2, "Matrix", 160);
		webTarget = client.target(BASE_URI).path("etag").path("modifieddate").path("movie");
		response = webTarget.request().headers(headersMap).accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(movie, MediaType.APPLICATION_JSON));

		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

		System.out.println("Testing with matching Etag and post-modified date");
		localDate = LocalDate.of(2019, Month.AUGUST, 13);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_UNMODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

		System.out.println("Testing with non-matching Etag and pre-modified date");
		entityTag = getEntityTag();
		localDate = LocalDate.of(2019, Month.JULY, 13);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_UNMODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

		System.out.println("Testing with non-matching Etag and post-modified date");
		entityTag = getEntityTag();
		localDate = LocalDate.of(2019, Month.AUGUST, 13);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_UNMODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);
	}

	/**
	 * NONE_MATCH: EntityTag;; IF_MODIFIED_SINCE: lastmodified date
	 * 
	 */
	public void updateMovie_etag_NM_lastModified_M() {
		Client client = ClientBuilder.newBuilder().register(new XMLMessageBodyReaderWriter()).build();

		WebTarget webTarget = client.target(BASE_URI).path("movie").path("2");

		String uriString = webTarget.getUri().toString();

		System.out.println("First request::\n" + uriString);
		Response response = webTarget.request().accept(MediaType.APPLICATION_XML).get();
		Movie movie = null;
		Object resultedEntity = response.readEntity(Movie.class);
		if (resultedEntity != null && resultedEntity instanceof Movie) {
			movie = (Movie) resultedEntity;
		}
		EntityTag entityTag = response.getEntityTag();
		Date lastModifiedDate = response.getLastModified();
		System.out.println("\nMovie: " + movie);
		System.out.println("\nEtag: " + entityTag + ", lastModified: " + lastModifiedDate);

		System.out.println("Testing with matching Etag and pre-modified date");
		LocalDate localDate = LocalDate.of(2019, Month.JULY, 13);
		Date lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		MultivaluedMap<String, Object> headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_MODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_NONE_MATCH, entityTag);

		movie = new Movie(2, "Matrix", 160);
		webTarget = client.target(BASE_URI).path("etag").path("modifieddate").path("movie");
		response = webTarget.request().headers(headersMap).accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(movie, MediaType.APPLICATION_JSON));

		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

		System.out.println("Testing with matching Etag and post-modified date");
		localDate = LocalDate.of(2019, Month.AUGUST, 13);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_MODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_NONE_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

		System.out.println("Testing with non-matching Etag and pre-modified date");
		entityTag = getEntityTag();
		localDate = LocalDate.of(2019, Month.JULY, 13);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_MODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_NONE_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

		System.out.println("Testing with non-matching Etag and post-modified date");
		entityTag = getEntityTag();
		localDate = LocalDate.of(2019, Month.AUGUST, 13);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_MODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_NONE_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);
	}

	/**
	 * MATCH: EntityTag;; IF_MODIFIED_SINCE: lastmodified date
	 * 
	 */
	public void updateMovie_etag_M_lastModified_M() {
		Client client = ClientBuilder.newBuilder().register(new XMLMessageBodyReaderWriter()).build();

		WebTarget webTarget = client.target(BASE_URI).path("movie").path("2");

		String uriString = webTarget.getUri().toString();

		System.out.println("First request::\n" + uriString);
		Response response = webTarget.request().accept(MediaType.APPLICATION_XML).get();
		Movie movie = null;
		Object resultedEntity = response.readEntity(Movie.class);
		if (resultedEntity != null && resultedEntity instanceof Movie) {
			movie = (Movie) resultedEntity;
		}
		EntityTag entityTag = response.getEntityTag();
		Date lastModifiedDate = response.getLastModified();
		System.out.println("\nMovie: " + movie);
		System.out.println("\nEtag: " + entityTag + ", lastModified: " + lastModifiedDate);

		System.out.println("Testing with matching Etag and pre-modified date");
		LocalDate localDate = LocalDate.of(2019, Month.JULY, 13);
		Date lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		MultivaluedMap<String, Object> headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_MODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_MATCH, entityTag);

		movie = new Movie(2, "Matrix", 120);
		webTarget = client.target(BASE_URI).path("etag").path("modifieddate").path("movie");
		response = webTarget.request().headers(headersMap).accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(movie, MediaType.APPLICATION_JSON));

		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

		System.out.println("Testing with matching Etag and post-modified date");
		localDate = LocalDate.of(2019, Month.AUGUST, 13);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_MODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

		System.out.println("Testing with non-matching Etag and pre-modified date");
		entityTag = getEntityTag();
		localDate = LocalDate.of(2019, Month.JULY, 13);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_MODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);

		System.out.println("Testing with non-matching Etag and post-modified date");
		entityTag = getEntityTag();
		localDate = LocalDate.of(2019, Month.AUGUST, 13);
		lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		headersMap = new MultivaluedHashMap<>();
		headersMap.add(HttpHeaders.IF_MODIFIED_SINCE, lastModified);
		headersMap.add(HttpHeaders.IF_MATCH, entityTag);

		response = webTarget.request().headers(headersMap).put(Entity.entity(movie, MediaType.APPLICATION_JSON));
		System.out.println("Response: " + response.readEntity(String.class));
		if (response.getEntityTag() != null) {
			entityTag = response.getEntityTag();
		}
		if (response.getLastModified() != null) {
			lastModified = response.getLastModified();
		}
		System.out.println("Etag: " + entityTag + ", lastModified: " + lastModified);
	}

	private EntityTag getEntityTag() {
		LocalDate localDate = LocalDate.of(2019, Month.SEPTEMBER, 20);
		Date lastModified = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		Movie movie = new Movie(1, "SUPERMAN", 30);
		movie.setLastModifieDate(lastModified);
		return new EntityTag(Integer.toString(movie.hashCode()));
	}
}
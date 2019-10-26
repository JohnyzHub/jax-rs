package com.javaee.ws.restful.service.entityprovider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.yaml.snakeyaml.Yaml;

import com.javaee.ws.restful.service.entity.Movie;
import com.javaee.ws.restful.service.entity.MovieProxy;

/**
 * @author johnybasha
 *
 */
//@Provider
@Consumes("application/yaml")
@Produces("application/yaml")
public class YamlMessageBodyReaderWriter implements MessageBodyReader<List<Movie>>, MessageBodyWriter<List<Movie>> {

	public YamlMessageBodyReaderWriter() {
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public void writeTo(List<Movie> movies, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {

		List<MovieProxy> movieProxyList = getMovieProxy(movies);
		Yaml yaml = new Yaml();
		OutputStreamWriter writer = new OutputStreamWriter(entityStream);
		yaml.dump(movieProxyList, writer);
		writer.close();

	}

	private List<MovieProxy> getMovieProxy(List<Movie> movies) {
		List<MovieProxy> movieProxyList = new ArrayList<>(0);

		MovieProxy movieProxy = new MovieProxy();
		for (Movie movie : movies) {
			movieProxy = new MovieProxy(movie);
			movieProxyList.add(movieProxy);
		}
		return movieProxyList;
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public List<Movie> readFrom(Class<List<Movie>> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		Yaml yaml = new Yaml();
		List<Movie> movies = yaml.loadAs(toString(entityStream), type);
		return movies;
	}

	public static String toString(InputStream inputStream) {
		try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
			return scanner.useDelimiter("\\A").next();
		}
	}

}

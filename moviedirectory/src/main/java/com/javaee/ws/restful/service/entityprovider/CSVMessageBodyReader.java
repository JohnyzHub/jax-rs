/**
 * 
 */
package com.javaee.ws.restful.service.entityprovider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.javaee.ws.restful.service.Movie;

/**
 * @author johnybasha
 *
 */
@Provider
@Consumes("application/csv")
public class CSVMessageBodyReader implements MessageBodyReader<Movie> {

	public CSVMessageBodyReader() {
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Movie.class.isAssignableFrom(type);
	}

	@Override
	public Movie readFrom(Class<Movie> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		final CellProcessor[] processor = new CellProcessor[] { 
				new NotNull(new ParseInt()), // movie number
				new NotNull(), // movie title
				new NotNull(new ParseInt()) // movie price
		};

		@SuppressWarnings("resource")
		ICsvBeanReader beanReader = 
				new CsvBeanReader(
						new InputStreamReader(entityStream),
						CsvPreference.STANDARD_PREFERENCE);
		String[] header = beanReader.getHeader(false);
		return beanReader.read(Movie.class, header, processor);
	}
}

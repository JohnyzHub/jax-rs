/**
 * 
 */
package com.javaee.ws.restful.service.entityprovider;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.javaee.ws.restful.service.entity.Movie;

/**
 * @author johnybasha
 *
 */
@Provider
@Consumes("application/csv")
@Produces("application/csv")
public class CSVMessageBodyReaderWriter implements MessageBodyReader<List<Movie>>, MessageBodyWriter<List<Movie>> {

	public CSVMessageBodyReaderWriter() {
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Collection.class.isAssignableFrom(type);
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Collection.class.isAssignableFrom(type);
	}

	@Override
	public void writeTo(List<Movie> movies, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		try (ICsvBeanWriter beanWriter = new CsvBeanWriter(new PrintWriter(entityStream),
				CsvPreference.STANDARD_PREFERENCE)) {

			if (movies == null || movies.isEmpty()) {
				return;
			}

			String[] mappingNames = getHeader(movies.get(0));
			beanWriter.writeHeader(mappingNames);

			for (Movie movie : movies) {
				beanWriter.write(movie, mappingNames);
			}
		}
	}

	private String[] getHeader(Object object) {
		List<String> headerStrings = new ArrayList<>();
		try {
			BeanInfo info = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor[] pds = info.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (!pd.getName().equals("class")) {
					headerStrings.add(pd.getDisplayName());
				}
			}
		} catch (IntrospectionException ex) {
			System.out.println(ex.getMessage());
		}
		return headerStrings.toArray(new String[headerStrings.size()]);
	}

	@Override
	public List<Movie> readFrom(Class<List<Movie>> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		final CellProcessor[] processor = new CellProcessor[] { new Optional(new ParseInt()), // movie number
				new Optional(new ParseInt()), // movie price
				new Optional() // movie title
		};

		List<Movie> moviesList = null;
		try (ICsvBeanReader beanReader = new CsvBeanReader(new InputStreamReader(entityStream),
				CsvPreference.STANDARD_PREFERENCE)) {
			 String[] header = beanReader.getHeader(false);
			Movie obj = null;
			moviesList = new ArrayList<>();
			while ((obj = beanReader.read(Movie.class, header, processor)) != null) {
				moviesList.add(obj);
			}
		}

		return moviesList;
	}
}

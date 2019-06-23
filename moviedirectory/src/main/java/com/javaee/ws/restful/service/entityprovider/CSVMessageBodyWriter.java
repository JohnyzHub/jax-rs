package com.javaee.ws.restful.service.entityprovider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.javaee.ws.restful.service.Movie;

/**
 * @author johnybasha
 *
 */

@Provider
@Produces("application/csv")
public class CSVMessageBodyWriter implements MessageBodyWriter<List<Movie>> {

	public CSVMessageBodyWriter() {
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Collection.class.isAssignableFrom(type);
	}

	@Override
	public void writeTo(List<Movie> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		try (ICsvBeanWriter beanWriter = new CsvBeanWriter(new PrintWriter(entityStream),
				CsvPreference.STANDARD_PREFERENCE)) {

			if (t == null || t.isEmpty()) {
				return;
			}

			String[] mappingNames = { "number", "title", "price" };
			beanWriter.writeHeader(mappingNames);

			t.forEach(p -> writeData(beanWriter, p));
		}
	}

	private void writeData(ICsvBeanWriter beanWriter, Movie movie) {
		String[] mappingNames = { "number", "title", "price" };
		try {
			beanWriter.write(movie, mappingNames);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
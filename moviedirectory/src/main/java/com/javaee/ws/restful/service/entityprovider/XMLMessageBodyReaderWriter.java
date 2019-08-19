/**
 * 
 */
package com.javaee.ws.restful.service.entityprovider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.javaee.ws.restful.service.entity.Movie;

/**
 * @author johnybasha
 *
 */
@Provider
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class XMLMessageBodyReaderWriter implements MessageBodyWriter<Movie>, MessageBodyReader<Movie> {

	public XMLMessageBodyReaderWriter() {
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == Movie.class;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == Movie.class;
	}

	@Override
	public void writeTo(Movie movie, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Movie.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(movie, entityStream);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Movie readFrom(Class<Movie> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		Movie movie = null;

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Movie.class);
			movie = (Movie) jaxbContext.createUnmarshaller().unmarshal(entityStream);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return movie;
	}
}
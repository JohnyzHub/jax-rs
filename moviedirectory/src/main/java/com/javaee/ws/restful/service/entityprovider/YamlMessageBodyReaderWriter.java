/**
 * 
 */
package com.javaee.ws.restful.service.entityprovider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.yaml.snakeyaml.Yaml;

/**
 * @author johnybasha
 *
 */
@Provider
@Consumes({ "application/yaml", MediaType.TEXT_PLAIN })
@Produces({ "application/yaml", MediaType.TEXT_PLAIN })
public class YamlMessageBodyReaderWriter<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {

	public YamlMessageBodyReaderWriter() {
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {

		Yaml yaml = new Yaml();
		OutputStreamWriter writer = new OutputStreamWriter(entityStream);
		yaml.dump(t, writer);
		writer.close();

	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		Yaml yaml = new Yaml();
		T t = yaml.loadAs(toString(entityStream), type);
		return t;
	}

	public static String toString(InputStream inputStream) {
		try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
			return scanner.useDelimiter("\\A").next();
		}
	}

}

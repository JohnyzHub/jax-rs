package com.javaee.ws.restful.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.javaee.ws.restful.service.MovieDirectoryService;
import com.javaee.ws.restful.service.entityprovider.CSVMessageBodyReader;
import com.javaee.ws.restful.service.entityprovider.CSVMessageBodyWriter;
import com.javaee.ws.restful.service.exception.ArithmenticExceptionMapper;
import com.javaee.ws.restful.service.sse.EventsResource;

/**
 * @author shaikjb
 *
 */
@ApplicationPath("rest")
public class ApplicationConfiguration extends Application {

	Set<Class<?>> classes = new HashSet<>(4);

	@Override
	public Set<Class<?>> getClasses() {
		classes.add(MovieDirectoryService.class);
		classes.add(ArithmenticExceptionMapper.class);
		classes.add(EventsResource.class);
		classes.add(CSVMessageBodyWriter.class);
		classes.add(CSVMessageBodyReader.class);
		return classes;
	}
}
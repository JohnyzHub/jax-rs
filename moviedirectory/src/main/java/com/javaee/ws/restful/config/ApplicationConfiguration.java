package com.javaee.ws.restful.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.javaee.ws.restful.service.MovieAsyncService;
import com.javaee.ws.restful.service.MovieDirectoryService;
import com.javaee.ws.restful.service.entityprovider.CSVMessageBodyReaderWriter;
import com.javaee.ws.restful.service.entityprovider.XMLMessageBodyReaderWriter;
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
		classes.add(CSVMessageBodyReaderWriter.class);
		classes.add(XMLMessageBodyReaderWriter.class);
		classes.add(MovieAsyncService.class);
		return classes;
	}
}
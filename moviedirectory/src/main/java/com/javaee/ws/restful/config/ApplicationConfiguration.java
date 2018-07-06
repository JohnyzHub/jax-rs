package com.javaee.ws.restful.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.javaee.ws.restful.service.MovieDirectoryRestService;
import com.javaee.ws.restful.service.exception.ArithmenticExceptionMapper;
import com.javaee.ws.restful.service.sse.EventsResource;

/**
 * @author shaikjb
 *
 */
@ApplicationPath("rest")
public class ApplicationConfiguration extends Application {

	Set<Class<?>> classes = new HashSet<Class<?>>(2);

	@Override
	public Set<Class<?>> getClasses() {
		classes.add(MovieDirectoryRestService.class);
		classes.add(ArithmenticExceptionMapper.class);
		classes.add(EventsResource.class);
		return classes;
	}

}

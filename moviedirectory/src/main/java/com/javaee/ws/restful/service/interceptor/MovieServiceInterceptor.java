package com.javaee.ws.restful.service.interceptor;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 * @author johnybasha
 *
 */
@Provider
public class MovieServiceInterceptor implements ReaderInterceptor, WriterInterceptor {

	public MovieServiceInterceptor() {

	}

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		context.setProperty("genre", "U");

		context.proceed();
	}

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
		context.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		return context.proceed();
	}
}
package com.javaee.ws.restful.service.subresource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

public interface Inventory {

	@GET
	@Path("all")
	public Response findAll();

	@GET
	public Response findRecord(@PathParam("person") String person, @QueryParam("name") String name);

	@POST
	public Response addRecord(@PathParam("person") String person, @QueryParam("name") String name,
			@QueryParam("movie") String movie);

}

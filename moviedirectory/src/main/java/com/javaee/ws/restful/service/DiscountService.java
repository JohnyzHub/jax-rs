/**
 * 
 */
package com.javaee.ws.restful.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author johnybasha
 *
 */
public interface DiscountService {

	@GET
	@Path("price/{discount:([1-9]|[1-4][0-9]|50)}")
	public int getTicketPrice(@PathParam("discount") int discount);

}

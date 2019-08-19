package com.javaee.ws.restful.service.subresource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.javaee.ws.restful.service.entity.Person;

/**
 * @author johnybasha
 *
 */
@Path("techinicianinventory")
public class TechnicianInventory implements Inventory {

	private static List<Person> technicians = new ArrayList<>(0);

	public TechnicianInventory() {
	}

	@Override
	public Response findRecord(String person, String technician) {
		Status status = Status.NOT_FOUND;
		Person technicianObj = getTechnician(technician);
		if (technicianObj == null) {
			return Response.status(status).build();
		}
		status = Status.FOUND;
		return Response.ok(technicianObj).status(status).build();
	}

	private Person getTechnician(String technician) {
		if (technicians == null || technicians.isEmpty()) {
			return null;
		}
		Optional<Person> result = technicians.stream().filter(a -> a.getName().equalsIgnoreCase(technician))
				.findFirst();
		;
		return result.orElse(null);
	}

	@Override
	public Response addRecord(String person, String technician, String movie) {
		Status status = Status.BAD_REQUEST;
		Person technicianObj = getTechnician(technician);
		if (technicianObj == null) {
			technicianObj = new Person(technician);
			technicianObj.addMovie(movie);
			technicians.add(technicianObj);
			status = Status.CREATED;
		}
		return Response.ok(technicianObj).status(status).build();
	}
}
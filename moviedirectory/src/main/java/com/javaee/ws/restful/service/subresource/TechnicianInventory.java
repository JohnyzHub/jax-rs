package com.javaee.ws.restful.service.subresource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.javaee.ws.restful.service.entity.Person;

/**
 * @author johnybasha
 *
 */
@ApplicationScoped
public class TechnicianInventory implements Inventory {

	private List<Person> technicians = new ArrayList<>(0);

	public TechnicianInventory() {
		Person technician1 = new Person("Technician1");
		technician1.addMovie("Movie1");
		Person technician2 = new Person("Technician2");
		technician2.addMovie("Movie2");
		technicians.addAll(new ArrayList<>(Arrays.asList(technician1, technician2)));
	}

	@Override
	public Response findAll() {
		return Response.ok(technicians).build();
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
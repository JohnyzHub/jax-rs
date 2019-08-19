package com.javaee.ws.restful.service.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author johnybasha
 *
 */
public class Person {

	private String name;

	private List<String> movies;

	public Person() {
	}

	public Person(String name) {
		super();
		this.name = name;
		movies = new ArrayList<>(0);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMovies() {
		return movies;
	}

	public void addMovie(String movie) {
		this.movies.add(movie);
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", movies=" + movies + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}

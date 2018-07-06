package com.javaee.ws.restful.service;

/**
 * @author shaikjb
 *
 */
public class Movie {

	private String title;

	private int number;

	public Movie() {
	}

	public Movie(String title, int number) {
		this.title = title;
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Movie [title=" + title + ", number=" + number + "]";
	}
}
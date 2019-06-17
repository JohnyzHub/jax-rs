package com.javaee.ws.restful.service;

/**
 * @author shaikjb
 *
 */
public class Movie {

	private String title;

	private int number;

	private int price;

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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		String movie = "Movie [title= " + title + ", number= " + number;
		if (price > 0) {
			movie = movie + ", price= " + price;
		}
		movie = movie + "]";
		return movie;
	}
}
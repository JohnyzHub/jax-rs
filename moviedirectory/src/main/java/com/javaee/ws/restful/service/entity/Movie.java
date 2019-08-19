package com.javaee.ws.restful.service.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shaikjb
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Movie {

	private int number;

	private String title;

	private int price;

	public Movie() {
	}

	public Movie(int number, String title) {
		this.number = number;
		this.title = title;
	}

	public Movie(int number, String title, int price) {
		this.number = number;
		this.title = title;
		this.price = price;
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
		String movie = "Movie [number= " + number + ", title= " + title;
		movie = movie + ", price= " + price;
		movie = movie + "]";
		return movie;
	}
}
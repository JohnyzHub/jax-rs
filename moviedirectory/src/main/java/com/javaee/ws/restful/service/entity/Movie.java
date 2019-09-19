package com.javaee.ws.restful.service.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.javaee.ws.restful.service.entity.adaptor.DateTimeAdaptor;

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

	@XmlJavaTypeAdapter(DateTimeAdaptor.class)
	private Date lastModifiedDate;

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

	public Movie(int number, String title, int price, Date lastModifiedDate) {
		this.number = number;
		this.title = title;
		this.price = price;
		this.lastModifiedDate = lastModifiedDate;
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

	public Date getLastModifieDate() {
		return lastModifiedDate;
	}

	public void setLastModifieDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lastModifiedDate == null) ? 0 : lastModifiedDate.hashCode());
		result = prime * result + number;
		result = prime * result + price;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Movie other = (Movie) obj;
		if (lastModifiedDate == null) {
			if (other.lastModifiedDate != null)
				return false;
		} else if (!lastModifiedDate.equals(other.lastModifiedDate))
			return false;
		if (number != other.number)
			return false;
		if (price != other.price)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String movie = "Movie [number= " + number + ", title= " + title + ", price= " + price;
		if (lastModifiedDate != null) {
			movie = movie + ", lastModified= " + lastModifiedDate;
		}
		movie = movie + "]";
		return movie;
	}
}
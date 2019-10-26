package com.javaee.ws.restful.service.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author johnybasha
 *
 */
public class MovieProxy {

	private int number;

	private String title;

	private int price;

	private Date lastModifiedDate;

	public MovieProxy() {
	}

	public MovieProxy(Movie movie) {
		this.number = movie.getNumber();
		this.title = movie.getTitle();
		this.price = movie.getPrice();
		LocalDateTime localDateTime = movie.getLastModifieDate();
		ZonedDateTime zdtDateTime = localDateTime.atZone(ZoneId.systemDefault());
		this.lastModifiedDate = Date.from(zdtDateTime.toInstant());
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
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
		MovieProxy other = (MovieProxy) obj;
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
		return "Movie [number=" + number + ", title=" + title + ", price=" + price + ", lastModifiedDate="
				+ lastModifiedDate + "]";
	}

}

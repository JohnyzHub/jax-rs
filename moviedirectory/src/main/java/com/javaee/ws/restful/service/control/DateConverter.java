package com.javaee.ws.restful.service.control;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author johnybasha
 *
 */
public class DateConverter {

	public DateConverter() {
	}

	/**
	 * Converts LocalDateTime to java.util.Date
	 * 
	 * @param zonedDateTime
	 * @return
	 */
	public static Date obtainDateFromLocalDateTime(LocalDateTime lastModifiedDateTime) {
		ZonedDateTime zdtDateTime = ZonedDateTime.of(lastModifiedDateTime, ZoneId.systemDefault());
		return Date.from(zdtDateTime.toInstant());
	}

	/**
	 * Converts java.util.Date to LocalDateTime
	 * 
	 * @param date
	 * @return
	 */
	public static LocalDateTime obtainLocalDateTimeFromDate(Date date) {

		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
}

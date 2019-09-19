/**
 * 
 */
package com.javaee.ws.restful.service.entity.adaptor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author johnybasha
 *
 */
public class DateTimeAdaptor extends XmlAdapter<String, Date> {

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public Date unmarshal(String xml) throws Exception {
		return dateFormat.parse(xml);
	}

	@Override
	public String marshal(Date object) throws Exception {
		return dateFormat.format(object);
	}
}

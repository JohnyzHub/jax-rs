package com.javaee.ws.restful.service.sse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

/**
 * @author johnybasha
 *
 */
@Path("events")
public class EventsResource {

	@GET
	@Path("read")
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void testServerEvents(@Context SseEventSink eventSink, @Context Sse sse) {
		for (int i = 0; i < 10; i++) {
			final OutboundSseEvent event = sse.newEventBuilder().name("message").data(String.class, "Hello Client" + i)
					.build();
			eventSink.send(event);
			try {
				Thread.sleep((3 * 1000));
			} catch (InterruptedException e) {
			}
		}
	}

}
package com.javaee.ws.restful.service.sse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void testServerEvents(@Context SseEventSink eventSink, @Context Sse sse) {
		ExecutorService singleThreadService = Executors.newSingleThreadExecutor();

		singleThreadService.submit(() -> {

			for (int i = 0; i < 10; i++) {
				final OutboundSseEvent event = sse.newEventBuilder().name("Event").id(Integer.toString(i))
						.data(String.class, "Message " + i + " from Client").build();
				eventSink.send(event);
				try {
					Thread.sleep((3 * 1000));
				} catch (InterruptedException e) {
				}
			}
		});
	}

}
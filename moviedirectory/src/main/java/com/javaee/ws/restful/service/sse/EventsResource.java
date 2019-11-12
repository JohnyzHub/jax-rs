package com.javaee.ws.restful.service.sse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

/**
 * @author johnybasha
 *
 */
@ApplicationScoped
@Path("events")
public class EventsResource {

	private Sse sse;
	private OutboundSseEvent.Builder eventBuilder;
	private SseBroadcaster sseBroadcaster;

	@Context
	private void setSseProperties(Sse sse) {
		this.sse = sse;
		this.eventBuilder = sse.newEventBuilder();
		this.sseBroadcaster = sse.newBroadcaster();
	}

	@GET
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void sendServerEvents(@Context SseEventSink sseEventSink) {
		ExecutorService singleThreadService = Executors.newSingleThreadExecutor();

		singleThreadService.submit(() -> {
			for (int i = 1; i <= 10; i++) {
				final OutboundSseEvent event = createEvent(i);
				sseEventSink.send(event);
			}

			sseEventSink.close();
		});
		singleThreadService.shutdown();
	}

	@GET
	@Path("subscribe")
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void subscribe(@Context SseEventSink sseEventSink) {
		sseBroadcaster.register(sseEventSink);
		sseEventSink.send(sse.newEvent("Successfully Registered"));
	}

	@GET
	@Path("publish")
	public void publishEvents() {

		ExecutorService singleThreadService = Executors.newSingleThreadExecutor();

		singleThreadService.submit(() -> {
			for (int i = 1; i <= 10; i++) {
				final OutboundSseEvent event = createEvent(i);
				sseBroadcaster.broadcast(event);

			}
		});

		singleThreadService.shutdown();
	}

	private void sleepingThread() {
		try {
			Thread.sleep((1000));
		} catch (InterruptedException e) {
		}
	}

	private OutboundSseEvent createEvent(int index) {
		sleepingThread();
		return eventBuilder.name("Event " + index).id(Integer.toString(index))
				.data(String.class, "Message " + index + " from Server").comment("Comment " + index).build();
	}
}
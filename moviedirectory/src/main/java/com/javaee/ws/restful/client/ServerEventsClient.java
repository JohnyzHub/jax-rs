package com.javaee.ws.restful.client;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;

/**
 * @author johnybasha
 *
 */
public class ServerEventsClient {

	private static final String BASE_URI = "http://localhost:8080/moviedirectory/rest/events";

	public ServerEventsClient() {

	}

	public static void main(String args[]) {
		new ServerEventsClient().testServerEvents();
	}

	public void testServerEvents() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI);
		SseEventSource sseEventSource = SseEventSource.target(webTarget).reconnectingEvery(4, TimeUnit.SECONDS).build();
		sseEventSource.register(this::onMessage);
		sseEventSource.open();
		while (true) {
			// Keeps the client never-ending to read server events.
		}
	}

	void onMessage(InboundSseEvent event) {
		System.out.println(
				"id: " + event.getId() + ", Name: " + event.getName() + ", comment: " + event.readData(String.class));

	}

}

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
		ServerEventsClient client = new ServerEventsClient();
		client.testServerEvents();
		//client.testBroadcastServerEvents();
	}

	public void testServerEvents() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget = client.target(BASE_URI);

		try (SseEventSource sseEventSource = SseEventSource.target(webTarget).build()) {
			sseEventSource.register(this::onMessage, this::onError, this::onComplete);
			sseEventSource.open();
			this.sleepThread();
		}

		client.close();
		System.out.println("End of Test");

	}

	public void testBroadcastServerEvents() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget subscribeTarget = client.target(BASE_URI).path("subscribe");

		try (SseEventSource subscribeSource1 = SseEventSource.target(subscribeTarget).build();
				SseEventSource subscribeSource2 = SseEventSource.target(subscribeTarget).build()) {
			subscribeSource1.register(this::onMessage, this::onError, this::onComplete);
			subscribeSource1.open();

			subscribeSource2.register(this::onMessage, this::onError, this::onComplete);
			subscribeSource2.open();

			/*-
			 * Sending this call triggers the event generation.
			 * client.target(BASE_URI).path("publish").request().get(); 
			 * Either use it here or make this call using postman or curl in a separate console.
			 */

			this.sleepThread();
		}

		client.close();
		System.out.println("End of Test");

	}

	void onMessage(InboundSseEvent event) {
		System.out.println("id: " + event.getId() + ", Name: " + event.getName() + ", data: "
				+ event.readData(String.class) + ", " + event.getComment());
	}

	void onError(Throwable t) {
		System.out.println("Error " + t.getMessage());
	}

	void onComplete() {
		System.out.println("Done!");
	}

	void sleepThread() {
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
}
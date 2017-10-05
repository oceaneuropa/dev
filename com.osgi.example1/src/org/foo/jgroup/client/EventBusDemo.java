package org.foo.jgroup.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.origin.common.cluster.Event;
import org.origin.common.cluster.EventBus;
import org.origin.common.cluster.EventBusUtil;
import org.origin.common.cluster.EventContext;
import org.origin.common.cluster.EventListener;

public class EventBusDemo {

	protected String clusterName;
	protected String userName;
	protected EventListener listener;
	protected EventBus eventBus;

	/**
	 * 
	 * @param clusterName
	 * @param userName
	 */
	public EventBusDemo(String clusterName, String userName) {
		this.clusterName = clusterName;
		this.userName = userName;
	}

	public void start() {
		this.eventBus = EventBusUtil.getEventBus();

		this.listener = new EventListener() {
			@Override
			public void receivedEvent(EventContext context, Event event) {
				String clusterName = (String) context.get(EventContext.PROP_CLUSTER_NAME);
				String sourceUserName = (String) event.getSource();
				System.out.println("Client [" + userName + "] received an event: " + event.toString() + " sent by client [" + sourceUserName + "] from cluster [" + clusterName + "]");
			}
		};

		this.eventBus.addEventListener(this.listener, this.clusterName);

		inputLoop();
	}

	private void inputLoop() {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				System.out.flush();
				String message = inputReader.readLine().toLowerCase();

				if ("exit".equalsIgnoreCase(message)) {
					break;
				}

				Event event = new Event("EventBusDemo", message, userName, null);
				this.eventBus.sendEvent(event, clusterName);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		if (this.listener != null) {
			this.eventBus.removeEventListener(this.listener, this.clusterName);
			this.listener = null;
		}
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String clusterName = System.getProperty("cluster.name", "n/a");
		String userName = System.getProperty("user.name", "n/a");

		EventBusDemo demo = new EventBusDemo(clusterName, userName);
		demo.start();
		demo.stop();
	}

}

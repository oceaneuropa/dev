package org.foo.jgroup.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.origin.common.cluster.Event;
import org.origin.common.cluster.EventBus;
import org.origin.common.cluster.EventBusUtil;
import org.origin.common.cluster.EventContext;
import org.origin.common.cluster.GroupEventAdapter;
import org.origin.common.thread.ThreadPoolTimer;
import org.origin.common.util.PrettyPrinter;

public class EventBusDemo {

	protected String clusterName;
	protected String userName;
	protected EventBus eventBus;
	protected GroupEventAdapter groupEventAdapter;
	protected ThreadPoolTimer beaconTimer;

	/**
	 * 
	 * @param clusterName
	 * @param userName
	 */
	public EventBusDemo(String clusterName, String userName) {
		this.clusterName = clusterName;
		this.userName = userName;
		this.eventBus = EventBusUtil.getEventBus();
	}

	public void start() {
		this.groupEventAdapter = new GroupEventAdapter() {
			@Override
			public void onReceiveEvent(EventContext context, Event event) {
				String clusterName = (String) context.get(EventContext.PROP_CLUSTER_NAME);
				String sourceUserName = (String) event.getSource();

				System.out.println("onReceiveEvent() Event: " + event.toString() + " sent by client [" + sourceUserName + "] from cluster [" + clusterName + "]");
			}

			@Override
			public void onSetProperties(EventContext context, Map<Object, Object> properties) {
				System.out.println("onSetProperties()");
				listProperties();
			}

			@Override
			public void onSetProperty(EventContext context, Object key, Object value) {
				System.out.println("onSetProperty()");
				Object clusterName = context.get(EventContext.PROP_CLUSTER_NAME);
				Object source = context.get(EventContext.PROP_SOURCE);

				System.out.println("\tfrom cluster: " + clusterName);
				System.out.println("\tfrom source: " + source);
				System.out.println("\t" + key + " = " + value);
				listProperties();
			}

			@Override
			public void onRemoveProperty(EventContext context, Object key) {
				System.out.println("onRemoveProperty()");
				// System.out.println("\tkey = " + key);
				listProperties();
			}

			@Override
			public void onClearProperty(EventContext context) {
				System.out.println("onClearProperty()");
				listProperties();
			}
		};
		this.eventBus.addEventListener(this.groupEventAdapter, this.clusterName);
		this.eventBus.addPropertyChangeListener(this.groupEventAdapter, this.clusterName);

		Runnable beaconRunner = new Runnable() {
			@Override
			public void run() {
				heartbeat();
			}
		};
		this.beaconTimer = new ThreadPoolTimer(this.clusterName + "." + this.userName);
		this.beaconTimer.setRunnable(beaconRunner);
		this.beaconTimer.setInterval(15 * 1000);
		this.beaconTimer.start();

		inputLoop();
	}

	public void stop() {
		if (this.beaconTimer != null) {
			this.beaconTimer.stop();
		}

		removeHeartbeat();

		if (this.groupEventAdapter != null) {
			this.eventBus.removeEventListener(this.groupEventAdapter, this.clusterName);
		}
		if (this.groupEventAdapter != null) {
			this.eventBus.removePropertyChangeListener(this.groupEventAdapter, this.clusterName);
		}
		this.groupEventAdapter = null;
	}

	protected void heartbeat() {
		String key = userName + ".heartbeat";
		Date date1 = new Date();
		// long time = System.currentTimeMillis();
		// Date date2 = new Date(time);
		setProperty(key, date1);
	}

	protected void removeHeartbeat() {
		String key = this.userName + ".heartbeat";
		removeProperty(key);
	}

	private void inputLoop() {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				System.out.flush();
				String command = inputReader.readLine().toLowerCase();

				if ("exit".equalsIgnoreCase(command)) {
					break;

				} else if ("lprop".equalsIgnoreCase(command)) {
					listProperties();

				} else if (command.startsWith("setprops")) {
					String[] segments = command.split(" ");
					if (segments.length > 1) {
						Map<Object, Object> properties = new HashMap<Object, Object>();
						for (int i = 1; i < segments.length; i++) {
							String keyValuePair = segments[i];
							int index = keyValuePair.indexOf("=");
							String key = keyValuePair.substring(0, index);
							String value = keyValuePair.substring(index + 1);
							properties.put(key, value);
						}

						setProperties(properties);
					}

				} else if (command.startsWith("setprop")) {
					String[] segments = command.split(" ");
					if (segments.length > 1) {
						String keyValuePair = segments[1];
						int index = keyValuePair.indexOf("=");
						String key = keyValuePair.substring(0, index);
						String value = keyValuePair.substring(index + 1);
						setProperty(key, value);
					}

				} else if (command.startsWith("removeprop")) {
					String[] segments = command.split(" ");
					if (segments.length > 1) {
						String key = segments[1];
						removeProperty(key);
					}

				} else if ("clearprops".equalsIgnoreCase(command)) {
					clearProperties();

				} else {
					Event event = new Event("EventBusDemo", command, userName, null);
					sendEvent(event);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void sendEvent(Event event) {
		this.eventBus.sendEvent(event, this.clusterName);
	}

	protected void listProperties() {
		// System.out.println("Client [" + this.userName + "] in cluster [" + this.clusterName + "] list local properties:");
		Map<Object, Object> localProperties = this.eventBus.getLocalProperties(this.clusterName);
		PrettyPrinter.prettyPrintMap(localProperties);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	protected void setProperties(Map<Object, Object> properties) {
		// System.out.println("Client [" + this.userName + "] in cluster [" + this.clusterName + "] set properties.");

		for (Iterator<Object> keyItor = properties.keySet().iterator(); keyItor.hasNext();) {
			Object key = keyItor.next();
			Object value = properties.get(key);
			System.out.println("\t" + key + " = " + value);
		}

		try {
			this.eventBus.setProperties(this.userName, properties, this.clusterName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	protected void setProperty(Object key, Object value) {
		// System.out.println("Client [" + this.userName + "] in cluster [" + this.clusterName + "] set property.");
		// System.out.println("\t" + key + " = " + value);

		try {
			this.eventBus.setProperty(this.userName, key, value, this.clusterName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param key
	 */
	protected void removeProperty(Object key) {
		// System.out.println("Client [" + this.userName + "] in cluster [" + this.clusterName + "] remove property.");
		// System.out.println("\tkey = " + key);

		try {
			this.eventBus.removeProperty(this.userName, key, this.clusterName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	protected void clearProperties() {
		// System.out.println("Client [" + this.userName + "] in cluster [" + this.clusterName + "] clear properties:");

		try {
			this.eventBus.clearProperties(this.userName, this.clusterName);
		} catch (Exception e) {
			e.printStackTrace();
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

		// commands
		// (1) lprop
		//
		// (2) setprops
		// setprops a1=11 a2=12 a3=13
		// setprops b1=21 b2=22 b3=23
		// setprops c1=31 c2=32 c3=33
		//
		// (3) setprop
		// setprops a5=15
		// setprops b5=25
		// setprops c5=35
		//
		// (4) removeprop
		// removeprop a5
		// removeprop b5
		// removeprop c5
		//
		// (5) clearprops
		// clearprops
	}

}

// System.out.println("size = " + localProperties.size());
// if (!localProperties.isEmpty()) {
// System.out.println("--------------------------------------------------");
// for (Iterator<Object> keyItor = localProperties.keySet().iterator(); keyItor.hasNext();) {
// Object key = keyItor.next();
// Object value = localProperties.get(key);
// System.out.println(key + " = " + value);
// }
// System.out.println("--------------------------------------------------");
// }
// System.out.println();
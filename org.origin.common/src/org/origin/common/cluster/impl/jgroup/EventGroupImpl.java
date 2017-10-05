package org.origin.common.cluster.impl.jgroup;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.SynchronousQueue;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.blocks.AsyncRequestHandler;
import org.jgroups.blocks.MessageDispatcher;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.Response;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.util.Buffer;
import org.jgroups.util.Rsp;
import org.jgroups.util.RspList;
import org.jgroups.util.Util;
import org.origin.common.cluster.Event;
import org.origin.common.cluster.EventContext;
import org.origin.common.cluster.EventGroup;
import org.origin.common.cluster.EventListener;
import org.origin.common.cluster.EventPackage;
import org.origin.common.cluster.impl.EventContextImpl;

/**
 * @see org.jgroups.blocks.ReplicatedHashMap
 * 
 */
public class EventGroupImpl implements EventGroup, Closeable {

	protected String clusterName;
	protected JChannel channel;
	protected MessageDispatcher messageDispatcher;
	protected RequestOptions options = new RequestOptions(ResponseMode.GET_NONE, 5000);

	// keeps track of all EventGroupImpl instances from different jvm which joined this group.
	protected List<Address> members = new ArrayList<>();

	protected List<EventListener> eventListeners = new ArrayList<EventListener>();
	// protected Queue<Event> events = new ConcurrentLinkedQueue<Event>();
	protected Queue<Event> events = new SynchronousQueue<Event>();

	/**
	 * 
	 * @param clusterName
	 * @param channel
	 */
	public EventGroupImpl(String clusterName, JChannel channel) {
		this.clusterName = clusterName;
		this.channel = channel;
	}

	public JChannel getChannel() {
		return this.channel;
	}

	public boolean isConnected() {
		return this.channel.isConnected();
	}

	public Address getLocalAddress() {
		return this.channel != null ? this.channel.getAddress() : null;
	}

	protected Queue<Event> getEvents() {
		return this.events;
	}

	/* ------------------------------------------------------- */
	// options
	/* ------------------------------------------------------- */
	public boolean isBlockingUpdates() {
		return this.options.getMode() == ResponseMode.GET_ALL;
	}

	/**
	 * Whether updates across the cluster should be asynchronous (default) or synchronous.
	 * 
	 * @param blockingUpdates
	 */
	public void setBlockingUpdates(boolean blockingUpdates) {
		this.options.setMode(blockingUpdates ? ResponseMode.GET_ALL : ResponseMode.GET_NONE);
	}

	/**
	 * The timeout (in milliseconds) for blocking updates
	 */
	public long getTimeout() {
		return this.options.getTimeout();
	}

	/**
	 * Sets the cluster call timeout (until all acks have been received)
	 * 
	 * @param timeout
	 *            The timeout (in milliseconds) for blocking updates
	 */
	public void setTimeout(long timeout) {
		this.options.setTimeout(timeout);
	}

	/* ------------------------------------------------------- */
	// life cycle
	/* ------------------------------------------------------- */
	@Override
	public void start() throws Exception {
		this.messageDispatcher = createMessageDispatcher(this.channel);

		long getStateTimeout = getTimeout();
		if (getStateTimeout < 0) {
			getStateTimeout = 0;
		}
		this.channel.getState(null, getStateTimeout);
	}

	/**
	 * 
	 * @param channel
	 * @return
	 */
	protected MessageDispatcher createMessageDispatcher(JChannel channel) {
		Receiver receiver = new Receiver() {
			@Override
			public void viewAccepted(View newView) {
				System.out.println(getClass().getSimpleName() + ".viewAccepted(View)");
			}

			@Override
			public void receive(Message message) {
				System.out.println(getClass().getSimpleName() + ".viewAccepted(Message)");
			}

			@Override
			public void getState(OutputStream outputStream) throws Exception {
				System.out.println(getClass().getSimpleName() + ".getState(OutputStream)");

				// I am coordinator number. A new member send request to me to get the cluster state.
				// - the state could be the payload of the cluster (e.g. data of business logic)
				// - write the payload to output stream
				ObjectOutputStream objectOutput = null;
				try {
					Queue<Event> eventsCopy = new ConcurrentLinkedQueue<Event>();
					synchronized (EventGroupImpl.this.events) {
						eventsCopy.addAll(EventGroupImpl.this.events);
					}

					objectOutput = new ObjectOutputStream(new BufferedOutputStream(outputStream, 1024));
					objectOutput.writeObject(eventsCopy);

					System.out.println("events state sent successfully");
					System.out.println("----------------------------------");
					for (Event event : eventsCopy) {
						System.out.println(event);
					}
					System.out.println("----------------------------------");
					System.out.println();

				} catch (Exception e) {
					e.printStackTrace();

				} finally {
					objectOutput.close();
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void setState(InputStream inputStream) throws Exception {
				System.out.println(getClass().getSimpleName() + ".setState(InputStream)");

				// I am the new member. I have requested to get the cluster state.
				// - the coordinator number has sent me the payload of the cluster (e.g. data of business logic)
				// - read the payload from the input stream
				// - keep the copy of the payload myself.
				Queue<Event> eventsCopy = null;

				ObjectInputStream objectInput = null;
				try {
					objectInput = new ObjectInputStream(inputStream);
					eventsCopy = (Queue<Event>) objectInput.readObject();

				} catch (Exception e) {
					e.printStackTrace();

				} finally {
					objectInput.close();
				}

				if (eventsCopy != null) {
					System.out.println("events state received successfully");
					System.out.println("----------------------------------");
					for (Event event : eventsCopy) {
						System.out.println(event);
					}
					System.out.println("----------------------------------");
					System.out.println();

				} else {
					System.out.println("events state received is null");
				}
			}

			/**
			 * Called when a member is suspected
			 * 
			 */
			@Override
			public void suspect(Address suspectedMember) {
				System.out.println(getClass().getSimpleName() + ".suspect(Address)");
			}

			/**
			 * Block sending and receiving of messages until ViewAccepted is called
			 * 
			 */
			@Override
			public void block() {
				System.out.println(getClass().getSimpleName() + ".block()");
			}

			@Override
			public void unblock() {
				System.out.println(getClass().getSimpleName() + ".unblock()");
			}
		};

		AsyncRequestHandler requestHandler = new AsyncRequestHandler() {
			@Override
			public void handle(Message receivedMessage, Response response) throws Exception {
				Object returnValue = handle(receivedMessage);
				if (response != null && returnValue != null) {
					response.send(returnValue, false);
				}
			}

			@Override
			public Object handle(Message receivedMessage) throws Exception {
				EventPackage receivedEventPackage = getEventPackage(receivedMessage);
				if (receivedEventPackage != null) {
					notifyListeners(receivedEventPackage);

					// Event is received by the group member
					return new Event("org.origin.common", "received", getChannel().getName(), "");

				} else {
					// Event is not received by the group member
					return new Event("org.origin.common", "not_received", getChannel().getName(), "");
				}
			}
		};

		MessageDispatcher messageDispatcher = new MessageDispatcher(channel, receiver, receiver, requestHandler);

		return messageDispatcher;
	}

	@Override
	public void stop() throws Exception {
		if (this.messageDispatcher != null) {
			this.messageDispatcher.stop();
			this.messageDispatcher = null;
		}
		// Util.close(this.channel);
	}

	/* ------------------------------------------------------- */
	// listener and notification
	/* ------------------------------------------------------- */
	/**
	 * 
	 * @param receivedMessage
	 * @return
	 */
	protected EventPackage getEventPackage(Message receivedMessage) {
		EventPackage eventPackage = null;
		if (receivedMessage != null) {
			Object body = receivedMessage.getObject();
			if (!(body instanceof EventPackage)) {
				throw new IllegalArgumentException("The received message does not contain an EventPackage object");
			}

			eventPackage = (EventPackage) body;
		}
		return eventPackage;
	}

	/**
	 * 
	 * @param event
	 */
	protected void notifyListeners(EventPackage eventPackage) {
		// System.out.println(getClass().getSimpleName() + ".notifyListeners(Event)");
		Event receivedEvent = eventPackage.getEvent();
		String clusterName = eventPackage.getClusterName();

		EventContextImpl context = new EventContextImpl();
		context.set(EventContext.PROP_CLUSTER_NAME, clusterName);

		synchronized (this.eventListeners) {
			for (EventListener eventListener : eventListeners) {
				eventListener.receivedEvent(context, receivedEvent);
			}
		}
	}

	@Override
	public boolean addEventListener(EventListener eventListener) {
		synchronized (this.eventListeners) {
			if (eventListener != null && !this.eventListeners.contains(eventListener)) {
				return this.eventListeners.add(eventListener);
			}
			return false;
		}
	}

	@Override
	public boolean removeEventListener(EventListener eventListener) {
		synchronized (this.eventListeners) {
			if (eventListener != null && this.eventListeners.contains(eventListener)) {
				return this.eventListeners.remove(eventListener);
			}
			return false;
		}
	}

	@Override
	public EventListener[] getEventListeners() {
		synchronized (this.eventListeners) {
			return this.eventListeners.toArray(new EventListener[this.eventListeners.size()]);
		}
	}

	@Override
	public void sendEvent(Event event) throws Exception {
		System.out.println(getClass().getSimpleName() + ".notifyEvent(Event)");
		if (event == null) {
			throw new IllegalArgumentException("event is null.");
		}
		System.out.println("\t" + event.toString());

		castEvent(event);
	}

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	protected synchronized void castEvent(Event event) throws Exception {
		EventPackage eventPackage = new EventPackage(event, this.clusterName);

		Buffer buf = Util.objectToBuffer(eventPackage);
		Message message = new Message().setBuffer(buf);

		RspList<?> responseList = this.messageDispatcher.castMessage(null, message, options);

		System.out.println("\tEvent is sent successfully.");
		if (responseList != null) {
			System.out.println("\tresponse size: " + responseList.size());
			for (Iterator<Address> addrItor = responseList.keySet().iterator(); addrItor.hasNext();) {
				Address address = addrItor.next();
				Rsp<?> response = responseList.get(address);

				Address sender = response.getSender();
				Object responseValue = response.getValue();
				Throwable exception = response.getException();
				System.out.println("\t\tsender = " + sender);
				System.out.println("\t\tresponseValue = " + responseValue);
				System.out.println("\t\texception = " + exception);
			}
		}
	}

	/* ------------------------------------------------------- */
	// Closeable interface
	/* ------------------------------------------------------- */
	@Override
	public void close() throws IOException {
		try {
			stop();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}

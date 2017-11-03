package org.origin.common.cluster;

import java.util.Date;
import java.util.Map;

import org.origin.common.thread.ThreadPoolTimer;

public class EventService {

	protected String clusterName;
	protected String name;

	protected EventBus eventBus;
	protected EventListener eventListener;
	protected PropertyChangeListener propertyChangeListener;
	protected ThreadPoolTimer beaconTimer;

	protected Runnable beaconRunnable;

	/**
	 * 
	 * @param clusterName
	 * @param name
	 */
	public EventService(String clusterName, String name) {
		this.clusterName = clusterName;
		this.name = name;
	}

	public String getClusterName() {
		return this.clusterName;
	}

	public String getName() {
		return this.name;
	}

	public Runnable getBeaconRunnable() {
		return this.beaconRunnable;
	}

	public void setRunnable(Runnable runnable) {
		this.beaconRunnable = runnable;
	}

	public void start() {
		// Add listeners to <namespace> cluster
		String clusterName = getClusterName();

		this.eventBus = EventBusUtil.getEventBus();
		this.eventListener = new EventListener() {
			@Override
			public void onReceiveEvent(EventContext context, Event event) {
			}
		};
		this.propertyChangeListener = new PropertyChangeListener() {
			@Override
			public void onSetProperties(EventContext context, Map<Object, Object> properties) {
			}

			@Override
			public void onSetProperty(EventContext context, Object key, Object value) {
			}

			@Override
			public void onRemoveProperty(EventContext context, Object key) {
			}

			@Override
			public void onClearProperty(EventContext context) {
			}
		};
		this.eventBus.addEventListener(this.eventListener, clusterName);
		this.eventBus.addPropertyChangeListener(this.propertyChangeListener, clusterName);

		startBeacon();
	}

	public void stop() {
		stopBeacon();

		// Remove listeners from <namespace> cluster
		if (this.eventBus != null) {
			String clusterName = getClusterName();
			if (this.eventListener != null) {
				this.eventBus.removeEventListener(this.eventListener, clusterName);
				this.eventListener = null;
			}
			if (this.propertyChangeListener != null) {
				this.eventBus.removePropertyChangeListener(this.propertyChangeListener, clusterName);
				this.propertyChangeListener = null;
			}
			this.eventBus = null;
		}
	}

	protected void startBeacon() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					String key = getName() + ".heartbeat";
					eventBus.setProperty(getName(), key, new Date(), getClusterName());
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					if (beaconRunnable != null) {
						beaconRunnable.run();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		this.beaconTimer = new ThreadPoolTimer(getClusterName() + "." + getName());
		this.beaconTimer.setRunnable(runnable);
		this.beaconTimer.setInterval(15 * 1000);
		this.beaconTimer.start();
	}

	protected void stopBeacon() {
		if (this.beaconTimer != null) {
			this.beaconTimer.stop();
		}

		String key = getName() + ".heartbeat";
		try {
			this.eventBus.removeProperty(getName(), key, getClusterName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

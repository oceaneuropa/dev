package org.origin.common.rest.switcher.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import org.origin.common.event.PropertyChangeEvent;
import org.origin.common.event.PropertyChangeListener;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherInput;
import org.origin.common.rest.switcher.SwitcherItemMonitor;
import org.origin.common.rest.switcher.SwitcherMonitor;
import org.origin.common.rest.switcher.SwitcherPolicy;

public class SwitcherImpl<ITEM> implements Switcher<ITEM> {

	protected SwitcherInput<ITEM> input;
	protected SwitcherPolicy<ITEM> policy;
	protected SwitcherMonitor<ITEM> monitor;
	protected Map<ITEM, SwitcherItemMonitor<ITEM>> itemMonitors = new HashMap<ITEM, SwitcherItemMonitor<ITEM>>();
	protected PropertyChangeListener listener;

	public SwitcherImpl() {
	}

	@Override
	public SwitcherInput<ITEM> getInput() {
		return this.input;
	}

	@Override
	public void setInput(SwitcherInput<ITEM> input) {
		this.input = input;
	}

	@Override
	public synchronized SwitcherPolicy<ITEM> getPolicy() {
		if (this.policy == null) {
			this.policy = new SwitcherPolicyRoundRobinImpl<ITEM>();
		}
		return this.policy;
	}

	@Override
	public synchronized void setPolicy(SwitcherPolicy<ITEM> policy) {
		this.policy = policy;
	}

	@Override
	public synchronized SwitcherMonitor<ITEM> getMonitor() {
		return this.monitor;
	}

	@Override
	public synchronized void setMonitor(SwitcherMonitor<ITEM> monitor) {
		this.monitor = monitor;
	}

	protected void checkPolicy() {
		if (this.policy == null) {
			throw new IllegalStateException("Switcher policy is not set.");
		}
	}

	protected void checkInput() {
		if (this.input == null) {
			throw new IllegalStateException("Switcher input is not set.");
		}
	}

	public void selfCheck() {
		checkPolicy();
		checkInput();
	}

	@Override
	public void start() {
		selfCheck();

		// Start listening adding/removing of ITEMs
		// - add property monitor to added ITEM
		// - remove property monitor from removed ITEM
		this.listener = new PropertyChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void notifyEvent(PropertyChangeEvent event) {
				String eventName = event.getName();
				if (SwitcherInput.P_ITEM_ADDED.equals(eventName)) {
					Object newValue = event.getNewValue();
					addItemMonitor((ITEM) newValue);

				} else if (SwitcherInput.P_ITEM_REMOVED.equals(eventName)) {
					Object oldValue = event.getOldValue();
					removeItemMonitor((ITEM) oldValue);
				}
			}
		};
		this.input.addPropertyChangeListener(this.listener);

		// Start policy
		this.policy.start(this);

		// Start monitor
		if (this.monitor != null) {
			// start monitoring status of each barrel
			List<ITEM> items = this.input.getItems();
			synchronized (this.itemMonitors) {
				for (ITEM item : items) {
					addItemMonitor(item);
				}
			}

			// start monitoring adding/removing of barrels (e.g. URIs)
			this.monitor.start(this);
		}
	}

	@Override
	public void stop() {
		// Stop monitor
		if (this.monitor != null) {
			// stop monitoring adding/removing of barrels (e.g. URIs)
			this.monitor.stop(this);

			// start monitoring status of each barrel (e.g. a URI)
			List<ITEM> items = this.input.getItems();
			synchronized (this.itemMonitors) {
				for (ITEM item : items) {
					removeItemMonitor(item);
				}
			}
		}

		// Stop policy
		this.policy.stop(this);

		// Stop listening adding/removing of ITEMs
		if (this.listener != null) {
			this.input.removePropertyChangeListener(this.listener);
			this.listener = null;
		}
	}

	protected void addItemMonitor(ITEM item) {
		if (this.monitor != null) {
			removeItemMonitor(item);
			synchronized (this.itemMonitors) {
				SwitcherItemMonitor<ITEM> itemMonitor = this.monitor.createItemMonitor(item);
				if (itemMonitor != null) {
					this.itemMonitors.put(item, itemMonitor);
					itemMonitor.start(this);
				}
			}
		}
	}

	protected void removeItemMonitor(ITEM item) {
		synchronized (this.itemMonitors) {
			SwitcherItemMonitor<ITEM> itemMonitor = this.itemMonitors.remove(item);
			if (itemMonitor != null) {
				itemMonitor.stop(this);
			}
		}
	}

	@Override
	public ITEM getNext(ContainerRequestContext requestContext, String methodPath) {
		return getPolicy().getNext(requestContext, methodPath);
	}

	@Override
	public ITEM getNext(ContainerRequestContext requestContext, String methodPath, int retry, long interval) {
		if (retry <= 0) {
			return getNext(requestContext, methodPath);
		}

		for (int i = 0; i < retry; i++) {
			ITEM item = getNext(requestContext, methodPath);
			if (item != null) {
				return item;
			}
			if (interval > 0) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}

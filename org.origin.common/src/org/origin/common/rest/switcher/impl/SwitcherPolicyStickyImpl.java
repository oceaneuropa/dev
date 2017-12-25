package org.origin.common.rest.switcher.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherPolicy;

public abstract class SwitcherPolicyStickyImpl<ITEM> implements SwitcherPolicy<ITEM> {

	protected Switcher<ITEM> switcher;
	protected Map<String, Integer> methodPathToPointer = new HashMap<String, Integer>();
	protected Map<String, ITEM> clientToStickedToItem = new HashMap<String, ITEM>();

	@Override
	public void start(Switcher<ITEM> switcher) {
		this.switcher = switcher;
	}

	@Override
	public void stop(Switcher<ITEM> switcher) {
		this.switcher = null;
	}

	protected void checkSwitcher() {
		if (this.switcher == null) {
			throw new IllegalStateException("Switcher is null.");
		}
	}

	@Override
	public synchronized ITEM getNext(ContainerRequestContext requestContext, String methodPath) {
		checkSwitcher();

		// check whether items are available
		List<ITEM> items = this.switcher.getInput().getItems();
		if (items == null || items.isEmpty()) {
			return null;
		}

		String clientId = getClientId(requestContext);
		if (clientId == null) {
			// 1. Cannot find clientId from request
			// - no sticky accessing of remote web services
			// - as how exactly SwitcherPolicyRoundRobinImpl works

			// initialize the pointer or reset the pointer
			Integer pointer = this.methodPathToPointer.get(methodPath);
			if (pointer == null || pointer >= items.size()) {
				pointer = 0;
			}
			ITEM item = items.get(pointer);

			// update pointer
			this.methodPathToPointer.put(methodPath, ++pointer);

			return item;

		} else {
			// 2. The clientId can be found from request
			// - perform sticky accessing of remote web services
			ITEM stickedToItem = this.clientToStickedToItem.get(clientId);

			// if the stickedItem is still one of the input, return it.
			if (stickedToItem != null && items.contains(stickedToItem)) {
				return stickedToItem;

			} else {
				// Note:
				// - there is no stickedItem or there is stickedItem but became unavailable
				// - in either case, assign another ITEM to the client.

				// initialize the pointer or reset the pointer
				Integer pointer = this.methodPathToPointer.get(methodPath);
				if (pointer == null || pointer >= items.size()) {
					pointer = 0;
				}
				ITEM item = items.get(pointer);

				// update pointer
				this.methodPathToPointer.put(methodPath, ++pointer);

				// keep the client and the sticked to ITEM
				this.clientToStickedToItem.put(clientId, item);

				return item;
			}
		}
	}

	protected abstract String getClientId(ContainerRequestContext requestContext);

}

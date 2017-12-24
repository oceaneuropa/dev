package org.origin.common.switcher.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.switcher.Switcher;
import org.origin.common.switcher.SwitcherPolicy;

public class SwitcherPolicyRoundRobinImpl<ITEM> implements SwitcherPolicy<ITEM> {

	protected Switcher<ITEM> switcher;
	protected Map<String, Integer> methodPathToPointer = new HashMap<String, Integer>();

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
	public synchronized ITEM getNext(String methodPath) {
		checkSwitcher();

		// check whether items are available
		List<ITEM> items = this.switcher.getInput().getItems();
		if (items == null || items.isEmpty()) {
			return null;
		}

		Integer pointer = this.methodPathToPointer.get(methodPath);
		// initialize the pointer or reset the pointer
		if (pointer == null || pointer >= items.size()) {
			pointer = 0;
		}

		ITEM item = items.get(pointer);

		// update pointer
		pointer += 1;
		this.methodPathToPointer.put(methodPath, pointer);

		return item;
	}

}

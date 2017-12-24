package org.origin.common.switcher.impl;

import org.origin.common.switcher.Switcher;
import org.origin.common.switcher.SwitcherPolicy;

public class SwitcherPolicyStickyRoundRobinImpl<ITEM> implements SwitcherPolicy<ITEM> {

	protected Switcher<ITEM> switcher;

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
	public ITEM getNext(String methodPath) {
		checkSwitcher();

		return null;
	}

}

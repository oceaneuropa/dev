package org.orbit.infra.runtime.lb;

import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherItemMonitor;
import org.origin.common.rest.switcher.SwitcherMonitor;

public class SwitcherMonitorURIImpl<URI> implements SwitcherMonitor<URI> {

	protected Switcher<URI> switcher;

	protected void checkSwitcher() {
		if (this.switcher == null) {
			throw new IllegalStateException("Switcher is null.");
		}
	}

	@Override
	public void start(Switcher<URI> switcher) {
		// keep reference
		this.switcher = switcher;
		// start tracking and updating
	}

	@Override
	public void stop(Switcher<URI> switcher) {
		// stop tracking and updating
		// cleanup
		this.switcher = null;
	}

	@Override
	public SwitcherItemMonitor<URI> createItemMonitor(URI item) {
		return null;
	}

}

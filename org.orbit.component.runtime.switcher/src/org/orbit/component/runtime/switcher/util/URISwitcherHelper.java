package org.orbit.component.runtime.switcher.util;

import java.net.URI;
import java.util.List;

import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherMonitor;
import org.origin.common.rest.switcher.SwitcherPolicy;
import org.origin.common.rest.switcher.impl.SwitcherImpl;
import org.origin.common.rest.switcher.impl.SwitcherInputURIImpl;
import org.origin.common.rest.switcher.impl.SwitcherPolicyRoundRobinImpl;

public class URISwitcherHelper {

	public static URISwitcherHelper INSTANCE = new URISwitcherHelper();

	public Switcher<URI> createURISwitcher(List<URI> uriList, int policyMode) {
		Switcher<URI> switcher = new SwitcherImpl<URI>();

		// (1) set input
		SwitcherInputURIImpl input = new SwitcherInputURIImpl(uriList);
		switcher.setInput(input);

		// (2) set policy
		SwitcherPolicy<URI> policy = null;
		if ((SwitcherPolicy.MODE_ROUND_ROBIN & policyMode) == SwitcherPolicy.MODE_ROUND_ROBIN) {
			policy = new SwitcherPolicyRoundRobinImpl<URI>();
		} else if ((SwitcherPolicy.MODE_STICKY & policyMode) == SwitcherPolicy.MODE_STICKY) {
			policy = new SwitcherPolicyStickyOrbitSessionImpl<URI>();
		}
		if (policy != null) {
			switcher.setPolicy(policy);
		}

		// (3) set monitor
		SwitcherMonitor<URI> monitor = new SwitcherMonitorURIImpl<URI>();
		switcher.setMonitor(monitor);

		return switcher;
	}

}

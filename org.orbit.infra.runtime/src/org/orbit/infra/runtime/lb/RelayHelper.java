package org.orbit.infra.runtime.lb;

import java.net.URI;
import java.util.List;

import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherMonitor;
import org.origin.common.rest.switcher.SwitcherPolicy;
import org.origin.common.rest.switcher.impl.SwitcherImpl;
import org.origin.common.rest.switcher.impl.SwitcherInputURIImpl;
import org.origin.common.rest.switcher.impl.SwitcherPolicyRoundRobinImpl;

public class RelayHelper {

	public static RelayHelper INSTANCE = new RelayHelper();

	public Switcher<URI> createURISwitcher(List<URI> uriList, int policyMode) {
		Switcher<URI> switcher = new SwitcherImpl<URI>();

		// (1) set input
		SwitcherInputURIImpl input = new SwitcherInputURIImpl(uriList);
		switcher.setInput(input);

		// (2) set policy
		SwitcherPolicy<URI> policy = null;
		if ((SwitcherPolicy.MODE_ROUND_ROBIN & policyMode) == SwitcherPolicy.MODE_ROUND_ROBIN) {
			policy = new SwitcherPolicyRoundRobinImpl<URI>();
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

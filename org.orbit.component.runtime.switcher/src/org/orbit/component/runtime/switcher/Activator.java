package org.orbit.component.runtime.switcher;

import java.net.URI;
import java.util.List;

import org.orbit.component.runtime.switcher.util.SwitcherMonitorURIImpl;
import org.orbit.component.runtime.switcher.util.SwitcherPolicyStickyOrbitSessionImpl;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryJerseyImpl;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherMonitor;
import org.origin.common.rest.switcher.SwitcherPolicy;
import org.origin.common.rest.switcher.impl.SwitcherImpl;
import org.origin.common.rest.switcher.impl.SwitcherInputURIImpl;
import org.origin.common.rest.switcher.impl.SwitcherPolicyRoundRobinImpl;
import org.origin.common.util.URIUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;

	public static BundleContext getContext() {
		return bundleContext;
	}

	protected AuthWSApplicationSwitcher authSwitcher;
	protected TransferAgentWSApplicationSwitcher transferAgentSwitcher;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = bundleContext;

		WSClientFactory factory = createClientFactory(bundleContext);

		startAuthSwitcher(bundleContext, "/orbit/v1/auth", factory, URIUtil.toList("http://127.0.0.1:11001/orbit/v1/auth;http://127.0.0.1:11002/orbit/v1/auth"));
		startTransferAgentSwitcher(bundleContext, "/orbit/v1/ta", factory, URIUtil.toList("http://127.0.0.1:12001/orbit/v1/ta;http://127.0.0.1:12002/orbit/v1/ta"));
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		stopTransferAgentSwitcher(bundleContext);
		stopAuthSwitcher(bundleContext);

		Activator.bundleContext = null;
	}

	protected WSClientFactory createClientFactory(BundleContext bundleContext) {
		return new WSClientFactoryJerseyImpl();
	}

	protected void startAuthSwitcher(BundleContext bundleContext, String contextRoot, WSClientFactory factory, List<URI> authUriList) {
		Switcher<URI> authUriSwitcher = createUriSwitcher(authUriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.authSwitcher = new AuthWSApplicationSwitcher(contextRoot, factory, authUriSwitcher);
		this.authSwitcher.start(bundleContext);
	}

	protected void stopAuthSwitcher(BundleContext bundleContext) {
		if (this.authSwitcher != null) {
			this.authSwitcher.stop(bundleContext);
			this.authSwitcher = null;
		}
	}

	protected void startTransferAgentSwitcher(BundleContext bundleContext, String contextRoot, WSClientFactory factory, List<URI> taUriList) {
		Switcher<URI> taUriSwitcher = createUriSwitcher(taUriList, SwitcherPolicy.MODE_STICKY);
		this.transferAgentSwitcher = new TransferAgentWSApplicationSwitcher(contextRoot, factory, taUriSwitcher);
		this.transferAgentSwitcher.start(bundleContext);
	}

	protected void stopTransferAgentSwitcher(BundleContext bundleContext) {
		if (this.transferAgentSwitcher != null) {
			this.transferAgentSwitcher.stop(bundleContext);
			this.transferAgentSwitcher = null;
		}
	}

	protected Switcher<URI> createUriSwitcher(List<URI> uriList, int policyMode) {
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

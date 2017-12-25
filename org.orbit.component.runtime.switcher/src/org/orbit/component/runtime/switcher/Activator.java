package org.orbit.component.runtime.switcher;

import java.net.URI;
import java.util.List;

import org.orbit.component.runtime.switcher.util.SwitcherMonitorURIImpl;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryJerseyImpl;
import org.origin.common.switcher.Switcher;
import org.origin.common.switcher.SwitcherMonitor;
import org.origin.common.switcher.impl.SwitcherImpl;
import org.origin.common.switcher.impl.SwitcherInputURIImpl;
import org.origin.common.switcher.impl.SwitcherPolicyRoundRobinImpl;
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

		// Start Auth switcher
		startAuthSwitcher(bundleContext, "/orbit/v1/auth", factory, URIUtil.toList("http://127.0.0.1:11001/orbit/v1/auth;http://127.0.0.1:11002/orbit/v1/auth"));

		// Start TransferAgent switcher
		startTransferAgentSwitcher(bundleContext, "/orbit/v1/ta", factory, URIUtil.toList("http://127.0.0.1:12001/orbit/v1/ta;http://127.0.0.1:12002/orbit/v1/ta"));
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// Stop TransferAgent switcher
		stopTransferAgentSwitcher(bundleContext);

		// Stop Auth switcher
		stopAuthSwitcher(bundleContext);

		Activator.bundleContext = null;
	}

	protected WSClientFactory createClientFactory(BundleContext bundleContext) {
		return new WSClientFactoryJerseyImpl();
	}

	protected void startAuthSwitcher(BundleContext bundleContext, String contextRoot, WSClientFactory factory, List<URI> authUriList) {
		Switcher<URI> authUriSwitcher = createSwitcher(authUriList);
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
		Switcher<URI> taUriSwitcher = createSwitcher(taUriList);
		this.transferAgentSwitcher = new TransferAgentWSApplicationSwitcher(contextRoot, factory, taUriSwitcher);
		this.transferAgentSwitcher.start(bundleContext);
	}

	protected void stopTransferAgentSwitcher(BundleContext bundleContext) {
		if (this.transferAgentSwitcher != null) {
			this.transferAgentSwitcher.stop(bundleContext);
			this.transferAgentSwitcher = null;
		}
	}

	protected Switcher<URI> createSwitcher(List<URI> uriList) {
		Switcher<URI> switcher = new SwitcherImpl<URI>();

		SwitcherInputURIImpl input = new SwitcherInputURIImpl(uriList);
		switcher.setInput(input);

		SwitcherPolicyRoundRobinImpl<URI> policy = new SwitcherPolicyRoundRobinImpl<URI>();
		switcher.setPolicy(policy);

		SwitcherMonitor<URI> monitor = new SwitcherMonitorURIImpl<URI>();
		switcher.setMonitor(monitor);

		return switcher;
	}

}

package org.orbit.component.runtime.switcher;

import java.net.URI;
import java.util.List;

import org.orbit.component.runtime.switcher.util.URISwitcherHelper;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryJerseyImpl;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherPolicy;
import org.origin.common.util.URIUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;

	public static BundleContext getContext() {
		return bundleContext;
	}

	// tier1
	protected UserRegistryWSApplicationSwitcher userRegistrySwitcher;
	protected AuthWSApplicationSwitcher authSwitcher;

	// tier2
	protected AppStoreWSApplicationSwitcher appStoreSwitcher;

	// tier3
	protected DomainServiceWSApplicationSwitcher domainServiceSwitcher;
	protected TransferAgentWSApplicationSwitcher transferAgentSwitcher;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = bundleContext;

		WSClientFactory factory = createClientFactory(bundleContext);

		// tier1
		startUserRegistrySwitcher(bundleContext, "/orbit/v1/userregistry", factory, URIUtil.toList("http://127.0.0.1:11001/orbit/v1/userregistry;http://127.0.0.1:11002/orbit/v1/userregistry"));
		startAuthSwitcher(bundleContext, "/orbit/v1/auth", factory, URIUtil.toList("http://127.0.0.1:11001/orbit/v1/auth;http://127.0.0.1:11002/orbit/v1/auth"));

		// tier2
		startAppStoreSwitcher(bundleContext, "/orbit/v1/appstore", factory, URIUtil.toList("http://127.0.0.1:11001/orbit/v1/appstore;http://127.0.0.1:11002/orbit/v1/appstore"));

		// tier3
		startDomainServiceSwitcher(bundleContext, "/orbit/v1/domain", factory, URIUtil.toList("http://127.0.0.1:12001/orbit/v1/domain;http://127.0.0.1:12002/orbit/v1/domain"));
		startTransferAgentSwitcher(bundleContext, "/orbit/v1/ta", factory, URIUtil.toList("http://127.0.0.1:12001/orbit/v1/ta;http://127.0.0.1:12002/orbit/v1/ta"));
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// tier3
		stopDomainServiceSwitcher(bundleContext);
		stopTransferAgentSwitcher(bundleContext);

		// tier2
		stopAppStoreSwitcher(bundleContext);

		// tier1
		stopUserRegistrySwitcher(bundleContext);
		stopAuthSwitcher(bundleContext);

		Activator.bundleContext = null;
	}

	protected WSClientFactory createClientFactory(BundleContext bundleContext) {
		return new WSClientFactoryJerseyImpl();
	}

	protected void startUserRegistrySwitcher(BundleContext bundleContext, String contextRoot, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = URISwitcherHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.userRegistrySwitcher = new UserRegistryWSApplicationSwitcher(contextRoot, factory, uriSwitcher);
		this.userRegistrySwitcher.start(bundleContext);
	}

	protected void stopUserRegistrySwitcher(BundleContext bundleContext) {
		if (this.userRegistrySwitcher != null) {
			this.userRegistrySwitcher.stop(bundleContext);
			this.userRegistrySwitcher = null;
		}
	}

	protected void startAuthSwitcher(BundleContext bundleContext, String contextRoot, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = URISwitcherHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.authSwitcher = new AuthWSApplicationSwitcher(contextRoot, factory, uriSwitcher);
		this.authSwitcher.start(bundleContext);
	}

	protected void stopAuthSwitcher(BundleContext bundleContext) {
		if (this.authSwitcher != null) {
			this.authSwitcher.stop(bundleContext);
			this.authSwitcher = null;
		}
	}

	protected void startAppStoreSwitcher(BundleContext bundleContext, String contextRoot, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = URISwitcherHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.appStoreSwitcher = new AppStoreWSApplicationSwitcher(contextRoot, factory, uriSwitcher);
		this.appStoreSwitcher.start(bundleContext);
	}

	protected void stopAppStoreSwitcher(BundleContext bundleContext) {
		if (this.appStoreSwitcher != null) {
			this.appStoreSwitcher.stop(bundleContext);
			this.appStoreSwitcher = null;
		}
	}

	protected void startDomainServiceSwitcher(BundleContext bundleContext, String contextRoot, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = URISwitcherHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);
		this.domainServiceSwitcher = new DomainServiceWSApplicationSwitcher(contextRoot, factory, uriSwitcher);
		this.domainServiceSwitcher.start(bundleContext);
	}

	protected void stopDomainServiceSwitcher(BundleContext bundleContext) {
		if (this.domainServiceSwitcher != null) {
			this.domainServiceSwitcher.stop(bundleContext);
			this.domainServiceSwitcher = null;
		}
	}

	protected void startTransferAgentSwitcher(BundleContext bundleContext, String contextRoot, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = URISwitcherHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);
		this.transferAgentSwitcher = new TransferAgentWSApplicationSwitcher(contextRoot, factory, uriSwitcher);
		this.transferAgentSwitcher.start(bundleContext);
	}

	protected void stopTransferAgentSwitcher(BundleContext bundleContext) {
		if (this.transferAgentSwitcher != null) {
			this.transferAgentSwitcher.stop(bundleContext);
			this.transferAgentSwitcher = null;
		}
	}

}

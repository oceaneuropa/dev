package org.orbit.component.runtime.relay.extensions;

import java.net.URI;
import java.util.List;

import org.orbit.component.runtime.relay.Extensions;
import org.orbit.component.runtime.relay.desc.UserRegistryWSApplicationDesc;
import org.orbit.component.runtime.relay.util.SwitcherUtil;
import org.orbit.platform.sdk.URLProvider;
import org.orbit.platform.sdk.URLProviderImpl;
import org.orbit.platform.sdk.WSRelayControlImpl;
import org.orbit.platform.sdk.extension.util.ProgramExtension;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherPolicy;
import org.osgi.framework.BundleContext;

public class UserRegistryWSRelayControl extends WSRelayControlImpl {

	public static UserRegistryWSRelayControl INSTANCE = new UserRegistryWSRelayControl();

	@Override
	public synchronized void start(BundleContext bundleContext, WSClientFactory factory, String hostURL, String contextRoot, List<URI> targetURLs) {
		String url = getURL(hostURL, contextRoot);

		// Start relay ws app
		WSRelayApplication wsApp = this.wsAppMap.get(url);
		if (wsApp == null) {
			UserRegistryWSApplicationDesc wsAppDesc = new UserRegistryWSApplicationDesc(contextRoot);
			Switcher<URI> switcher = SwitcherUtil.INSTANCE.createURISwitcher(targetURLs, SwitcherPolicy.MODE_ROUND_ROBIN);
			WSRelayApplication newWsApp = new WSRelayApplication(wsAppDesc, switcher, factory);
			newWsApp.start(bundleContext);

			this.wsAppMap.put(url, newWsApp);
		}

		// Register URL provider extension
		ProgramExtension urlProviderExtension = this.extensionMap.get(url);
		if (urlProviderExtension == null) {
			urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.USER_REGISTRY_URL_PROVIDER_EXTENSION_ID);
			urlProviderExtension.setName("URL provider for user registry");
			urlProviderExtension.setDescription("URL provider for user registry description");
			urlProviderExtension.adapt(URLProvider.class, new URLProviderImpl(hostURL, contextRoot));
			Extensions.INSTANCE.addExtension(urlProviderExtension);

			this.extensionMap.put(url, urlProviderExtension);
		}
	}

	@Override
	public synchronized void stop(BundleContext bundleContext, String hostURL, String contextRoot) {
		String url = getURL(hostURL, contextRoot);

		// Unregister URL provider extension
		ProgramExtension urlProviderExtension = this.extensionMap.remove(url);
		if (urlProviderExtension != null) {
			Extensions.INSTANCE.removeExtension(urlProviderExtension);
		}

		// Stop relay ws app
		WSRelayApplication wsApp = this.wsAppMap.remove(url);
		if (wsApp != null) {
			wsApp.stop(bundleContext);
		}
	}

}
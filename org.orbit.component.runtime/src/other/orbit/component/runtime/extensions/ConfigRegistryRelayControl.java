package other.orbit.component.runtime.extensions;

import java.net.URI;
import java.util.List;

import org.orbit.component.runtime.Extensions;
import org.orbit.component.runtime.relay.desc.ConfigRegistryWSApplicationDesc;
import org.orbit.component.runtime.relay.util.SwitcherUtil;
import org.orbit.platform.sdk.relaycontrol.WSRelayControlImpl;
import org.origin.common.extensions.Extension;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherPolicy;
import org.origin.common.service.WebServiceAwareImpl;
import org.osgi.framework.BundleContext;

public class ConfigRegistryRelayControl extends WSRelayControlImpl {

	public static ConfigRegistryRelayControl INSTANCE = new ConfigRegistryRelayControl();

	@Override
	public synchronized void start(BundleContext bundleContext, WSClientFactory factory, String hostURL, String contextRoot, List<URI> uriList) {
		String url = getURL(hostURL, contextRoot);

		// Start relay ws app
		WSRelayApplication wsApp = this.wsAppMap.get(url);
		if (wsApp == null) {
			ConfigRegistryWSApplicationDesc wsAppDesc = new ConfigRegistryWSApplicationDesc(new WebServiceAwareImpl(null, null, contextRoot));
			Switcher<URI> switcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
			WSRelayApplication newWsApp = new WSRelayApplication(wsAppDesc, switcher, factory);
			newWsApp.start(bundleContext);

			this.wsAppMap.put(url, newWsApp);
		}

		// Register URL provider extension
		// ProgramExtension urlProviderExtension = this.extensionMap.get(url);
		// if (urlProviderExtension == null) {
		// urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.CONFIG_REGISTRY_URL_PROVIDER_EXTENSION_ID);
		// urlProviderExtension.setName("URL provider for config registry");
		// urlProviderExtension.setDescription("URL provider for config registry description");
		// // urlProviderExtension.addInterface(URLProvider.class, new URLProviderImpl(hostURL, contextRoot));
		// Extensions.INSTANCE.addExtension(urlProviderExtension);
		// this.extensionMap.put(url, urlProviderExtension);
		// }
	}

	@Override
	public synchronized void stop(BundleContext bundleContext, String hostURL, String contextRoot) {
		String url = getURL(hostURL, contextRoot);

		// Unregister URL provider extension
		Extension urlProviderExtension = this.extensionMap.remove(url);
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

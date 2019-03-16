package org.orbit.component.cli;

import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.tier2.appstore.AppQuery;
import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.platform.sdk.command.CommandActivator;
import org.origin.common.annotation.Annotated;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppStoreClientCommand implements Annotated, CommandActivator {

	public static final String ID = "org.orbit.component.cli.AppStoreClientCommand";

	protected static Logger LOG = LoggerFactory.getLogger(AppStoreClientCommand.class);

	protected BundleContext bundleContext;
	protected Map<Object, Object> properties;

	public void start(BundleContext bundleContext) {
		LOG.info("start()");
		this.bundleContext = bundleContext;

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function", new String[] { "list_apps" });

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		// PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_APP_STORE_URL);
		this.properties = properties;

		OSGiServiceUtil.register(this.bundleContext, AppStoreClientCommand.class.getName(), this, props);
	}

	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(AppStoreClientCommand.class.getName(), this);

		this.bundleContext = null;
	}

	protected AppStoreClient getAppStore() {
		// String appStoreUrl = (String) this.properties.get(ComponentConstants.ORBIT_APP_STORE_URL);
		AppStoreClient appStore = ComponentClientsUtil.AppStore.getAppStoreClient(null);
		if (appStore == null) {
			throw new IllegalStateException("AppStore is null.");
		}
		return appStore;
	}

	@Descriptor("List Apps")
	public void list_apps() throws ClientException {
		AppStoreClient appStore = getAppStore();

		AppQuery query = new AppQuery();
		AppManifest[] appManifests = appStore.getApps(query);
		int numberOfApps = (appManifests != null) ? appManifests.length : 0;
		System.out.println("Number of apps is: " + numberOfApps);

		for (AppManifest appManifest : appManifests) {
			String appId = appManifest.getAppId();
			String name = appManifest.getName();
			String version = appManifest.getAppVersion();
			String type = appManifest.getType();
			String fileName = appManifest.getFileName();

			String text = String.format("%10s|%10s|%10s|%10s|%10s|", appId, name, version, type, fileName);
			System.out.println(text);
		}
	}

	public static void main(String[] args) {
		// @see https://dzone.com/articles/java-string-format-examples
		String text = String.format("|%20s|%20s|%20s|%20s|%20s|", "appId", "name", "version", "type", "fileName");
		System.out.println(text);
	}

}

// @Descriptor("List AppStores")
// public void lappstores() throws ClientException {
// LoadBalancer<AppStore> lb = this.appStoreConnector.getLoadBalancer();
// if (lb == null) {
// System.out.println("AppStore LoadBalancer is not available.");
// return;
// }
//
// List<LoadBalanceResource<AppStore>> resources = lb.getResources();
// if (resources == null) {
// System.out.println("AppStore LoadBalancer's resource is null.");
// return;
// }
//
// System.out.println("Number of AppStores: " + resources.size());
// for (LoadBalanceResource<AppStore> resource : resources) {
// // String id = resource.getId();
// AppStore appStore = resource.getService();
// String name = appStore.getName();
// String url = appStore.getURL();
// // System.out.println(name + " (id = '" + id + "', url = '" + url + "')");
// System.out.println(name + " (url = '" + url + "')");
// Map<?, ?> properties = resource.getProperties();
// Printer.pl(properties);
// System.out.println("ping: " + appStore.ping());
// System.out.println();
// }
// }

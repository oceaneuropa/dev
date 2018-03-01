package org.orbit.component.api.cli;

import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.component.api.tier2.appstore.request.AppQuery;
import org.origin.common.annotation.Annotated;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppStoreCommand implements Annotated {

	protected static Logger LOG = LoggerFactory.getLogger(AppStoreCommand.class);

	protected BundleContext bundleContext;
	protected Map<Object, Object> properties;

	/**
	 * 
	 * @param bundleContext
	 */
	public AppStoreCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		LOG.info("start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function", new String[] { "list_apps" });

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_APP_STORE_URL);
		this.properties = properties;

		OSGiServiceUtil.register(this.bundleContext, AppStoreCommand.class.getName(), this, props);
	}

	public void stop() {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(AppStoreCommand.class.getName(), this);
	}

	protected AppStore getAppStore() {
		AppStore appStore = OrbitClients.getInstance().getAppStore(this.properties);
		if (appStore == null) {
			throw new IllegalStateException("AppStore is null.");
		}
		return appStore;
	}

	@Descriptor("List Apps")
	public void list_apps() throws ClientException {
		AppStore appStore = getAppStore();

		AppQuery query = new AppQuery();
		AppManifest[] appManifests = appStore.getApps(query);
		int numberOfApps = (appManifests != null) ? appManifests.length : 0;
		System.out.println("Number of apps is: " + numberOfApps);

		for (AppManifest appManifest : appManifests) {
			String appId = appManifest.getAppId();
			String name = appManifest.getName();
			String version = appManifest.getVersion();
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

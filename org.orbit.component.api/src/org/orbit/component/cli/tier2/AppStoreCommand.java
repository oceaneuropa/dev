package org.orbit.component.cli.tier2;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.component.api.tier2.appstore.AppStoreConnector;
import org.orbit.component.api.tier2.appstore.request.AppQuery;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.Printer;
import org.osgi.framework.BundleContext;

public class AppStoreCommand implements Annotated {

	protected BundleContext bundleContext;

	@Dependency
	protected AppStoreConnector appStoreConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	public AppStoreCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("AppStoreCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function", new String[] { "lappstores", "lapps" });
		OSGiServiceUtil.register(this.bundleContext, AppStoreCommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		System.out.println("AppStoreCommand.stop()");

		OSGiServiceUtil.unregister(AppStoreCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void appStoreConnectorSet() {
		System.out.println("AppStoreConnector is set.");
	}

	@DependencyUnfullfilled
	public void appStoreConnectorUnset() {
		System.out.println("AppStoreConnector is unset.");
	}

	protected void checkConnector() throws ClientException {
		if (this.appStoreConnector == null) {
			System.out.println("AppStoreConnector is not available.");
			throw new ClientException(500, "AppStoreConnector is not available.");
		}
	}

	@Descriptor("List AppStores")
	public void lappstores() throws ClientException {
		checkConnector();

		LoadBalancer<AppStore> lb = this.appStoreConnector.getLoadBalancer();
		if (lb == null) {
			System.out.println("AppStore LoadBalancer is not available.");
			return;
		}

		List<LoadBalanceResource<AppStore>> resources = lb.getResources();
		if (resources == null) {
			System.out.println("AppStore LoadBalancer's resource is null.");
			return;
		}

		System.out.println("Number of AppStores: " + resources.size());
		for (LoadBalanceResource<AppStore> resource : resources) {
			// String id = resource.getId();
			AppStore appStore = resource.getService();
			String name = appStore.getName();
			String url = appStore.getURL();
			// System.out.println(name + " (id = '" + id + "', url = '" + url + "')");
			System.out.println(name + " (url = '" + url + "')");
			Map<?, ?> properties = resource.getProperties();
			Printer.pl(properties);
			System.out.println("ping: " + appStore.ping());
			System.out.println();
		}
	}

	@Descriptor("List Apps")
	public void lapps() throws ClientException {
		checkConnector();

		AppStore appStore = this.appStoreConnector.getService();
		if (appStore == null) {
			System.out.println("AppStore is not available.");
			return;
		}

		AppQuery query = new AppQuery();
		AppManifest[] appManifests = appStore.getApps(query);
		int numberOfApps = (appManifests != null) ? appManifests.length : 0;
		System.out.println("Number of apps is: " + numberOfApps);

		for (AppManifest appManifest : appManifests) {
			String appId = appManifest.getAppId();
			String categoryId = appManifest.getCategoryId();
			String namespace = appManifest.getNamespace();
			String name = appManifest.getName();
			String version = appManifest.getVersion();
			String fileName = appManifest.getFileName();

			String text = String.format("%10s|%10s|%10s|%10s|%10s|%10s|", appId, categoryId, namespace, name, version, fileName);
			System.out.println(text);
		}
	}

	public static void main(String[] args) {
		// @see https://dzone.com/articles/java-string-format-examples
		String text = String.format("|%20s|%20s|%20s|%20s|%20s|%20s|", "appId", "categoryId", "namespace", "name", "version", "fileName");
		System.out.println(text);
	}

}

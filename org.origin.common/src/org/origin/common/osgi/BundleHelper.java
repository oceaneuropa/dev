package org.origin.common.osgi;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;

public class BundleHelper {

	public static BundleHelper INSTANCE = new BundleHelper();

	/**
	 * 
	 * @param bundles
	 * @param bundleName
	 * @param bundleVersion
	 * @return
	 */
	public Bundle findBundle(Bundle[] bundles, String bundleName, String bundleVersion) {
		Bundle bundle = null;

		if (bundles != null) {
			for (Bundle currBundle : bundles) {
				String currBundleId = currBundle.getSymbolicName();
				String currBundleVersion = currBundle.getVersion().toString();
				// String currBundleLocation = currBundle.getLocation();

				boolean matchBundleId = (currBundleId.equals(bundleName)) ? true : false;
				boolean matchBundleVersion = (currBundleVersion.equals(bundleVersion)) ? true : false;
				// boolean matchBundleLocation = false;

				if (matchBundleId && matchBundleVersion) {
					bundle = currBundle;
					break;
				}
			}
		}

		return bundle;
	}

	public String getSimpleName(Bundle bundle) {
		return bundle.getSymbolicName() + " (" + bundle.getVersion().toString() + ")";
	}

	public String getSimpleName(Bundle bundle, boolean withId, boolean withState, boolean withLocation) {
		if (bundle == null) {
			return null;
		}

		long id = bundle.getBundleId();
		String name = bundle.getSymbolicName();
		String version = bundle.getVersion().toString();
		String location = bundle.getLocation();
		int state = bundle.getState();

		String stateName = null;
		if (Bundle.INSTALLED == state) { // 2
			stateName = "Bundle.INSTALLED";

		} else if (Bundle.RESOLVED == state) { // 4
			stateName = "Bundle.RESOLVED";

		} else if (Bundle.STARTING == state) { // 8
			stateName = "Bundle.STARTING";

		} else if (Bundle.ACTIVE == state) { // 32
			stateName = "Bundle.ACTIVE";

		} else if (Bundle.STOPPING == state) { // 16
			stateName = "Bundle.STOPPING";

		} else if (Bundle.UNINSTALLED == state) { // 1
			stateName = "Bundle.UNINSTALLED";

		} else {
			stateName = "unknown";
		}

		String simpleName = name + " (" + version + ")";
		if (withId) {
			simpleName = id + " " + simpleName;
		}
		if (withState) {
			simpleName += (" " + stateName);
		}
		if (withLocation) {
			simpleName += (" " + location);
		}
		return simpleName;
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public String getSimpleName(BundleEvent event) {
		if (event == null) {
			return null;
		}

		int eventType = event.getType();
		String eventName = null;
		if (BundleEvent.INSTALLED == eventType) { // 1
			eventName = "BundleEvent.INSTALLED";

		} else if (BundleEvent.UPDATED == eventType) { // 8
			eventName = "BundleEvent.UPDATED";

		} else if (BundleEvent.RESOLVED == eventType) { // 32
			eventName = "BundleEvent.RESOLVED";

		} else if (BundleEvent.STARTING == eventType) { // 128
			eventName = "BundleEvent.STARTING";

		} else if (BundleEvent.STARTED == eventType) { // 2
			eventName = "BundleEvent.STARTED";

		} else if (BundleEvent.STOPPING == eventType) { // 256
			eventName = "BundleEvent.STOPPING";

		} else if (BundleEvent.STOPPED == eventType) { // 4
			eventName = "BundleEvent.STOPPED";

		} else if (BundleEvent.UNRESOLVED == eventType) { // 64
			eventName = "BundleEvent.UNRESOLVED";

		} else if (BundleEvent.UNINSTALLED == eventType) { // 16
			eventName = "BundleEvent.UNINSTALLED";

		} else if (BundleEvent.LAZY_ACTIVATION == eventType) { // 512
			eventName = "BundleEvent.LAZY_ACTIVATION";

		} else {
			eventName = "unknown";
		}
		return eventType + "|" + eventName;
	}

	/**
	 * 
	 * @param bundle
	 */
	public void debugBundle(Bundle bundle) {
		System.out.println(getClass().getSimpleName() + ".debugBundle()");
		if (bundle == null) {
			System.out.println("bundle is null");
			return;
		}

		long id = bundle.getBundleId();
		String name = bundle.getSymbolicName();
		String version = bundle.getVersion().toString();
		String location = bundle.getLocation();
		int state = bundle.getState();

		String stateName = null;
		if (Bundle.INSTALLED == state) { // 2
			stateName = "Bundle.INSTALLED";

		} else if (Bundle.RESOLVED == state) { // 4
			stateName = "Bundle.RESOLVED";

		} else if (Bundle.STARTING == state) { // 8
			stateName = "Bundle.STARTING";

		} else if (Bundle.ACTIVE == state) { // 32
			stateName = "Bundle.ACTIVE";

		} else if (Bundle.STOPPING == state) { // 16
			stateName = "Bundle.STOPPING";

		} else if (Bundle.UNINSTALLED == state) { // 1
			stateName = "Bundle.UNINSTALLED";

		} else {
			stateName = "unknown";
		}

		System.out.println("(id = " + id + ") " + name + "_" + version + " (state = " + state + ", state name = " + stateName + ") (location = " + location + ")");
	}

	/**
	 * 
	 * @param event
	 */
	public void debugBundleEvent(BundleEvent event) {
		// System.out.println(getClass().getSimpleName() + ".debugBundleEvent()");
		if (event == null) {
			System.out.println("event is null");
			return;
		}

		int eventType = event.getType();
		String eventName = null;
		if (BundleEvent.INSTALLED == eventType) { // 1
			eventName = "BundleEvent.INSTALLED";

		} else if (BundleEvent.UPDATED == eventType) { // 8
			eventName = "BundleEvent.UPDATED";

		} else if (BundleEvent.RESOLVED == eventType) { // 32
			eventName = "BundleEvent.RESOLVED";

		} else if (BundleEvent.STARTING == eventType) { // 128
			eventName = "BundleEvent.STARTING";

		} else if (BundleEvent.STARTED == eventType) { // 2
			eventName = "BundleEvent.STARTED";

		} else if (BundleEvent.STOPPING == eventType) { // 256
			eventName = "BundleEvent.STOPPING";

		} else if (BundleEvent.STOPPED == eventType) { // 4
			eventName = "BundleEvent.STOPPED";

		} else if (BundleEvent.UNRESOLVED == eventType) { // 64
			eventName = "BundleEvent.UNRESOLVED";

		} else if (BundleEvent.UNINSTALLED == eventType) { // 16
			eventName = "BundleEvent.UNINSTALLED";

		} else if (BundleEvent.LAZY_ACTIVATION == eventType) { // 512
			eventName = "BundleEvent.LAZY_ACTIVATION";

		} else {
			eventName = "unknown";
		}

		System.out.println(eventType + "|" + eventName);
	}

}

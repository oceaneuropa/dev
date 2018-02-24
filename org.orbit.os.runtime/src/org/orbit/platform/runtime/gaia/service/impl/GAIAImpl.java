package org.orbit.platform.runtime.gaia.service.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.platform.runtime.PlatformConstants;
import org.orbit.platform.runtime.gaia.service.GAIA;
import org.orbit.platform.runtime.gaia.world.Worlds;
import org.orbit.platform.runtime.gaia.world.impl.WorldsImpl;
import org.orbit.platform.runtime.util.SetupUtil;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.editpolicy.WSEditPoliciesImpl;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GAIAImpl implements GAIA {

	protected static Logger LOG = LoggerFactory.getLogger(GAIAImpl.class);

	protected BundleContext bundleContext;
	protected Map<String, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected ServiceRegistration<?> serviceRegistry;
	protected WSEditPolicies wsEditPolicies;
	protected Worlds worlds;

	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	/**
	 * 
	 * @param initProperties
	 */
	public GAIAImpl(Map<String, Object> initProperties) {
		if (initProperties == null) {
			initProperties = new HashMap<String, Object>();
		}
		this.initProperties = initProperties;

		this.wsEditPolicies = new WSEditPoliciesImpl();
		this.wsEditPolicies.setService(GAIA.class, this);

		this.worlds = new WorldsImpl();
	}

	@Override
	public String getName() {
		String name = getProperty(PlatformConstants.GAIA_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = getProperty(PlatformConstants.GAIA_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = getProperty(PlatformConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = getProperty(PlatformConstants.GAIA_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public String getHome() {
		return SetupUtil.getNodeHome(this.bundleContext).toString();
	}

	@Override
	public synchronized void updateProperties(Map<Object, Object> configProps) {
		LOG.info("updateProperties()");

		if (configProps == null) {
			configProps = new HashMap<Object, Object>();
		}

		String globalHostURL = getProperty(PlatformConstants.ORBIT_HOST_URL);
		String name = getProperty(PlatformConstants.GAIA_NAME);
		String hostURL = getProperty(PlatformConstants.GAIA_HOST_URL);
		String contextRoot = getProperty(PlatformConstants.GAIA_CONTEXT_ROOT);

		LOG.info(PlatformConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		LOG.info(PlatformConstants.GAIA_NAME + " = " + name);
		LOG.info(PlatformConstants.GAIA_HOST_URL + " = " + hostURL);
		LOG.info(PlatformConstants.GAIA_CONTEXT_ROOT + " = " + contextRoot);

		this.properties = configProps;
		// this.databaseProperties = getConnectionProperties();
	}

	protected String getProperty(String key) {
		return getProperty(key, String.class);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getProperty(String key, Class<T> valueClass) {
		// Config properties from bundle context or from system/env properties takes precedence over properties defined in config.ini file.
		Object object = this.properties.get(key);
		if (object != null && valueClass.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		// If config properties cannot be found, read from config.ini file
		if (this.initProperties != null && String.class.equals(valueClass)) {
			String value = (String) this.initProperties.get(key.toString());
			if (value != null) {
				return (T) value;
			}
		}
		return null;
	}

	public synchronized void start(BundleContext bundleContext) {
		LOG.info("start()");
		if (isStarted()) {
			LOG.info("GAIA is readly started.");
			return;
		}
		this.isStarted.set(true);

		this.bundleContext = bundleContext;

		// load properties
		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(this.bundleContext, configProps, PlatformConstants.ORBIT_HOST_URL);
		// PropertyUtil.loadProperty(this.bundleContext, configProps, OSConstants.GAIA_NAMESPACE);
		PropertyUtil.loadProperty(this.bundleContext, configProps, PlatformConstants.GAIA_NAME);
		PropertyUtil.loadProperty(this.bundleContext, configProps, PlatformConstants.GAIA_HOST_URL);
		PropertyUtil.loadProperty(this.bundleContext, configProps, PlatformConstants.GAIA_CONTEXT_ROOT);
		updateProperties(configProps);

		// start OS service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = this.bundleContext.registerService(GAIA.class, this, props);
	}

	public synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	protected void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException(getClass().getSimpleName() + " is not started.");
		}
	}

	public synchronized void stop(BundleContext bundleContext) {
		LOG.info("stop()");
		if (!this.isStarted.compareAndSet(true, false)) {
			LOG.info("GAIA is readly stopped.");
			return;
		}

		// Stop GAIA service
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		this.bundleContext = null;
	}

	@Override
	public WSEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	@Override
	public Worlds getWorlds() {
		return this.worlds;
	}

}

// protected List<AppHandler> appHandlers = new ArrayList<AppHandler>();
//
// /**
// *
// * @param appFolder
// */
// public void loadApp(File appFolder) {
// AppManifest appManifest = AppUtil.getManifestFromAppFolder(appFolder);
// if (appManifest != null) {
// AppHandler appHandler = new AppHandler(this, appFolder, appManifest);
// appHandler.open(this.bundleContext);
// this.appHandlers.add(appHandler);
// }
// }

//// load existing bundles
// Bundle[] bundles = this.bundleContext.getBundles();
//// start tracking bundles
// this.bundleTracker = new BundleTracker<Bundle>(this.bundleContext, Bundle.RESOLVED | Bundle.STARTING | Bundle.ACTIVE | Bundle.STOPPING, new
//// BundleTrackerCustomizer<Bundle>() {
// @Override
// public Bundle addingBundle(Bundle bundle, BundleEvent event) {
// return bundle;
// }
//
// @Override
// public void modifiedBundle(Bundle bundle, BundleEvent event, Bundle bundle2) {
//
// }
//
// @Override
// public void removedBundle(Bundle bundle, BundleEvent event, Bundle bundle2) {
//
// }
// });

// private BundleTracker<Bundle> bundleTracker;
// if (this.bundleTracker != null) {
// this.bundleTracker.close();
// this.bundleTracker = null;
// }

// protected Framework framework;

// public Framework getFramework() {
// return this.framework;
// }

// Map<String, String> configuration = new HashMap<String, String>();
// this.framework = FrameworkUtil.createFramework(configuration);
// try {
// this.framework.start();
// } catch (BundleException e) {
// e.printStackTrace();
// }

// if (this.framework != null) {
// try {
// this.framework.stop();
// } catch (BundleException e) {
// e.printStackTrace();
// }
// this.framework = null;
// }

// protected Properties databaseProperties;
// /**
// *
// * @param props
// * @return
// */
// protected synchronized Properties getConnectionProperties() {
// String driver = getProperty(Constants.COMPONENT_NODE_JDBC_DRIVER);
// String url = getProperty(Constants.COMPONENT_NODE_JDBC_URL);
// String username = getProperty(Constants.COMPONENT_NODE_JDBC_USERNAME);
// String password = getProperty(Constants.COMPONENT_NODE_JDBC_PASSWORD);
// return DatabaseUtil.getProperties(driver, url, username, password);
// }
// protected Connection getConnection() {
// return DatabaseUtil.getConnection(this.databaseProperties);
// }

// public class NodeOSWSEditPolicies implements WSEditPolicies {
// WSEditPoliciesSupport editPoliciesSupport = new WSEditPoliciesSupport();
//
// @Override
// public List<WSEditPolicy> getEditPolicies() {
// return this.editPoliciesSupport.getEditPolicies();
// }
//
// @Override
// public WSEditPolicy getEditPolicy(String id) {
// return this.editPoliciesSupport.getEditPolicy(id);
// }
//
// @Override
// public boolean installEditPolicy(WSEditPolicy editPolicy) {
// // Initialize the WSEditPolicy with service
// editPolicy.setService(GAIA.class, GAIAImpl.this);
//
// return this.editPoliciesSupport.installEditPolicy(editPolicy);
// }
//
// @Override
// public boolean uninstallEditPolicy(WSEditPolicy editPolicy) {
// boolean succeed = this.editPoliciesSupport.uninstallEditPolicy(editPolicy);
// if (succeed) {
// editPolicy.setService(GAIA.class, null);
// }
// return succeed;
// }
//
// @Override
// public WSEditPolicy uninstallEditPolicy(String id) {
// WSEditPolicy editPolicy = this.editPoliciesSupport.uninstallEditPolicy(id);
// if (editPolicy != null) {
// editPolicy.setService(GAIA.class, null);
// }
// return editPolicy;
// }
//
// @Override
// public WSCommand getCommand(Request request) {
// return this.editPoliciesSupport.getCommand(request);
// }
// }

// @Override
// public String getOSName() {
// return OSConstants.OS__VERSION;
// }
//
// @Override
// public String getOSVersion() {
// return OSConstants.OS__NAME;
// }
//
// @Override
// public String getNamespace() {
// String namespace = getProperty(OSConstants.GAIA_NAMESPACE);
// return namespace;
// }
// @Override
// public String getLabel() {
// return "(" + getOSName() + "-" + getOSVersion() + ") '" + getName();
// }

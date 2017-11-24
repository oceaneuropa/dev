package org.orbit.services.http.jetty;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.startlevel.StartLevel;

/**
 * @see org.eclipse.equinox.http.jetty.internal.Activator
 * 
 */
public class Activator implements BundleActivator {

	private static final String WORK_DIR = "orbit_http_work"; //$NON-NLS-1$
	private static final String DEFAULT_PID = "default"; //$NON-NLS-1$
	private static final String MANAGED_SERVICE_FACTORY_PID = "org.orbit.service.http.jetty.config"; //$NON-NLS-1$
	// controls whether start() should automatically start an Http Service based on BundleContext properties (default is true)
	// Note: only used if the bundle is explicitly started (e.g. not "lazy" activated)
	private static final String AUTOSTART = "org.orbit.service.http.jetty.autostart"; //$NON-NLS-1$

	// Use these properties for setting the default ports
	private static final String ORG_OSGI_SERVICE_HTTP_PORT = "org.orbit.service.http.port"; //$NON-NLS-1$
	private static final String ORG_OSGI_SERVICE_HTTP_PORT_SECURE = "org.orbit.service.http.port.secure"; //$NON-NLS-1$

	// Used by createDefaultSettings(BundleContext context) method
	private static final String PROPERTY_PREFIX = "org.orbit.service.http.jetty."; //$NON-NLS-1$

	// The staticServerManager is use by the start and stopServer methods and must be accessed in a static synchronized block
	// to ensure it is correctly handled in terms of the bundle life-cycle.
	private static HttpServerManager staticServerManager;

	/**
	 * 
	 * @param pid
	 * @param settings
	 * @throws Exception
	 */
	public synchronized static void startServer(String pid, Dictionary<String, ?> settings) throws Exception {
		if (staticServerManager == null) {
			throw new IllegalStateException("Inactive"); //$NON-NLS-1$
		}
		staticServerManager.updated(pid, settings);
	}

	/**
	 * 
	 * @param pid
	 * @throws Exception
	 */
	public synchronized static void stopServer(String pid) throws Exception {
		if (staticServerManager != null) {
			staticServerManager.deleted(pid);
		}
	}

	private synchronized static void setStaticServerManager(HttpServerManager httpServerManager) {
		staticServerManager = httpServerManager;
	}

	protected HttpServerManager httpServerManager;
	protected ServiceRegistration<?> registration;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		// Create new instance of HttpServerManager
		File workDir = new File(bundleContext.getDataFile(""), WORK_DIR); //$NON-NLS-1$
		workDir.mkdir();
		this.httpServerManager = new HttpServerManager(bundleContext, workDir);

		// Update HttpServerManager to start Jetty Server immediately and to trigger starting of HttpService (HttpServiceFactory) and WebSocketService
		// (WebSocketServiceFactory).
		String autostart = bundleContext.getProperty(AUTOSTART);
		if ((autostart == null || Boolean.valueOf(autostart).booleanValue()) && !isBundleActivationPolicyUsed(bundleContext)) {
			Hashtable<String, Object> defaultSettings = createDefaultSettings(bundleContext);
			this.httpServerManager.updated(DEFAULT_PID, defaultSettings);
		}

		// Register HttpServerManager as ManagedServiceFactory using "org.orbit.service.http.jetty.config" as factory ID.
		Hashtable<String, Object> dictionary = new Hashtable<String, Object>();
		dictionary.put(Constants.SERVICE_PID, MANAGED_SERVICE_FACTORY_PID);
		this.registration = bundleContext.registerService(ManagedServiceFactory.class.getName(), this.httpServerManager, dictionary);
		setStaticServerManager(this.httpServerManager);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		setStaticServerManager(null);

		this.registration.unregister();
		this.registration = null;

		this.httpServerManager.shutdown();
		this.httpServerManager = null;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	protected boolean isBundleActivationPolicyUsed(BundleContext bundleContext) {
		@SuppressWarnings("rawtypes")
		ServiceReference reference = bundleContext.getServiceReference(StartLevel.class.getName());
		StartLevel sl = ((reference != null) ? (StartLevel) bundleContext.getService(reference) : null);
		if (sl != null) {
			try {
				Bundle bundle = bundleContext.getBundle();
				Method isBundleActivationPolicyUsed = StartLevel.class.getMethod("isBundleActivationPolicyUsed", new Class[] { Bundle.class }); //$NON-NLS-1$
				Boolean result = (Boolean) isBundleActivationPolicyUsed.invoke(sl, new Object[] { bundle });
				return result.booleanValue();

			} catch (Exception e) {
				// ignore
				// - Bundle Activation Policy only available in StartLevel Service 1.1
			} finally {
				bundleContext.ungetService(reference);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	protected Hashtable<String, Object> createDefaultSettings(BundleContext context) {
		Hashtable<String, Object> defaultSettings = new Hashtable<String, Object>();

		// PID
		defaultSettings.put(Constants.SERVICE_PID, DEFAULT_PID);

		// HTTP Enabled (default is true)
		String httpEnabledProperty = context.getProperty(PROPERTY_PREFIX + JettyConstants.HTTP_ENABLED);
		Boolean httpEnabled = (httpEnabledProperty == null) ? Boolean.TRUE : new Boolean(httpEnabledProperty);
		defaultSettings.put(JettyConstants.HTTP_ENABLED, httpEnabled);

		// HTTP Port
		// using "org.orbit.service.http.jetty.http.port"
		// or
		// using "org.orbit.service.http.port"
		String httpPortProperty = context.getProperty(PROPERTY_PREFIX + JettyConstants.HTTP_PORT);
		if (httpPortProperty == null) {
			httpPortProperty = context.getProperty(ORG_OSGI_SERVICE_HTTP_PORT);
		}

		int httpPort = 80;
		if (httpPortProperty != null) {
			try {
				httpPort = Integer.parseInt(httpPortProperty);
			} catch (NumberFormatException e) {
				// (log this) ignore and use default
			}
		}
		defaultSettings.put(JettyConstants.HTTP_PORT, new Integer(httpPort));

		// HTTP Host (default is 0.0.0.0)
		String httpHost = context.getProperty(PROPERTY_PREFIX + JettyConstants.HTTP_HOST);
		if (httpHost != null) {
			defaultSettings.put(JettyConstants.HTTP_HOST, httpHost);
		}

		// HTTPS Enabled (default is false)
		Boolean httpsEnabled = new Boolean(context.getProperty(PROPERTY_PREFIX + JettyConstants.HTTPS_ENABLED));
		defaultSettings.put(JettyConstants.HTTPS_ENABLED, httpsEnabled);

		if (httpsEnabled.booleanValue()) {
			// HTTPS Port
			String httpsPortProperty = context.getProperty(PROPERTY_PREFIX + JettyConstants.HTTPS_PORT);
			if (httpsPortProperty == null) {
				httpsPortProperty = context.getProperty(ORG_OSGI_SERVICE_HTTP_PORT_SECURE);
			}

			int httpsPort = 443;
			if (httpsPortProperty != null) {
				try {
					httpsPort = Integer.parseInt(httpsPortProperty);
				} catch (NumberFormatException e) {
					// (log this) ignore and use default
				}
			}
			defaultSettings.put(JettyConstants.HTTPS_PORT, new Integer(httpsPort));

			// HTTPS Host (default is 0.0.0.0)
			String httpsHost = context.getProperty(PROPERTY_PREFIX + JettyConstants.HTTPS_HOST);
			if (httpsHost != null) {
				defaultSettings.put(JettyConstants.HTTPS_HOST, httpsHost);
			}

			// SSL SETTINGS
			String keystore = context.getProperty(PROPERTY_PREFIX + JettyConstants.SSL_KEYSTORE);
			if (keystore != null) {
				defaultSettings.put(JettyConstants.SSL_KEYSTORE, keystore);
			}

			String password = context.getProperty(PROPERTY_PREFIX + JettyConstants.SSL_PASSWORD);
			if (password != null) {
				defaultSettings.put(JettyConstants.SSL_PASSWORD, password);
			}

			String keypassword = context.getProperty(PROPERTY_PREFIX + JettyConstants.SSL_KEYPASSWORD);
			if (keypassword != null) {
				defaultSettings.put(JettyConstants.SSL_KEYPASSWORD, keypassword);
			}

			String needclientauth = context.getProperty(PROPERTY_PREFIX + JettyConstants.SSL_NEEDCLIENTAUTH);
			if (needclientauth != null) {
				defaultSettings.put(JettyConstants.SSL_NEEDCLIENTAUTH, new Boolean(needclientauth));
			}

			String wantclientauth = context.getProperty(PROPERTY_PREFIX + JettyConstants.SSL_WANTCLIENTAUTH);
			if (wantclientauth != null) {
				defaultSettings.put(JettyConstants.SSL_WANTCLIENTAUTH, new Boolean(wantclientauth));
			}

			String protocol = context.getProperty(PROPERTY_PREFIX + JettyConstants.SSL_PROTOCOL);
			if (protocol != null) {
				defaultSettings.put(JettyConstants.SSL_PROTOCOL, protocol);
			}

			String algorithm = context.getProperty(PROPERTY_PREFIX + JettyConstants.SSL_ALGORITHM);
			if (algorithm != null) {
				defaultSettings.put(JettyConstants.SSL_ALGORITHM, algorithm);
			}

			String keystoretype = context.getProperty(PROPERTY_PREFIX + JettyConstants.SSL_KEYSTORETYPE);
			if (keystoretype != null) {
				defaultSettings.put(JettyConstants.SSL_KEYSTORETYPE, keystoretype);
			}
		}

		// Servlet Context Path
		String contextpath = context.getProperty(PROPERTY_PREFIX + JettyConstants.CONTEXT_PATH);
		if (contextpath != null) {
			defaultSettings.put(JettyConstants.CONTEXT_PATH, contextpath);
		}

		// Session Inactive Interval (timeout)
		String sessionInactiveInterval = context.getProperty(PROPERTY_PREFIX + JettyConstants.CONTEXT_SESSIONINACTIVEINTERVAL);
		if (sessionInactiveInterval != null) {
			try {
				defaultSettings.put(JettyConstants.CONTEXT_SESSIONINACTIVEINTERVAL, new Integer(sessionInactiveInterval));
			} catch (NumberFormatException e) {
				// (log this) ignore
			}
		}

		// Other Info
		String otherInfo = context.getProperty(PROPERTY_PREFIX + JettyConstants.OTHER_INFO);
		if (otherInfo != null) {
			defaultSettings.put(JettyConstants.OTHER_INFO, otherInfo);
		}

		// customizer
		String customizerClass = context.getProperty(PROPERTY_PREFIX + JettyConstants.CUSTOMIZER_CLASS);
		if (customizerClass != null) {
			defaultSettings.put(JettyConstants.CUSTOMIZER_CLASS, customizerClass);
		}

		return defaultSettings;
	}

}

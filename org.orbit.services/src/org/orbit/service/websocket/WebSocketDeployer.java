package org.orbit.service.websocket;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.websocket.server.ServerEndpointConfig;

import org.osgi.framework.BundleContext;

/**
 * About java.lang.RuntimeException: Cannot load platform configurator
 * 
 * @see https://stackoverflow.com/questions/39740531/jetty-websocket-java-lang-runtimeexception-cannot-load-platform-configurator
 * 
 * 
 * @see http://grepcode.com/file_/repo1.maven.org/maven2/javax.websocket/javax.websocket-api/1.0/javax/websocket/server/ServerEndpointConfig.java/?v=source
 * 
 *      javax.websocket.server.ServerEndpointConfig class has an inner Configurator class. The java.lang.RuntimeException: "Cannot load platform configurator"
 *      exception is thrown from the ServerEndpointConfig.Configurator.fetchContainerDefaultConfigurator() method. Using this code:
 *      java.util.ServiceLoader.load(javax.websocket.server.ServerEndpointConfig.Configurator.class)
 * 
 *      When null ServerEndpointConfig.Configurator instance can be returned, the RuntimeException is thrown.
 * 
 * 
 * 
 * @see https://www.eclipse.org/forums/index.php/t/474446/
 * 
 *      it mentioned about Apache Aries project called SPI Fly (http://aries.apache.org/modules/spi-fly.html) for osgi.servlvice.loader.registrar capability
 *      provider.
 * 
 * @see http://mail-archives.apache.org/mod_mbox/aries-user/201610.mbox/%3CCAMit8SpApJpVQBt1LdRpKDdhdZv2h6X_uBCZ011c_K0twJG6Ww@mail.gmail.com%3E
 * @see http://mail-archives.apache.org/mod_mbox/aries-user/201610.mbox/%3C1475591592.35953414@f245.i.mail.ru%3E
 * 
 *      It mentioned about java.lang.RuntimeException:Cannot load platform configurator and says "You also need to add some configuration in the form of
 *      manifest headers to instruct SPI Fly what to do."
 * 
 * @see https://mvnrepository.com/artifact/org.apache.servicemix.bundles/org.apache.servicemix.bundles.javax-websocket-api/1.1_1
 * 
 *      Download org.apache.servicemix.bundles.javax-websocket-api-1.1_1.jar, which is a OSGi wrapper bundle for javax.websocket-api-1.1.jar
 * 
 */
public class WebSocketDeployer {

	protected ReadWriteLock lock;
	protected Map<WebSocketService, Set<WebSocketEndpoint>> webSocketService_to_deployed_endpoints;
	protected WebSocketServiceListener webSocketServiceListener;
	protected WebSocketEndpointListener webSocketEndpointListener;
	protected boolean debug = true;

	public WebSocketDeployer() {
		this.lock = new ReentrantReadWriteLock();
		this.webSocketService_to_deployed_endpoints = new HashMap<WebSocketService, Set<WebSocketEndpoint>>();
	}

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void start(final BundleContext bundleContext) throws Exception {
		// Note:
		// The WebSocketEndpoint and the WebSocketService are tracked separately.
		// When a WebSocketService is removed, the WebSocketEndpoint list can still be there waiting for the WebSocketService to be available
		// again. So do not remove WebSocketEndpoint when a WebSocketService becomes unavailable.

		// 1. Start tracking WebSocketService
		this.webSocketServiceListener = new WebSocketServiceListener() {
			@Override
			public void added(WebSocketService webSocketService) {
				lock.writeLock().lock();
				try {
					Set<WebSocketEndpoint> allEndpoints = getEndpoints();
					for (WebSocketEndpoint currEndpoint : allEndpoints) {
						if (!isDeployedTo(webSocketService, currEndpoint)) {
							if (shouldBeDeployedTo(webSocketService, currEndpoint)) {
								doDeploy(webSocketService, currEndpoint);
							}
						} else {
							activate(currEndpoint);
						}
					}
				} finally {
					lock.writeLock().unlock();
				}
			}

			@Override
			public void removed(WebSocketService webSocketService) {
				lock.writeLock().lock();
				try {
					Set<WebSocketEndpoint> allEndpoints = getEndpoints();
					for (WebSocketEndpoint currEndpoint : allEndpoints) {
						if (isDeployedTo(webSocketService, currEndpoint)) {
							// There is no API to remove endpoint from websocket ServerContainer
							// @see https://stackoverflow.com/questions/33611866/remove-endpoint-from-servercontainer-inside-weblistener
							// doUndeploy(webSocketService, currEndpoint);
							deactivate(currEndpoint);
						}
					}
				} finally {
					lock.writeLock().unlock();
				}
			}
		};
		this.webSocketServiceListener.start(bundleContext);

		// 2. Start tracking WebSocketEndpoint
		this.webSocketEndpointListener = new WebSocketEndpointListener() {
			@Override
			public void added(WebSocketEndpoint endpoint) {
				lock.writeLock().lock();
				try {
					Set<WebSocketService> allWebSocketServices = getWebSocketServices();
					for (WebSocketService currWebSocketService : allWebSocketServices) {
						if (!isDeployedTo(currWebSocketService, endpoint)) {
							if (shouldBeDeployedTo(currWebSocketService, endpoint)) {
								doDeploy(currWebSocketService, endpoint);
							}
						} else {
							activate(endpoint);
						}
					}
				} finally {
					lock.writeLock().unlock();
				}
			}

			@Override
			public void removed(WebSocketEndpoint endpoint) {
				lock.writeLock().lock();
				try {
					Set<WebSocketService> allWebSocketServices = getWebSocketServices();
					for (WebSocketService currWebSocketService : allWebSocketServices) {
						if (isDeployedTo(currWebSocketService, endpoint)) {
							// There is no API to remove endpoint from websocket ServerContainer
							// @see https://stackoverflow.com/questions/33611866/remove-endpoint-from-servercontainer-inside-weblistener
							// doUndeploy(currWebSocketService, endpoint);
							deactivate(endpoint);
						}
					}
				} finally {
					lock.writeLock().unlock();
				}
			}
		};
		this.webSocketEndpointListener.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void stop(final BundleContext bundleContext) throws Exception {
		// 1. Stop tracking WebSocketEndpoint
		this.webSocketEndpointListener.stop(bundleContext);
		this.webSocketEndpointListener = null;

		// 2. Stop tracking WebSocketService
		this.webSocketServiceListener.stop(bundleContext);
		this.webSocketServiceListener = null;

		// 3. clear data
		this.webSocketService_to_deployed_endpoints.clear();
	}

	public Set<WebSocketService> getWebSocketServices() {
		Set<WebSocketService> set = this.webSocketServiceListener.getWebSocketServices();
		if (set == null) {
			set = new HashSet<WebSocketService>();
		}
		return set;
	}

	public Set<WebSocketEndpoint> getEndpoints() {
		Set<WebSocketEndpoint> set = this.webSocketEndpointListener.getEndpoints();
		if (set == null) {
			set = new HashSet<WebSocketEndpoint>();
		}
		return set;
	}

	public Set<WebSocketEndpoint> getDeployedEndpoints(WebSocketService webSocketService) {
		Set<WebSocketEndpoint> set = this.webSocketService_to_deployed_endpoints.get(webSocketService);
		if (set == null) {
			set = new HashSet<WebSocketEndpoint>();
		}
		return set;
	}

	public Set<WebSocketService> getDeployedWebSocketServices(WebSocketEndpoint endpoint) {
		Set<WebSocketService> set = new HashSet<WebSocketService>();
		for (Iterator<WebSocketService> itor = this.webSocketService_to_deployed_endpoints.keySet().iterator(); itor.hasNext();) {
			WebSocketService currWebSocketService = itor.next();
			Set<WebSocketEndpoint> currEndpoints = this.webSocketService_to_deployed_endpoints.get(currWebSocketService);
			if (currEndpoints != null && currEndpoints.contains(endpoint)) {
				set.add(currWebSocketService);
			}
		}
		return set;
	}

	public boolean isDeployedTo(WebSocketService webSocketService, WebSocketEndpoint endpoint) {
		Set<WebSocketEndpoint> endpoints = this.webSocketService_to_deployed_endpoints.get(webSocketService);
		if (endpoints != null && endpoints.contains(endpoint)) {
			return true;
		}
		return false;
	}

	protected boolean shouldBeDeployedTo(WebSocketService webSocketService, WebSocketEndpoint endpoint) {
		Map<String, Object> webSocketProps = this.webSocketServiceListener.getProperties(webSocketService);
		String webSocketHttpPort = (String) webSocketProps.get("http.port");
		String webSocketHttpsPort = (String) webSocketProps.get("https.port");

		Map<String, Object> endpointProps = this.webSocketEndpointListener.getProperties(endpoint);
		String endpointHttpPort = (String) endpointProps.get("http.port");
		String endpointHttpsPort = (String) endpointProps.get("https.port");

		boolean matchHttpPort = false;
		boolean matchHttpsPort = false;
		if (webSocketHttpPort != null && !webSocketHttpPort.isEmpty() && webSocketHttpPort.equals(endpointHttpPort)) {
			matchHttpPort = true;
		}
		if (webSocketHttpsPort != null && !webSocketHttpsPort.isEmpty() && webSocketHttpsPort.equals(endpointHttpsPort)) {
			matchHttpsPort = true;
		}
		if (matchHttpPort || matchHttpsPort) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param webSocketService
	 * @param endpoint
	 */
	protected void doDeploy(WebSocketService webSocketService, WebSocketEndpoint endpoint) {
		boolean succeed = false;

		activate(endpoint);
		ServerEndpointConfig endpointConfig = endpoint.getEndpointConfig();
		if (endpointConfig != null) {
			try {
				webSocketService.addEndpoint(endpointConfig);
				succeed = true;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			Class<?> endpointClass = endpoint.getEndpointClass();
			if (endpointClass != null) {
				try {
					webSocketService.addEndpoint(endpointClass);
					succeed = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (succeed) {
			Set<WebSocketEndpoint> endpoints = this.webSocketService_to_deployed_endpoints.get(webSocketService);
			if (endpoints == null) {
				endpoints = new HashSet<WebSocketEndpoint>();
				this.webSocketService_to_deployed_endpoints.put(webSocketService, endpoints);
			}
			endpoints.add(endpoint);

			if (debug) {
				println("WebSocketEndpoint is deployed to WebSocketService.");
			}
		}
	}

	/**
	 *
	 * @param webSocketService
	 * @param endpoint
	 */
	protected void doUndeploy(WebSocketService webSocketService, WebSocketEndpoint endpoint) {
		boolean succeed = false;
		if (succeed) {
			Set<WebSocketEndpoint> endpoints = this.webSocketService_to_deployed_endpoints.get(webSocketService);
			if (endpoints != null && endpoints.contains(endpoint)) {
				endpoints.remove(endpoint);

				if (debug) {
					println("WebSocketEndpoint is undeployed from WebSocketService.");
				}
			}
		}
	}

	protected void activate(WebSocketEndpoint endpoint) {
		if (!endpoint.isActivated()) {
			endpoint.setActivated(true);
		}
	}

	protected void deactivate(WebSocketEndpoint endpoint) {
		if (endpoint.isActivated()) {
			endpoint.setActivated(false);
		}
	}

	protected void println() {
		System.out.println(getClass().getSimpleName());
	}

	protected void println(String msg) {
		System.out.println(getClass().getSimpleName() + " - " + msg);
	}

}

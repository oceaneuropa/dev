package org.orbit.service.servlet;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.Servlet;

import org.orbit.service.util.HttpServiceTracker;
import org.orbit.service.util.WebApplicationTracker;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see OsgiManager
 * @see LogConsole
 *
 */
public class HttpServletDeployer {

	protected static Logger LOG = LoggerFactory.getLogger(HttpServletDeployer.class);

	protected HttpServiceTracker httpServiceTracker;
	protected WebApplicationTracker webAppTracker;
	protected Map<WebApplication, List<HttpService>> webAppToDeployedHttpServices = new HashMap<WebApplication, List<HttpService>>();
	protected ReadWriteLock lock;

	public HttpServletDeployer() {
		this.lock = new ReentrantReadWriteLock();
	}

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void start(BundleContext bundleContext) throws Exception {
		this.httpServiceTracker = new HttpServiceTracker() {
			@Override
			protected void serviceAdded(HttpService httpService) {
				lock.writeLock().lock();
				try {
					WebApplication[] webApps = getWebApplications();
					for (WebApplication webApp : webApps) {
						if (!isDeployed(httpService, webApp)) {
							deploy(httpService, webApp);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.writeLock().unlock();
				}
			}

			@Override
			protected void serviceRemoved(HttpService httpService) {
				lock.writeLock().lock();
				try {
					WebApplication[] webApps = getWebApplications();
					for (WebApplication webApp : webApps) {
						if (isDeployed(httpService, webApp)) {
							undeploy(httpService, webApp);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.writeLock().unlock();
				}
			}
		};

		this.webAppTracker = new WebApplicationTracker() {
			@Override
			protected void serviceAdded(WebApplication webApp) {
				lock.writeLock().lock();
				try {
					HttpService[] httpServices = getHttpServices();
					for (HttpService httpService : httpServices) {
						if (!isDeployed(httpService, webApp)) {
							deploy(httpService, webApp);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.writeLock().unlock();
				}
			}

			@Override
			protected void serviceRemoved(WebApplication webApp) {
				lock.writeLock().lock();
				try {
					HttpService[] httpServices = getHttpServices();
					for (HttpService httpService : httpServices) {
						if (isDeployed(httpService, webApp)) {
							undeploy(httpService, webApp);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.writeLock().unlock();
				}
			}
		};

		this.httpServiceTracker.start(bundleContext);
		this.webAppTracker.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.httpServiceTracker != null) {
			this.httpServiceTracker.stop(bundleContext);
			this.httpServiceTracker = null;
		}

		if (this.webAppTracker != null) {
			this.webAppTracker.stop(bundleContext);
			this.webAppTracker = null;
		}

		this.webAppToDeployedHttpServices.clear();
	}

	protected HttpService[] getHttpServices() {
		HttpService[] httpServices = null;
		if (this.httpServiceTracker != null) {
			httpServices = this.httpServiceTracker.getServices(HttpService.class);
		}
		if (httpServices == null) {
			httpServices = new HttpService[0];
		}
		return httpServices;
	}

	protected WebApplication[] getWebApplications() {
		WebApplication[] webApps = null;
		if (this.webAppTracker != null) {
			webApps = this.webAppTracker.getServices(WebApplication.class);
		}
		if (webApps == null) {
			webApps = new WebApplication[0];
		}
		return webApps;
	}

	/**
	 * 
	 * @param httpService
	 * @param webApp
	 * @return
	 */
	protected boolean isDeployed(HttpService httpService, WebApplication webApp) {
		if (httpService != null && webApp != null) {
			List<HttpService> httpServices = this.webAppToDeployedHttpServices.get(webApp);
			if (httpServices != null && httpServices.contains(httpService)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param httpService
	 * @param webApp
	 * @return
	 */
	protected boolean deploy(HttpService httpService, WebApplication webApp) {
		LOG.info("deploy()");
		if (httpService == null || webApp == null) {
			return false;
		}

		boolean succeed = false;
		try {
			String contextRoot = getContextRoot(webApp);

			HttpContext defaultHttpContext = webApp.getHttpContext(httpService);

			// Register resources
			ResourceMetadata[] resourceMetadatas = webApp.getResources();
			if (resourceMetadatas != null) {
				for (ResourceMetadata resourceMetadata : resourceMetadatas) {
					String path = resourceMetadata.getPath();
					String localPath = resourceMetadata.getLocalPath();
					HttpContext httpContext = resourceMetadata.getHttpContext(defaultHttpContext);

					String fullPath = getFullPath(contextRoot, path);
					httpService.registerResources(fullPath, localPath, httpContext);
					LOG.info("    Deployed resource: \"" + fullPath + "\"");
				}
			}

			// Register servlets
			ServletMetadata[] servletMetadatas = webApp.getServlets();
			if (servletMetadatas != null) {
				for (ServletMetadata servletMetadata : servletMetadatas) {
					String path = servletMetadata.getPath();
					Servlet servlet = servletMetadata.getServlet();
					Dictionary<?, ?> properties = servletMetadata.getProperties();
					HttpContext httpContext = servletMetadata.getHttpContext(defaultHttpContext);

					if (servlet == null) {
						continue;
					}

					String fullPath = getFullPath(contextRoot, path);
					httpService.registerServlet(fullPath, servlet, properties, httpContext);
					LOG.info("    Deployed servlet: \"" + fullPath + "\"");
				}
			}

			succeed = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (succeed) {
			List<HttpService> httpServices = this.webAppToDeployedHttpServices.get(webApp);
			if (httpServices == null) {
				httpServices = new ArrayList<HttpService>();
				this.webAppToDeployedHttpServices.put(webApp, httpServices);
			}
			if (!httpServices.contains(httpService)) {
				httpServices.add(httpService);
			}
		}
		return succeed;
	}

	/**
	 * 
	 * @param httpService
	 * @param webApp
	 * @return
	 */
	protected boolean undeploy(HttpService httpService, WebApplication webApp) {
		LOG.info("undeploy()");
		if (httpService == null || webApp == null) {
			return false;
		}

		boolean succeed = false;
		try {
			String contextRoot = getContextRoot(webApp);

			// Unregister resources
			ResourceMetadata[] resourceMetadatas = webApp.getResources();
			if (resourceMetadatas != null) {
				for (ResourceMetadata resourceMetadata : resourceMetadatas) {
					String path = resourceMetadata.getPath();
					String fullPath = getFullPath(contextRoot, path);
					httpService.unregister(fullPath);
					LOG.info("    Undeployed resource: \"" + fullPath + "\"");
				}
			}

			// Unregister servlets
			ServletMetadata[] servletMetadatas = webApp.getServlets();
			if (servletMetadatas != null) {
				for (ServletMetadata servletMetadata : servletMetadatas) {
					String path = servletMetadata.getPath();
					String fullPath = getFullPath(contextRoot, path);
					httpService.unregister(fullPath);
					LOG.info("    Undeployed servlet: \"" + fullPath + "\"");
				}
			}

			succeed = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (succeed) {
			List<HttpService> httpServices = this.webAppToDeployedHttpServices.get(webApp);
			if (httpServices != null && httpServices.contains(httpService)) {
				httpServices.remove(httpService);
			}
		}
		return succeed;
	}

	/**
	 * 
	 * @param webApp
	 * @return
	 */
	protected String getContextRoot(WebApplication webApp) {
		String contextRoot = null;
		if (webApp != null) {
			contextRoot = webApp.getContextRoot();
		}
		if (contextRoot == null) {
			contextRoot = "";
		}
		return contextRoot;
	}

	/**
	 * 
	 * @param contextRoot
	 * @param path
	 * @return
	 */
	protected String getFullPath(String contextRoot, String path) {
		if (contextRoot == null) {
			contextRoot = "";
		}
		if (path == null) {
			path = "";
		}
		String fullPath = contextRoot;
		if (!fullPath.endsWith("/") && !path.startsWith("/")) {
			fullPath += "/";
		}
		fullPath += path;

		if (fullPath.length() > 1 && fullPath.endsWith("/")) {
			fullPath = fullPath.substring(0, fullPath.length() - 1);
		}
		return fullPath;
	}

}

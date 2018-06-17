package org.orbit.service.servlet;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.Servlet;

import org.eclipse.equinox.jsp.jasper.JspServlet;
import org.orbit.service.util.HttpServiceTracker;
import org.osgi.framework.Bundle;
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
public class HttpServletSelfDeployer {

	protected static Logger LOG = LoggerFactory.getLogger(HttpServletSelfDeployer.class);

	protected WebApplication[] webApps;
	protected HttpServiceTracker httpServiceTracker;
	protected Map<WebApplication, List<HttpService>> webAppToDeployedHttpServices = new HashMap<WebApplication, List<HttpService>>();
	protected ReadWriteLock lock;

	/**
	 * 
	 * @param webApps
	 */
	public HttpServletSelfDeployer(WebApplication[] webApps) {
		this.webApps = webApps;
		this.lock = new ReentrantReadWriteLock();
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
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
		this.httpServiceTracker.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		if (this.httpServiceTracker != null) {
			this.httpServiceTracker.stop(bundleContext);
			this.httpServiceTracker = null;
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

	protected synchronized WebApplication[] getWebApplications() {
		if (this.webApps == null) {
			this.webApps = new WebApplication[0];
		}
		return this.webApps;
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

			// Register JSPs
			JspMetadata[] jspMetadatas = webApp.getJSPs();
			if (jspMetadatas != null) {
				for (JspMetadata jspMetadata : jspMetadatas) {
					Bundle bundle = jspMetadata.getBundle();
					String path = jspMetadata.getPath();
					String fullPath = getFullPath(contextRoot, path);
					// String fullPath = path;
					String bundleResourcePath = jspMetadata.getBundleResourcePath();
					// String alias = jspMetadata.getContextRoot();
					Dictionary<?, ?> properties = jspMetadata.getProperties();
					HttpContext httpContext = jspMetadata.getHttpContext(defaultHttpContext);

					JspServlet jspServlet = new JspServlet(bundle, bundleResourcePath, contextRoot);
					httpService.registerServlet(fullPath, jspServlet, properties, httpContext);
					LOG.info("    Deployed JSP: \"" + fullPath + "\"");
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

			// Unregister JSPs
			JspMetadata[] jspMetadatas = webApp.getJSPs();
			if (jspMetadatas != null) {
				for (JspMetadata jspMetadata : jspMetadatas) {
					String path = jspMetadata.getPath();
					String fullPath = getFullPath(contextRoot, path);
					// String fullPath = path;
					httpService.unregister(fullPath);
					LOG.info("    Undeployed JSP: \"" + fullPath + "\"");
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

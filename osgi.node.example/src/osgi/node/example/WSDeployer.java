package osgi.node.example;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

public class WSDeployer {

	protected BundleContext bundleContext;
	protected HttpServiceAdapter httpServiceAdapter;
	protected ApplicationServiceAdapter applicationServicesAdapter;
	protected List<ApplicationServiceReference> applicationServices;
	protected ReadWriteLock lock;
	protected boolean debug = true;

	/**
	 * 
	 * @param bundleContext
	 */
	public WSDeployer(BundleContext bundleContext) {
		this.applicationServices = new ArrayList<ApplicationServiceReference>();
		this.lock = new ReentrantReadWriteLock();
		this.bundleContext = bundleContext;
	}

	public void start() throws Exception {
		if (debug) {
			Printer.pl("WSDeployer.start()");
		}

		// 1. Start tracking HttpService services
		this.httpServiceAdapter = new HttpServiceAdapter(bundleContext) {
			@Override
			public void deploy(ServiceReference<HttpService> reference, HttpService httpService) {
				// A HttpService becomes available.
				for (ApplicationServiceReference applicationService : applicationServices) {
					// If the Application is not deployed to the given httpService, the Application will be deployed to it.
					if (!applicationService.isDeployedTo(httpService)) {
						deployApplication(applicationService, httpService);
					}
				}
			}

			@Override
			public void undeploy(ServiceReference<HttpService> reference, HttpService httpService) {
				// // A HttpService becomes unavailable.
				for (ApplicationServiceReference applicationService : applicationServices) {
					// If the Application is deployed to the given httpService, the Application will be undeployed from it.
					if (applicationService.isDeployedTo(httpService)) {
						httpService.unregister(applicationService.getContextRoot());
						applicationService.unsetDeployedTo(httpService);
					}
				}

				// Note:
				// The ApplicationService and the HttpService are tracked separately.
				// When a HttpService is removed, the ApplicationService list can still be there waiting for the HttpService to be available
				// again. So do not remove ApplicationServices when a HttpService becomes unavailable.
			}
		};
		this.httpServiceAdapter.start();

		// 2. Start tracking Application services
		this.applicationServicesAdapter = new ApplicationServiceAdapter(bundleContext) {
			@Override
			public void deploy(ServiceReference<Application> reference, Application application) {
				String contextRoot = (String) reference.getProperty(Constants.CONTEXT_ROOT);
				if (debug) {
					Printer.pl("ApplicationServiceAdapter.deploy() contextRoot = " + contextRoot + " application = " + application);
				}
				if (contextRoot == null) {
					return;
				}

				ApplicationServiceReference applicationService = findApplicationService(application, contextRoot);
				if (applicationService == null) {
					applicationService = new ApplicationServiceReference(contextRoot, application);
					applicationServices.add(applicationService);
				}

				Set<HttpService> httpServices = httpServiceAdapter.getHttpServices();
				for (HttpService httpService : httpServices) {
					if (!applicationService.isDeployedTo(httpService)) {
						deployApplication(applicationService, httpService);
					}
				}
			}

			@Override
			public void undeploy(ServiceReference<Application> reference, Application application) {
				lock.writeLock().lock();
				try {
					String contextRoot = (String) reference.getProperty(Constants.CONTEXT_ROOT);
					if (debug) {
						Printer.pl("ApplicationServiceAdapter.undeploy() contextRoot = " + contextRoot + " application = " + application);
					}
					if (contextRoot == null) {
						return;
					}

					ApplicationServiceReference applicationService = findApplicationService(application, contextRoot);
					if (applicationService != null) {
						Set<HttpService> httpServices = applicationService.getDeployedHttpServices();
						for (HttpService httpService : httpServices) {
							if (httpService != null) {
								try {
									httpService.unregister(contextRoot);
								} catch (Exception e) {
									if (debug) {
										Printer.pl("ApplicationServiceAdapter.undeploy(). Cannot undeply Application from HttpService. contextRoot = " + contextRoot);
									}
									e.printStackTrace();
								}
							}
						}

						// The only time to remove ApplicationServiceReference from the list is when the service (Application + rootContext) becomes
						// unavailable.
						applicationServices.remove(applicationService);
					}
				} finally {
					lock.writeLock().unlock();
				}
			}
		};
		this.applicationServicesAdapter.start();
	}

	public void stop() throws Exception {
		if (debug) {
			Printer.pl("WSDeployer.stop()");
		}

		// 1. Stop tracking Application services
		this.applicationServicesAdapter.stop();
		this.applicationServicesAdapter = null;

		// 2. Stop tracking HttpService services
		this.httpServiceAdapter.stop();
		this.httpServiceAdapter = null;

		// 3. clear data
		this.applicationServices.clear();
	}

	/**
	 * 
	 * @param applicationService
	 * @param httpService
	 */
	public void deployApplication(ApplicationServiceReference applicationService, HttpService httpService) {
		lock.writeLock().lock();
		try {
			String contextRoot = applicationService.getContextRoot();
			Application application = applicationService.getApplication();
			if (debug) {
				Printer.pl("WSDeployer.deployApplication() contextRoot = " + contextRoot + " application = " + application);
			}
			if (contextRoot == null || application == null || httpService == null) {
				return;
			}

			try {
				ResourceConfig resourceconfig = ResourceConfig.forApplication(application);
				resourceconfig.register(JacksonFeature.class);
				ServletContainer servletContainer = new ServletContainer(resourceconfig);

				Hashtable<String, String> initparams = new Hashtable<String, String>();
				initparams.put("javax.ws.rs.Application", application.getClass().getName());

				ClassLoader cl = Thread.currentThread().getContextClassLoader();
				try {
					Thread.currentThread().setContextClassLoader(ServletContainer.class.getClassLoader());
					httpService.registerServlet(contextRoot, servletContainer, initparams, null);

					applicationService.setDeployedTo(httpService);

					if (debug) {
						Printer.pl("WSDeployer.deployApplication(). Application is deployed to HttpService. contextRoot = " + contextRoot);
					}
				} finally {
					Thread.currentThread().setContextClassLoader(cl);
				}
			} catch (Exception e) {
				if (debug) {
					Printer.pl("WSDeployer.deployApplication(). Cannot deply Application to HttpService. contextRoot = " + contextRoot);
				}
				e.printStackTrace();
			}

		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * Find ApplicationService with give Application and contextRoot
	 * 
	 * @param contextRoot
	 * @return
	 */
	protected ApplicationServiceReference findApplicationService(Application application, String contextRoot) {
		ApplicationServiceReference result = null;
		if (application != null && contextRoot != null) {
			for (ApplicationServiceReference currApplicationService : applicationServices) {
				Application currApplication = currApplicationService.getApplication();
				String currContextRoot = currApplicationService.getContextRoot();
				if (currApplication == application && contextRoot.equals(currContextRoot)) {
					result = currApplicationService;
					break;
				}
			}
		}
		return result;
	}

}

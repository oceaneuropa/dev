package org.origin.common.rest.server;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryImpl;
import org.origin.common.rest.switcher.Switcher;
import org.osgi.framework.BundleContext;

/*
 * https://www.javaworld.com/article/2077921/architecture-scalability/server-load-balancing-architectures--part-1--transport-level-load-balancing.html
 * 
 * https://www.javaworld.com/article/2077922/architecture-scalability/server-load-balancing-architectures-part-2-application-level-load-balanci.html
 * 
 */
public class WSRelayApplication extends AbstractJerseyWSApplication {

	protected Switcher<URI> switcher;
	protected WSClientFactory factory;

	public WSRelayApplication(String contextRoot, int feature, Switcher<URI> switcher) {
		super(contextRoot, feature);
		this.switcher = switcher;
	}

	public WSClientFactory getWSClientFactory() {
		if (this.factory == null) {
			this.factory = new WSClientFactoryImpl();
		}
		return this.factory;
	}

	public void setWSClientFactory(WSClientFactory factory) {
		this.factory = factory;
	}

	protected Client createClient() {
		return createClient(null);
	}

	protected Client createClient(Map<String, Object> properties) {
		return getWSClientFactory().createClient(properties);
	}

	/**
	 * 
	 * @param appDesc
	 * @param switcher
	 * @param factory
	 */
	public WSRelayApplication(WSApplicationDesc appDesc, Switcher<URI> switcher, WSClientFactory factory) {
		this(appDesc.getContextRoot(), appDesc.getFeature(), switcher);

		List<Resource.Builder> wsResources = new ArrayList<Resource.Builder>();

		List<WSResourceDesc> resourceDescs = appDesc.getResources();
		for (WSResourceDesc resourceDesc : resourceDescs) {
			String resourcePath = resourceDesc.getPath();
			List<WSMethodDesc> methodDescs = resourceDesc.getMethods();

			Resource.Builder wsResource = Resource.builder(resourcePath);
			for (WSMethodDesc methodDesc : methodDescs) {
				String methodName = methodDesc.getMethod();
				String produces = methodDesc.getProduces();
				String methodPath = methodDesc.getPath();

				new WSMethodInflector(wsResource, methodPath, methodName, produces, factory.createClient(null), switcher);
			}

			wsResources.add(wsResource);
		}

		for (Resource.Builder wsResource : wsResources) {
			registerResources(wsResource.build());
		}
	}

	public Switcher<URI> getSwitcher() {
		return this.switcher;
	}

	public void setSwitcher(Switcher<URI> switcher) {
		this.switcher = switcher;
	}

	@Override
	public void start(BundleContext bundleContext) {
		if (this.switcher != null) {
			this.switcher.start();
		}

		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		super.stop(bundleContext);

		if (this.switcher != null) {
			this.switcher.stop();
		}
	}

}

package org.origin.common.rest.server;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.switcher.Switcher;

public class WSApplicationDescriptiveSwitcher extends WSApplicationSwitcher {

	/**
	 * 
	 * @param appDescriptor
	 * @param switcher
	 * @param factory
	 */
	public WSApplicationDescriptiveSwitcher(WSApplicationDesc appDescriptor, Switcher<URI> switcher, WSClientFactory factory) {
		super(appDescriptor.getContextRoot(), appDescriptor.getFeature(), switcher);

		List<Resource.Builder> wsResources = new ArrayList<Resource.Builder>();

		List<WSResourceDesc> resourceDescriptors = appDescriptor.getResources();
		for (WSResourceDesc resourceDescriptor : resourceDescriptors) {
			String resourcePath = resourceDescriptor.getPath();
			List<WSMethodDesc> methodDescriptors = resourceDescriptor.getMethods();

			Resource.Builder wsResource = Resource.builder(resourcePath);
			for (WSMethodDesc methodDescriptor : methodDescriptors) {
				String methodName = methodDescriptor.getMethod();
				String produces = methodDescriptor.getProduces();
				String methodPath = methodDescriptor.getPath();

				new WSMethodInflector(wsResource, methodPath, methodName, produces, factory.createClient(null), switcher);
			}

			wsResources.add(wsResource);
		}

		for (Resource.Builder wsResource : wsResources) {
			registerResources(wsResource.build());
		}
	}

}

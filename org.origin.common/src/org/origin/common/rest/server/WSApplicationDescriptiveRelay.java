package org.origin.common.rest.server;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.switcher.Switcher;

public class WSApplicationDescriptiveRelay extends WSApplicationRelay {

	/**
	 * 
	 * @param appDesc
	 * @param switcher
	 * @param factory
	 */
	public WSApplicationDescriptiveRelay(WSApplicationDesc appDesc, Switcher<URI> switcher, WSClientFactory factory) {
		super(appDesc.getContextRoot(), appDesc.getFeature(), switcher);

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

}

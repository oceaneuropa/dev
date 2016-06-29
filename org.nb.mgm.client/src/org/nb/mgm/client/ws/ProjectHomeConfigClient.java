package org.nb.mgm.client.ws;

import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;

/*
 * ProjectHomeConfig resource client.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs/{homeConfigId}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs (Body parameter: ProjectHomeConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs (Body parameter: ProjectHomeConfigDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs/{homeConfigId}
 * 
 */
public class ProjectHomeConfigClient extends AbstractClient {

	public ProjectHomeConfigClient(ClientConfiguration config) {
		super(config);
	}

}
